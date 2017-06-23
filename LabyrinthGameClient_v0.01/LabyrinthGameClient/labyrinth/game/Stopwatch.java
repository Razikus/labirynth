package labyrinth.game;

import javax.swing.JLabel;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.IOException;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Klasa reprezentuje stoper odmierzający czas.
 *
 * @author Zuzanna Łaś
 */
public class Stopwatch implements Serializable
{
    /**
     * Tworzy obiekt stopera wyświetlajacego czas na podanej etykiecie.
     *
     * @param label etykieta wyświetlająca czas stopera
     */
    public Stopwatch(JLabel label)
    {
        this.label = label;
        timer = new Timer(MILLISECONDS_PER_SECOND, new StopwatchAction());
    }

    /**
     * Ustawia etykietę wyświetlającą czas stopera.
     *
     * @param newLabel nowa etykieta wyświetlająca czas
     */
    public void setLabel(JLabel newLabel)
    {
        if (label == null) {
            label = newLabel;
            label.setText(toString());
        }
    }

    /**
     * Ustawia czas stopera.
     *
     * @param hour liczba godzin
     * @param minute liczba minut
     * @param second liczba sekund
     */
    public void setTime(int hour, int minute, int second)
	{
	    setHour(hour);
		setMinute(minute);
		setSecond(second);
        label.setText(toString());
	}

    /**
     * Ustawia godzinę.
     *
     * @param hour nowa ustawiana godzina
     */
	private void setHour(int hour)
	{
        if (hour < 0) {
			this.hour = 0;
		} else {
            this.hour = hour;
        }
	}

    /**
     * Ustawia minutę.
     *
     * @param minute nowa ustawiana minuta
     */
	private void setMinute(int minute)
	{
		if (minute > 59) {
			this.minute = 59;
		} else if (minute < 0) {
			this.minute = 0;
		} else {
            this.minute = minute;
        }
	}

    /**
     * Ustawia sekundę.
     *
     * @param second nowa ustawiana sekunda
     */
	private void setSecond(int second)
	{
		if (second > 59) {
			this.second = 59;
		} else if (second < 0) {
			this.second = 0;
		} else {
            this.second = second;
        }
	}

    /**
     * Zwiększa godzinę o sekundę.
     */
    private void increaseOfTheSecond()
	{
		if (second < 59) {
		    ++second;
		} else {
            second = 0;
            if (minute < 59) {
                ++minute;
            } else {
                minute = 0;
                ++hour;
            }
        }
	}

    /**
     * Zwraca łańcuch znaków reprezentujący godzinę wskazywaną przez stoper.
     *
     * @return łańcuch znaków reprezentujący godzinę
     */
    public String toString()
    {
		String hourFormat = (hour < 10 ? "0" : "") + hour;
		String minuteFormat = (minute < 10 ? "0" : "") + minute;
		String secondFormat = (second < 10 ? "0" : "") + second;
		return hourFormat + ":" + minuteFormat + ":" + secondFormat;
    }

    /**
     * Wczytuje obiekt ze strumienia obiektowego.
     *
     * Przywraca dokładny stan obiektu sprzed serializacji, uruchamiając stoper.
     *
     * @param in wejściowy strumień obiektowy
     *
     * @throws IOException błąd odczytu ze strumienia obiektowego
     * @throws ClassNotFoundException błąd odczytu zserializowanej klasy
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        timer = new Timer(MILLISECONDS_PER_SECOND, new StopwatchAction());
        timer.start();
        if (isPause) {
            timer.stop();
        }
    }

    /**
     * Uruchamia stoper.
     */
    public void start()
    {
        timer.start();
        if (isPause) {
            isPause = false;
        }
    }

    /**
     * Wstrzymuje pracę stopera.
     */
    public void stop()
    {
        if (!isPause) {
            isPause = true;
            timer.stop();
        }
    }

    /**
     * Kończy pracę stopera, zerując jego ustawienia czasu.
     */
    public void end()
    {
        if (isPause) {
            isPause = false;
        } else {
            timer.stop();
        }
        setTime(0, 0, 0);
    }

    /**
     * Klasa reprezentująca akcję wykonywaną na rzecz stopera w sekundowych
     * odstępach czasu.
     */
    private class StopwatchAction implements ActionListener
    {
        /**
         * Akcja wykonywana na rzecz stopera.
         *
         * @param event zdarzenie generujące akcję
         */
        public void actionPerformed(ActionEvent event)
        {
            increaseOfTheSecond();
            label.setText(Stopwatch.this.toString());
        }
    }

    private static final int MILLISECONDS_PER_SECOND = 1000;

    private int hour;
    private int minute;
    private int second;
    private boolean isPause;
    private transient JLabel label;
    private transient Timer timer;
}

