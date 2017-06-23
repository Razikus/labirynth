import labyrinth.gui.*;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JDialog;

/**
 * Klasa uruchamiająca grę "Labirynt".
 *
 * @author Zuzanna Łaś
 */
public class LabyrinthGame
{
    /**
     * @param args argumenty programu
     */
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
            {
                public void run()
                {
                    JDialog.setDefaultLookAndFeelDecorated(true);
                    JFrame frame = new GameFrame();
                    frame.setVisible(true);
                }
            });
    }
}

