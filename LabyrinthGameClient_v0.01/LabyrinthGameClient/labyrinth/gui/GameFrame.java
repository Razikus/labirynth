package labyrinth.gui;

import labyrinth.control.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Klasa reprezentująca ramkę gry "Labirynt".
 *
 * @author Zuzanna Łaś
 */
public class GameFrame extends JFrame
{
    /**
     * Tworzy ramkę, inicjalizując wszystkie komponenty i kontroler gry.
     */
    public GameFrame()
    {
        setTitle("Labirynt");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setSize();
        Image image = new ImageIcon("images/labirynt_kolor.png").getImage();
        setIconImage(image);
        gameController = new OperationsControl(this);
        initComponents();
        gameController.setGameComponents(gameMenu, gameToolbar, gameBoard); 
        locateComponents();
    }

    /**
     * Ustawia minimalny rozmiar okna.
     */
    private void setSize()
    {
        Dimension screen = getScreenSize();
        int width = (int) screen.getWidth();
        int height = (int) screen.getHeight();
        setMinimumSize(new Dimension(width - width/20 , height - height/20));
    }

    /**
     * Zwraca rozmiar ekranu w pikselach.
     *
     * @return rozmiar ekranu
     */
    private Dimension getScreenSize()
    {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        return screenSize;
    }

    /**
     * Inicjuje komponenty znajdujące się wewnątrz ramki.
     */
    private void initComponents()
    {
        gameMenu = new GameMenu(gameController);
        setJMenuBar(gameMenu);
        gameToolbar = new GameToolbar(gameController);
        gameBoard = new GameBoard(gameController);
    }

    /**
     * Układa komponenty w odpowiednich miejscach ramki.
     */
    private void locateComponents()
    {
        add(gameToolbar, BorderLayout.NORTH);
        add(createGamePanel(), BorderLayout.CENTER);
    }

    /**
     * Tworzy panel opakowujący panel planszy.
     *
     * @return panel opakowujący planszę
     */
    private JPanel createGamePanel()
    {
        JPanel gamePanel = new JPanel();
        gamePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        gamePanel.setLayout(new BorderLayout());
        gamePanel.add(gameBoard, BorderLayout.CENTER);
        return gamePanel;
    }

    private OperationsControl gameController;
    private GameMenu gameMenu;
    private GameToolbar gameToolbar;
    private GameBoard gameBoard;
}

