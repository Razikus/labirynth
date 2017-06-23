/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinth.server.clientthread;

import labyrinth.server.console.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa reprezentująca wątek klienta.
 * @author informatyka
 */
public class ClientThread implements Runnable {
    
    /**
     *
     * @param incoming Parametr odpowiadający za przychodzące połączenie z klientem
     */
    public ClientThread(Socket incoming)
    {
        this.incoming = incoming; 
        addToList(this);
        
    }
    
    /**
     *
     * @return Zwraca socket odpowiadający klientowi
     */
    public Socket getSocket()
    {
        return this.incoming;
    }
    
    private static void addToList(ClientThread client)
    {
        clientList.add(client);
    }
    
    
    private static void removeFromList(ClientThread client)
    {
        clientList.remove(client);
    }
    
    /**
     *
     * @param what parametr określający co ma być wysłane do klienta
     */
    public void broadcast(String what)
    {
        out.println(what);
        
    }
    
    @Override
    public void run() {
        try
        {  
            try
            {
                InputStream inStream = incoming.getInputStream();
                OutputStream outStream = incoming.getOutputStream();
            
                in = new Scanner(inStream);         
                out = new PrintWriter(outStream, true);
            
                
                boolean done = false;
                while (!done && in.hasNextLine())
                {  
                    String line = in.nextLine();  
                    labyrinth.server.console.Logger.log(incoming.getInetAddress() + " -> Server : " + line);
                    System.out.println(incoming.getInetAddress() + " -> Server : " + line);
                    
                    try {
                        Console.checkAndDo(line, this);
                        
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                              
                    
                    if (line.trim().equals("/QUIT"))
                    {
                        done = true;
                    }
                }
            }
            finally
            {
                System.out.println(incoming.getInetAddress() + "/" + "CLOSED CONNECTION");
                
                labyrinth.server.console.Logger.log(incoming.getInetAddress() + "/" + "CLOSED CONNECTION");
                
                
                removeFromList(this);
                incoming.close();
            }
        }
        catch (IOException e)
        {  
            e.printStackTrace();
        }
    }
    
    private static ArrayList<ClientThread> clientList = new ArrayList<>();
    
    private Scanner in;
    private PrintWriter out;
    private Socket incoming;
}
