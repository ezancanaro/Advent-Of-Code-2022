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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jdk.nashorn.internal.objects.Global;

/**
 *
 * @author Eric
 */
public class Puzzle11 implements AdventPuzzle {

    BufferedReader br;

    @Override
    public InputStream example() {
        return new ByteArrayInputStream(
                (""
                        + "Monkey 0:\n"
                        + "  Starting items: 79, 98\n"
                        + "  Operation: new = old * 19\n"
                        + "  Test: divisible by 23\n"
                        + "    If true: throw to monkey 2\n"
                        + "    If false: throw to monkey 3\n"
                        + "\n"
                        + "Monkey 1:\n"
                        + "  Starting items: 54, 65, 75, 74\n"
                        + "  Operation: new = old + 6\n"
                        + "  Test: divisible by 19\n"
                        + "    If true: throw to monkey 2\n"
                        + "    If false: throw to monkey 0\n"
                        + "\n"
                        + "Monkey 2:\n"
                        + "  Starting items: 79, 60, 97\n"
                        + "  Operation: new = old * old\n"
                        + "  Test: divisible by 13\n"
                        + "    If true: throw to monkey 1\n"
                        + "    If false: throw to monkey 3\n"
                        + "\n"
                        + "Monkey 3:\n"
                        + "  Starting items: 74\n"
                        + "  Operation: new = old + 3\n"
                        + "  Test: divisible by 17\n"
                        + "    If true: throw to monkey 0\n"
                        + "    If false: throw to monkey 1"
                        + "")
                        .getBytes());
    }

    public void printThisThing(String thing) {
        if (printMonkeyAction) {
            System.out.println(thing);
        }
    }

    private class Monkey {

        //A list of items held by this monkey boy
        private Queue<Double> heldItems;
        //A function of the worry level returning a new worry level
        private Function<Double, Double> operation;
        //The test and action of the monkey when inspecting something
        private Consumer<Double> testAndAct;

        private long itemsInspected = 0;

        public Monkey() {

        }

        public Monkey(Queue<Double> items, Function<Double, Double> operation, Consumer<Double> testAndAct) {
            this.heldItems = items;
            this.operation = operation;
            this.testAndAct = testAndAct;
        }

        public void takeTurn() {
            //The monkey always throws it's item so we can remove the first item of the queue with no worries
            Double item = this.heldItems.remove();
            itemsInspected++;
            printThisThing("\tMonkey inspects an item with a worry level of " + item);
//            if (this.operation.apply(item).equals(Global.Infinity)) {
//                System.out.println("FUCKING OVERFLOW ON : " + item);
//                System.out.println("Max Double:" + Double.MAX_VALUE);
//                System.out.println("Max Double:" + Double.MAX_VALUE);
//                System.exit(1);
//            }
            Double newWorryLevel = this.operation.apply(item);
            printThisThing("\t\tWorry Level increased to " + newWorryLevel);
            newWorryLevel = reduceWorries ? (int) Math.floor(newWorryLevel / 3) : newWorryLevel;
            printThisThing("\t\tMonkey gets bored with item. Worry level is divided by 3 to " + newWorryLevel);
            this.testAndAct.accept(newWorryLevel);
        }

        public void catchItem(Double item) {
            this.heldItems.add(item);
        }

    }

    ArrayList<Monkey> monkeyList;

    Pattern intPattern = Pattern.compile("\\d+");

    boolean reduceWorries = true;
    boolean printMonkeyAction = true;

    public LinkedList<Double> getIntsFromString(String line) {
        Matcher m = intPattern.matcher(line);
        LinkedList<Double> list = new LinkedList<>();
        while (m.find()) {
            list.add(Double.parseDouble(m.group()));
        }
        return list;
    }

    public Function<Double, Double> parseFunctionFromString(String line) {
        String[] parts = line.substring(line.indexOf("=") + 1).trim().split(" ");
        final String left = parts[0];
        final String operand = parts[1];
        final String right = parts[2];
        if (debugApply) {

        }
        Function<Double, Double> f = "+".equals(operand)
                ? ((t)
                -> {
            if (debugApply) {
                System.out.println(("old".equals(left) ? t : Double.parseDouble(left)) + " + " + ("old".equals(right) ? t : Double.parseDouble(right)));
            }
            return ("old".equals(left) ? t : Double.parseDouble(left))
                    + ("old".equals(right) ? t : Double.parseDouble(right));
        })
                : ((t)
                -> {
            if (debugApply) {
                System.out.println(("old".equals(left) ? t : Double.parseDouble(left)) + " * " + ("old".equals(right) ? t : Double.parseDouble(right)));
            }
            return ("old".equals(left) ? t : Double.parseDouble(left))
                    * ("old".equals(right) ? t : Double.parseDouble(right));
        });
        return f;
    }

    public Predicate<Double> parsePredicateFromString(String line) {
        final Double divisor = getIntsFromString(line).getFirst();
        return ((t) -> t % divisor == 0);

    }

    public Consumer<Double> createMonkeyActFunction(Predicate<Double> predicate, int monkeyTrue, int monkeyFalse) {
        //Tests the predicate and throws the item to the next monke
        return ((t) -> {
            boolean passedTheTest = predicate.test(t);
            Monkey catcher = passedTheTest ? monkeyList.get(monkeyTrue) : monkeyList.get(monkeyFalse);
            printThisThing("\t\tItem with worry level " + t + " is thrown to monkey " + (passedTheTest ? monkeyTrue : monkeyFalse));
            catcher.catchItem(t);
        });
    }

    public ArrayList<Monkey> parseMonkeyDescriptions(InputStream input) throws IOException {
        String line = "";
        monkeyList = new ArrayList<>();
        br = new BufferedReader(new InputStreamReader(input));
        //Parse Instructions
        Monkey current = null;
        Predicate<Double> testPredicate = null;
        int monkeyWhenTrue = -1;
        int monkeyWhenFalse = -1;
        while ((line = br.readLine()) != null) {
            String[] op = line.trim().split(" ");
            switch (op[0]) {
                case "Monkey":
                    current = new Monkey();
                    break;
                case "Starting":
                    current.heldItems = getIntsFromString(line);
                    break;
                case "Operation:":
                    current.operation = parseFunctionFromString(line.replace("Operation:", "").trim());
                    break;
                case "Test:":
                    testPredicate = parsePredicateFromString(line.replace("Test:", "").trim());
                    printThisThing("created test predicate");
                    break;
                case "If":
                    if ("true:".equals(op[1])) {
                        monkeyWhenTrue = getIntsFromString(line).getFirst().intValue();
                        printThisThing("set monkeyTrue");
                    } else {
                        monkeyWhenFalse = getIntsFromString(line).getFirst().intValue();
                        printThisThing("set monkeyFalse");
                    }
                    break;
                case "":
                    if (current != null) {
                        current.testAndAct = createMonkeyActFunction(testPredicate, monkeyWhenTrue, monkeyWhenFalse);
                        printThisThing("Created testAndAct");
                        monkeyList.add(current);
                        current = null;
                    }
            }
        }
        if (current != null) {
            current.testAndAct = createMonkeyActFunction(testPredicate, monkeyWhenTrue, monkeyWhenFalse);
            printThisThing("Created testAndAct");
            monkeyList.add(current);
            current = null;
        }
        printThisThing("Created " + monkeyList.size() + " monkeys!");
        return monkeyList;
    }

    public void goOneRound() {
        int i = 0;
        for (Monkey m : monkeyList) {
            printThisThing("Monkey " + i++);
            while (m.heldItems.size() > 0) {
                m.takeTurn();
            }
        }
    }

    public int solvePart1(InputStream input) throws IOException, Exception {
        int sum = 0;
        parseMonkeyDescriptions(input);
        reduceWorries = true;
        for (int i = 1; i <= 20; i++) {
            printThisThing("¬¬¬¬¬¬¬¬¬¬¬¬  ROUND " + i + "  ¬¬¬¬¬¬¬¬¬¬¬¬");
            goOneRound();
        }
        //Find the 2 most active monkes
        long max = 0;
        long almostMax = 0;
        for (Monkey m : monkeyList) {
            if (m.itemsInspected > max) {
                almostMax = max;
                max = m.itemsInspected;
            } else if (m.itemsInspected > almostMax) {
                almostMax = m.itemsInspected;
            }
        }
        System.out.println("Most Devious: " + max);
        System.out.println("2nd Most Devious: " + almostMax);
        printThisThing("Total Items for the 2 Most Devious Monkes: " + max * almostMax);
        return sum;
    }

    public void printMonkeysItems() {
        int i = 0;
        for (Monkey m : monkeyList) {
            System.out.println("Monkey " + i++ + " inspected items " + m.itemsInspected + " times.");
        }
    }

    boolean debugApply = false;

    //X = middle of the sprite, sprite always has 3 pixels
    public int solvePart2(InputStream input) throws IOException, Exception {
        int sum = 0;
        parseMonkeyDescriptions(input);
        reduceWorries = false;
        printMonkeyAction = false;
//        debugApply = true;
        for (int i = 1; i <= 10000; i++) {
//        for (int i = 1; i <= 20; i++) { //simulate only 20 rounds
            printThisThing("¬¬¬¬¬¬¬¬¬¬¬¬  ROUND " + i + "  ¬¬¬¬¬¬¬¬¬¬¬¬");
            goOneRound();
            int round = i;
            if (round == 1 || round == 20 || round % 1000 == 0) {
                System.out.println("\n== After round " + round + " ==");
                printMonkeysItems();
            }
        }
        //Find the 2 most active monkes
        long max = 0;
        long almostMax = 0;
        for (Monkey m : monkeyList) {
            if (m.itemsInspected > max) {
                almostMax = max;
                max = m.itemsInspected;
            } else if (m.itemsInspected > almostMax) {
                almostMax = m.itemsInspected;
            }
        }
        System.out.println("Most Devious: " + max);
        System.out.println("2nd Most Devious: " + almostMax);
        System.out.println("Total Items for the 2 Most Devious Monkes: " + max * almostMax);
        return sum;
    }

    /**
     * Initial thoughts:
     * Seems like we would need a pretty complex parser for this description, but I will look into the examples to see how bad it gets.
     * As long as we parse the monkeys correctly the solution should not be a problem
     *
     * Implementation Remarks:
     * The input structure simplifies the parsing quite a lot.
     * Using functional interfaces to create lambdas make expressing the monkey functions a lot easier than it looked
     *
     * Part 2:
     * Fucking overflow man
     */
    @Override
    public int solve(InputStream input) throws IOException, Exception {
//        return solvePart1(input);
        return solvePart2(input);
    }

}
