/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventofcode2022;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
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
                        + "R 5\n"
                        + "U 8\n"
                        + "L 8\n"
                        + "D 3\n"
                        + "R 17\n"
                        + "D 10\n"
                        + "L 25\n"
                        + "U 20"
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
    File f = new File("check");
//added propagateKnots to remake part2

    public boolean moveTheRope(int xSteps, int ySteps, boolean propagateKnots) throws IOException, Exception {
        boolean movementIsFine = true;
        boolean tailMoved = false;
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
                tailMoved = true;
            }
            if (!propagateKnots) {
                visitedSpots.putIfAbsent(tailX + "_" + tailY, headX + "_" + headY);
                movementIsFine = checkAndPrintPositions();
            } else {
                notsX[0] = headX;
                notsY[0] = headY;
                if (tailMoved) {
                    movementIsFine = propagateKnotPosition();
                }
            }
//            allVisitedSpots.add("H:" + headX + "_" + headY + " | T:" + tailX + "_" + tailY);
            //            printSillyBoard();
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
                tailMoved = true;
            }
            if (!propagateKnots) {
                visitedSpots.putIfAbsent(tailX + "_" + tailY, headX + "_" + headY);
                movementIsFine = checkAndPrintPositions();
            } else {
                notsX[0] = headX;
                notsY[0] = headY;
                if (tailMoved) {
                    movementIsFine = propagateKnotPosition();

                }

            }
//            allVisitedSpots.add("H:" + headX + "_" + headY + " | T:" + tailX + "_" + tailY);
//            printSillyBoard();
        }
        return movementIsFine;
    }

    public boolean propagateKnotPosition() {
        System.out.println("        -)-)-)-(-(-(-");
        for (int i = notsX.length - 1; i > 1; i--) {
            notsX[i] = notsX[i - 1];
            notsY[i] = notsY[i - 1];
        }
        notsX[1] = tailX;
        notsY[1] = tailY;
        for (int i = 1; i < notsX.length; i++) {
            if (!checkAndPrintPositions(i)) {
                return false;
            }
        }

        if (visitedSpots.putIfAbsent(notsX[9] + "_" + notsY[9], notsX[0] + "_" + notsX[0]) == null) {
            System.out.println("NEW KEY:" + notsX[9] + "_" + notsY[9]);
            try {
                FileWriter fw = new FileWriter(f, true);
                fw.write("New key: " + notsX[9] + "_" + notsY[9] + "\n");
                fw.flush();
                fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        };
        return true;
    }

    //Creating a step class to make it easier to see which decision was wrong when movement fails
    public class Step {

        int xStep;
        int yStep;

        private Step(int x, int y) {
            this.xStep = x;
            this.yStep = y;
        }
    }

    public Step moveStraightRight() {
        return new Step(1, 0);
    }

    public Step moveStraightLeft() {
        return new Step(-1, 0);
    }

    public Step moveStraightUp() {
        return new Step(0, 1);
    }

    public Step moveStraightDown() {
        return new Step(0, -1);
    }

    public Step moveDiagonalUpRight() {
        return new Step(1, 1);
    }

    public Step moveDiagonalDownRight() {
        return new Step(1, -1);
    }

    public Step moveDiagonalUpLeft() {
        return new Step(-1, 1);
    }

    public Step moveDiagonalDownLeft() {
        return new Step(-1, -1);
    }

    public Step stayStill() {
        return new Step(0, 0);
    }

    public Step chooseKnotMovement(int leadingX, int leadingY, int knotX, int knotY, int currentKnot) {

        double deltaX = Math.sqrt(Math.pow((leadingX - knotX), 2));
        double deltaY = Math.sqrt(Math.pow((leadingY - knotY), 2));
        //if both coordinates are at most 1 step away, stay still my beating heart
        if (deltaX <= 1 && deltaY <= 1) {
            return stayStill();
        }
        //IF only X is drifting, move in the X direction
        if (deltaX > 1 && deltaY == 0) {
            return leadingX < knotX ? moveStraightLeft() : moveStraightRight();
        }
        //IF only Y is drifting, move in the Y direction
        if (deltaY > 1 && deltaX == 0) {
            return leadingY < knotY ? moveStraightDown() : moveStraightUp();
        }
        //IF both coordinates drifted, choose which diagonal to take based on the X position
        if (leadingX > knotX) {
            return leadingY > knotY ? moveDiagonalUpRight() : moveDiagonalDownRight();
        }
        if (leadingX < knotX) {
            return leadingY > knotY ? moveDiagonalUpLeft() : moveDiagonalDownLeft();
        }
        //this should never happen if we mapped all possible movements
        System.out.println("ERROR: UNMAPPED MOVEMENT:____" + deltaX + "," + deltaY);
        return stayStill();
    }

    /**
     * Move the knots recursively, starting from the knot 1.
     *
     * @param currentKnot
     * @param xSteps
     * @param ySteps
     * @return
     */
    public boolean moveTheKnot(int currentKnot, int xSteps, int ySteps) {

        Step stepToTake = chooseKnotMovement(notsX[currentKnot - 1], notsY[currentKnot - 1], notsX[currentKnot], notsY[currentKnot], currentKnot);
        notsX[currentKnot] += stepToTake.xStep;
        notsY[currentKnot] += stepToTake.yStep;
        if (!checkAndPrintPositions(currentKnot)) {
            System.out.println("Failed with Step=" + stepToTake.xStep + " " + stepToTake.yStep);
            return false;
        }
        if (currentKnot == notsX.length - 1) {
            visitedSpots.putIfAbsent(notsX[currentKnot] + "_" + notsY[currentKnot], notsX[0] + "_" + notsY[0]);
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
            if (!moveTheRope(xSteps, ySteps, false)) {
                break;
            }
        }
//        printHeadAndTailMovements();
        System.out.println("Tail visited " + visitedSpots.keySet().size() + " different spots");
        //We got there with 6357 different spots for the tail. Took more time than I tought it woudl
        return sum;
    }

    public int solvePart2(InputStream input) throws IOException, Exception {
        try {
            FileWriter fw = new FileWriter(f, false);
            fw.write("AGAIN!: " + notsX[9] + "_" + notsY[9] + "\n");
            fw.flush();
            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
                    ySteps = Integer.parseInt(moveToMake[1]);
                    break;
                case "D":
                    ySteps = -Integer.parseInt(moveToMake[1]);
                    break;
            }
            System.out.println("Moving " + line + " X:" + xSteps + " Y:" + ySteps);
            //Check the resulting position and stop the run to find out where it's going wrong
            //6357 is too high!
            if (!moveTheRopeWithMultipleKnots(xSteps, ySteps)) {
//            if (!moveTheRope(xSteps, ySteps, true)) {
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
     * It took me too long to figure out I could use the distance between 2 points calc to determine the movement. It's quite embarrassing really.
     * I kept using the abs(x)-abs(x1) to calculate the distance but that obviously wont work because I haven't normalized the coordinates to be non-negative
     * 
     */
    @Override
    public int solve(InputStream input) throws IOException, Exception {
//        return solvePart1(input);
        return solvePart2(input);
    }

}
