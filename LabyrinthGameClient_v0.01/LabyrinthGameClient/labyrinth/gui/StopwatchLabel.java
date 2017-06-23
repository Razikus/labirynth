package labyrinth.gui;

import labyrinth.game.Stopwatch;

import javax.swing.JLabel;
import java.awt.Font;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

/**
 * Klasa reprezentująca etykietę ze stoperem.
 *
 * @author Zuzanna Łaś
 */
public class StopwatchLabel extends JLabel
{
    /**
     * Tworzy etykietę stopera.
     */
    public StopwatchLabel()
    {
        super(DEFAULT_TIME);
        Font timeFont = new Font("SansSerif", Font.BOLD, 22);
        setFont(timeFont);
        stopwatch = new Stopwatch(this);
    }

    /**
     * Startuje stoper.
     */
    public void startStopwatch()
    {
        stopwatch.start();
    }

    /**
     * Wstrzymuje pracę stopera.
     */
    public void pauseStopwatch()
    {
        stopwatch.stop();
    }

    /**
     * Kończy pracę stopera.
     */
    public void endStopwatch()
    {
        stopwatch.end();
    }

    /**
     * Pobiera łańcuch znaków reprezentujący czas stopera.
     *
     * @return łańcuch znaków reprezentujący czas stopera w postaci: HH:MM:SS
     */
    public String getTime()
    {
        return stopwatch.toString();
    }

    /**
     * Serializuje etykietę, zapisując do strumienia obiektowego stoper.
     *
     * @param out wyjściowy strumień obiektowy
     *
     * @throws IOException błąd zapisu do strumienia obiektowego
     */
    public void serialize(ObjectOutputStream out) throws IOException
    {
        out.writeObject(stopwatch);
    }

    /**
     * Deserializuje etykietę, wczutując stoper ze strumienia obiektowego.
     *
     * @param in wejściowy strumień obiektowy
     *
     * @throws IOException bład odczytu ze strumienia obiektowego
     * @throws ClassNotFoundException błąd odczytu zserializowanej klasy
     */
    public void deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        stopwatch = (Stopwatch) in.readObject();
        stopwatch.setLabel(this);
    }

    private static final String DEFAULT_TIME = "00:00:00";

    private Stopwatch stopwatch;
}

