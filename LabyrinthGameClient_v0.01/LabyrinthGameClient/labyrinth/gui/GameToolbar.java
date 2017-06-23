package labyrinth.gui;

import labyrinth.control.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

/**
 * Klasa reprezentująca pasek narzędzi z przyciskami sterującymi i stoperem.
 *
 * @author Zuzanna Łaś
 */
public class GameToolbar extends JToolBar
{
    /**
     * Tworzy pasek narzędzi i ustawia kontroler gry.
     *
     * @param gameController kontroler gry
     */
    public GameToolbar(OperationsControl gameController)
    {
        this.gameController = gameController;
        setFloatable(false);
        setMargin(new Insets(10, 10, 10, 10));
        createToolbar();
    }

    /**
     * Tworzy i inicjalizuje poszczególne elementy paska narzędzi.
     */
    private void createToolbar()
    {
        createStartButton();
        addSeparator(new Dimension(20, 10));
        createPauseButton();
        addSeparator(new Dimension(20, 10));
        createEndButton();
        add(Box.createHorizontalGlue());
        timeLabel = new StopwatchLabel();
        add(timeLabel);
    }

    /**
     * Tworzy przycisk startu i dodaje do niego odpowiedniego słuchacza akcji.
     */
    private void createStartButton()
    {
        Action startAction = new ToolAction("images/start.png", "Rozpocznij grę") {
            public void actionPerformed(ActionEvent event)
            {
                gameController.startGame();
            }
        };    
        startButton = add(startAction);
        startButton.setOpaque(true);
        startButton.setBackground(Color.BLACK);
    }

    /**
     * Tworzy przycisk pauzy i dodaje do niego odpowiedniego słuchacza akcji.
     */
    private void createPauseButton()
    {
        Action pauseAction = new ToolAction("images/pauza.png", "Wstrzymaj grę") {
            public void actionPerformed(ActionEvent event)
            {
                gameController.pauseGame();
            }
        };    
        pauseButton = add(pauseAction);
        pauseButton.setOpaque(true);
        pauseButton.setBackground(Color.BLACK);
    }

    /**
     * Tworzy przycisk końca gry i dodaje do niego odpowiedniego słuchacza akcji.
     */
    private void createEndButton()
    {
        Action endAction = new ToolAction("images/koniec.png", "Zakończ grę") {
            public void actionPerformed(ActionEvent event)
            {
                gameController.endGame();
            }
        };    
        endButton = add(endAction);
        endButton.setOpaque(true);
        endButton.setBackground(Color.BLACK);
    }

    /**
     * Ustawia możliwość aktywacji wszystkich przycisków paska narzędzi.
     *
     * @param focus możliwość aktywacji przycisków (true, jeżeli przyciski mogą być aktywowane)
     */
    public void setButtonsFocusable(boolean focus)
    {
        startButton.setFocusable(focus);
        pauseButton.setFocusable(focus);
        endButton.setFocusable(focus);
    }

    /**
     * Rozpoczyna pracę stopera.
     */
    public void startStopwatch()
    {
        timeLabel.startStopwatch();
    }

    /**
     * Wstrzymuje stoper.
     */
    public void pauseStopwatch()
    {
        timeLabel.pauseStopwatch();
    }

    /**
     * Kończy pracę stopera.
     */
    public void endStopwatch()
    {
        timeLabel.endStopwatch();
    }

    /**
     * Pobiera bieżący czas stopera.
     *
     * @return łańcuch zaków reprezentujący bieżący czas stopera w postaci: HH:MM:SS
     */
    public String getTime()
    {
        return timeLabel.getTime();
    }

    /**
     * Serializuje pasek narzędzi.
     *
     * Jedynym elementem paska narzedzi podlegającym serializacji jest etykieta czasu.
     *
     * @param out wyjściowy strumień obiektowy
     *
     * @throws IOException błąd zapisu do strumienia obiektowego
     */
    public void serialize(ObjectOutputStream out) throws IOException
    {
        timeLabel.serialize(out);
    }

    /**
     * Deserializuje pasek narzędzi.
     *
     * @param in wejściowy strumień obiektowy
     *
     * @throws IOException błąd odczytu ze strumienia obiektowego
     * @throws ClassNotFoundException błąd odczytu zserializowanej klasy
     */
    public void deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        timeLabel.deserialize(in);
    }

    /**
     * Klasa reprezentująca akcję przypisywaną przyciskowi.
     */
    private abstract class ToolAction extends AbstractAction
    {
        /**
         * Tworzy akcję (obiekt pośrednio implementujący interfejs Action).
         *
         * @param iconPath ścieżka do pliku z ikoną
         * @param description opis akcji
         */
        public ToolAction(String iconPath, String description)
        {
            super("", new ImageIcon(iconPath));
            putValue(Action.SHORT_DESCRIPTION, description);
        }
    }

    private OperationsControl gameController;
    private StopwatchLabel timeLabel;
    private JButton startButton;
    private JButton pauseButton;
    private JButton endButton;
} 
    
