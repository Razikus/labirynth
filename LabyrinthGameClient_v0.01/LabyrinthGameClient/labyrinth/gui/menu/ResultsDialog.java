package labyrinth.gui.menu;

import labyrinth.game.Result;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * Klasa reprezentująca okno wyników.
 *
 * @author Zuzanna Łaś
 */
public class ResultsDialog extends JDialog
{
    /**
     * Tworzy okno wyników, którego właścicielem jest ramka podana jako argument.
     *
     * @param owner ramka będąca właścicielem okna wyników
     * @param results lista wyników
     */
    public ResultsDialog(JFrame owner, ArrayList<Result> results)
    {
        super(owner, "Wyniki", true);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        DefaultListModel<ResultPanel> resultsPanels = createResultsPanelsList(results);
        initComponents(resultsPanels);
        pack();
    }

    /**
     * Przetwarza listę wyników na listę paneli wyników.
     *
     * Konwersja wspomaga proces prezentacji wyników w sposób graficzny.
     *
     * @param results lista wyników
     *
     * @return lista paneli wyników
     */
    private DefaultListModel<ResultPanel> createResultsPanelsList(ArrayList<Result> results)
    {
        DefaultListModel<ResultPanel> resultsPanels = new DefaultListModel<>();
        ResultPanel titlePanel = new ResultPanel();
        resultsPanels.addElement(titlePanel);
        for (Result result : results) {
            resultsPanels.addElement(new ResultPanel(result));
        }
        return resultsPanels;
    }

    /**
     * Tworzy panel przewijalny z listą paneli rezultatów.
     *
     * @param resultsPanels lista paneli wyników
     */
    private void initComponents(DefaultListModel<ResultPanel> resultsPanels)
    {
        JList<ResultPanel> resultsList = new JList<>(resultsPanels);
        resultsList.setCellRenderer(new ResultPanelCellRenderer());
        JScrollPane scrollPane = new JScrollPane(resultsList);
        add(scrollPane);
    }

    /**
     * Klasa reprezentująca panel wyniku.
     */
    private class ResultPanel extends JPanel
    {
        /**
         * Tworzy tytułowy panel wyniku.
         */
        public ResultPanel()
        {
            color = new Color(224, 224, 224);
            isTitle = true;
            panelSettings();
            initTitleComponent();
        }

        /**
         * Tworzy panel dla podanego wyniku.
         *
         * @param result wynik
         */
        public ResultPanel(Result result)
        {
            this.result = result;
            color = Color.WHITE;
            panelSettings();
            initComponent();
        }

        /**
         * Ustawia graficzne elementy panelu.
         */
        private void panelSettings()
        {
            setBackground(color);
            setLayout(new FlowLayout());
            setBorder(new EtchedBorder());
        }

        /**
         * Inicjuje tytułowy panel, w którym oznaczenia elementów wyniku są reprezentowane przez etykiety.
         */
        private void initTitleComponent()
        {
            add(new ResultElementLabel("Użytkownik", new ImageIcon("images/userIcon.png")));
            add(new ResultElementLabel("Pionek", new ImageIcon("images/pawns.png")));
            add(new ResultElementLabel("Plansza", new ImageIcon("images/shape.png")));
            add(new ResultElementLabel("Poziom", new ImageIcon("images/level.png")));
            add(new ResultElementLabel("Czas", new ImageIcon("images/clock.png")));
        }

        /**
         * Inicjuje panel, w którym elementy wyniku są reprezentowane przez etykiety.
         */
        private void initComponent()
        {
            add(new ResultElementLabel(result.getUserName()));
            add(new ResultElementLabel(new ImageIcon(result.getPawnIconPath())));
            add(new ResultElementLabel(new ImageIcon(result.getLabyrinthShapeIconPath())));
            add(new ResultElementLabel(new ImageIcon(result.getDifficultyLevelIconPath())));
            add(new ResultElementLabel(result.getTime()));
        }

        /**
         * Pobiera bieżący kolor panelu.
         *
         * @return bieżący kolor panelu
         */
        public Color getColor()
        {
            return color;
        }

        /**
         * Sprawdza, czy panel jest panelem tytułowym.
         *
         * @return true, jeżeli panel jest tytułowy
         */
        public boolean isTitlePanel()
        {
            return isTitle;
        }

        private Result result;
        private Color color;
        private boolean isTitle;
    }

    /**
     * Klasa reprezentująca etykietę elementu wyniku.
     */
    private class ResultElementLabel extends JLabel
    {
        /**
         * Tworzy etykietę dla tekstowego elementu wyniku.
         *
         * @param text tekstowy element wyniku
         */
        public ResultElementLabel(String text)
        {
            super(text, SwingConstants.CENTER);
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
        }

        /**
         * Tworzy etykietę dla elementu wyniku będącego ikoną.
         *
         * @param image element wyniku w postaci ikony
         */
        public ResultElementLabel(Icon image)
        {
            super(image, SwingConstants.CENTER);
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
        }

        /**
         * Tworzy etykietę dla elementu wyniku tekstowego wraz z ikoną.
         *
         * @param text tekstowy element wyniku
         * @param icon element wyniku w postaci ikony
         */
        public ResultElementLabel(String text, Icon icon)
        {
            super(text, icon, SwingConstants.CENTER);
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            setOpaque(true);
        }
        
        private static final int WIDTH = 130;
        private static final int HEIGHT = 40;
    }

    /**
     * Klasa reprezentująca obiekt renderujący elementy listy.
     */
    private class ResultPanelCellRenderer implements ListCellRenderer<Object>
    {
        /**
         * Zwraca komponent przygotowany do wyrenderowania.
         *
         * @param list lista elementów
         * @param value bieżący element listy
         * @param index indeks bieżącego elementu listy
         * @param isSelected stan zaznaczenia bieżącego elementu
         * @param cellHasFocus moliwość aktywowania bieżącego elementu
         *
         * @return komponent przygotowany do wyrenderowania
         */
        public Component getListCellRendererComponent(
                JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus)
        {
            ResultPanel resultPanel = (ResultPanel) value;
            if (isSelected && !resultPanel.isTitlePanel()) {
                resultPanel.setBackground(new Color(228, 255, 185));
            } else {
                resultPanel.setBackground(resultPanel.getColor());
            }
            resultPanel.setEnabled(list.isEnabled());
            resultPanel.setFont(list.getFont());
            return resultPanel;
        }
    }
}

