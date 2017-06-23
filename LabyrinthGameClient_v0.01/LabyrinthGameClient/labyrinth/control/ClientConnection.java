package labyrinth.control;

import labyrinth.server.serialization.Coord;
import labyrinth.client.serverclient.ServerClient;
import labyrinth.client.configuration.Configuration;

import java.util.ArrayList;
import java.io.IOException;

/**
 * Klasa ClientConnection realizuje połączenie klienta z serwerem.
 *
 * @author Zuzanna Łaś
 */
public class ClientConnection
{
    /**
     * Pobiera z serwera wygenerowaną listę współrzędnych ścianek labiryntu.
     *
     * @param width szerokość labiryntu
     * @param height wysokość labiryntu
     *
     * @return lista współrzędnych ścianek labiryntu
     *
     * @throws IOException błąd połączenia z serwerem
     * @throws ClassNotFoundException błąd odczytu zserializowanych danych
     */
    public ArrayList<Coord> getLabyrinthCoordinates(int width, int height) throws IOException, ClassNotFoundException
    {
        if (connect()) {
            ArrayList<Coord> coordinates = client.getLabirynt(width, height);
            disconnect();
            return coordinates;
        }
        return null;
    }

    /**
     * Tworzy połączenie z serwerem.
     *
     * Próba połączenia jest podejmowana po poprawnym załadowaniu konfiguracji.
     *
     * @return true, jeżeli udało się nazwiązać połączenie z serwerem
     *
     * @throws IOException błąd połączenia z serwerem
     */
    private boolean connect() throws IOException
    {
        if (Configuration.load()) {
            client = new ServerClient(Configuration.getIp(), Configuration.getPort());
            if (client.connect()) {
                client.start();
                return true;
            }
        }
        return false;
    }

    /**
     * Zamyka połączenie z serwerem.
     */
    private void disconnect()
    {
        client.stop();
    }

    private ServerClient client;
}

