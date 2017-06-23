package labyrinth.game;

import static labyrinth.gui.GameBoard.LabyrinthShape;
import static labyrinth.gui.GameBoard.DifficultyLevel;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Klasa reprezentuje wynik wygranej gry.
 *
 * @author Zuzanna Łaś
 */
public class Result implements Comparable<Result>
{
    /**
     * Tworzy pusty obiekt wyniku.
     */
    public Result() {}

    /**
     * Tworzy wynik na podstawie podanych danych.
     *
     * @param userName nazwa użytkownika
     * @param pawnImagePath ciąg znaków reprezentujący ścieżkę do pliku z obrazkiem pionka
     * @param time ciąg znaków reprezentujący czas w postaci: HH:MM:SS
     * @param labyrinthShape kształt planszy
     * @param difficultyLevel poziom trudności gry
     */
    public Result(String userName, String pawnImagePath, String time, LabyrinthShape labyrinthShape, DifficultyLevel difficultyLevel)
    {
        this.userName = userName.replace(SPECIAL_CHARACTER, ' ');
        this.pawnIconPath = convertImagePathToIconPath(pawnImagePath);
        this.time = time;
        this.labyrinthShape = labyrinthShape;
        this.difficultyLevel = difficultyLevel;
    }

    /**
     * Zamienia ścieżkę do pliku z obrazkiem pionka na ścieżkę do pliku z ikoną
     * odpowiadającą obrazkowi.
     *
     * @param imagePath ścieżka do pliku z obrazkiem pionka
     *
     * @return ścieżka do pliku z ikoną pionka
     */
    private String convertImagePathToIconPath(String imagePath)
    {
        String[] pathElements = imagePath.split("[.]");
        String iconPath = pathElements[0] + "Icon" + "." + pathElements[1];
        return iconPath;
    }

    /**
     * Zwraca nazwę użytkownika.
     *
     * @return nazwa użytkownika
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * Zwraca ścieżkę do pliku z obrazkiem pionka.
     *
     * @return ścieżka do pliku z obrazkiem pionka
     */
    public String getPawnIconPath()
    {
        return pawnIconPath;
    }

    /**
     * Zwraca czas gry.
     *
     * @return ciąg znaków reprezentujący czas w postaci: HH:MM:SS
     */
    public String getTime()
    {
        return time;
    }

    /**
     * Zwraca kształt planszy.
     *
     * @return kształt planszy gry
     */
    public LabyrinthShape getLabyrinthShape()
    {
        return labyrinthShape;
    }

    /**
     * Zwraca ścieżkę do pliku z ikoną kształtu labiryntu.
     *
     * @return ścieżka do pliku z ikoną kształtu labiryntu
     */
    public String getLabyrinthShapeIconPath()
    {
        return labyrinthShape.getIconPath();
    }

    /**
     * Zwraca poziom trudności gry.
     *
     * @return poziom trudności
     */
    public DifficultyLevel getDifficultyLevel()
    {
        return difficultyLevel;
    }

    /**
     * Zwraca ścieżkę do pliku z ikoną poziomu trudności gry.
     *
     * @return ścieżka do pliku z ikoną poziomu trudności gry
     */
    public String getDifficultyLevelIconPath()
    {
        return difficultyLevel.getIconPath();
    }

    /**
     * Porównuje bieżący rezultat do rezultatu podanego jako argument.
     *
     * @param otherResult rezultat podany do porównania z rezultatem bieżącym
     *
     * @return 0, jeżeli poziom gry, kształt planszy i czas obu rezultatów jest identyczny;
     *        -1, jeżeli bieżący rezultat występuje przed rezultatem podanym jako argument;
     *         1, jeżeli bieżący rezultat występuje za rezultatem podanym jako argument;
     *         porównywanie uwzględnia w pierwszej kolejności poziom trudności, potem kształt
     *         planszy, a na końcu czas i wykorzystuje metody porównujące poziomy, kształty
     *         i czas
     */
    public int compareTo(Result otherResult)
    {
        int levelsComparing = difficultyLevel.compareWith(otherResult.getDifficultyLevel());
        if (levelsComparing != 0) {
            return levelsComparing;
        }
        int shapesComparing = labyrinthShape.compareWith(otherResult.getLabyrinthShape());
        if (shapesComparing != 0) {
            return shapesComparing;
        }
        return compareTimes(otherResult.getTime());
    }

    /**
     * Porównuje bieżący czas z czasem podanym jako argument.
     *
     * @param otherTime łańcuch znaków reprezentujący czas podany do porównania w postaci: HH:MM:SS
     *
     * @return 0, jeżeli porównywane czasy są identyczne;
     *        -1, jeżeli bieżący okres czasu jest krótszy niż okres przekazany do porównania;
     *         1, jeżeli bieżący okres czasu jest dłuższy niż okres przekazany do porównania;
     */
    private int compareTimes(String otherTime)
    {
        long secondsSum = computeSecondsSum(time);
        long otherSecondsSum = computeSecondsSum(otherTime);
        if (secondsSum > otherSecondsSum) {
            return 1;
        } else if (secondsSum < otherSecondsSum) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Na podstawie podanego czasu oblicza reprezentującą go liczbę sekund.
     *
     * @param clock łańcuch znaków reprezentujący czas w postaci: HH:MM:SS
     *
     * @return liczba sekund reprezentująca wprowadzony czas
     */
    private long computeSecondsSum(String clock)
    {
        String[] clockTable = clock.split("[:]");
        int hours = Integer.parseInt(clockTable[0]);
        int minutes = Integer.parseInt(clockTable[1]);
        int seconds = Integer.parseInt(clockTable[2]);
        long secondsSum = (hours * 3600) + (minutes * 60) + seconds;
        return secondsSum;
    }

    /**
     * Tworzy zaszyfrowany łańcuch znaków reprezentujący rezultat.
     *
     * @return zaszyfrowany łańcuch znaków reprezentujący rezultat
     */
    public String write()
    {
        String record = userName + "*" + pawnIconPath + "*" + time + "*" + labyrinthShape + "*" + difficultyLevel;
        String encoded = encodeText(record);
        return encoded;
    }

    /**
     * Szyfruje wprowadzony tekst za pomocą Base64.
     *
     * @param text tekst wprowadzony do zaszyfrowania
     *
     * @return zaszyfrowany tekst
     */
    private String encodeText(String text)
    {
        String encodedText = Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
        return encodedText;
    }

    /**
     * Wczytuje rezultat na podstawie podanego zaszyfrowanego łańcucha znaków.
     *
     * @param record zaszyfrowany łańcuch znaków - wygenerowany za pomocą metody write()
     */
    public void read(String record)
    {
        String decoded = decodeText(record);
        String[] recordItems = decoded.split("[" + SPECIAL_CHARACTER + "]");
        userName = recordItems[0];
        pawnIconPath = recordItems[1];
        time = recordItems[2];
        labyrinthShape = (LabyrinthShape) Enum.valueOf(LabyrinthShape.class, recordItems[3]);
        difficultyLevel = (DifficultyLevel) Enum.valueOf(DifficultyLevel.class, recordItems[4]);
    }

    /**
     * Deszyfruje wprowadzony tekst zaszyfrowany za pomocą Base64.
     *
     * @param text zaszyfrowany tekst
     *
     * @return odszyfrowany tekst
     */
    public String decodeText(String text)
    {
        String decodedText = new String(Base64.getDecoder().decode(text));
        return decodedText;
    }

    private static final char SPECIAL_CHARACTER = '*';

    private String userName;
    private String pawnIconPath;
    private String time;
    private LabyrinthShape labyrinthShape;
    private DifficultyLevel difficultyLevel;
}

