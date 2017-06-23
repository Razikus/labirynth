/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinth.server.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

/**
 * Klasa reprezentująca Serializator - służy do serializowania i odserializowywania klas.
 * @author Slepy
 */
public class Serializator {


     /**
     *
     * @param o Obiekt implementujący intefejs Serializable, który ma być przekształcony do String
     * @return Zwraca obiekt zamieniony na String
     * @throws IOException
     */

    public static String toString(Serializable o) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray()); 
    }

     /**
     *
     * @param s Obiekt zamieniony wcześniej na String przy pomocy tego Serializatora
     * @return Zwraca obiekt zdeserializowany
     * @throws IOException
     * @throws ClassNotFoundException Generuje wyjątek, kiedy nie jest znalziona klasa
     */
    
    public static Object fromString(String s) throws IOException ,
                                                       ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode( s );
        ObjectInputStream ois = new ObjectInputStream( 
                                        new ByteArrayInputStream( data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
   }        
    
}
