/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventofcode2022;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Eric
 */
public class AdventOfCode2022 {

    public static int solvePuzzle(AdventPuzzle puzzle, String inputFileLocation) throws FileNotFoundException, IOException, Exception {
        System.out.println("Example Solution:");
        puzzle.solve(puzzle.example());
        System.out.println("Puzzle Solution:");
        int i = 0;
        try {
            i = puzzle.solve(AdventPuzzle.getInput(inputFileLocation));
        } catch (FileNotFoundException fnf) {
            System.out.println("File " + inputFileLocation
                    + " not found");
        }
        return i;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {

//        System.out.println("\n-----------Puzzle 1---------------\n");
//        solvePuzzle(new Puzzle1(), "inputs/puzzle1.txt");
//        System.out.println("\n-----------Puzzle 2---------------\n");
//        solvePuzzle(new Puzzle2(), "inputs/puzzle2.txt");
//        System.out.println("\n-----------Puzzle 3---------------\n");
//        solvePuzzle(new Puzzle3(), "inputs/puzzle3.txt");
//        System.out.println("\n-----------Puzzle 4---------------\n");
//        solvePuzzle(new Puzzle4(), "inputs/puzzle4.txt");
//        System.out.println("\n-----------Puzzle 5---------------\n");
//        solvePuzzle(new Puzzle5(), "inputs/puzzle5.txt");
//        System.out.println("\n-----------Puzzle 6---------------\n");
//        solvePuzzle(new Puzzle6(), "inputs/puzzle6.txt");
//        System.out.println("\n-----------Puzzle 7---------------\n");
//        solvePuzzle(new Puzzle7(), "inputs/puzzle7.txt");
//        System.out.println("\n-----------Puzzle 8---------------\n");
//        solvePuzzle(new Puzzle8(), "inputs/puzzle8.txt");
        System.out.println("\n-----------Puzzle 9---------------\n");
        solvePuzzle(new Puzzle9(), "inputs/puzzle9.txt");
//        solveP1();
//        solveP2();
//        System.out.println("a:" + Character.getNumericValue('a') + ",z:" + Character.getNumericValue('z')
//                + ",A:" + Character.getNumericValue('A') + ",Z:" + Character.getNumericValue('Z'));
// TODO code application logic here
    }

}
