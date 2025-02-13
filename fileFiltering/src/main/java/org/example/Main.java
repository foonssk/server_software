package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    private static final FileUtility fileUtility = new FileUtility();

    public static void main(String[] args) {
        Set<String> inputFiles = new HashSet<>();
        fileUtility.parseArguments(args, inputFiles);

        List<Integer> integers = new ArrayList<>();
        List<Double> floats = new ArrayList<>();
        List<String> strings = new ArrayList<>();

        fileUtility.filterFiles(inputFiles, integers, floats, strings);

        fileUtility.saveResultOnFiles("integers.txt", integers);
        fileUtility.saveResultOnFiles("floats.txt", floats);
        fileUtility.saveResultOnFiles("strings.txt", strings);

        fileUtility.showStatistics();
    }
}