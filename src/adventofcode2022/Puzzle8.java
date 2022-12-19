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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Optional;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import sun.misc.Queue;

/**
 *
 * @author Eric
 */
public class Puzzle8 implements AdventPuzzle {

    int numRows = 0;
    int numCols = 0;//different for the example and the input. jut counted manually for the matrix and checked during parsing
    int[][] forest = new int[99][99];

    private class Coordinate {

        int row;
        int column;
        int scenicScore;

        public Coordinate(int row, int column, int scenicScore) {
            this.row = row;
            this.column = column;
            this.scenicScore = scenicScore;
        }
    }

    @Override
    public InputStream example() {
        return new ByteArrayInputStream(
                (""
                        + "30373\n"
                        + "25512\n"
                        + "65332\n"
                        + "33549\n"
                        + "35390"
                        + "")
                        .getBytes());
    }

    public boolean isTreeInvisible(int row, int column) {
        int treeSize = forest[row][column];
//        //Trees with size==9 are always visible but they block the line of sight of everything in their row/column.
//      This assumption is completely wrong, I'm a moron
//        if (treeSize == 9) {
//            return false;
//        }
        //Trees with size < all 4 bordering trees are always invisible
        //This assumption is true but it is checked by the next loops anyway.
//        if (forest[row][0] >= treeSize && forest[row][numCols - 1] >= treeSize
//                && forest[0][column] >= treeSize && forest[numRows - 1][column] >= treeSize) {
//            return true;
//        }
        ///check if it is visible from the left. Stop checking if found taller tree
        boolean isInvisible = forest[row][0] >= treeSize;
        for (int i = 1; i < column && !isInvisible; i++) {
            isInvisible |= forest[row][i] >= treeSize;
        }
        if (!isInvisible) {
            System.out.println("(" + row + "," + column + ") is visible from the left!");
            return false;
        }
        ///check if it is visible from the right
        isInvisible = forest[row][numCols - 1] >= treeSize;
        for (int i = numCols - 2; i > column && !isInvisible; i--) {
            isInvisible |= forest[row][i] >= treeSize;
        }
        if (!isInvisible) {
            System.out.println("(" + row + "," + column + ") is visible from the right!");
            return false;
        }
        ///check if it is visible from the top
        isInvisible = forest[0][column] >= treeSize;
        for (int i = 1; i < row && !isInvisible; i++) {
            isInvisible |= forest[i][column] >= treeSize;
        }
        if (!isInvisible) {
            System.out.println("(" + row + "," + column + ") is visible from the top!");
            return false;
        }
        ///check if it is visible from the bottom
        isInvisible = forest[numRows - 1][column] >= treeSize;
        for (int i = numRows - 2; i > row && !isInvisible; i--) {
            isInvisible |= forest[i][column] >= treeSize;
        }
        if (!isInvisible) {
            System.out.println("(" + row + "," + column + ") is visible from the bottom!");
            return false;
        }
        return true;
    }

    public long findInvisbleTreesAndScenicScore(int row, int column) {
        int treeSize = forest[row][column];
        int leftScenicScore = 1;//The 1st tree of the outer ring is assumed to be visible by default
        int rightScenicScore = 1;
        int topScenicScore = 1;
        int bottomScenicScore = 1;
        boolean isInvisible = forest[row][0] >= treeSize;
        for (int i = 1; i < column; i++) {
            boolean blockingTree = forest[row][i] >= treeSize;
            isInvisible = isInvisible || blockingTree;
            //need to count the scenic score so we don't break after finding the tree that blocks my view
            if (blockingTree) {
                //There's a tree blocking my view, reset my scenic score counter
                leftScenicScore = 0;
            }
            //IF a tree blocks my view, the scenic score is still 1, so we increment after resetting the counter
            leftScenicScore++;
        }
//        if (!isInvisible) {
//            return -1;
//        }
        ///check if it is visible from the right
        isInvisible = forest[row][numCols - 1] >= treeSize;
        for (int i = numCols - 2; i > column; i--) {
            boolean blockingTree = forest[row][i] >= treeSize;
            isInvisible = isInvisible || blockingTree;
            //need to count the scenic score so we don't break after finding the tree that blocks my view
            if (blockingTree) {
                //There's a tree blocking my view, reset my scenic score counter
                rightScenicScore = 0;
            }
            //IF a tree blocks my view, the scenic score is still 1, so we increment after resetting the counter
            rightScenicScore++;
        }
        //IF the tree is visible it is not eligible for a tree house, so we return scenic score = -1;
//        if (!isInvisible) {
//            return -1;
//        }
        ///check if it is visible from the top
        isInvisible = forest[0][column] >= treeSize;
        for (int i = 1; i < row; i++) {
            boolean blockingTree = forest[i][column] >= treeSize;
            isInvisible = isInvisible || blockingTree;
            //need to count the scenic score so we don't break after finding the tree that blocks my view
            if (blockingTree) {
                //There's a tree blocking my view, reset my scenic score counter
                topScenicScore = 0;
            }
            //IF a tree blocks my view, the scenic score is still 1, so we increment after resetting the counter
            topScenicScore++;
        }
//        if (!isInvisible) {
//            return -1;
//        }
        ///check if it is visible from the bottom
        isInvisible = forest[numRows - 1][column] >= treeSize;
        for (int i = numRows - 2; i > row; i--) {
            boolean blockingTree = forest[i][column] >= treeSize;
            isInvisible = isInvisible || blockingTree;
            //need to count the scenic score so we don't break after finding the tree that blocks my view
            if (blockingTree) {
                //There's a tree blocking my view, reset my scenic score counter
                bottomScenicScore = 0;
            }
            //IF a tree blocks my view, the scenic score is still 1, so we increment after resetting the counter
            bottomScenicScore++;
        }
//        if (!isInvisible) {
//            return -1;
//        }
        return leftScenicScore * rightScenicScore * topScenicScore * bottomScenicScore;
    }

    public void printForest() {
        System.out.print("\033[32m _|\033[0m");
        for (int i = 0; i < numCols; i++) {
            System.out.print("\033[32m " + (i < 10 ? " " + i : i) + "\033[0m");
        }
        System.out.println("");
        for (int i = 0; i < numRows; i++) {
            System.out.print("\033[32m" + (i < 10 ? " " + i : i) + "|  \033[0m");
            for (int j = 0; j < numCols; j++) {
                System.out.print(forest[i][j] + "  ");
            }
            System.out.println("");
        }

    }

    public void parseForest(InputStream input) throws IOException, Exception {
        String line = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        boolean keepParsing = true;
        int curRow = 0;
        //I'm an idiot and kept the state of the example execution in the control variables
        numCols = 0;
        numRows = 0;
        while ((line = br.readLine()) != null && keepParsing) {
            char[] cs = line.toCharArray();
            //update tracking variables for differentiating example from actual input
            numCols = cs.length > numCols ? cs.length : numCols;
            numRows = cs.length > 0 ? numRows + 1 : numRows;
            for (int i = 0; i < numCols; i++) {
                forest[curRow][i] = cs[i] - '0';
            }
            curRow++;
        }
    }

    public int solvePart1(InputStream input) throws IOException, Exception {
        int sum = 0;
        parseForest(input);
        System.out.println("Rows x Cols: " + numRows + " x " + numCols);
        printForest();
        //only look from the second to the second-last tree in the row/column
        for (int i = 1; i < numRows - 1; i++) {
            for (int j = 1; j < numCols - 1; j++) {
                if (isTreeInvisible(i, j)) {
//                    System.out.println("Invisble tree at " + i + "," + j + " (" + forest[i][j] + ")");
                } else {
//                    System.out.println("Visible tree at " + i + "," + j + " (" + forest[i][j] + ")");
                    sum++;
                }
            }
        }
        System.out.println("Total Visible Trees in the inner Ring:" + sum);
        //4 trees are overlapped on the corners of the forest
        System.out.println("Total Visible Trees on the outer Ring " + numRows + "*2 + " + numCols + "*2 -4:" + (numRows * 2 + numCols * 2 - 4));
        System.out.println("SUM:" + (sum + (numRows * 2 + numCols * 2 - 4)));
        //2074 is TOO HIGH. I'M DUMB. HELP ME PLEASE
        return sum;
    }

    public int solvePart2(InputStream input) throws IOException, Exception {
        int sum = 0;
        parseForest(input);
        System.out.println("Rows x Cols: " + numRows + " x " + numCols);
        printForest();
        long maxScenicScore = 0;
        ArrayList<Coordinate> usableTrees = new ArrayList<Coordinate>();
        //only look from the second to the second-last tree in the row/column
        for (int i = 1; i < numRows - 1; i++) {
            for (int j = 1; j < numCols - 1; j++) {
                long scenicScore = findInvisbleTreesAndScenicScore(i, j);
                if (scenicScore == -1) {
                    sum++;//keep the sum to check we didn't fuck up the visibility 
                } else {
//                    System.out.println("Visible tree at " + i + "," + j + " (" + forest[i][j] + ")");
                    if (scenicScore > maxScenicScore) {
                        maxScenicScore = scenicScore;
                        System.out.println("Tree (" + i + "," + j + ") has the highest scenic score with " + scenicScore);
                    }

                }
            }
        }//4050 was too low
        System.out.println("Total Visible Trees in the inner Ring:" + sum);
        //4 trees are overlapped on the corners of the forest
        System.out.println("Total Visible Trees on the outer Ring " + numRows + "*2 + " + numCols + "*2 -4:" + (numRows * 2 + numCols * 2 - 4));
        System.out.println("SUM:" + (sum + (numRows * 2 + numCols * 2 - 4)));
        System.out.println("Highest Scenic Score: " + maxScenicScore);
//2074 is TOO HIGH, I'm dumb 1835 is the right answer
        return sum;
    }

    /**
     * Initial thoughts:
     * I should probably yeet the BufferedStream instantiation and while to a reusable piece of code, but I'm very lazy.
     * So, checking a grid of numbers. Do I create an object to store which side a tree is visible from??
     * There's probably a smart way to do this with math but hell if I can spot it. Just loop though this stuff I guess
     *
     * Implementation Remarks:
     * Spent some time thinking of clever designs such as:
     * - Storing the coordinates for tallest trees in the row/column
     * - Storing the tallest tree so far in row
     * - Storing visibility status of tree in a special inner class for each direction
     *
     * In the end, just made a bunch of for loops and forgot to put the equals in the comparison.
     * Honesly, so much time lost because I kept comparing tree1 > checkedTree instead of tree >= checkTree.
     * There're too many trees to check one by one if you're not correct, so triple check your algorithm.
     * Also made the wrong assumption that every one of the tallest trees would be visible, even without considering their peers, so I was off by a lot.
     * Essentially misunderstood the condition for trees being visible, thinking that trees with the same size would not block their peers.
     *
     * Not confident that this solution is gonna be too great for part 2 though :/
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
