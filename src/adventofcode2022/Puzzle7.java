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
public class Puzzle7 implements AdventPuzzle {

    @Override
    public InputStream example() {
        return new ByteArrayInputStream(
                (""
                        + "$ cd /\n"
                        + "$ ls\n"
                        + "dir a\n"
                        + "14848514 b.txt\n"
                        + "8504156 c.dat\n"
                        + "dir d\n"
                        + "$ cd a\n"
                        + "$ ls\n"
                        + "dir e\n"
                        + "29116 f\n"
                        + "2557 g\n"
                        + "62596 h.lst\n"
                        + "$ cd e\n"
                        + "$ ls\n"
                        + "584 i\n"
                        + "$ cd ..\n"
                        + "$ cd ..\n"
                        + "$ cd d\n"
                        + "$ ls\n"
                        + "4060174 j\n"
                        + "8033020 d.log\n"
                        + "5626152 d.ext\n"
                        + "7214296 k"
                        + "")
                        .getBytes());
    }

    private class ElfFile {

        public String fileName;
        public boolean isDir;
        public long size;
        public ArrayList<ElfFile> children;
        ElfFile parentDir;

        public ElfFile(String fileName, boolean isDir, long size, ElfFile parentDir) {
            this.fileName = fileName;
            this.isDir = isDir;
            this.size = size;
            this.children = new ArrayList<>();
            this.parentDir = parentDir;
        }

        public void propagateSizeUpdates(long sizeIncrement) {
            ElfFile p = this;
            if (this.isDir) {
                this.size += sizeIncrement;
            }
            while ((p = p.parentDir) != null) {
                p.size += sizeIncrement;
            }
        }

        public ElfFile getChild(String childName) {
            Optional<ElfFile> filtered = this.children.stream().filter((item) -> item.fileName.equals(childName)).findFirst();
            return filtered.isPresent() ? filtered.get() : null;
        }

        public boolean addChild(ElfFile child) {
            Optional<ElfFile> filtered = this.children.stream().filter((item) -> item.fileName.equals(child.fileName)).findAny();
            if (!filtered.isPresent()) {
                if (!child.isDir) {
                    this.propagateSizeUpdates(child.size);
                }
                return this.children.add(child);
            }
            return false;
        }
    }

    public void printFile(ElfFile file, int level) {
        for (int i = 0; i < level; i++) {
            System.out.print("\t");
        }
        System.out.println("- " + file.fileName + (file.isDir ? " (dir,tSize=" : "(file, size=") + file.size + ")");
    }

    public void printFileSystem(ElfFile root, int curLevel) {
        ElfFile current = root;
        printFile(current, curLevel);
        for (ElfFile f : current.children) {
            printFileSystem(f, curLevel + 1);
        }
    }

    public void findDirsWithSizeAtMost(ElfFile dir, long maxSize, ArrayList<ElfFile> dirsThatMatch) {
        if (dir.size < maxSize) {
//            System.out.println("Dir " + dir.fileName + " size (" + dir.size + ") < " + maxSize);
            dirsThatMatch.add(dir);
        }
        dir.children.forEach((file) -> {
            if (file.isDir) {
                findDirsWithSizeAtMost(file, maxSize, dirsThatMatch);
            }
        });
    }

    public void parseCommandsIntoFileSystemTree(ElfFile fsRoot, InputStream input) throws IOException {
        ElfFile currentDir = fsRoot;
        boolean listingDirFiles = false;
        String line = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        while ((line = br.readLine()) != null) {
            String cmd[] = line.split(" ");
//            System.out.println("Line:" + line);
            switch (cmd[1]) {
                case "cd":
                    listingDirFiles = false;
                    ElfFile nextCheck = "/".equals(cmd[2]) ? fsRoot : "..".equals(cmd[2]) ? currentDir.parentDir : currentDir.getChild(cmd[2]);
                    //IF the requested dir was not found, stay in the same directory. It probably should not happen if the example has no such mistake.
                    currentDir = nextCheck != null ? nextCheck : currentDir;
//                    System.out.println("$$$Switched to dir " + currentDir.fileName);
                    break;
                case "ls":
                    listingDirFiles = true;
                    break;
                default:
                    //it's listing the files inside a dir, add it to my current dir
                    if (!listingDirFiles) {
                        System.out.println("Unknown line parsed:" + line);
                    }
                    currentDir.addChild(new ElfFile(cmd[1], cmd[0].equals("dir"), cmd[0].equals("dir") ? 0 : Long.parseLong(cmd[0]), currentDir));
                //look for directory cmd[1] in root or, if it's / change to root
            }
        }
    }

    public int solvePart1(InputStream input) throws IOException, Exception {
        int sum = 0;
        HashMap<Integer, Integer> charCount = new HashMap<>(52);
        final Pattern intValues = Pattern.compile("(\\d+)");
        long maxSize = 100000;
        ElfFile fsRoot = new ElfFile("/", true, 0, null);
        //Build up my fileSystem tree
        parseCommandsIntoFileSystemTree(fsRoot, input);
        //print the fileSystem so we can see what we are working with
        printFileSystem(fsRoot, 0);
        ArrayList<ElfFile> dirs = new ArrayList<>();
        //Fill the array with dirs matching our size limit
        findDirsWithSizeAtMost(fsRoot, maxSize, dirs);
        //Sum all the entries with a map reduce
        Long totalSize = dirs.stream().map((dir) -> dir.size).reduce(0l, (acc, size) -> acc + size);
        //Annoying streams syntax for converting types
        Long ts = dirs.stream().reduce(0l, (acc, dir) -> acc + dir.size, (acc, val) -> acc + val);
        System.out.println("Total Size of deletable files:" + totalSize +" _ " + ts);
        return sum;
    }

    public int solvePart2(InputStream input) throws IOException, Exception {
        int sum = 0;
        HashMap<Integer, Integer> charCount = new HashMap<>(52);
        final Pattern intValues = Pattern.compile("(\\d+)");
        long maxSize = 100000;
        long totalDiskSpace = 70000000;
        long neededFreeSpace = 30000000;
        ElfFile fsRoot = new ElfFile("/", true, 0, null);
        parseCommandsIntoFileSystemTree(fsRoot, input);
        long currentFreeSpace = totalDiskSpace - fsRoot.size;
        long deletableSize = neededFreeSpace - currentFreeSpace;
        System.out.println("CurrentFreeSpace:" + currentFreeSpace);
//        printFileSystem(fsRoot, 0);
        ArrayList<ElfFile> dirs = new ArrayList<>();
        //just grab all directories without implementing anything new because I'm lazy
        findDirsWithSizeAtMost(fsRoot, Long.MAX_VALUE, dirs);
        //no more mapping, just reduce. Identity is root because it's the biggest of them all
        //Find The smallest dir such that dir.size > neededFreeSpace-currentFreeSpace. Size has to be bigger than deletable size AND smaller than previous elligible entry, starting at the fsRoot
        ElfFile victim = dirs.stream().reduce(fsRoot, (deleteVictim, dirToCheck) -> dirToCheck.size > deletableSize && dirToCheck.size < deleteVictim.size ? dirToCheck : deleteVictim);
        System.out.println("Should delete dir :" + victim.fileName + " with " + victim.size + " sizess");
        return sum;
    }

    /**
     * Initial thoughts:
     * Looks quite daunting at the start but should not be very complicated if we can parse it all into a Tree.
     * To keep the sizes we should just update the directory size count as we go instead of summing only at the end.
     *
     * Implementation Remarks:
     * Going by the linux idea of "everything is a file" makes this a lot easier.
     * There was no need to keep any reference to the files in the end of Part1, just marking the size would be enough, but alas. Maybe it's useful in part 2.
     * Having each "file" keep a reference to it's parent makes the size bookkeeping better, since we
     * just keep the file size updated as you are adding files to a directory by propagating it recursively to it's parent dirs.
     *
     * Quite fun to use recursion to print the Directory trees and to find the dirs mentioned in the solution.
     * Also applied a bunch of stream functions to filter the file arrays, which is very neat.
     *
     *
     * Part 2:
     * Did not need the reference to the files in my Tree after all :(.
     * In any case, another simple part due to the data structure chosen for dealing with part 1.
     * Just changed my filters in the final lines. Eliminated the map at the end in order to match the directory name with the printed tree for double checking my result.
     *
     * As an aside, I'm annoyed with Java's syntax when trying to reduce the array into a value that is not the same of the contained objects (ElfFile->Long), makes mapping first a lot easier to understand.
     * Specifying both the accumulator and combiner is annoying because the javadoc doesn't make it clear when the combiner function is applied. 
     * (It's for parallel processing by the way, this cool answer illustrates it nicely https://stackoverflow.com/a/33971436)
     * (U identity, BiFunction<U,? super T,U> accumulator, BinaryOperator<U> combiner)
     */
    @Override
    public int solve(InputStream input) throws IOException, Exception {
//        return solvePart1(input);
        return solvePart2(input);
    }

}
