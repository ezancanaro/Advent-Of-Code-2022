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
import java.util.Iterator;
import java.util.TreeSet;

/**
 *
 * @author Eric
 */
public class Puzzle1 implements AdventPuzzle {

    public final String inp = "1000\n"
            + "2000\n"
            + "3000\n"
            + "\n"
            + "4000\n"
            + "\n"
            + "5000\n"
            + "6000\n"
            + "\n"
            + "7000\n"
            + "8000\n"
            + "9000\n"
            + "\n"
            + "10000";

    @Override
    public InputStream example() {
        return new ByteArrayInputStream(inp.getBytes());
    }

    @Override
    public int solve(InputStream input) throws IOException {
        int maxCal = 0;
        TreeSet<Integer> caloriesList = new TreeSet<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
//        String[] lines = input.split("\n");
        int elfCalories = 0;
        int carrierElf = 0;
        int top3[] = {0, 0, 0};
        String line = "";
        while ((line = br.readLine()) != null) {
            if (line.length() == 0) {
                caloriesList.add(elfCalories);
                for (int i = 2, j = 0; i >= 0; i--) {
                    if (top3[i] < elfCalories) {
                        System.out.println("B->[0]:" + top3[0] + "[1]:" + top3[1] + "[2]:" + top3[2]);
                        try {
                            top3[i - 2] = top3[i - 1];
                        } catch (ArrayIndexOutOfBoundsException aiof) {
                        }
                        try {
                            top3[i - 1] = top3[i];
                        } catch (ArrayIndexOutOfBoundsException aiof) {
                        }
                        top3[i] = elfCalories;
                        System.out.println("A->[0]:" + top3[0] + "[1]:" + top3[1] + "[2]:" + top3[2]);
                        break;
                    }
                }
                elfCalories = 0;
                continue;
            }
            try {
                elfCalories += Integer.parseInt(line);
            } catch (NumberFormatException nfe) {
                System.out.println("Input não é int:" + line);
            }
        }
        maxCal = top3[0] + top3[1] + top3[2];
        System.out.println("Elf " + carrierElf + " is carrying the most Calories: " + maxCal);
        int i = 0;
        Iterator it = caloriesList.descendingIterator();
        maxCal = 0;
        System.out.println("Top 3: ");
        while (it.hasNext() && i++ < 3) {
            int cal = (Integer) it.next();
            maxCal += cal;
            System.out.println(cal);
        }
        System.out.println("Elf " + carrierElf + " is carrying the most Calories: " + maxCal);
        return maxCal;
    }
}
