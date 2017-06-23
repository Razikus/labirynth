package labyrinth.game;

import labyrinth.server.serialization.Coord;

import java.awt.Graphics;
import java.io.Serializable;

/**
 * Klasa reprezentuje abstrakcyjne pole w labiryncie.
 *
 * @author Zuzanna Łaś
 */
public class Field implements Comparable<Field>, Serializable
{
    /**
     * Tworzy polę dla podanej współrzędnej.
     *
     * @param coordinates współrzędna pola labiryntu
     */
    public Field(Coord coordinates)
    {
        labyrinthCoordinates = coordinates;
    }

    /**
     * Rysuje pole w odpowiednim miejscu zależnym od lewego i górnego przesunięcia
     * względem krawędzi panelu planszy gry.
     *
     * @param g obiekt rysujący
     * @param leftOffset przesunięcie wzglądem lewej krawędzi panelu
     * @param upOffset przesunięcie względem górnej krawędzi panelu
     */
    public void draw(Graphics g, int leftOffset, int upOffset)
    {
        g.fillRect(leftOffset, upOffset, size, size);
    }

    /**
     * Zwraca współrzędną x pola.
     *
     * @return współrzędna x
     */
    public int getXCoordinate()
    {
        return labyrinthCoordinates.getX();
    }

    /**
     * Zwraca współrzędną y pola.
     *
     * @return współrzędna y
     */
    public int getYCoordinate()
    {
        return labyrinthCoordinates.getY();
    }

    /**
     * Porównuje bieżące pole do obiektu podanego jako argument.
     *
     * @param otherObject obiekt przekazany do porównania
     *
     * @return true, jeżeli porównywane obiekty są polami i mają identyczne współrzędne
     */
    public boolean equals(Object otherObject)
    {
        if (otherObject == this) {
            return true;
        }
        if (otherObject == null) {
            return false;
        }
        if (!(otherObject instanceof Field)) {
            return false;
        }
        Field otherField = (Field) otherObject;
        return getXCoordinate() == otherField.getXCoordinate() && getYCoordinate() == otherField.getYCoordinate();
    }

    /**
     * Porównuje bieżące pole do pola podanego jako argument.
     *
     * @param otherField inne pole przekazane do porównania
     *
     * @return 0, jeżeli pola mają identyczne współrzędne;
     *        -1, jeżeli współrzędna x bieżącego pola jest mniejsza od pola porównywanego,
     *            a w wypadku identycznych współrzędnych x - jeżeli współrzędna y jest mniejsza;
     *         1, jeżeli współrzędna x bieżącego pola jest większa od pola porównywanego,
     *            a w wypadku identycznych współrzędnych x - jeżeli współrzędna y jest większa
     */
    public int compareTo(Field otherField)
    {
        if (getXCoordinate() < otherField.getXCoordinate()) {
            return -1;
        } else if (getXCoordinate() > otherField.getXCoordinate()) {
            return 1;
        } else {
            if (getYCoordinate() < otherField.getYCoordinate()) {
                return -1;
            } else if (getYCoordinate() > otherField.getYCoordinate()) {
                return 1;
            }
            return 0;
        }
    }

    /**
     * Tworzy i zwraca pole o współrzędnej x + 1 względem pola bieżącego.
     *
     * @return pole sąsiadujące z prawej z polem bieżącym
     */
    public Field getRightField()
    {
        int x = labyrinthCoordinates.getX();
        int y = labyrinthCoordinates.getY();
        Coord coordinates = new Coord(x + 1, y);
        Field right = new Field(coordinates);
        return right;
    }

    /**
     * Tworzy i zwraca pole o współrzędnej x - 1 względem pola bieżącego.
     *
     * @return pole sąsiadujące z lewej z polem bieżącym
     */
    public Field getLeftField()
    {
        int x = labyrinthCoordinates.getX();
        int y = labyrinthCoordinates.getY();
        Coord coordinates = new Coord(x - 1, y);
        Field left = new Field(coordinates);
        return left;
    }

    /**
     * Tworzy i zwraca pole o współrzędnej y + 1 względem pola bieżącego.
     *
     * @return pole sąsiadujące od góry z polem bieżącym
     */
    public Field getUpField()
    {
        int x = labyrinthCoordinates.getX();
        int y = labyrinthCoordinates.getY();
        Coord coordinates = new Coord(x, y + 1);
        Field up = new Field(coordinates);
        return up;
    }

    /**
     * Tworzy i zwraca pole o współrzędnej y - 1 względem pola bieżącego.
     *
     * @return pole sąsiadujące od dołu z polem bieżącym
     */
    public Field getDownField()
    {
        int x = labyrinthCoordinates.getX();
        int y = labyrinthCoordinates.getY();
        Coord coordinates = new Coord(x, y - 1);
        Field down = new Field(coordinates);
        return down;
    }

    /**
     * Ustawia wspólny rozmiar wszystkich obiektów klasy Field.
     *
     * @param newSize nowy podany rozmiar pól
     */
    public static void setSize(int newSize)
    {
        size = newSize;
    }

    /**
     * Zwraca wspólny rozmiar wszystkich obiektów klasy Field.
     *
     * @return rozmiar pól
     */
    public static int getSize()
    {
        return size;
    }

    private static int size;
    
    private Coord labyrinthCoordinates;
}

