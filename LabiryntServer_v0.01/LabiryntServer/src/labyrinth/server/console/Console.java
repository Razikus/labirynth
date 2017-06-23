/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinth.server.console;

import labyrinth.server.maze.LabUtils;
import labyrinth.server.serialization.Serializator;
import labyrinth.server.clientthread.ClientThread;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import labyrinth.server.serialization.Coord;

/**
 *
 * @author informatyka
 */
public class Console {
    
    /**
     * Klasa reprezentująca konsolę serwera - potrafi przetwarzać dane na polecenia i zwracać odpowiednią odpowiedź.
     * @return Zwraca aktualną datę w ładnym formacie
     */
    public static String getCurrentDate()
    {
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss"); 
        String dateNow = formatter.format(currentDate.getTime());
        
        return dateNow;
    }
            
    /**
     *
     * @param what Parametr w którym określamy jakie polecenie chcemy rozpoznać
     * @param client Parametr w którym określamy wysyłającego to polecenie
     * @throws IOException
     * @throws SQLException
     */
    public static void checkAndDo(String what, ClientThread client) throws IOException, SQLException
    {
        String response;
        if(what.startsWith("/getLabirynt"))
        {
            String[] splitted = what.split(" ");
            try {
                int x = Integer.parseInt(splitted[1]);
                int y = Integer.parseInt(splitted[2]);
                ArrayList<Coord> lab = LabUtils.genLab(x, y);
                response = Serializator.toString(lab.toArray(new Coord[lab.size()]));
            }
            catch(Exception e)
            {
                response = "ERROR";
            }
            client.broadcast(response);
            
        }
        else
        {
            response = "COMMAND ERROR";
            client.broadcast(response);
               
        }
        System.out.println("SERVER -> " + client.getSocket().getInetAddress() + " : " + response);
        labyrinth.server.console.Logger.log("SERVER -> " + client.getSocket().getInetAddress() + " : " + response);
    }
    
}
