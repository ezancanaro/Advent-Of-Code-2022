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
public class Puzzle3 implements AdventPuzzle {

    public final String example = "vJrwpWtwJgWrhcsFMMfFFhFp\n"
            + "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL\n"
            + "PmmdzqPrVvPwwTWBwg\n"
            + "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn\n"
            + "ttgJtRGJQctTZtZT\n"
            + "CrZsJsPPZsGzwwsLwLmpwMDw";

    public char getFromNumericValue(int numVal) {
        boolean toUpper = numVal > 26;
        if (toUpper) {
            numVal -= 26;
        }
        char base = Character.forDigit(numVal + 9, Character.MAX_RADIX);
        return toUpper ? Character.toUpperCase(base) : base;
    }

    public int getPriorityFromChar(char charac) {
        return Character.getNumericValue(charac) - 9 + (Character.isUpperCase(charac) ? 26 : 0);
    }

    //Java code numeric val: a|A==10,z|Z==35
    //Priorities of puzzle: a=1,A=27,z=26,Z=52
    public int solvePart1(InputStream input) throws IOException {
        int sum = 0;
        String line = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        while ((line = br.readLine()) != null) {
            HashMap<Integer, Integer> charCount = new HashMap<>(52);
            int halfMark = (int) Math.ceil(line.length() / 2);
            char[] firstCompartment = line.substring(0, halfMark).toCharArray();
            char[] secondCompartment = line.substring(halfMark).toCharArray();
            int compartmentSize = firstCompartment.length;
            for (int i = 0; i < compartmentSize; i++) {
                int priority1 = this.getPriorityFromChar(firstCompartment[i]);
                int priority2 = this.getPriorityFromChar(secondCompartment[i]);
                int c1 = charCount.merge(priority1, 1, (originalCompartment, thisCompartment) -> originalCompartment == thisCompartment || originalCompartment == 3 ? originalCompartment : 3);
                int c2 = charCount.merge(priority2, 2, (originalCompartment, thisCompartment) -> originalCompartment == thisCompartment || originalCompartment == 3 ? originalCompartment : 3);
            }
            System.out.println("RuckSack::");;;
            System.out.println("\t" + line.substring(0, halfMark));
            System.out.println("\t" + line.substring(halfMark));
            int ruckSackSum = 0;
            for (Integer key : charCount.keySet()) {
                System.out.print(key + "(" + getFromNumericValue(key) + ")[" + charCount.get(key) + "]" + ",");
                if (charCount.get(key) == 3) {
                    ruckSackSum += key;
                }
            }
            System.out.println("Rucksack " + line + " has count=" + ruckSackSum);
            sum += ruckSackSum;
        }
        System.out.println("Finished counting Rucksacks with " + sum);
        return sum;
    }

    //Make elf position into known binary forms (0001),(0010),(0100)
    public int getElfCodeFromPosition(int elfPosition) {
        switch (elfPosition) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 4;
        }
        return 0;
    }

    public int solvePart2(InputStream input) throws IOException {
        int sum = 0;
        String line = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        HashMap<Integer, Integer> charCount = new HashMap<>(52);
        int elfCount = 0;
        int groupCount = 0;
        String groupRuckSack = "";
        //extra check to process the last group after parsing final line of input
        while ((line = br.readLine()) != null || (line == null && elfCount == 3)) {
            if (elfCount == 3) {
                groupCount++;
                int elfGroupBadge = 0;
                for (Integer key : charCount.keySet()) {
                    System.out.print(key + "(" + getFromNumericValue(key) + ")[" + charCount.get(key) + "]" + ",");
                    if (charCount.get(key) == 7) {
                        if (elfGroupBadge != 0) {
                            System.err.println("MORE THAN 1 BADGE FOUND FOR ELFGROUP " + groupCount + "!!!");
                        }
                        elfGroupBadge = key;
                    }
                }
                System.out.println("\n+@#$%++@\nElfGroup " + groupCount + " has badge item " + elfGroupBadge + "(" + getFromNumericValue(elfGroupBadge) + ") in RuckSacks\n " + groupRuckSack);
                sum += elfGroupBadge;
                groupRuckSack = "";
                elfCount = 0;
                charCount = new HashMap<>(52);
            }
            if (line != null) {
                elfCount++;
                groupRuckSack += line + "\n";
                char[] ruckSackItems = line.toCharArray();
                int compartmentSize = ruckSackItems.length;
                for (int i = 0; i < compartmentSize; i++) {
                    int priority1 = this.getPriorityFromChar(ruckSackItems[i]);
                    int c1 = charCount.merge(priority1, getElfCodeFromPosition(elfCount),
                            //Bit-wise or to merge the bits of all values (0001 | 0010 = 0011),(0011 | 0100 = 0111) so the final value for an item present in all 3 rucksacks is 7 (0111)
                            (originalElf, thisElf) -> originalElf | thisElf);
                }
            }
        }
        System.out.println("Finished counting Rucksacks with " + sum);
        return sum;
    }

    @Override
    public InputStream example() {
        return new ByteArrayInputStream(example.getBytes());
    }

    @Override
    public int solve(InputStream input) throws IOException {
//        return solvePart1(input);
        return solvePart2(input);
    }
}
