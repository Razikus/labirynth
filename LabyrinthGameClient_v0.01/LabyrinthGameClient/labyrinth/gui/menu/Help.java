package labyrinth.gui.menu;

import labyrinth.control.OperationsControl;

import javax.swing.*;

/**
 * Klasa reprezentująca menu pomocy.
 *
 * @author Zuzanna Łaś
 */
public class Help extends JMenu
{
    /**
     * Tworzy menu pomocy i ustawia kontroler programu.
     *
     * @param gameController kontroler programu
     */
    public Help(OperationsControl gameController)
    {
        super("Pomoc");
        JMenuItem aboutProgram = new JMenuItem("O programie", new ImageIcon("images/info.png"));
        aboutProgram.addActionListener(event -> gameController.showGameInformation());
        add(aboutProgram);
    }

    /**
     * Wyświetla okienko z informacją o programie.
     *
     * @param frame ramka będąca właścicielem okienka
     */
    public void showGameInformation(JFrame frame)
    {
        ImageIcon icon = new ImageIcon("images/aboutGameIcon.png");
        JOptionPane.showMessageDialog(frame,
                "<html>Autorzy:<br><br><h3>Zuzanna Łaś<br>Damian Mądrzyk<br>Rafał Radziejewski<br>Adam Raźniewski</h3></html>", 
                "O programie", JOptionPane.PLAIN_MESSAGE, icon);
    }

    private OperationsControl gameController;
}

