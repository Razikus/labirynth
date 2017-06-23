package labyrinth.server.maze;


import java.util.ArrayList;
import labyrinth.server.serialization.Coord;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Klasa reprezentująca uproszczony dostęp do funkcji labiryntów.
 * @author informatyka
 */
public class LabUtils {
    
    /**
     *
     * @param x Parametr odpowiadający za szerokość labiryntu
     * @param y Parametr odpowiadający za wysokość labiryntu
     * @return Zwraca wygenerowany labirynt
     */
    public static ArrayList<Coord> genLab(int x, int y)
    {
        return Maze.generateLabyrinth(x, y);
    }
}
