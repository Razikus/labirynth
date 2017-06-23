package labyrinth.gui.menu;

import static labyrinth.gui.GameBoard.LabyrinthShape;
import static labyrinth.gui.GameBoard.DifficultyLevel;

import labyrinth.control.OperationsControl;
import labyrinth.game.Result;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Klasa reprezentująca menu dodatków.
 *
 * @author Zuzanna Łaś
 */
public class Accessories extends JMenu
{
    /**
     * Tworzy menu dodatków i ustawia kontroler gry.
     *
     * @param gameController kontroler gry
     */
    public Accessories(OperationsControl gameController)
    {
        super("Dodatki");
        this.gameController = gameController;
        addAccessoriesItems();
        addMenuListener(new AccessoriesMenuListener());
    }

    /**
     * Tworzy, inicjuje i dodaje kolejne elementy menu dodatków.
     */
    private void addAccessoriesItems()
    {
        submenus = new ArrayList<>();
        createShapeSubmenu();
        createDifficultyLevelSubmenu();
        createPawnIconSubmenu(); 
        addSeparator();
        JMenuItem results = new JMenuItem("Wyniki", new ImageIcon("images/results.png"));
        results.addActionListener(event -> gameController.showResults());
        add(results);
    }

    /**
     * Tworzy, inicjuje i dodaje podmenu rozmiaru planszy gry.
     */
    private void createShapeSubmenu()
    {
        JMenu shapeSubmenu = new JMenu(new SubmenuAction(SHAPE_SUBMENU_NAME, "images/shape.png") {});
        ButtonGroup group = new ButtonGroup();
        Action squareAction = new ShapeAction(SQUARE_ITEM_NAME, "images/square.png", LabyrinthShape.SQUARE);
        JRadioButtonMenuItem squareShape = new JRadioButtonMenuItem(squareAction);
        squareShape.setSelected(true);
        Action rectangleAction = new ShapeAction(RECTANGLE_ITEM_NAME, "images/rectangle.png", LabyrinthShape.RECTANGLE);
        JRadioButtonMenuItem rectangularShape = new JRadioButtonMenuItem(rectangleAction);
        group.add(squareShape);
        group.add(rectangularShape);
        shapeSubmenu.add(squareShape);
        shapeSubmenu.add(rectangularShape);
        add(shapeSubmenu);
        submenus.add(shapeSubmenu);
    }

    /**
     * Tworzy, inicjuje i dodaje podmenu poziomu trudności gry.
     */
    private void createDifficultyLevelSubmenu()
    {
        JMenu levelSubmenu = new JMenu(new SubmenuAction(LEVEL_SUBMENU_NAME, "images/level.png") {});
        ButtonGroup group = new ButtonGroup();
        Action easyAction = new DifficultyLevelAction(EASY_ITEM_NAME, "images/easyLevel.png", DifficultyLevel.EASY);
        JRadioButtonMenuItem easy = new JRadioButtonMenuItem(easyAction);
        easy.setSelected(true);
        Action mediumAction = new DifficultyLevelAction(MEDIUM_ITEM_NAME, "images/mediumLevel.png", DifficultyLevel.MEDIUM);
        JRadioButtonMenuItem medium = new JRadioButtonMenuItem(mediumAction);
        Action hardAction = new DifficultyLevelAction(HARD_ITEM_NAME, "images/hardLevel.png", DifficultyLevel.HARD);
        JRadioButtonMenuItem hard = new JRadioButtonMenuItem(hardAction);
        group.add(easy);
        group.add(medium);
        group.add(hard);
        levelSubmenu.add(easy);
        levelSubmenu.add(medium);
        levelSubmenu.add(hard);
        add(levelSubmenu);
        submenus.add(levelSubmenu);
    }

    /**
     * Tworzy, inicjuje i dodaje podmenu wyboru pionka.
     */
    private void createPawnIconSubmenu()
    {
        JMenu pawnIconSubmenu = new JMenu(new SubmenuAction(PAWN_SUBMENU_NAME, "images/pawns.png") {});
        ButtonGroup group = new ButtonGroup();
        Action greenAction = new PawnAction("images/greenMonster.png", "images/greenMonsterIcon.png");
        JRadioButtonMenuItem greenPawn = new JRadioButtonMenuItem(greenAction);
        greenPawn.setSelected(true);
        Action purpleAction = new PawnAction("images/purpleMonster.png", "images/purpleMonsterIcon.png");
        JRadioButtonMenuItem purplePawn = new JRadioButtonMenuItem(purpleAction);
        Action blueAction = new PawnAction("images/blueMonster.png", "images/blueMonsterIcon.png");
        JRadioButtonMenuItem bluePawn = new JRadioButtonMenuItem(blueAction);
        Action orangeAction = new PawnAction("images/orangeMonster.png", "images/orangeMonsterIcon.png");
        JRadioButtonMenuItem orangePawn = new JRadioButtonMenuItem(orangeAction);
        group.add(greenPawn);
        group.add(bluePawn);
        group.add(purplePawn);
        group.add(orangePawn);
        pawnIconSubmenu.add(greenPawn);
        pawnIconSubmenu.add(purplePawn);
        pawnIconSubmenu.add(bluePawn);
        pawnIconSubmenu.add(orangePawn);
        add(pawnIconSubmenu);
        submenus.add(pawnIconSubmenu);
    }

    /**
     * Zwraca ścieżkę do pliku z domyślnym obrazkiem pionka.
     *
     * @return ścieżka do pliku z domyślnym obrazkiem pionka
     */
    public String getDefaultPawnImage()
    {
        return DEFAULT_PAWN_IMAGE;
    }

    /**
     * Zwraca domyślny kształt planszy gry.
     *
     * @return domyślny kształt planszy gry
     */
    public LabyrinthShape getDefaultLabyrinthShape()
    {
        return DEFAULT_LABYRINTH_SHAPE;
    }

    /**
     * Zwraca domyślny poziom trudności.
     *
     * @return domyślny poziom trudności
     */
    public DifficultyLevel getDefaultDifficultyLevel()
    {
        return DEFAULT_DIFFICULTY_LEVEL;
    }

    /**
     * Ustawia odpowiednie pozycje w menu jako wybrane.
     *
     * @param labyrinthShape kształt planszy gry
     * @param difficultyLevel poziom trudności
     * @param pawnImage ścieżka do pliku z obrazkiem pionka
     */
    public void setLabyrinthSettings(LabyrinthShape labyrinthShape, DifficultyLevel difficultyLevel, String pawnImage)
    {
        for (JMenu submenu : submenus) {
            String submenuName = submenu.getActionCommand();
            if (submenuName.equals(SHAPE_SUBMENU_NAME)) {
                setShapeSubmenuItems(submenu, labyrinthShape);
                continue;
            }
            if (submenuName.equals(LEVEL_SUBMENU_NAME)) {
                setDifficultyLevelSubmenuItems(submenu, difficultyLevel);
                continue;
            }
            if (submenuName.equals(PAWN_SUBMENU_NAME)) {
                setPawnSubmenuItems(submenu, pawnImage);
                continue;
            }
        }
    }

    /**
     * Ustawia odpowiedni komponent podmenu kształtu planszy jako wybrany.
     *
     * @param shapeSubmenu podmenu kształtu planszy
     * @param labyrinthShape kształt planszy gry
     */
    private void setShapeSubmenuItems(JMenu shapeSubmenu, LabyrinthShape labyrinthShape)
    {
        int itemsNumber = shapeSubmenu.getItemCount();
        for (int j = 0; j < itemsNumber; ++j) {
            JMenuItem menuItem = shapeSubmenu.getItem(j);
            String itemName = menuItem.getActionCommand();
            LabyrinthShape itemShape = (LabyrinthShape) Enum.valueOf(LabyrinthShape.class, itemName);
            if (itemShape == labyrinthShape) {
                menuItem.setSelected(true);
                break;
            }
        }
    }

    /**
     * Ustawia odpowiedni komponent podmenu poziomu trudności jako wybrany.
     *
     * @param levelSubmenu podmenu poziomu trudnosci
     * @param difficultyLevel poziom trudności
     */
    private void setDifficultyLevelSubmenuItems(JMenu levelSubmenu, DifficultyLevel difficultyLevel)
    {
        int itemsNumber = levelSubmenu.getItemCount();
        for (int j = 0; j < itemsNumber; ++j) {
            JMenuItem menuItem = levelSubmenu.getItem(j);
            String itemName = menuItem.getActionCommand();
            DifficultyLevel itemLevel = (DifficultyLevel) Enum.valueOf(DifficultyLevel.class, itemName);
            if (itemLevel == difficultyLevel) {
                menuItem.setSelected(true);
                break;
            }
        }
    }

    /**
     * Ustawia odpowiedni komponent podmenu pionka jako wybrany.
     *
     * @param pawnSubmenu podmenu wyboru pionka
     * @param pawnImage ścieżka do pliku z obrazkiem pionka
     */
    private void setPawnSubmenuItems(JMenu pawnSubmenu, String pawnImage)
    {
        int itemsNumber = pawnSubmenu.getItemCount();
        for (int j = 0; j < itemsNumber; ++j) {
            JMenuItem menuItem = pawnSubmenu.getItem(j);
            String itemName = menuItem.getActionCommand();
            if (itemName.equals(pawnImage)) {
                menuItem.setSelected(true);
                break;
            }
        }
    }

    /**
     * Tworzy i wyświetla okno wyników, którego właścicielem jest ramka podana jako argument.
     *
     * @param gameFrame ramka będąca właścicielem okna
     * @param results lista wyników
     */
    public void showResults(JFrame gameFrame, ArrayList<Result> results)
    {
        JDialog resultsDialog = new ResultsDialog(gameFrame, results);
        resultsDialog.setVisible(true);
    }

    /**
     * Klasa reprezentująca akcję podmenu.
     */
    private abstract class SubmenuAction extends AbstractAction
    {
        /**
         * Tworzy obiekt akcji podmenu.
         *
         * Akcji nadawana jest nazwa i ustawiana jest jej ikona.
         *
         * @param name nazwa akcji
         * @param iconPath ikona akcji
         */
        public SubmenuAction(String name, String iconPath)
        {
            super(name, new ImageIcon(iconPath));
        }

        /**
         * Akcja wykonywana w odpowiedzi na zdarzenie.
         *
         * Definicja metody jest pusta.
         *
         * @param e zdarzenie generujące akcję
         */
        public void actionPerformed(ActionEvent e) {}
    }

    /**
     * Klasa reprezentująca akcję kształtu labiryntu.
     */
    private class ShapeAction extends AbstractAction
    {
        /**
         * Tworzy obiekt akcji kształtu planszy.
         *
         * @param name nazwa akcji
         * @param iconPath ścieżka do pliku z ikoną akcji kształtu
         * @param labyrinthShape kształt planszy gry
         */
        public ShapeAction(String name, String iconPath, LabyrinthShape labyrinthShape)   
        {
            this.labyrinthShape = labyrinthShape;
            putValue(Action.NAME, name);
            putValue(Action.SMALL_ICON, new ImageIcon(iconPath));
            putValue(Action.ACTION_COMMAND_KEY, labyrinthShape.toString());
        }

        /**
         * Akcja wykonywana w odpowiedzi na zdarzenie.
         *
         * Zdarzeniem generującym jest wybór komponentu kształtu planszy gry, który
         * implementuje interfejs Action. Informacja o wybranym przez użytkownika kształcie
         * planszy jest automatycznie przekazywana do kontrolera gry.
         *
         * @param e zdarzenie generujące akcję
         */
        public void actionPerformed(ActionEvent e)
        {
            gameController.setLabyrinthShape(labyrinthShape);
        }

        private LabyrinthShape labyrinthShape;
    }

    /**
     * Klasa reprezentująca akcję poziomu trudności.
     */
    private class DifficultyLevelAction extends AbstractAction
    {
        /**
         * Tworzy obiekt akcji poziomu trudności gry.
         *
         * @param name nazwa akcji
         * @param iconPath ścieżka do pliku z ikoną akcji poziomu trudności
         * @param difficultyLevel poziom trudności gry
         */
        public DifficultyLevelAction(String name, String iconPath, DifficultyLevel difficultyLevel)   
        {
            this.difficultyLevel = difficultyLevel;
            putValue(Action.NAME, name);
            putValue(Action.SMALL_ICON, new ImageIcon(iconPath));
            putValue(Action.ACTION_COMMAND_KEY, difficultyLevel.toString());
        }

        /**
         * Akcja wykonywana w odpowiedzi na zdarzenie.
         *
         * Zdarzeniem generującym jest wybór komponentu poziomu trudności, który
         * implementuje interfejs Action. Informacja o wybranym przez użytkownika poziomie
         * jest automatycznie przekazywana do kontrolera gry.
         *
         * @param e zdarzenie generujące akcję
         */
        public void actionPerformed(ActionEvent e)
        {
            gameController.setDifficultyLevel(difficultyLevel);
        }

        private DifficultyLevel difficultyLevel;
    }

    /**
     * Klasa reprezentująca akcję pionka.
     */
    private class PawnAction extends AbstractAction
    {
        /**
         * Tworzy obiekt akcji pionka.
         *
         * @param imagePath ścieżka do pliku z obrazkiem pionka
         * @param iconPath ścieżka do pliku z ikoną akcji pionka
         */
        public PawnAction(String imagePath, String iconPath)
        {
            pawnImage = imagePath;
            putValue(Action.SMALL_ICON, new ImageIcon(iconPath));
            putValue(Action.ACTION_COMMAND_KEY, imagePath);
        }

        /**
         * Akcja wykonywana w odpowiedzi na zdarzenie.
         *
         * Zdarzeniem generującym jest wybór komponentu pionka, który implementuje
         * interfejs Action. Informacja o pionku wybranym przez użytkownika
         * jest automatycznie przekazywana do kontrolera gry.
         *
         * @param e zdarzenie generujące akcję
         */
        public void actionPerformed(ActionEvent e)
        {
            gameController.setPawnImage(pawnImage);
        }

        private String pawnImage;
    }

    /**
     * Klasa reprezentująca słuchacza akcji związanych z menu dodatków.
     */
    private class AccessoriesMenuListener implements MenuListener
    {
        /**
         * Wykonuje akcję w odpowiedzi na wybranie menu dodatków.
         *
         * Jeżeli gra jest uruchomiona w chwili otwierania menu,
         * komponenty reprezentujące wybór kształtu planszy, poziomu trudności
         * i pionka stają się niedostępne.
         *
         * @param event zdarzenie generujące akcję
         */
        public void menuSelected(MenuEvent event)
        {
            boolean enabled = !gameController.isGamePlay() && !gameController.isGamePause();
            submenus.forEach(item -> item.setEnabled(enabled));
        }

        /**
         * Wykonuje akcję w odpowiedzi na odznaczenie menu dodatków.
         *
         * Definicja metody jest pusta.
         *
         * @param event zdarzenie generujące akcję
         */
        public void menuDeselected(MenuEvent event) {}

        /**
         * Wykonuje akcję w odpowiedzi na anulowanie menu dodatków.
         *
         * Definicja metody jest pusta.
         *
         * @param event zdarzenie generujące akcję
         */
        public void menuCanceled(MenuEvent event) {}
    }

    private static final String SHAPE_SUBMENU_NAME = "Kształt planszy";
    private static final String SQUARE_ITEM_NAME = "Kwadratowa";
    private static final String RECTANGLE_ITEM_NAME = "Prostokątna";
    private static final String LEVEL_SUBMENU_NAME = "Stopień trudności";
    private static final String EASY_ITEM_NAME = "Łatwy";
    private static final String MEDIUM_ITEM_NAME = "Średni";
    private static final String HARD_ITEM_NAME = "Trudny";
    private static final String PAWN_SUBMENU_NAME = "Wybór pionka";
    private static final String DEFAULT_PAWN_IMAGE = "images/greenMonster.png";
    private static final LabyrinthShape DEFAULT_LABYRINTH_SHAPE = LabyrinthShape.SQUARE;
    private static final DifficultyLevel DEFAULT_DIFFICULTY_LEVEL = DifficultyLevel.EASY;
    
    private OperationsControl gameController;
    private ArrayList<JMenu> submenus;
}

