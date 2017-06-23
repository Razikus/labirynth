/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinth.server.serialization;

import java.io.Serializable;

/**
 * Klasa reprezentująca Koordynat, jedną z podstawowych jednostek w labiryncie.
 * @author informatyka
 */
public class Coord implements Serializable {

    /**
     *
     * @param x parametr odpowiadający za xowy koordynat pola.
     * @param y parametr odpowiadający za yowy koordynat pola.
     */
    public Coord(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    /**
     *
     * @return Zwraca xowy koordynat pola.
     */
    public int getX()
    {
        return x;
    }
    
    /**
     *
     * @return Zwraca yowy koordynat pola.
     */
    public int getY()
    {
        return y;
    }
    
    @Override
    public String toString()
    {
        return "<" + x + ", " + y + ">";
    }
    
    private int x;
    private int y;
}
