/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinth.client.serverclient;

import labyrinth.server.serialization.Serializator;
import labyrinth.server.serialization.Coord;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 * Klasa reprezentująca wątek połączenia oraz komunikacji z serwerem
 * @author Slepy
 */
public class ServerClient implements Runnable{
     /**
     *
     * @param server Parametr odpowiadający za ip serwera
     * @param port Parametr odpowiadający za port serwera
     */
    public ServerClient(String server, int port)
    {
        this.server = server;
        this.port = port;
    }

     /**
     *
     * @return Zwraca wartosc logiczna odpowiadajaca w przypadku udanego polaczenia TRUE
     *         bądź FALSE w przypadku problemu z połączeniem
     */
    
    public boolean connect() throws IOException
    {
        s = new Socket(server, port);
    
        inS = new BufferedReader(new InputStreamReader(s.getInputStream()));
        outS = new PrintWriter(s.getOutputStream(), true); 

        if(s.isBound())
        {
            return true;
        }
            
        return false;
    }
    
    public void start()
    {
        start = true;
        thread = new Thread(this);
        thread.start();
    }
    
    public void stop()
    {
        start = false;
    }

    /**
     *
     * @param what Parametr odpowiadający komende wysylana do serwera
     * 
     */
    
    public void sendCommand(String what) throws InterruptedException
    {
        this.preparedCommand = what;
        action(true);
        while(getAction())
        {
            Thread.sleep(100);
        }
    }

    /**
     *
     * @return Zwraca wartosc logiczna, kiedy otrzymuje odpowiedź z serwera równą TRUE
     */
    
    public boolean waitForResponse()
    {
        while(getAction())
        {
            this.isWaiting = true;
        }
        this.isWaiting = false;
        return true;
    }


    /**
     *
     * @param x Parametr odpowiadający szerokości potrzebnego labiryntu
     * @param y Parametr odpowiadający wysokości potrzebnego labiryntu
     * @return Zwraca zdeserializowany labirynt
     */


    
    public ArrayList<Coord> getLabirynt(int x, int y) throws IOException, ClassNotFoundException
    {
        this.preparedCommand = "/getLabirynt " + x + " " + y;
        action(true);
        waitForResponse();
	    Coord[] response = (Coord[]) Serializator.fromString(this.response);
	    return new ArrayList<Coord>(Arrays.asList(response));
    }

    /**
     *
     * @param act Parametr reprezentujacy czy klient wyslal jakas komende
     * 
     */
    
    public synchronized void action(boolean act)
    {
        this.action = act;
    }

     /**
     *
     * @return Zwraca wartosc logiczna odpowiadajaca TRUE - jeżeli klient wyslal jakas komende
     * bądź FALSE jeśli nie
     */
    
    public synchronized boolean getAction()
    {
        return this.action;
    }


     /**
     *
     * @return Zwraca odpowiedź serwera na komende
     */
    
    public synchronized String getResponse()
    {
        return this.response;
    }
    
    @Override
    public void run() {
        while(start)
        {
            if(getAction())
            {
                outS.println(preparedCommand);
                try {
                    response = inS.readLine();
                } catch (IOException ex) {
                    Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                action(false);
            }
            
        }
    }
           
    
    private boolean action;
    
    private String response;
    private String preparedCommand;
    private BufferedReader inS;
    private PrintWriter outS;
    private Socket s;
    private String server;
    private int port;
    private boolean start;
    
    private boolean isWaiting;
    
    private Thread thread;
}

