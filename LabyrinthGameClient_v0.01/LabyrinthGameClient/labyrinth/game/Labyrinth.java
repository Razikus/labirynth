package labyrinth.game;

import labyrinth.control.OperationsControl;
import labyrinth.server.serialization.Coord;

import java.awt.*;
import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.TreeSet;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

/**
 * Klasa reprezentuje abstrakcyjny labirynt.
 *
 * @author Zuzanna Łaś
 */
public class Labyrinth implements Serializable
{
    /**
     * Tworzy labirynt na podstawie podanej listy współrzędnych ścianek
     * i ustawia kontroler gry.
     *
     * @param coordinates lista współrzędnych ścianek labiryntu
     * @param gameController kontroler gry
     */
    public Labyrinth(ArrayList<Coord> coordinates, OperationsControl gameController)
    {
        if (coordinates != null && coordinates.size() >= 1) {
            start = new Field(coordinates.get(0));
            meta = new Field(coordinates.get(coordinates.size() - 1));
            createWalls(coordinates);
            pawn = new Pawn(start);
        }
        this.gameController = gameController;
    }

    /**
     * Zamienia listę współrzędnych ścianek na listę pól labiryntu.
     *
     * @param coordinates współrzędne ścianek labiryntu
     */
    private void createWalls(ArrayList<Coord> coordinates)
    {
        walls = new TreeSet<>();
        for (int j = 1; j < coordinates.size() - 1; ++j) {
            Coord element = coordinates.get(j);
            walls.add(new Field(element));
        }
    }

    /**
     * Rysuje labirynt w odpowiednim miejscu zależnym od lewego i górnego przesunięcia
     * względem krawędzi panelu planszy gry.
     *
     * @param g obiekt rysujący
     * @param leftOffset przesunięcie wzglądem lewej krawędzi panelu
     * @param upOffset przesunięcie względem górnej krawędzi panelu
     */
    public void draw(Graphics g, int leftOffset, int upOffset)
    {
        drawLabyrinthBackground(g, leftOffset, upOffset);
        g.setColor(Color.BLACK);
        int fieldSize = Field.getSize();
        for (Field wall : walls) {
            int x = wall.getXCoordinate();
            int y = wall.getYCoordinate();
            int leftFieldOffset = leftOffset + (fieldSize * (x - 1));
            int upFieldOffset = upOffset + ((height - y) * fieldSize);
            wall.draw(g, leftFieldOffset, upFieldOffset);
        }
        drawPawn(g, leftOffset, upOffset);
    }

    /**
     * Rysuje tło labiryntu w odpowiednim miejscu zależnym od lewego i górnego przesunięcia
     * względem krawędzi panelu planszy gry.
     *
     * @param g obiekt rysujący
     * @param leftOffset przesunięcie wzglądem lewej krawędzi panelu
     * @param upOffset przesunięcie względem górnej krawędzi panelu
     */
    private void drawLabyrinthBackground(Graphics g, int leftOffset, int upOffset)
    {
        g.setColor(Color.WHITE);
        int fieldSize = Field.getSize();
        g.fillRect(leftOffset, upOffset, width * fieldSize, height * fieldSize);
    }

    /**
     * Rysuje pionek w odpowiednim miejscu zależnym od lewego i górnego przesunięcia
     * względem krawędzi panelu planszy gry.
     *
     * @param g obiekt rysujący
     * @param leftOffset przesunięcie wzglądem lewej krawędzi panelu
     * @param upOffset przesunięcie względem górnej krawędzi panelu
     */
    private void drawPawn(Graphics g, int leftOffset, int upOffset)
    {
        Field currentPawnField = pawn.getCurrentField();
        int fieldSize = Field.getSize();
        int x = currentPawnField.getXCoordinate();
        int y = currentPawnField.getYCoordinate();
        int leftFieldOffset = leftOffset + (fieldSize * (x - 1));
        int upFieldOffset = upOffset + ((height - y) * fieldSize);
        pawn.draw(g, leftFieldOffset, upFieldOffset);
    }

    /**
     * Przesuwa pionek w prawo.
     *
     * Jeżeli ruch na wskazaną pozycję nie jest możliwy, pionek nie zmienia
     * swojego położenia.
     */
    public void movePawnRight()
    {
        Field rightField = pawn.goRight();
        checkField(rightField);
    }

    /**
     * Przesuwa pionek w lewo.
     *
     * Jeżeli ruch na wskazaną pozycję nie jest możliwy, pionek nie zmienia
     * swojego położenia.
     */
    public void movePawnLeft()
    {
        Field leftField = pawn.goLeft();
        checkField(leftField);
    }

    /**
     * Przesuwa pionek do góry.
     *
     * Jeżeli ruch na wskazaną pozycję nie jest możliwy, pionek nie zmienia
     * swojego położenia.
     */
    public void movePawnUp()
    {
        Field upField = pawn.goUp();
        checkField(upField);
    }

    /**
     * Przesuwa pionek w dół.
     *
     * Jeżeli ruch na wskazaną pozycję nie jest możliwy, pionek nie zmienia
     * swojego położenia.
     */
    public void movePawnDown()
    {
        Field downField = pawn.goDown();
        checkField(downField);
    }

    /**
     * Sprawdza pole podane jako argument.
     *
     * Jeżeli sprawdzane pole okaże się polem mety, kontroler jest informowany o zwycięskim
     * zakończeniu gry.
     *
     * @param field podane pole
     */
    public void checkField(Field field)
    {
        if (field != null) {
            if (field.equals(meta)) {
                gameController.specialRepaintGameBoard();
                gameController.endWinnerGame();
            } else {
                gameController.repaintGameBoard();
            }
        }
    }

    /**
     * Zapisuje obiekt do strumienia obiektowego.
     *
     * Metoda wspomaga proces serializacji ze względu na konieczność zapisania
     * wartości pól statycznych.
     *
     * @param out wyjściowy strumień obiektowy
     *
     * @throws IOException błąd zapisu do strumienia obiektowego
     */
    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
        out.writeInt(width);
        out.writeInt(height);
        out.writeObject(pawnImage);
    }

    /**
     * Wczytuje obiekt ze strumienia obiektowego.
     *
     * Metoda wspomaga proces serializacji ze względu na konieczność odczytu
     * wartości i inicjalizacji pól statycznych.
     *
     * @param in wejściowy strumień obiektowy
     *
     * @throws IOException błąd odczytu ze strumienia obiektowego
     * @throws ClassNotFoundException błąd odczytu zserializowanej klasy
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        width = in.readInt();
        height = in.readInt();
        pawnImage = (String) in.readObject();
    }

    /**
     * Ustawia kontroler gry na podany.
     *
     * @param gameController kontroler gry
     */
    public void setGameController(OperationsControl gameController)
    {
        this.gameController = gameController;
    }

    /**
     * Ustawia szerokość labiryntu.
     *
     * @param newWidth nowa szerokość labiryntu
     */
    public static void setWidth(int newWidth)
    {
        width = newWidth; 
    }

    /**
     * Zwraca szerokość labiryntu.
     *
     * @return bieżąca szerokość labiryntu
     */
    public static int getWidth()
    {
        return width;
    }

    /**
     * Ustawia wysokość labiryntu na podaną.
     *
     * @param newHeight nowa wysokość labiryntu
     */
    public static void setHeight(int newHeight)
    {
        height = newHeight; 
    }

    /**
     * Zwraca wysokość labiryntu.
     *
     * @return bieżąca wysokość labiryntu
     */
    public static int getHeight()
    {
        return height;
    }

    /**
     * Ustawia obrazek pionka.
     *
     * @param pawnImagePath ścieżka do pliku z obrazkiem pionka
     */
    public static void setPawnImage(String pawnImagePath)
    {
        pawnImage = pawnImagePath; 
    }

    /**
     * Zwraca obrazek pionka.
     *
     * @return ciąg znaków reprezentujący ścieżkę do pliku z obrazkiem pionka
     */
    public static String getPawnImage()
    {
        return pawnImage;
    }

    /**
     * Klasa reprezentuje abstrakcyjny pionek w labiryncie.
     */
    private class Pawn implements Serializable
    {
        /**
         * Tworzy pionek na podstawie podanego pola.
         *
         * @param start startowe pole, na którym jest ustawiany pionek
         */
        public Pawn(Field start)
        {
            currentField = start;
        }

        /**
         * Zwraca bieżące pole, na którym znajduje się pionek
         *
         * @return pole, na którym w danej chwili znajduje się pionek
         */
        public Field getCurrentField()
        {
            return currentField;
        }

        /**
         * Przesuwa pionek w prawo.
         *
         * Jeżeli przesunięcie nie jest możliwe, pionek nie zmienia swojej pozycji.
         *
         * @return pole, na którym znajduje się pionek po wykonaniu ruchu
         */
        public Field goRight()
        {
            Field rightField = currentField.getRightField();
            return checkAndSetField(rightField);
        }

        /**
         * Przesuwa pionek w lewo.
         *
         * Jeżeli przesunięcie nie jest możliwe, pionek nie zmienia swojej pozycji.
         *
         * @return pole, na którym znajduje się pionek po wykonaniu ruchu
         */
        public Field goLeft()
        {
            Field leftField = currentField.getLeftField();
            return checkAndSetField(leftField);
        }

        /**
         * Przesuwa pionek w do góry.
         *
         * Jeżeli przesunięcie nie jest możliwe, pionek nie zmienia swojej pozycji.
         *
         * @return pole, na którym znajduje się pionek po wykonaniu ruchu
         */
        public Field goUp()
        {
            Field upField = currentField.getUpField();
            return checkAndSetField(upField);
        }

        /**
         * Przesuwa pionek na dół.
         *
         * Jeżeli przesunięcie nie jest możliwe, pionek nie zmienia swojej pozycji.
         *
         * @return pole, na którym znajduje się pionek po wykonaniu ruchu
         */
        public Field goDown()
        {
            Field downField = currentField.getDownField();
            return checkAndSetField(downField);
        }

        /**
         * Sprawdza i przesuwa pionek, ustawiając sprawdzone pole jako bieżące, o ile
         * ruch jest możliwy.
         *
         * @param checkedField sprawdzane pole
         *
         * @return pole, na które został przesunięty pionek lub null, jeżeli ruch
         *         nie może zostać wykonany
         */
        private Field checkAndSetField(Field checkedField)
        {
            int x = checkedField.getXCoordinate();
            int y = checkedField.getYCoordinate();
            if (walls.contains(checkedField) || (x < 1 || x > width || y < 1 || y > height)) {
                return null;
            }
            currentField = checkedField;
            return currentField;
        }

        /**
         * Rysuje pionek w odpowiednim miejscu zależnym od lewego i górnego przesunięcia
         * względem krawędzi panelu planszy gry.
         *
         * @param g obiekt rysujący
         * @param leftOffset przesunięcie wzglądem lewej krawędzi panelu
         * @param upOffset przesunięcie względem górnej krawędzi panelu
         */
        public void draw(Graphics g, int leftOffset, int upOffset)
        {
            ImageIcon icon = new ImageIcon(pawnImage);
            int fieldSize = Field.getSize();
            g.drawImage(icon.getImage(), leftOffset, upOffset, fieldSize, fieldSize, null);
        }

        private Field currentField;
    }

    private static int width;
    private static int height;
    private static String pawnImage;

    private Field start;
    private Field meta;
    private Pawn pawn;
    private TreeSet<Field> walls;
    private transient OperationsControl gameController;
}

