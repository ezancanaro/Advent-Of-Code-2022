/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventofcode2022;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author Eric
 */
public class Puzzle9 implements AdventPuzzle {

    @Override
    public InputStream example() {
        return new ByteArrayInputStream(
                (""
                        + "R 4\n"
                        + "U 4\n"
                        + "L 3\n"
                        + "D 1\n"
                        + "R 4\n"
                        + "D 1\n"
                        + "L 5\n"
                        + "R 2"
                        + "")
                        .getBytes());
    }

    BufferedReader br;

    int tailX = 0;
    int tailY = 0;
    int headX = 0;
    int headY = 0;
    int[] notsX = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    int[] notsY = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    //Just keep the spots visitedByTheTail. Used LinkedHashMap for the ordering but it's unnecessary
    LinkedHashMap<String, String> visitedSpots = new LinkedHashMap<>();
    char[][] sillyBoard = new char[10][10];

    public boolean moveTheRope(int xSteps, int ySteps) throws IOException, Exception {
        boolean movementIsFine = true;
        for (int i = 0; i < Math.abs(xSteps) && movementIsFine; i++) {
            int stepDir = xSteps < 0 ? -1 : 1;
            headX += stepDir;
            int deltaX = Math.abs(headX - tailX);
            int deltaY = Math.abs(headY) - Math.abs(tailY);
            System.out.println("DeltaX:" + deltaX + " DeltaY: " + deltaY);
            if (deltaX > 1) {
                tailX += stepDir;
                if (Math.abs(deltaY) >= 1) {
                    tailY += (headY > tailY ? 1 : -1);
                }
            }
            visitedSpots.putIfAbsent(tailX + "_" + tailY, headX + "_" + headY);
            //            allVisitedSpots.add("H:" + headX + "_" + headY + " | T:" + tailX + "_" + tailY);
            //            printSillyBoard();
            movementIsFine = checkAndPrintPositions();
        }
        for (int i = 0; i < Math.abs(ySteps) && movementIsFine; i++) {
            int stepDir = ySteps < 0 ? -1 : 1;
            headY += stepDir;
            int deltaX = Math.abs(headX) - Math.abs(tailX);
            int deltaY = headY - tailY;
            if (Math.abs(deltaY) > 1) {
                tailY += stepDir;
                if (Math.abs(deltaX) >= 1) {
                    tailX += (headX > tailX ? 1 : -1);
                }
            }
            visitedSpots.putIfAbsent(tailX + "_" + tailY, headX + "_" + headY);
//            allVisitedSpots.add("H:" + headX + "_" + headY + " | T:" + tailX + "_" + tailY);
//            printSillyBoard();
            movementIsFine = checkAndPrintPositions();
        }
        return movementIsFine;
    }

    /**
     * Move the knots recursively, starting from the knot 1. FUCKING ANNOYING
     *
     * @param currentKnot
     * @param xSteps
     * @param ySteps
     * @return
     */
    public boolean moveTheKnot(int currentKnot, int xSteps, int ySteps) {
        int deltaX = 0;
        int deltaY = 0;
        if (xSteps != 0) {
            deltaX = Math.abs(notsX[currentKnot - 1] - notsX[currentKnot]);
            deltaY = Math.abs(notsY[currentKnot - 1]) - Math.abs(notsY[currentKnot]);
            if (deltaX > 1) {
                notsX[currentKnot] += xSteps;
                if (Math.abs(deltaY) >= 1) {
                    notsY[currentKnot] += (notsY[currentKnot - 1] > notsY[currentKnot] ? 1 : -1);
                }
            }
        }
        if (ySteps != 0) {
            deltaX = Math.abs(notsX[currentKnot - 1]) - Math.abs(notsX[currentKnot]);
            deltaY = Math.abs(notsY[currentKnot - 1] - notsY[currentKnot]);
            if (deltaY > 1) {
                notsY[currentKnot] += ySteps;
                if (Math.abs(deltaX) >= 1) {
                    notsX[currentKnot] += (notsX[currentKnot - 1] > notsX[currentKnot] ? 1 : -1);
                }
            }
        }
        if (!checkAndPrintPositions(currentKnot)) {
            System.out.println("Failed for Deltas X/Y:" + deltaX + "/" + deltaY);
            return false;
        }
        if (currentKnot == notsX.length - 1) {
            visitedSpots.putIfAbsent(notsX[currentKnot - 1] + "_" + notsY[currentKnot], notsX[0] + "_" + notsY[0]);
            return true;
        }
        return moveTheKnot(currentKnot + 1, xSteps, ySteps);
    }

    public boolean moveTheRopeWithMultipleKnots(int xSteps, int ySteps) throws IOException, Exception {
        boolean movementIsFine = true;
        for (int i = 0; i < Math.abs(xSteps) && movementIsFine; i++) {
            int stepDir = xSteps < 0 ? -1 : 1;
            headX += stepDir;
            notsX[0] = headX;
            movementIsFine = moveTheKnot(1, stepDir, 0);
            System.out.println("_______-----------------");
        }
        for (int i = 0; i < Math.abs(ySteps) && movementIsFine; i++) {
            int stepDir = ySteps < 0 ? -1 : 1;
            headY += stepDir;
            notsY[0] = headY;
            movementIsFine = moveTheKnot(1, 0, stepDir);
            System.out.println("_______-----------------");
        }
        return movementIsFine;
    }

    public boolean checkAndPrintPositions() {
        System.out.println("\t H " + headX + "," + headY + " | T" + tailX + "," + tailY);
        int deltaX = Math.abs(headX) - Math.abs(tailX);
        int deltaY = Math.abs(headY) - Math.abs(tailY);
        if (Math.abs(deltaX) > 1 || Math.abs(deltaY) > 1) {
            System.out.println("Tail Drifted too far from the Head!!!");
            return false;
        }
        return true;
    }

    public boolean checkAndPrintPositions(int knot) {
        System.out.println("\tKnot " + knot + " H: " + notsX[knot - 1] + "," + notsY[knot - 1] + " | T" + notsX[knot] + "," + notsY[knot]);
        int deltaX = Math.abs(notsX[knot - 1]) - Math.abs(notsX[knot]);
        int deltaY = Math.abs(notsY[knot - 1]) - Math.abs(notsY[knot]);
        if (Math.abs(deltaX) > 1 || Math.abs(deltaY) > 1) {
            System.out.println("Someone Drifted too far from it's friend!!!");
            return false;
        }
        return true;
    }

    /**
     * Small board for validating initial solution based on the example.
     */
    public void printSillyBoard() {
        System.out.println("H " + headX + "," + headY);
        System.out.println("T " + tailX + "," + tailY);
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                System.out.print(headX == i && headY + 4 == j ? "H" : tailX == i && tailY + 4 == j ? "T" : ".");
            }
            System.out.println("");
        }
        System.out.println("---___");
    }

    public int solvePart1(InputStream input) throws IOException, Exception {
        int sum = 0;
        br = new BufferedReader(new InputStreamReader(input));
        int posX = 0;
        int posY = 0;
        String line = "";
        tailX = 0;
        tailY = 0;
        headX = 0;
        headY = 0;
        visitedSpots = new LinkedHashMap<>();
        while ((line = br.readLine()) != null) {
            String[] moveToMake = line.split(" ");
            int xSteps = 0;
            int ySteps = 0;
            switch (moveToMake[0]) {
                case "L":
                    xSteps = -Integer.parseInt(moveToMake[1]);
                    break;
                case "R":
                    xSteps = Integer.parseInt(moveToMake[1]);
                    break;
                case "U":
                    ySteps = -Integer.parseInt(moveToMake[1]);
                    break;
                case "D":
                    ySteps = Integer.parseInt(moveToMake[1]);
                    break;
            }
            System.out.println("Moving " + line + " X:" + xSteps + " Y:" + ySteps);
            //Check the resulting position and stop the run to find out where it's going wrong
            if (!moveTheRope(xSteps, ySteps)) {
                break;
            }
        }
//        printHeadAndTailMovements();
        System.out.println("Tail visited " + visitedSpots.keySet().size() + " different spots");
        //We got there with 6357 different spots for the tail. Took more time than I tought it woudl
        return sum;
    }

    public int solvePart2(InputStream input) throws IOException, Exception {
        int sum = 0;
        notsX = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        notsY = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        headX = 0;
        headY = 0;
        tailX = 0;
        tailY = 0;
        visitedSpots = new LinkedHashMap<>();
        String line = "";
        br = new BufferedReader(new InputStreamReader(input));
        while ((line = br.readLine()) != null) {
            String[] moveToMake = line.split(" ");
            int xSteps = 0;
            int ySteps = 0;
            switch (moveToMake[0]) {
                case "L":
                    xSteps = -Integer.parseInt(moveToMake[1]);
                    break;
                case "R":
                    xSteps = Integer.parseInt(moveToMake[1]);
                    break;
                case "U":
                    ySteps = -Integer.parseInt(moveToMake[1]);
                    break;
                case "D":
                    ySteps = Integer.parseInt(moveToMake[1]);
                    break;
            }
            System.out.println("Moving " + line + " X:" + xSteps + " Y:" + ySteps);
            //Check the resulting position and stop the run to find out where it's going wrong
            if (!moveTheRopeWithMultipleKnots(xSteps, ySteps)) {
                System.out.println("FAILED");
                break;
            }
        }
        System.out.println("Tail visited " + visitedSpots.keySet().size() + " different spots");
        return sum;
    }

    /**
     * Initial thoughts:
     * The explanation of the puzzle seems very complicated but it should be simpler than the last one.
     * Should be easy to count the tail positions as long as we implement the movement properly. Could be a decent application for a sparse matrix?
     *
     * How do I know which is the starting state of the board??
     *
     * Implementation Remarks:
     * There is no need to keep the whole board state, just need to keep track of the steps you take with the tail.
     * Using a HashMap in order to avoid duplication and we got there.
     * Got a bit confused when trying to implement the diagonal stepping though, as I kept insisting on using Math.abs(headX)-Math.abs(tailX) instead of just comparing using greater than
     *
     * Part 2:
     * Got quite annoyed with this one.
     * I kept thinking that the only eligible trees where the ones found on part 1, so I only summed the scores for those birches.
     * Felt really silly when I just needed to do it for every tree in the grid (except the outer rign which will always give 0)
     */
    @Override
    public int solve(InputStream input) throws IOException, Exception {
//        return solvePart1(input);
        return solvePart2(input);
    }

}
