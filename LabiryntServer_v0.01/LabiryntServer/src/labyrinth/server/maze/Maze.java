package labyrinth.server.maze;

import java.util.*;
import labyrinth.server.serialization.Coord;

public class Maze {

        private static final int WALL = 0;
        private static final int SPACE = 1;
	private int xStart;
	private int xMeta;

        private byte[][] data;
        private int width;
        private int height;
        private java.util.Random rand = new java.util.Random();

    /**
     *
     * @param width - parametr odpowiadający szerokości zadanego labiryntu
     * @param height - parametr odpowiadający wysokości zadanego labiryntu
     */
    public Maze(int width, int height) {
            this.width = width;
            this.height = height;
            data = new byte[width][];
            generate();
        }

    /**
     * 
     * @param x - xowy coord startu rzeźbienia labiryntu
     * @param y - yowy coord startu rzeźbienia labiryntu
     */
        private void carve(int x, int y) {

            final int[] upx = {1, -1, 0, 0};
            final int[] upy = {0, 0, 1, -1};

            int dir = rand.nextInt(4);
            int count = 0;
            while (count < 20) {
                final int x1 = x + upx[dir];
                final int y1 = y + upy[dir];
                final int x2 = x1 + upx[dir];
                final int y2 = y1 + upy[dir];
                if (data[x1][y1] == WALL && data[x2][y2] == WALL) {
                    data[x1][y1] = SPACE;
                    data[x2][y2] = SPACE;
                    carve(x2, y2);
                } else {
                    dir = rand.nextInt(4);
                    count += 1;
                }
            }
        }

    /**
     * Odpowiada za generowanie labiryntu
     */
    public void generate() {
			//wypelnienie labiryntu scianami
            for (int x = 0; x < width; x++) {
                data[x] = new byte[height];
                for (int y = 0; y < height; y++) {
                    data[x][y] = WALL;
                }
            }
			//tworzenie poziomej otoczki wolnej przestrzeni(ograniczenie poziome)
            for (int x = 0; x < width; x++) {
                data[x][0] = SPACE;
                data[x][height - 1] = SPACE;
            }
			//tworzenie pionowej otoczki wolnej przestrzeni(ograniczenie pionowe)
            for (int y = 0; y < height; y++) {
                data[0][y] = SPACE;
                data[width - 1][y] = SPACE;
            }

			//losowanie koordu start
			xStart = rand.nextInt(width-3) + 2;
			while(xStart % 2 != 0){
				xStart = rand.nextInt(width-3) + 2;	
			}

			//losowanie coordu meta
			xMeta = rand.nextInt(width-3) + 2;
			while(xMeta % 2 != 0){
				xMeta = rand.nextInt(width-3) + 2;
			}

			//coord start
            data[xStart][2] = SPACE;
			//rzezbienie labiryntu
            carve(xStart, 2);
			
            data[xStart][1] = SPACE;
			//coord meta
            data[xMeta][height - 2] = SPACE;
			//data[xMeta][height - 3] = SPACE;
        }

    /**
     *
     * @return zwraca ArrayListę Coordynatów labiryntu
     */
    public ArrayList<Coord> genLabirynt() {
            ArrayList<Coord> cor = new ArrayList<>();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (data[x][y] == WALL) {
                        cor.add(new Coord(x, y));
                    }
                }
            }
			//coord start
			cor.add(0, new Coord(xStart, 1));
			//coord meta
        	cor.add(new Coord(xMeta, height-2));
            return cor;
        }
   
    /**
     *
     * @param x - szerokość zadanego labiryntu
     * @param y - wysokość zadanego labiryntu
     * @return zwraca listę koordynatów 
     */
    public static ArrayList<Coord> generateLabyrinth(int x, int y)
    	{
			Maze maze = new Maze(x+2, y+2);
        	return maze.genLabirynt();
    	}

}

