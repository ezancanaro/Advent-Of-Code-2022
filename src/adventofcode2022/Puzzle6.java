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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import sun.misc.Queue;

/**
 *
 * @author Eric
 */
public class Puzzle6 implements AdventPuzzle {

    @Override
    public InputStream example() {
        return new ByteArrayInputStream(
                (""
                        + "mjqjpqmgbljsphdztnvjfqwrcgsmlb\n"
                        + "bvwbjplbgvbhsrlpgdmjqwftvncz\n"
                        + "nppdvjthqldpwncqszvftbrmjlhg\n"
                        + "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg\n"
                        + "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"
                        + "")
                        .getBytes());
    }

    public int solvePart1(InputStream input) throws IOException, Exception {
        int sum = 0;
        String line = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        HashMap<Integer, Integer> charCount = new HashMap<>(52);
        final Pattern intValues = Pattern.compile("(\\d+)");
        while ((line = br.readLine()) != null) {
            int curChar = 0;
            char[] msg = line.toCharArray();
            for (int i = 0; i < msg.length; i++) {
                //current char is the count of non repating characters
                //if we find a repeating character, curChar will be retracked to start at the offending char, otherwise it will be incremented by 1
                boolean retrackCurChar = false;
                for (int backTrack = 1; backTrack <= curChar; backTrack++) {
                    //there is a repeating char, keep going from this index forward
                    //n p p d v j t h q l d p w n c q s z v f t b r m j l h g
                    //0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7
                    char atI = msg[i];
                    char atBT = msg[i - backTrack];
                    retrackCurChar = atI == atBT;
                    int indexBT = i - backTrack;
                    if (retrackCurChar) {
                        curChar = backTrack;
                        break;
                    }
                }
                if (!retrackCurChar) {
                    curChar++;
                }
                //found 4 different chars, go to next example
                if (curChar == 4) {
                    //Response Index is i+1 because char count starts at 1 in the example not at 0
                    System.out.println(line + " \n: Found marker " + msg[i - 3] + msg[i - 2] + msg[i - 1] + msg[i] + " on index " + (i + 1));
                    break;
                }
            }
        }
        return sum;
    }

     public int solvePart2(InputStream input) throws IOException, Exception {
        int sum = 0;
        String line = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        HashMap<Integer, Integer> charCount = new HashMap<>(52);
        final Pattern intValues = Pattern.compile("(\\d+)");
        while ((line = br.readLine()) != null) {
            int curChar = 0;
            char[] msg = line.toCharArray();
            for (int i = 0; i < msg.length; i++) {
                //current char is the count of non repating characters
                //if we find a repeating character, curChar will be retracked to start at the offending char, otherwise it will be incremented by 1
                boolean retrackCurChar = false;
                for (int backTrack = 1; backTrack <= curChar; backTrack++) {
                    //there is a repeating char, keep going from this index forward
                    //n p p d v j t h q l d p w n c q s z v f t b r m j l h g
                    //0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7
                    char atI = msg[i];
                    char atBT = msg[i - backTrack];
                    retrackCurChar = atI == atBT;
                    int indexBT = i - backTrack;
                    if (retrackCurChar) {
                        curChar = backTrack;
                        break;
                    }
                }
                if (!retrackCurChar) {
                    curChar++;
                }
                //found 4 different chars, go to next example
                if (curChar == 14) {
                    //Response Index is i+1 because char count starts at 1 in the example not at 0
                    System.out.println(line + " \n: Found marker " + msg[i - 3] + msg[i - 2] + msg[i - 1] + msg[i] + " on index " + (i + 1));
                    break;
                }
            }
        }
        return sum;
    }
    
    public int solvePart1WithQueue(InputStream input) throws IOException, Exception {
        int sum = 0;
        String line = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        HashMap<Integer, Integer> charCount = new HashMap<>(52);
        final Pattern intValues = Pattern.compile("(\\d+)");
        Queue<Character> queue = new Queue<>();
        while ((line = br.readLine()) != null) {
            int curChar = 0;
            char[] msg = line.toCharArray();
            int queueElCount = 0;
            for (int i = 0; i < msg.length; i++) {
                //current char is the count of non repating characters
                if (queueElCount == 3) {
                    Enumeration<Character> el = queue.elements();
                    int pos = 0;
                    for (pos = 0; el.hasMoreElements() && el.nextElement() != msg[i]; pos++);
                    if (pos == 3) {
                        curChar = 4;
                    } else {
                        //remove elements from the queue
                        for (int j = 0; j < pos; j++) {
                            queue.dequeue();
                        }
                        queueElCount = 3 - pos;
                    }
                }
                queue.enqueue(msg[i]);
                queueElCount++;
                //found 4 different chars, go to next example
                if (curChar == 4) {
                    //Response Index is i+1 because char count starts at 1 in the example not at 0
                    System.out.println(line + " \n: Found marker " + msg[i - 3] + msg[i - 2] + msg[i - 1] + msg[i] + " on index " + (i + 1));
                    break;
                }
            }
        }
        return sum;
    }

    /**
     * Initial thoughts:
     * Parsing text input from a garbled message? Hits Too close to my job dealing with badly implemented HL7 message systems via TCP.s
     *
     * Implementation Remarks:
     * Backtracking with the counters was a bit fiddly. I forgot we kept incrementing the charCount after modifying it in the backtracking so got quite confused
     * Could probably have done it better with a Queue of 3 chars to be checked:
     * - Enqueue chars until there's 3 in the queue; Check if the 4th char is already in the queue and, if so, deque the first N chars up until the offending char is out; Repeat.
     * Just trying to express it with Java's queue made me quite annoyed. The backtracker works though, so I leave the "smart" version as is (not working I mean)
     * Part 2:
     * Just needed to change the charCount and it just worked.
     * Nice.
     */
    @Override
    public int solve(InputStream input) throws IOException, Exception {
//        return solvePart1(input);
        return solvePart2(input);
    }

}
