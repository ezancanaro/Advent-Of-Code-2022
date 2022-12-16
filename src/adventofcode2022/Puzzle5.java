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
import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;

/**
 *
 * @author Eric
 */
public class Puzzle5 implements AdventPuzzle {

    private Stack<String>[] boxStacks;
    int totalNumberOfStacks = 0;

    @Override
    public InputStream example() {
        return new ByteArrayInputStream(
                (""
                        + "    [D]    \n"
                        + "[N] [C]    \n"
                        + "[Z] [M] [P]\n"
                        + " 1   2   3 \n"
                        + "\n"
                        + "move 1 from 2 to 1\n"
                        + "move 3 from 1 to 3\n"
                        + "move 2 from 2 to 1\n"
                        + "move 1 from 1 to 2")
                        .getBytes());
    }

    public void parseInitialBoxState(BufferedReader statefulReader, InputStream input) throws IOException {

        Stack<String> boxRows = new Stack<>();
        String line = "";
        while ((line = statefulReader.readLine()) != null) {
            if (line.contains("[")) {
                boxRows.push(line);
            } else if (line.length() == 0) {
                //finish initial box parsing
                break;
            } else {
                totalNumberOfStacks = line.replace(" ", "").length();
                System.out.println("Total number of Stacks:" + totalNumberOfStacks + " _ " + line);
            }
        }
        boxStacks = new Stack[totalNumberOfStacks];
        for (int currentStack = 0; currentStack < totalNumberOfStacks; currentStack++) {
            boxStacks[currentStack] = new Stack<>();
        }
        while (!boxRows.isEmpty()) {
            String row = boxRows.pop();
            System.out.println("Row:" + row);
            int currentStack = 0;
            for (int i = 0; i < row.length(); i += 4) {
                String box = "";
                try {
                    box = row.substring(i, i + 3);
                } catch (IndexOutOfBoundsException iob) {
                    box = row.substring(i, row.length() - 1);
                }
                if (box.trim().length() > 0) {
                    boxStacks[currentStack].push(box.replace("[", "").replace("]", ""));
                }
                currentStack++;
            }
        }
    }

    public void moveBoxes(int numBoxesToMove, int from, int to) throws Exception {
        if (boxStacks[from] == null || boxStacks[to] == null) {
            throw new Exception("Invalid Box Stack Number:" + from + " | " + to);
        }
        if (numBoxesToMove > boxStacks[from].size()) {
            throw new Exception("Trying to move more boxes than contained in stack " + from + " (" + numBoxesToMove + " boxes) of " + boxStacks[from].size());
        }
        for (int i = 0; i < numBoxesToMove; i++) {
            boxStacks[to].push(boxStacks[from].pop());
        }
    }

    public int solvePart1(InputStream input) throws IOException, Exception {
        int sum = 0;
        String line = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        HashMap<Integer, Integer> charCount = new HashMap<>(52);
        //Pass this bufferedReader so that the cursor position in the file is kept in it's own state and we don't need to seek
        parseInitialBoxState(br, input);
        //Parse the moves that will be made
        final Pattern intValues = Pattern.compile("(\\d+)");
        while ((line = br.readLine()) != null) {
            if (line.length() == 0) {
                continue;
            }
            Matcher m = intValues.matcher(line);
            int[] moveInfo = new int[3];
            int index = 0;
            while (m.find()) {
                moveInfo[index++] = Integer.parseInt(m.group());
            }
            //make the move (subtract 1 because stacks are indexed from 1 in the input and from 0 in Java
            moveBoxes(moveInfo[0], moveInfo[1] - 1, moveInfo[2] - 1);
        }
        String topBoxes = "";
        for (int i = 0; i < totalNumberOfStacks; i++) {
            topBoxes += !boxStacks[i].isEmpty() ? boxStacks[i].pop() : "_";
        }
        System.out.println("The top crates are: " + topBoxes);
        return sum;
    }

    /**
     * Initial thoughts:
     * Probably the trickiest part of the puzzle is parsing the initial stacks into a workable format with the proper stack indexes for the move,
     * whitespace is significant in the lines depicting this initial state. 
     * The moves should be simple enough to parse and implement with multiples stacks
     * 
     * Implementation Remarks:
     * 2 separate parsing phases for the input, with different strategies for each. A bit fiddly to parse the boxes drawing but not so hard;
     * Parsing the moves is simple enough since only the numbers are relevant for the solution 
     * 
     * Stacks are nice to implement problems base on stacks of this. Who would've guessed.
     */
    @Override
    public int solve(InputStream input) throws IOException, Exception {
        return solvePart1(input);
//        return solvePart2(input);
    }

}
