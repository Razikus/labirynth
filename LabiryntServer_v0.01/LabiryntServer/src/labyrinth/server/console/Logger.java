/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinth.server.console;

import labyrinth.server.configuration.Configuration;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Klasa reprezentująca Logger - uproszczone logowanie danych do pliku.
 * @author informatyka
 */
public class Logger {
    
    /**
     *
     * @param logPath Parametr w którym przekazujemy ścieżkę do zapisywania logów
     */
    public static void setPath(String logPath)
    {
        Logger.logPath = logPath;
    }

    /**
     *
     * @param what Parametr w którym przekazujemy co chcemy zalogować
     * @throws IOException
     */
    public static void log(String what) throws IOException
    {
        if(Configuration.isLogging())
        {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(logPath, true)));
        
        
            out.println(Console.getCurrentDate() + "|" + what);
            out.close();
        }
    }
    
    
    private static String logPath;
}
