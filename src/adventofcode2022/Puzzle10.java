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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Eric
 */
public class Puzzle10 implements AdventPuzzle {

    BufferedReader br;

    @Override
    public InputStream example() {
        return new ByteArrayInputStream(
                (""
                        + "addx 15\n"
                        + "addx -11\n"
                        + "addx 6\n"
                        + "addx -3\n"
                        + "addx 5\n"
                        + "addx -1\n"
                        + "addx -8\n"
                        + "addx 13\n"
                        + "addx 4\n"
                        + "noop\n"
                        + "addx -1\n"
                        + "addx 5\n"
                        + "addx -1\n"
                        + "addx 5\n"
                        + "addx -1\n"
                        + "addx 5\n"
                        + "addx -1\n"
                        + "addx 5\n"
                        + "addx -1\n"
                        + "addx -35\n"
                        + "addx 1\n"
                        + "addx 24\n"
                        + "addx -19\n"
                        + "addx 1\n"
                        + "addx 16\n"
                        + "addx -11\n"
                        + "noop\n"
                        + "noop\n"
                        + "addx 21\n"
                        + "addx -15\n"
                        + "noop\n"
                        + "noop\n"
                        + "addx -3\n"
                        + "addx 9\n"
                        + "addx 1\n"
                        + "addx -3\n"
                        + "addx 8\n"
                        + "addx 1\n"
                        + "addx 5\n"
                        + "noop\n"
                        + "noop\n"
                        + "noop\n"
                        + "noop\n"
                        + "noop\n"
                        + "addx -36\n"
                        + "noop\n"
                        + "addx 1\n"
                        + "addx 7\n"
                        + "noop\n"
                        + "noop\n"
                        + "noop\n"
                        + "addx 2\n"
                        + "addx 6\n"
                        + "noop\n"
                        + "noop\n"
                        + "noop\n"
                        + "noop\n"
                        + "noop\n"
                        + "addx 1\n"
                        + "noop\n"
                        + "noop\n"
                        + "addx 7\n"
                        + "addx 1\n"
                        + "noop\n"
                        + "addx -13\n"
                        + "addx 13\n"
                        + "addx 7\n"
                        + "noop\n"
                        + "addx 1\n"
                        + "addx -33\n"
                        + "noop\n"
                        + "noop\n"
                        + "noop\n"
                        + "addx 2\n"
                        + "noop\n"
                        + "noop\n"
                        + "noop\n"
                        + "addx 8\n"
                        + "noop\n"
                        + "addx -1\n"
                        + "addx 2\n"
                        + "addx 1\n"
                        + "noop\n"
                        + "addx 17\n"
                        + "addx -9\n"
                        + "addx 1\n"
                        + "addx 1\n"
                        + "addx -3\n"
                        + "addx 11\n"
                        + "noop\n"
                        + "noop\n"
                        + "addx 1\n"
                        + "noop\n"
                        + "addx 1\n"
                        + "noop\n"
                        + "noop\n"
                        + "addx -13\n"
                        + "addx -19\n"
                        + "addx 1\n"
                        + "addx 3\n"
                        + "addx 26\n"
                        + "addx -30\n"
                        + "addx 12\n"
                        + "addx -1\n"
                        + "addx 3\n"
                        + "addx 1\n"
                        + "noop\n"
                        + "noop\n"
                        + "noop\n"
                        + "addx -9\n"
                        + "addx 18\n"
                        + "addx 1\n"
                        + "addx 2\n"
                        + "noop\n"
                        + "noop\n"
                        + "addx 9\n"
                        + "noop\n"
                        + "noop\n"
                        + "noop\n"
                        + "addx -1\n"
                        + "addx 2\n"
                        + "addx -37\n"
                        + "addx 1\n"
                        + "addx 3\n"
                        + "noop\n"
                        + "addx 15\n"
                        + "addx -21\n"
                        + "addx 22\n"
                        + "addx -6\n"
                        + "addx 1\n"
                        + "noop\n"
                        + "addx 2\n"
                        + "addx 1\n"
                        + "noop\n"
                        + "addx -10\n"
                        + "noop\n"
                        + "noop\n"
                        + "addx 20\n"
                        + "addx 1\n"
                        + "addx 2\n"
                        + "addx 2\n"
                        + "addx -6\n"
                        + "addx -11\n"
                        + "noop\n"
                        + "noop\n"
                        + "noop"
                        + "")
                        .getBytes());
    }

    public long X;

    public class Instruction {

        int totalCycles;
        int cyclesExecuted;
        long argument;

        public Instruction(int totalCycles) {
            this.totalCycles = totalCycles;
            this.cyclesExecuted = 0;
            this.argument = 0;
        }

        public Instruction(int totalCycles, long argument) {
            this.totalCycles = totalCycles;
            this.cyclesExecuted = 0;
            this.argument = argument;
        }

        public long execute() {
            return this.argument;
        }

    }

    public Queue<Instruction> parseInstructions(InputStream input) throws IOException {
        String line = "";
        Queue<Instruction> instructions = new LinkedList<>();
        br = new BufferedReader(new InputStreamReader(input));
        //Parse Instructions
        while ((line = br.readLine()) != null) {
            String[] op = line.split(" ");
            if (op.length == 1) {
                instructions.add(new Instruction(1));
            } else if (op.length == 2) {
                instructions.add(new Instruction(2, Long.parseLong(op[1])));
            }
        }
        return instructions;
    }

    public int solvePart1(InputStream input) throws IOException, Exception {
        int sum = 0;
        X = 1;
        int startingPeek = 20;
        int finalPeek = 220;
        int incrementPeek = 40;
        int peekAt = startingPeek;
        long sumPeekedXvalues = 0;
        Queue<Instruction> instructions = parseInstructions(input);
        int cycleCount = 0;
        Instruction currentInstruction = instructions.remove();
        while (cycleCount++ != Integer.MAX_VALUE && currentInstruction != null) {
            if (cycleCount == peekAt && cycleCount <= finalPeek) {
                peekAt += incrementPeek;
                System.out.println("\t\t---@" + cycleCount + "X=" + X + " SignalStrength=" + X * cycleCount);
                sumPeekedXvalues += X * cycleCount;
            }
            System.out.println("$ " + cycleCount + ": " + X);
            currentInstruction.cyclesExecuted += 1;
            if (currentInstruction.cyclesExecuted == currentInstruction.totalCycles) {
                X += currentInstruction.execute();
                //cannot break on the while because there might be a need to finish execution of instruction after the stack is empty
                if (instructions.size() == 0) {
                    break;
                }
                currentInstruction = instructions.remove();
            }
            System.out.println("& " + cycleCount + ": " + X);
        }
        System.out.println("" + cycleCount + ": " + X);
        //just to make sure we got all the cycles correctly
        if (cycleCount == peekAt && cycleCount <= finalPeek) {
            peekAt += incrementPeek;
            System.out.println("\t\t---@" + cycleCount + " X=" + X);
        }
        System.out.println("Summed Xs:" + sumPeekedXvalues);
        return sum;
    }

    int CRT_X = 40;
    int CRT_Y = 6;

    char[][] crtMonitor = new char[CRT_Y][CRT_X];

    public int getCurrentRow(int currentCycle) {
        return (int) Math.floor((currentCycle - 1) / 40);
    }

    public int getCurrentColumn(int currentCycle, int currentRow) {
        int col = currentCycle - (40 * currentRow) - 1;
        if (col < 0) {
            col = 39;
        }
        return col;
    }

    public void setPixel(int currentCycle, char pixelValue, int row, int col) {
        try {
            crtMonitor[row][col] = pixelValue;
        } catch (ArrayIndexOutOfBoundsException oob) {
            throw oob;
        }
    }

    public void fillCRT(char startingChar) {
        for (int i = 1; i <= 240; i += 1) {
            int row = getCurrentRow(i);
            int col = getCurrentColumn(i, row);
            setPixel(i, startingChar, row, col);
        }
    }

    public void renderCRT() {
        for (int i = 0; i < CRT_Y; i++) {
            for (int j = 0; j < CRT_X; j++) {
                System.out.print(crtMonitor[i][j]);
            }
            System.out.println(" " + (i + 1) * 40);
        }
    }

    //X = middle of the sprite, sprite always has 3 pixels
    public int solvePart2(InputStream input) throws IOException, Exception {
        int sum = 0;
        X = 1;
        Queue<Instruction> instructions = parseInstructions(input);
        int cycleCount = 0;
        //fill the crt with dark spots.
        fillCRT('.');
        Instruction currentInstruction = instructions.remove();
        while (cycleCount++ != Integer.MAX_VALUE && currentInstruction != null) {

            int renderingRow = getCurrentRow(cycleCount);
            int renderingCol = getCurrentColumn(cycleCount, renderingRow);
            //to find the center column for the pixel, treat it as if it was on the first row
            int pixelCenter = (int) X;
            System.out.println("Cycle|Row|Col|Center: " + cycleCount + " | " + renderingRow + " | " + renderingCol + " | " + pixelCenter);
            if (renderingCol - pixelCenter >= -1 && renderingCol - pixelCenter <= 1) {
                setPixel(cycleCount, '#', renderingRow, renderingCol);
            }
            currentInstruction.cyclesExecuted += 1;
            if (currentInstruction.cyclesExecuted == currentInstruction.totalCycles) {
                X += currentInstruction.execute();
                //cannot break on the while because there might be a need to finish execution of instruction after the stack is empty
                if (instructions.size() == 0) {
                    break;
                }
                currentInstruction = instructions.remove();
            }
        }
        renderCRT();
        return sum;
    }

    /**
     * Initial thoughts:
     * Seems to be a simple case of counting values by inserting things in a queue. Just use a cycleCount and keeep state in each instruction to see how many cycles were ran on it
     *
     * Implementation Remarks:
     * Pretty straight forward, but I did start using a Stack instead of a Queue of instructions because I'm a bit daft.
     *
     * Part 2:
     * Very fun little puzzle. Again I've had difficulties when specifying the proper indexes for the char[][] given a cycleCount.
     * I kept getting only one of the boundaries right (either cycle 1 or cycle 40 had proper position but not both) because of bad maths
     * Once that was over the puzzle was simple
     */
    @Override
    public int solve(InputStream input) throws IOException, Exception {
//        return solvePart1(input);
        return solvePart2(input);
    }

}
