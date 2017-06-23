/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinth.client.configuration;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Klasa służąca do ładowania konfiguracji i uzyskiwania z niej danych.
 * @author Slepy
 */
public class Configuration {
    public static boolean load()
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
                
                ip = e.getElementsByTagName("ip").item(0).getTextContent();
                port = Integer.parseInt(e.getElementsByTagName("port").item(0).getTextContent());
                
            }
            return true;
        } catch (Exception ex) {
            System.out.println("Brak konfiguracji, lub blad z nia!");
            return false;
        }
        
    }
     /**
     *
     * @return Zwraca ip odczytane z konfiguracji
     */

    public static String getIp() {
        return ip;
    }

     /**
     *
     * @return Zwraca port odczytany z konfiguracji
     */
    public static int getPort() {
        return port;
    }
    
    
    private static String ip;
    private static int port;
    
}
