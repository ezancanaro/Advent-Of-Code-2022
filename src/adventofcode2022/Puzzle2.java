/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventofcode2022;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 *
 * @author Eric
 */
public class Puzzle2 implements AdventPuzzle {

    final HashMap<String, Integer> pointsForSymbol = new HashMap<>();
    final HashMap<String, String> symbolForPose = new HashMap<>();
    final HashMap<String, Integer> symbolForResult = new HashMap<>();

    public Puzzle2() {
        pointsForSymbol.put("A", 1);//Rock
        pointsForSymbol.put("B", 2);//Paper
        pointsForSymbol.put("C", 3);//Scissor
        pointsForSymbol.put("X", 1);//Rock
        pointsForSymbol.put("Y", 2);//Paper
        pointsForSymbol.put("Z", 3);//Scissors

        symbolForPose.put("A", "Rock");
        symbolForPose.put("B", "Paper");
        symbolForPose.put("C", "Scissors");
        symbolForPose.put("X", "Rock");
        symbolForPose.put("Y", "Paper");
        symbolForPose.put("Z", "Scissors");

        symbolForResult.put("X", 0);
        symbolForResult.put("Y", 3);
        symbolForResult.put("Z", 6);
    }

    /**
     * -------R----P----S
     * _----| 1 | 2 | 3 |
     * R: 1 | 0D |-1W |-2L |
     * P: 2 | 1L | 0D |-1W
     * S: 3 | 2W | 1L | 0D
     *
     * @param elf
     * @param me
     * @return
     */
    public int calculateRoundPoints(int elf, int me) {
        //0,3,6
        switch (elf - me) {
            case -1:
            case 2:
                return 6;
            case 0:
                return 3;
//            case 2:
//                return 0;
            default:
                return 0;
        }
    }

    public int chooseMyShape(int elfShape, int desiredResult) {
        int myShape = 1;
        //X=Lose
        //Y=Draw
        //Z=Win
        switch (desiredResult) {
            case 3:
                return elfShape;
            case 6:
                return elfShape + 1 > 3 ? 1 : elfShape + 1;
            case 0:
                return elfShape - 1 < 1 ? 3 : elfShape - 1;
        }
        return myShape;
    }

    final static String example = ""
            //+ "A Y\n"
            //+ "B X\n"
            //+ "C Z\n"
            + "A X\n"
            + "B X\n"
            + "C X\n"
            + "A Y\n"
            + "B Y\n"
            + "C Y\n"
            + "A Z\n"
            + "B Z\n"
            + "C Z\n";

    public int solvePart1(InputStream input) throws IOException {
        int total = 0;
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String line = "";
        int i = 0;
        while ((line = br.readLine()) != null) {
            System.out.println("Round: " + ++i + " (Elf X Me):");
            String[] plays = line.split(" ");
            int mySymbolPoints = pointsForSymbol.get(plays[1]);
            int elfSymbolPoints = pointsForSymbol.get(plays[0]);
            int roundPoints = calculateRoundPoints(elfSymbolPoints, mySymbolPoints);
            int roundTotal = mySymbolPoints + roundPoints;
            total += roundTotal;
            System.out.println(symbolForPose.get(plays[0]) + "(" + elfSymbolPoints + ") X (" + mySymbolPoints + ") " + symbolForPose.get(plays[1]) + " = "
                    + (roundPoints == 0 ? "LOSS" : roundPoints == 3 ? "DRAW" : "WIN") + " for " + roundTotal + "(" + mySymbolPoints + "+" + roundPoints + ") points of (" + total + ")");
        }
        return total;
    }

    @Override
    public InputStream example() {
        return new ByteArrayInputStream(example.getBytes());
    }
    @Override
    public int solve(InputStream input) throws IOException {
        int total = 0;
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String line = "";
        int i = 0;
        while ((line = br.readLine()) != null) {
            System.out.println("Round: " + ++i + " (Elf X Me):");
            String[] plays = line.split(" ");
            int elfSymbolPoints = pointsForSymbol.get(plays[0]);
            int mySymbolPoints = chooseMyShape(elfSymbolPoints, symbolForResult.get(plays[1]));
            int roundPoints = calculateRoundPoints(elfSymbolPoints, mySymbolPoints);
            int roundTotal = mySymbolPoints + roundPoints;
            total += roundTotal;
            System.out.println(symbolForPose.get(plays[0]) + "(" + elfSymbolPoints + ") X (" + mySymbolPoints + ") " + symbolForPose.get(plays[1]) + " = "
                    + (roundPoints == 0 ? "LOSS" : roundPoints == 3 ? "DRAW" : "WIN") + " for " + roundTotal + "(" + mySymbolPoints + "+" + roundPoints + ") points of (" + total + ")");
        }
        return total;
    }
}
