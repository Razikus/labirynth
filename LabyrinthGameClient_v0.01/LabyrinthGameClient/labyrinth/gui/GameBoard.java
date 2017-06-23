package labyrinth.gui;

import labyrinth.control.*;
import labyrinth.game.*;
import labyrinth.server.serialization.Coord;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

/**
 * Klasa reprezentująca planszę labiryntu.
 *
 * @author Zuzanna Łaś
 */
public class GameBoard extends JPanel
{
    /**
     * Tworzy panel reprezentujący planszę gry i ustawia kontroler gry.
     *
     * @param gameController kontroler gry
     */
    public GameBoard(OperationsControl gameController)
    {
        this.gameController = gameController;
        setBackground(Color.GRAY);
        setBorder(new EtchedBorder());
        setFocusable(true);
    }

    /**
     * Rysuje panel z planszą w zależności od bieżącego stanu gry.
     *
     * @param g obiekt rysujący
     */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (gameController.isGamePlay()) {
            labyrinth.draw(g, leftOffset, upOffset);
        } else if (gameController.isGameWin()) {
            showLogo(g, WIN);
        } else if (gameController.isGamePause()) {
            showLogo(g, PAUSE);
        } else {
            showLogo(g, LOGO);
        }
    }

    /**
     * Rysuje logo gry na środku planszy.
     *
     * Rozmiar loga jest dostosowywany do rozdzielczości ekranu.
     *
     * @param g obiekt rysujący
     * @param imagePath ścieżka do pliku z obrazkiem loga
     */
    private void showLogo(Graphics g, String imagePath)
    {
        int height = getHeight();
        int width = getWidth();
        ImageIcon icon = new ImageIcon(imagePath);
        int iconWidth, iconHeight;
        if (height < width) {
            double proportion = ((double) icon.getIconWidth())/((double) icon.getIconHeight());
            iconHeight = height - 200;
            iconWidth = (int) (iconHeight * proportion);
        } else {
            double proportion = ((double) icon.getIconHeight())/((double) icon.getIconWidth());
            iconWidth = width - 200;
            iconHeight = (int) (iconWidth * proportion);
        }
        g.drawImage(icon.getImage(), width/2 - iconWidth/2, height/2 - iconHeight/2, iconWidth, iconHeight, null);
    }

    /**
     * Rozpoczyna nową grę, tworząc labirynt.
     *
     * @param coordinates lista współrzędnych ścianek labiryntu
     */
    public void startNewGame(ArrayList<Coord> coordinates)
    {
        labyrinth = new Labyrinth(coordinates, gameController);
        startGame();
    }

    /**
     * Rozpoczyna grę, dodając odpowiedniego słuchacza klawiatury
     * i aktualizując komponent.
     */
    public void startGame()
    {
        arrowKeyListener = new ArrowKeyListener();
        addKeyListener(arrowKeyListener);
        requestFocusInWindow();
        repaint();
    }

    /**
     * Wyznacza rozmiar labiryntu na podstawie rozdzielczości ekranu.
     *
     * Rozmiar labiryntu określa liczbę pól (obiektów klasy Field) na szerokość i wysokość.
     * Podczas wyznaczania rozmiaru uwzględniana jest orientacja dłuższego
     * i krótszego boku ekranu.
     *
     * @return rozmiar labiryntu
     */
    public Dimension designateLabyrinthSize()
    {
        int width = getWidth();
        int height = getHeight();
        Dimension labyrinthSize;
        if (width > height) {
            labyrinthSize = getLabyrinthSize(height, width, true);
        } else {
            labyrinthSize = getLabyrinthSize(width, height, false);
        }
        return labyrinthSize;
    }

    /**
     * Wyznacza rozmiar labiryntu na podstawie orientacji oraz podanej długości dłuższego i krótszego
     * boku ekranu.
     *
     * Rozmiar labiryntu określa liczbę pól (obiektów klasy Field) na szerokość i wysokość.
     *
     * @param shorterSide długość krótszego boku ekranu
     * @param longerSide długość dłuższego boku ekranu
     * @param horizontal orientacja położenia: pozioma (poziomy bok ekranu dłuższy) lub nie
     *
     * @return wyznaczony rozmiar labiryntu
     */
    private Dimension getLabyrinthSize(int shorterSide, int longerSide, boolean horizontal)
    {
        int labyrinthShorterSideSize = designateLabyrinthShorterSideSize(shorterSide);
        int fieldSize = shorterSide/labyrinthShorterSideSize;
        Field.setSize(fieldSize);
        int labyrinthLongerSideSize = designateLabyrinthLongerSideSize(longerSide, labyrinthShorterSideSize);
        Dimension labyrinthSize;
        if (horizontal) {
            setSizesAndOffsets(labyrinthLongerSideSize, labyrinthShorterSideSize);
            labyrinthSize = new Dimension(labyrinthLongerSideSize, labyrinthShorterSideSize);
        } else {
            setSizesAndOffsets(labyrinthShorterSideSize, labyrinthLongerSideSize);
            labyrinthSize = new Dimension(labyrinthShorterSideSize, labyrinthLongerSideSize);
        }
        return labyrinthSize;
    }

    /**
     * Wyznacza rozmiar krótszego boku labiryntu.
     *
     * Rozmiar labiryntu określa liczbę pól (obiektów klasy Field).
     * W procesie wyznaczaia rozmiaru krótszego boku jest uwzględniany
     * poziom trudności i kształt planszy.
     *
     * @param shorterSide długość krótszego boku labiryntu (w pikselach)
     *
     * @return liczba abstrakcyjnych pól przypadających na krótszy bok labiryntu
     */
    private int designateLabyrinthShorterSideSize(int shorterSide)
    {
        double sidePart = 0.0;
        if (difficultyLevel == DifficultyLevel.EASY) {
            sidePart = shorterSide/3;
        } else if (difficultyLevel == DifficultyLevel.MEDIUM) {
            sidePart = 2 * (shorterSide/3);
        } else if (difficultyLevel == DifficultyLevel.HARD) {
            sidePart = shorterSide;
        }
        return adjustSizeIfGeminate((int) sidePart/10);
    }

    /**
     * Wyznacza rozmiar dłuższego boku labiryntu.
     *
     * Rozmiar labiryntu określa liczbę pól (obiektów klasy Field).
     * W procesie wyznaczaia rozmiaru dłuższego boku jest uwzględniany
     * rozmiar (w pikselach) abstrakcyjnego pola labiryntu.
     *
     * @param longerSide długość dłuższego boku labiryntu (w pikselach)
     * @param labyrinthShorterSideSize liczba abstrakcyjnych pól przypadających na krótszy bok labiryntu
     *
     * @return liczba abstrakcyjnych pól przypadających na dłuższy bok labiryntu
     */
    private int designateLabyrinthLongerSideSize(int longerSide, int labyrinthShorterSideSize)
    {
        int labyrinthLongerSideSize;
        if (labyrinthShape == LabyrinthShape.SQUARE) {
            labyrinthLongerSideSize = labyrinthShorterSideSize;
        } else {
            labyrinthLongerSideSize = longerSide/Field.getSize();
            labyrinthLongerSideSize = adjustSizeIfGeminate(labyrinthLongerSideSize);
        }
        return labyrinthLongerSideSize;
    }

    /**
     * Dostosowuje rozmiar, zamieniając liczbę parzystą na nieparzystą poprzez odjęcie 1.
     *
     * @param size liczba reprezentująca rozmiar
     *
     * @return nieparzysta liczba reprezentująca rozmiar
     */
    private int adjustSizeIfGeminate(int size)
    {
        if (size % 2 == 0) {
            size -= 1;
        }
        return size;
    }

    /**
     * Ustawia rozmiar labiryntu oraz związane z tym rozmiarem przesunięcia względem
     * panelu planszy.
     *
     * Rozmiar labiryntu określa liczbę pól (obiektów klasy Field).
     *
     * @param labyrinthWidth liczba pól labiryntu na szerokość
     * @param labyrinthHeight liczba pól labiryntu na wysokość
     */
    private void setSizesAndOffsets(int labyrinthWidth, int labyrinthHeight)
    {
        int width = getWidth();
        int height = getHeight();
        int fieldSize = Field.getSize();
        Labyrinth.setWidth(labyrinthWidth);
        Labyrinth.setHeight(labyrinthHeight);
        upOffset = (height - (fieldSize * labyrinthHeight))/2;
        leftOffset = (width - (fieldSize * labyrinthWidth))/2;
    }

    /**
     * Wstrzymuje grę poprzez zablokowanie możliwości wykonywania ruchów na planszy.
     */
    public void pauseGame()
    {
        removeKeyListener(arrowKeyListener);
        repaint();
    }

    /**
     * Kończy grę, usuwając labirynt oraz słuchaczy klawiatury.
     */
    public void endGame()
    {
        removeKeyListener(arrowKeyListener);
        labyrinth = null;
        repaint();
    }

    /**
     * Ustawia ścieżkę do pliku reprezentującego obrazek pionka.
     *
     * @param imagePath ścieżka do pliku reprezentującego obrazek pionka
     */
    public void setPawnImage(String imagePath)
    {
        Labyrinth.setPawnImage(imagePath);
    }

    /**
     * Zwraca ścieżkę do pliku reprezentującego obrazek pionka.
     *
     * @return ścieżka do pliku reprezentującego obrazek pionka
     */
    public String getPawnImage()
    {
        return Labyrinth.getPawnImage();    
    }

    /**
     * Ustawia kształt labiryntu.
     *
     * @param labyrinthShape nowy kształt labiryntu
     */
    public void setLabyrinthShape(LabyrinthShape labyrinthShape)
    {
        this.labyrinthShape = labyrinthShape;
    }

    /**
     * Zwraca bieżący kształt labiryntu.
     *
     * @return bieżący kształt labiryntu
     */
    public LabyrinthShape getLabyrinthShape()
    {
        return labyrinthShape;
    }

    /**
     * Ustawia poziom trudności gry.
     *
     * @param difficultyLevel nowy poziom trudności
     */
    public void setDifficultyLevel(DifficultyLevel difficultyLevel)
    {
        this.difficultyLevel = difficultyLevel;
    }

    /**
     * Zwraca bieżący poziom trudności gry.
     *
     * @return bieżący poziom trudności
     */
    public DifficultyLevel getDifficultyLevel()
    {
        return difficultyLevel;
    }

    /**
     * Serializuje planszę gry.
     *
     * @param out wyjściowy strumień obiektowy
     *
     * @throws IOException błąd zapisu do strumienia obiektowego
     */
    public void serialize(ObjectOutputStream out) throws IOException
    {
        out.writeObject(labyrinthShape);
        out.writeObject(difficultyLevel);
        out.writeObject(labyrinth);
    }

    /**
     * Deserializuje planszę gry.
     *
     * Przywracany jest dokładny stan gry sprzed serializacji.
     * Rozmiary (w pikselach) elementów planszy i przesunięć są ponownie wyznaczane na podstawie
     * rozdzielczości ekranu.
     *
     * @param in wejściowy strumień obiektowy
     *
     * @throws IOException błąd odczytu ze strumienia obiektowego
     * @throws ClassNotFoundException błąd odczytu zserializowanej klasy
     */
    public void deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        LabyrinthShape newShape = (LabyrinthShape) in.readObject();
        DifficultyLevel newLevel = (DifficultyLevel) in.readObject();
        Labyrinth newLabyrinth = (Labyrinth) in.readObject();
        labyrinthShape = newShape;
        difficultyLevel = newLevel;
        labyrinth = newLabyrinth;
        String pawnImage = Labyrinth.getPawnImage();
        gameController.setLabyrinthSettingsInMenu(labyrinthShape, difficultyLevel, pawnImage);
        labyrinth.setGameController(gameController);
        designateBoardSettings();
        if (gameController.isGamePlay()) {
            startGame();
        } else {
            repaint();
        }
    }

    /**
     * Wyznacza rozmiary (w pikselach) elementów planszy i przesunięć na podstawie
     * rozdzielczości ekranu.
     */
    private void designateBoardSettings()
    {
        int width = getWidth();
        int height = getHeight();
        int labyrinthHeight = Labyrinth.getHeight();
        int labyrinthWidth = Labyrinth.getWidth();
        int fieldSize, lengthUpRemainder, lengthLeftRemainder;
        if (width > height) {
            fieldSize = height/labyrinthHeight;
            lengthUpRemainder = height % labyrinthHeight;
            lengthLeftRemainder = width - (labyrinthWidth * fieldSize);
        } else {
            fieldSize = width/labyrinthWidth;
            lengthUpRemainder = height - (labyrinthHeight * fieldSize);
            lengthLeftRemainder = width % labyrinthWidth;
        }
        upOffset = lengthUpRemainder/2;
        leftOffset = lengthLeftRemainder/2;
        Field.setSize(fieldSize);
    }

    /**
     * Klasa reprezentująca słuchacza strzałek klawiaturowych.
     */
    private class ArrowKeyListener extends KeyAdapter
    {
        /**
         * Wykonuje akcję właściwą dla wciśnięcia jednej ze strzałek na klawiaturze.
         *
         * @param event zdarzenie generujące akcję
         */
        public void keyPressed(KeyEvent event)
        {
            int keyCode = event.getKeyCode();
            if (keyCode == KeyEvent.VK_RIGHT) {
                labyrinth.movePawnRight();
            } else if (keyCode == KeyEvent.VK_LEFT) {
                labyrinth.movePawnLeft();
            } else if (keyCode == KeyEvent.VK_UP) {
                labyrinth.movePawnUp();
            } else if (keyCode == KeyEvent.VK_DOWN) {
                labyrinth.movePawnDown();
            }
        }
    }

    /**
     * Klasa wyliczeniowa reprezentująca kształt labiryntu.
     */
    public static enum LabyrinthShape
    {
        SQUARE("images/square.png"), RECTANGLE("images/rectangle.png");

        /**
         * Tworzy kształt labiryntu, ustawiając ikonę reprezentującą ten kształt.
         *
         * @param iconPath ścieżka do pliku z ikoną kształtu
         */
        private LabyrinthShape(String iconPath)
        {
            this.iconPath = iconPath;
        }

        /**
         * Zwraca ścieżkę do pliku z ikoną kształtu labiryntu.
         *
         * @return ścieżka do pliku z ikoną kształtu
         */
        public String getIconPath()
        {
            return iconPath;
        }

        /**
         * Porównuje kształt bieżącego labiryntu z kształtem podanym jako argument.
         *
         * @param other kształt labiryntu podany do porównania
         *
         * @return 0, jeżeli kształty są identyczne;
         *        -1, jeżeli bieżący kształt jest prostokątem, a podany - kwadratem;
         *         1, jeżeli bieżący kształt jest kwadratem, a podany - prostokątem
         */
        public int compareWith(LabyrinthShape other)
        {
            if (this == LabyrinthShape.SQUARE && other == LabyrinthShape.RECTANGLE) {
                return 1;
            } else if (this == LabyrinthShape.RECTANGLE && other == LabyrinthShape.SQUARE) {
                return -1;
            } else {
                return 0;
            }
        }

        private String iconPath;
    }

    /**
     * Klasa wyliczeniowa reprezentująca poziom trudności gry.
     */
    public static enum DifficultyLevel
    { 
        EASY("images/easyLevel.png"), MEDIUM("images/mediumLevel.png"), HARD("images/hardLevel.png");

        /**
         * Tworzy poziom trudności gry, ustawiając ikonę reprezentującą ten poziom.
         *
         * @param iconPath ścieżka do pliku z ikoną poziomu
         */
        private DifficultyLevel(String iconPath)
        {
            this.iconPath = iconPath;
        }

        /**
         * Zwraca ścieżkę do pliku z ikoną poziomu gry.
         *
         * @return ścieżka do pliku z ikoną poziomu
         */
        public String getIconPath()
        {
            return iconPath;
        }

        /**
        * Porównuje poziom trudności bieżącej gry z poziomem podanym jako argument.
        *
        * @param other poziom trudności gry podany do porównania
        *
        * @return 0, jeżeli poziomy są identyczne;
        *        -1, jeżeli bieżący poziom jest trudniejszy od podanego;
        *         1, jeżeli bieżący poziom jest łatwiejszy od podanego
        */
        public int compareWith(DifficultyLevel other)
        {
            if ((this == DifficultyLevel.EASY && (other == DifficultyLevel.MEDIUM || other == DifficultyLevel.HARD)) ||
                    (this == DifficultyLevel.MEDIUM && other == DifficultyLevel.HARD)) {
                return 1;
            } else if ((this == DifficultyLevel.MEDIUM && other == DifficultyLevel.EASY) || 
                    (this == DifficultyLevel.HARD && (other == DifficultyLevel.EASY || other == DifficultyLevel.MEDIUM))) {
                return -1;
            } else {
                return 0;
            }
        }

        private String iconPath;
    }

    private static final String LOGO = "images/labirynt_kolor.png";
    private static final String WIN = "images/labirynt_wygrana.png";
    private static final String PAUSE = "images/labirynt_logo_pauza.png";

    private Labyrinth labyrinth;
    private LabyrinthShape labyrinthShape;
    private DifficultyLevel difficultyLevel;
    private int leftOffset;
    private int upOffset;
    private OperationsControl gameController;
    private KeyListener arrowKeyListener;
}

