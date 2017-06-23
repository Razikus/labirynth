package labyrinth.control;

import static labyrinth.gui.GameBoard.LabyrinthShape;
import static labyrinth.gui.GameBoard.DifficultyLevel;

import static java.nio.file.StandardOpenOption.*;

import labyrinth.gui.*;
import labyrinth.game.Result;
import labyrinth.server.serialization.Coord;

import java.util.ArrayList;
import java.io.*;
import javax.sound.sampled.*;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;
import java.util.regex.Pattern;
import java.util.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

/**
 * Klasa pełni rolę kontrolera - steruje wykonywaniem głównych operacji w programie
 * i pośredniczy w komunikacji pomiędzy komponentami graficznego interfejsu użytkownika.
 *
 * @author Zuzanna Łaś
 */
public class OperationsControl
{
    /**
     * Tworzy kontroler gry, ustawiając główną ramkę programu.
     *
     * @param gameFrame główna ramka programu
     */
    public OperationsControl(GameFrame gameFrame)
    {
        this.gameFrame = gameFrame;
        connection = new ClientConnection();
        gameState = GameState.START;
    }

    /**
     * Ustawia główne komponenty graficznego interfejsu użytkownika.
     *
     * @param gameMenu komponent reprezentujący menu gry
     * @param gameToolbar komponent reprezentujący pasek czasu i przycisków sterujących
     * @param gameBoard komponent reprezentujący planszę gry
     */
    public void setGameComponents(GameMenu gameMenu, GameToolbar gameToolbar, GameBoard gameBoard)
    {
        this.gameMenu = gameMenu;
        this.gameToolbar = gameToolbar;
        this.gameBoard = gameBoard;
        setPawnImage(gameMenu.getDefaultPawnImage());
        setLabyrinthShape(gameMenu.getDefaultLabyrinthShape());
        setDifficultyLevel(gameMenu.getDefaultDifficultyLevel());
    }

    /**
     * Ustawia obrazek dla pionka na planszy gry.
     *
     * @param imagePath ścieżka do pliku z obrazkiem pionka
     */
    public void setPawnImage(String imagePath)
    {
        gameBoard.setPawnImage(imagePath);
    }

    /**
     * Ustawia kształt labiryntu.
     *
     * @param labyrinthShape kształt labiryntu
     */
    public void setLabyrinthShape(LabyrinthShape labyrinthShape)
    {
        gameBoard.setLabyrinthShape(labyrinthShape);
    }

    /**
     * Ustawia poziom trudności gry.
     *
     * @param difficultyLevel poziom trudności
     */
    public void setDifficultyLevel(DifficultyLevel difficultyLevel)
    {
        gameBoard.setDifficultyLevel(difficultyLevel);
    }

    /**
     * Rozpoczyna grę.
     *
     * Komponenty graficznego interfejsu użytkownika są odpowiednio przygotowywane do rozpoczęcia nowej gry.
     */
    public void startGame()
    {
        if (gameState == GameState.PAUSE) {
            gameBoard.startGame();
        } else {
            if (gameState == GameState.PLAY) {
                gameBoard.endGame();
            }
            gameToolbar.endStopwatch();
            if (!startWithGeneratedLabyrinthFromServer()) {
                return;
            }
        }
        gameState = GameState.PLAY;
        gameToolbar.setButtonsFocusable(false);
        gameToolbar.startStopwatch();
    }

    /**
     * Pobiera z serwera wygenerowaną listę współrzędnych ścianek labiryntu i ustawia planszę gry.
     *
     * W razie wystąpienia problemów z połączeniem wyświetlane jest okienko z informacją o błędzie.
     *
     * @return true jest zwracane, jeżeli nie wystąpił żaden problem podczas połączenia z serwerem
     *         i labirynt dla planszy gry został poprawnie zainicjalizowany
     */
    private boolean startWithGeneratedLabyrinthFromServer()
    {
        Dimension labyrinthSize = gameBoard.designateLabyrinthSize();
        int width = (int) labyrinthSize.getWidth();
        int height = (int) labyrinthSize.getHeight();
        try {
            ArrayList<Coord> coordinates = connection.getLabyrinthCoordinates(width, height);
            if (coordinates != null) {
                gameBoard.startNewGame(coordinates);
                return true;
            }
        } catch (Exception e) {
            showWarningDialog();
        }
        return false;
    }

    /**
     * Obsługuje proces otwarcia zapisanej wcześniej gry.
     *
     * Wyświetla okienko wyboru pliku. W razie zatwierdzenia przez użytkownika ustawia
     * bieżący plik gry na ścieżkę do wskazanego pliku i odpowiednio przygotowuje komponenty
     * graficznego interfejsu do wczytania zapisanej gry.
     */
    public void openGame()
    {
        JFileChooser chooser = prepareFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);
        int result = chooser.showOpenDialog(gameFrame);
        if (result == JFileChooser.APPROVE_OPTION) {
            currentFile = chooser.getSelectedFile().toPath();
            endGame();
            readGame();
            gameToolbar.setButtonsFocusable(false);
        }
    }

    /**
     * Wczytuje z bieżącego pliku zapisaną wcześniej grę.
     *
     * Bieżący plik jest ostatnim używanym w programie do zapisu lub wczytania gry.
     * Zapisany stan gry jest deserializowany z pliku. W razie wystąpienia problemów
     * podczas wczytywania przywracany jest stabilny stan programu i wyświetlane jest
     * okienko informujące o błędzie.
     */
    private void readGame()
    {
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(currentFile))) {
            gameState = (GameState) in.readObject();
            gameBoard.deserialize(in);
            gameToolbar.deserialize(in);
        } catch (Exception e) {
            currentFile = null;
            resetGame();
            showWarningDialog();
        }
    }

    /**
     * Zapisuje bieżący stan gry do pliku.
     *
     * Wyświetla okienko wyboru nazwy i po zatwierdzeniu zajmuje się
     * odpowiednim przygotowaniem pliku.
     */
    public void saveGameAs()
    {
        JFileChooser chooser = prepareFileChooser();
        int result = chooser.showSaveDialog(gameFrame);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = chooser.getSelectedFile().getPath();
            if (!Pattern.matches(".*\\." + FILE_GAME_EXTENSION + "$", filePath.subSequence(0, filePath.length()))) {
                filePath = filePath + "." + FILE_GAME_EXTENSION;
            }
            prepareGameFile(filePath);
        }
    }

    /**
     * Zwraca obiekt klasy służącej do wyboru pliku gry z zapisanym stanem labiryntu.
     *
     * @return obiekt klasy wyboru pliku
     */
    private JFileChooser prepareFileChooser()
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        FileFilter filter = new FileNameExtensionFilter("Pliki gry", FILE_GAME_EXTENSION);
        chooser.setFileFilter(filter);
        chooser.setFileView(new FileIconView(filter, new ImageIcon("labyrinthGameIcon.png")));
        return chooser;
    }

    /**
     * Tworzy nowy plik gry i ustawia jako bieżący.
     *
     * Jeżeli w podanej lokalizacji istnieje już plik o podanej nazwie, to jest on nadpisywany.
     * Stan gry jest zapisywany. W razie wystąpienia nieoczekiwanych problemów wyświetlane jest
     * okienko z informacją o błędzie.
     *
     * @param filePath ścieżka do pliku, w którym zostanie zapisana gra
     */
    private void prepareGameFile(String filePath)
    {
        Path path = Paths.get(filePath);
        try {
            Files.deleteIfExists(path);
            currentFile = Files.createFile(path);
            saveGame();
        } catch (Exception e) {
            currentFile = null;
            showWarningDialog();
        }
    }

    /**
     * Zapisuje stan gry do bieżącego pliku.
     *
     * Jeżeli bieżacy plik nie jest ustawiony, wywoływana jest metoda savaGameAs().
     * Stan gry jest serializowany do pliku. W razie wystąpienia nieoczekiwanych problemów wyświetlane
     * jest okienko z informacją o błędzie.
     */
    public void saveGame()
    {
        if (currentFile == null) {
            saveGameAs();
        } else {
            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(currentFile))) {
                out.writeObject(gameState);
                gameBoard.serialize(out);
                gameToolbar.serialize(out);
            } catch (Exception e) {
                currentFile = null;
                showWarningDialog();
            }
        }
    }

    /**
     * Wyświelta ostrzeżenie w postaci okienka z informacją o nieoczekiwanym błędzie.
     */
    private void showWarningDialog()
    {
        JOptionPane.showMessageDialog(gameFrame,
                "Wystąpił nieoczekiwany błąd!", 
                "Uwaga!", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Kończy zwycięską grę.
     *
     * Odpowiednio modyfikuje stan gry, aktualizując odpowiednio komponenty graficznego interfejsu.
     * Odtwarzana jest zwycięska melodia, a po potwierdzeniu przez użytkownika zapisywany jest wynik.
     */
    public void endWinnerGame()
    {
        gameState = GameState.META;
        gameToolbar.setButtonsFocusable(true);
        gameBoard.endGame();
        gameToolbar.pauseStopwatch();
        playMusic();
        saveWinnerResult();
    }

    /**
     * Odtwarza zwycięską melodię. W razie wystąpienia nieoczekiwanych problemów wyświetlane jest
     * okienko z informacją o błędzie.
     */
    private void playMusic()
    {
        try {
            File audioFile = new File("music/winMusic.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip audioClip = AudioSystem.getClip();
            audioClip.open(audioStream);
            audioClip.start();
            Thread.sleep(audioClip.getMicrosecondLength()/1000);
        } catch (Exception e) {
            showWarningDialog();
        }
    }

    /**
     * Zapisuje wynik wygranej gry do pliku.
     *
     * Wyświelta okienko wprowadzania loginu gracza, a po potwierdzeniu przez użytkownika
     * tworzy lub aktualizuje plik z wynikami. W razie wystąpienia nieoczekiwanych problemów wyświetlane jest
     * okienko z informacją o błędzie.
     */
    private void saveWinnerResult()
    {
        String userLogin = insertUserLogin();
        if (!userLogin.equals("")) {
            try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(Paths.get(RESULT_FILE), StandardCharsets.UTF_8, CREATE, APPEND))) {
                String pawnImage = gameBoard.getPawnImage();
                String timeResult = gameToolbar.getTime();
                LabyrinthShape labyrinthShape = gameBoard.getLabyrinthShape();
                DifficultyLevel difficultyLevel = gameBoard.getDifficultyLevel();
                Result result = new Result(userLogin, pawnImage, timeResult, labyrinthShape, difficultyLevel);
                out.println(result.write()); 
            } catch (Exception e) {
                showWarningDialog();
            }
        }
    }

    /**
     * Obsługuje proces wczytania loginu użytkownika.
     *
     * Wyświetlane jest okienko służące do wprowadzenia nazwy gracza. Po potwierdzeniu przez
     * użytkownika tekst jest odpowiednio dostosowywany.
     *
     * @return wczytana nazwa użytkownika
     */
    private String insertUserLogin()
    {
        ImageIcon icon = new ImageIcon("images/user.png");
        String login = (String) JOptionPane.showInputDialog(gameFrame,
                "Podaj nick:", "Zapis wyniku gry",
                JOptionPane.PLAIN_MESSAGE, icon, null, null);
        login = login.trim();
        if (login == null || login.equals("")) {
            return "";
        }
        if (login.length() > 25) {
            login = login.substring(0, 25);
        }
        return login;
    }

    /**
     * Kończy grę.
     *
     * Odpowiednio modyfikuje stany komponentów graficznego interfejsu użytkownika.
     */
    public void endGame()
    {
        if (gameState == GameState.PLAY || gameState == GameState.PAUSE) {
            resetGame();
        } else if (gameState == GameState.META) {
            gameState = GameState.END;
            repaintGameBoard();
            gameToolbar.endStopwatch();
        }
    }

    /**
     * Resetuje stan gry i komponentów graficznego interfejsu.
     */
    public void resetGame()
    {
        gameState = GameState.END;
        gameToolbar.setButtonsFocusable(true);
        gameBoard.endGame();
        gameToolbar.endStopwatch();
    }

    /**
     * Odświeża wygląd planszy gry.
     */
    public void repaintGameBoard()
    {
        gameBoard.repaint();
    }

    /**
     * Odświeża wygląd planszy gry w specjalny sposób wymuszający większą kontrolę
     * wątków graficznego interfejsu.
     */
    public void specialRepaintGameBoard()
    {
        gameBoard.paint(gameBoard.getGraphics());  
    }

    /**
     * Wstrzymuje grę, informując komponenty graficznego interfejsu o pauzie.
     */
    public void pauseGame()
    {
        if (gameState == GameState.PLAY) {
            gameState = GameState.PAUSE;
            gameToolbar.setButtonsFocusable(true);
            gameBoard.pauseGame();
            gameToolbar.pauseStopwatch();
        }
    }

    /**
     * Sprawdza, czy gra trwa w danej chwili.
     *
     * @return true, jeżeli gra trwa
     */
    public boolean isGamePlay()
    {
        return gameState == GameState.PLAY;
    }

    /**
     * Sprawdza, czy obecny stan gry jest stanem ustawianym po wygranej.
     *
     * @return true, jeżeli w bieżącej grze została osiągnięta meta na planszy
     */
    public boolean isGameWin()
    {
        return gameState == GameState.META;
    }

    /**
     * Sprawdza, czy gra jest w danej chwili wstrzymana.
     *
     * @return true, jeżeli została wywołana pauza
     */
    public boolean isGamePause()
    {
        return gameState == GameState.PAUSE;
    }

    /**
     * Ustawia odpowiednie pozycje w menu jako wybrane.
     *
     * @param labyrinthShape bieżący kształt labiryntu
     * @param difficultyLevel bieżący poziom trudności gry
     * @param pawnImage bieżący obrazek pionka
     */
    public void setLabyrinthSettingsInMenu(LabyrinthShape labyrinthShape, DifficultyLevel difficultyLevel, String pawnImage)
    {
        gameMenu.setLabyrinthSettings(labyrinthShape, difficultyLevel, pawnImage);
    }

    /**
     * Wywołuje opcję menu pokazującą informacje o programie.
     */
    public void showGameInformation()
    {
        gameMenu.showGameInformation(gameFrame);
    }

    /**
     * Wczytuje i wyświetla wyniki wygranych gier.
     *
     * Zwycięskie wyniki są wczytywane z pliku przechowującego rezultaty.
     * W razie wystąpienia nieoczekiwanych problemów wyświetlane jest
     * okienko z informacją o błędzie.
     */
    public void showResults()
    {
        ArrayList<Result> results = new ArrayList<>();
        try {
            Path filePath = Paths.get(RESULT_FILE);
            if (Files.exists(filePath)) {
                List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
                for (String line : lines) {
                    Result result = new Result();
                    result.read(line);
                    results.add(result);
                }
                results.sort(null);
            }
        } catch (Exception e) {
            showWarningDialog();
        }
        gameMenu.showResults(gameFrame, results);
    }

    /**
     * Klasa wewnętrzna reprezentuje mechanizm prezentacji ikony pliku podczas przeglądania.
     */
    private class FileIconView extends FileView
    {
        /**
         * Tworzy obiekt widoku ikony pliku dla podanego filtra i ikony.
         *
         * @param filter filtr plików
         * @param icon ikona wyświetlana dla plików
         */
        public FileIconView(FileFilter filter, Icon icon)
        {
            this.filter = filter;
            this.icon = icon;
        }

        /**
         * Zwraca ikonę dla podanego pliku.
         *
         * Ikona jest zwracana tylko dla plików zaakceptwanych przez filtr.
         *
         * @param f sprawdzany plik
         * @return ikona dla pliku zaakceptowanego przez filtr lub null w przeciwnym wypadku
         */
        public Icon getIcon(File f)
        {
            if (!f.isDirectory() && filter.accept(f))
                return icon;
            return null;
        }

        private FileFilter filter;
        private Icon icon;
    }

    /**
     * Klasa wyliczeniowa reprezentująca możliwe stany przyjmowane przez grę w trakcie działania programu.
     */
    public static enum GameState { START, PLAY, PAUSE, META, END };

    private static final String FILE_GAME_EXTENSION = "lbrt";
    private static final String RESULT_FILE = "result.txt";

    private GameFrame gameFrame;
    private GameMenu gameMenu;
    private GameToolbar gameToolbar;
    private GameBoard gameBoard;
    private ClientConnection connection;
    private GameState gameState;
    private Path currentFile;
}

