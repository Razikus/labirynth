/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinth.server.configuration;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Klasa służąca do ładowania konfiguracji i uzyskiwania z niej danych.
 * @author informatyka
 */
public class Configuration {

    /**
     *
     */
    public static void load()
    {
        try {
            File xMLF = new File("./config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xMLF);
            doc.getDocumentElement().normalize();
            
            NodeList nList = doc.getElementsByTagName("Server");
            
            for(int temp = 0; temp < nList.getLength(); temp++)
            {
                Node nNode = nList.item(temp);
                
                Element e = (Element) nNode;
                
                logging = Boolean.parseBoolean(e.getElementsByTagName("logging").item(0).getTextContent());
                port = Integer.parseInt(e.getElementsByTagName("port").item(0).getTextContent());
                logpath = e.getElementsByTagName("logpath").item(0).getTextContent();
                
            }
        } catch (Exception ex) {
            System.out.println("Brak konfiguracji, lub blad z nia!");
            System.exit(1);
        }
        
    }

    /**
     *
     * @return Zwraca port odczytany z konfiguracji
     */
    public static int getPort() {
        return port;
    }
    
    /**
     *
     * @return Zwraca ścieżkę do logów z konfiguracji
     */
    public static String getLogpath() {
        return logpath;
    }
    
    /**
     *
     * @return Zwraca true jeśli aktualnie jeżeli serwer loguje do pliku
     */
    public static boolean isLogging()
    {
        return logging;
    }
    

    
    private static boolean logging;
    private static int port;
    private static String logpath;
}
