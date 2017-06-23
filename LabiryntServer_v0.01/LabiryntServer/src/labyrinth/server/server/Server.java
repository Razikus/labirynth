/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinth.server.server;

import labyrinth.server.configuration.Configuration;
import labyrinth.server.clientthread.ClientThread;
import labyrinth.server.console.Logger;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Klasa reprezentująca Server - jest klasą posiadającą funkcję main.
 * @author informatyka
 */
public class Server {

    /**
     *
     * @param args
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void main(String args[]) throws IOException, ClassNotFoundException
    {
        
        
        Configuration.load();
        Logger.setPath(Configuration.getLogpath());
        System.out.println("Server started at port: " + Configuration.getPort());
        Logger.log("TURN ON SERVER");
        
        try
        {
            ServerSocket s = new ServerSocket(Configuration.getPort());
            while(true)
            {
                Socket inc = s.accept();
                System.out.println(inc.getInetAddress() + "/" + "OPENED CONNECTION");
                Logger.log(inc.getInetAddress() + "/" + "OPENED CONNECTION");
                Runnable r = new ClientThread(inc);
                Thread t = new Thread(r);
                t.start();
            }
            
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        
    }
}
