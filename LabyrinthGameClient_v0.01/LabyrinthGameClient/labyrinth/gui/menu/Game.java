package labyrinth.gui.menu;

import labyrinth.control.OperationsControl;

import javax.swing.*;
import javax.swing.event.MenuListener;
import javax.swing.event.MenuEvent;

/**
 * Klasa reprezentująca menu gry.
 *
 * @author Zuzanna Łaś
 */
public class Game extends JMenu
{
    /**
     * Tworzy menu i ustawia kontroler gry.
     *
     * @param gameController kontroler gry
     */
    public Game(OperationsControl gameController)
    {
        super("Gra");
        this.gameController = gameController;
        addGameItems();
        addMenuListener(new GameMenuListener());
    }

    /**
     * Tworzy, inicjuje i dodaje kolejne elementy wraz z słuchaczami akcji do menu gry.
     */
    private void addGameItems()
    {
        JMenuItem newGameItem = new JMenuItem("Nowa", new ImageIcon("images/newLabyrinth.png"));
        newGameItem.addActionListener(event -> gameController.startGame());
        add(newGameItem);
        JMenuItem openItem = new JMenuItem("Otwórz", new ImageIcon("images/open.png"));
        openItem.addActionListener(event -> gameController.openGame());
        add(openItem);
        addSeparator();
        saveItem = new JMenuItem("Zapisz", new ImageIcon("images/save.png"));
        saveItem.addActionListener(event -> gameController.saveGame());
        add(saveItem);
        saveAsItem = new JMenuItem("Zapisz jako", new ImageIcon("images/saveAs.png"));
        saveAsItem.addActionListener(event -> gameController.saveGameAs());
        add(saveAsItem);
        addSeparator();
        JMenuItem exitItem = new JMenuItem("Zakończ", new ImageIcon("images/exit.png"));
        exitItem.addActionListener(event -> System.exit(0));
        add(exitItem);
    }

    /**
     * Klasa reprezentująca słuchacza akcji związanych z menu gry.
     */
    private class GameMenuListener implements MenuListener
    {
        /**
         * Wykonuje akcję w odpowiedzi na wybranie menu gry.
         *
         * Jeżeli gra nie jest uruchomiona w chwili otwierania menu,
         * komponenty reprezentujące zapis stanu gry stają się niedostępne.
         *
         * @param event zdarzenie generujące akcję
         */
        public void menuSelected(MenuEvent event)
        {
            boolean enabled = gameController.isGamePlay() || gameController.isGamePause();
            saveItem.setEnabled(enabled);
            saveAsItem.setEnabled(enabled);
        }

        /**
         * Wykonuje akcję w odpowiedzi na odznaczenie menu gry.
         *
         * Definicja metody jest pusta.
         *
         * @param event zdarzenie generujące akcję
         */
        public void menuDeselected(MenuEvent event) {}

        /**
         * Wykonuje akcję w odpowiedzi na anulowanie menu gry.
         *
         * Definicja metody jest pusta.
         *
         * @param event zdarzenie generujące akcję
         */
        public void menuCanceled(MenuEvent event) {}
    }

    private OperationsControl gameController;
    private JMenuItem saveItem;
    private JMenuItem saveAsItem;
}

