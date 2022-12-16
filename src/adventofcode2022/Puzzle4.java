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
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

/**
 *
 * @author Eric
 */
public class Puzzle4 implements AdventPuzzle {

    private class Range {

        private int min;
        private int max;

        public Range(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public Range(String range) {
            String[] minMax = range.split("-");
            this.min = Integer.parseInt(minMax[0]);
            this.max = Integer.parseInt(minMax[1]);
        }

        public int getMin() {
            return this.min;
        }

        public int getMax() {
            return this.max;
        }

        public boolean fullyContains(Range b) {
            return this.min <= b.getMin() && this.max >= b.getMax();
        }

        public boolean hasOverlapWith(Range b) {
            return (b.getMin() >= this.min && b.getMin() <= this.getMax());
//                    || (b.getMax() <= this.getMax() && b.getMax() >= this.getMin());
        }
    }

    @Override
    public InputStream example() {
        return new ByteArrayInputStream(
                (""
                        + "2-4,6-8\n"
                        + "2-3,4-5\n"
                        + "5-7,7-9\n"
                        + "2-8,3-7\n"
                        + "6-6,4-6\n"
                        + "2-6,4-8")
                        .getBytes());
    }

    public int solvePart1(InputStream input) throws IOException {
        int sum = 0;
        String line = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        HashMap<Integer, Integer> charCount = new HashMap<>(52);
        //extra check to process the last group after parsing final line of input
        while ((line = br.readLine()) != null) {
            String[] ranges = line.split(",");
            Range r1 = new Range(ranges[0]);
            Range r2 = new Range(ranges[1]);
            if (r1.fullyContains(r2) || r2.fullyContains(r1)) {
                sum++;
            }
        }
        System.out.println("Assignments with overlapping Ranges:" + sum);
        return sum;
    }

    public int solvePart2(InputStream input) throws IOException {
        int sum = 0;
        String line = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        HashMap<Integer, Integer> charCount = new HashMap<>(52);
        //extra check to process the last group after parsing final line of input
        while ((line = br.readLine()) != null) {
            String[] ranges = line.split(",");
            Range r1 = new Range(ranges[0]);
            Range r2 = new Range(ranges[1]);
            if (r1.hasOverlapWith(r2) || r2.hasOverlapWith(r1)) {
                sum++;
                //if answer is too high, print overlaps to find out the wrong pairs
            }//else if answar is too low, print non-overlapping to find out the wrong pairs
        }
        System.out.println("Assignments with overlapping Ranges:" + sum);
        return sum;
    }

    /**
     * Simple puzzle to solve by creating a range class.
     * Just write down the sets on paper and visualize the patterns for part 2.
     * The 2nd boolean check was not needed because either set can overlap (1 overlaps with 2 or 2 overlaps with 1), so a check on the min val covers it
     */
    @Override
    public int solve(InputStream input) throws IOException {
//        return solvePart1(input);
        return solvePart2(input);
    }

}
