package labyrinth.gui;

import static labyrinth.gui.GameBoard.LabyrinthShape;
import static labyrinth.gui.GameBoard.DifficultyLevel;

import labyrinth.game.Result;
import labyrinth.control.*;
import labyrinth.gui.menu.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.util.ArrayList;

/**
 * Klasa reprezentująca menu gry.
 *
 * @author Zuzanna Łaś
 */
public class GameMenu extends JMenuBar
{
    /**
     * Tworzy menu i ustawia kontroler gry.
     *
     * @param gameController kontroler gry
     */
    public GameMenu(OperationsControl gameController)
    {
        this.gameController = gameController;
        setBorder(new EtchedBorder());
        createMenu();
    }

    /**
     * Tworzy menu, inicjalizując i dodając kolejne elementy.
     */
    private void createMenu()
    {
        createGameMenu();
        createAccessoriesMenu();
        createHelpMenu();
    }

    /**
     * Tworzy menu "Gra".
     */
    private void createGameMenu()
    {
        gameMenu = new Game(gameController);
        gameMenu.setMnemonic('G');
        add(gameMenu);
    }

    /**
     * Tworzy menu "Dodatki".
     */
    private void createAccessoriesMenu()
    {
        accessoriesMenu = new Accessories(gameController);
        accessoriesMenu.setMnemonic('D');
        add(accessoriesMenu);    
    }

    /**
     * Tworzy menu "Pomoc".
     */
    private void createHelpMenu()
    {
        helpMenu = new Help(gameController);
        helpMenu.setMnemonic('P');
        add(helpMenu);
    }

    /**
     * Zwraca ścieżkę do pliku z domyślnym obrazkiem pionka.
     *
     * @return ścieżka do pliku z domyślnym obrazkiem pionka
     */
    public String getDefaultPawnImage()
    {
        return accessoriesMenu.getDefaultPawnImage();
    }

    /**
     * Zwraca ścieżkę do pliku z domyślnym kształtem labiryntu.
     *
     * @return ścieżka do pliku z domyślnym kształtem labiryntu
     */
    public LabyrinthShape getDefaultLabyrinthShape()
    {
        return accessoriesMenu.getDefaultLabyrinthShape();
    }

    /**
     * Zwraca ścieżkę do pliku z domyślnym poziomem trudności gry.
     *
     * @return ścieżka do pliku z domyślnym poziomem trudności
     */
    public DifficultyLevel getDefaultDifficultyLevel()
    {
        return accessoriesMenu.getDefaultDifficultyLevel();
    }

    /**
     * Ustawia podane pozycje w menu jako wybrane.
     *
     * @param labyrinthShape kształt labiryntu
     * @param difficultyLevel poziom trudności
     * @param pawnImage ścieżka do pliku z obrazkiem pionka
     */
    public void setLabyrinthSettings(LabyrinthShape labyrinthShape, DifficultyLevel difficultyLevel, String pawnImage)
    {
        accessoriesMenu.setLabyrinthSettings(labyrinthShape, difficultyLevel, pawnImage);
    }

    /**
     * Wywołuje opcję menu pomocy pokazującą informację o grze.
     *
     * @param gameFrame główna ramka gry
     */
    public void showGameInformation(JFrame gameFrame)
    {
        helpMenu.showGameInformation(gameFrame);
    }

    /**
     * Wywołuje opcję menu dodatków pokazującą wyniki wygranych gier.
     *
     * @param gameFrame główna ramka gry
     * @param results lista wyników
     */
    public void showResults(JFrame gameFrame, ArrayList<Result> results)
    {
        accessoriesMenu.showResults(gameFrame, results);
    }

    private OperationsControl gameController;
    private Game gameMenu;
    private Accessories accessoriesMenu;
    private Help helpMenu;
}

