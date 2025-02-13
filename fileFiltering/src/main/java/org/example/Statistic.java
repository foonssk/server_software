package org.example;

public class Statistic {
public void printShortStatistics(Stats stats) {
    System.out.println("Short statistics");

    if (stats.getCountIntegers() > 0) {
        System.out.println("Count Integers: " + stats.getCountIntegers());
    }

    if (stats.getCountFloats() > 0) {
        System.out.println("Count Floats: " + stats.getCountFloats());
    }

    if (stats.getCountString() > 0) {
        System.out.println("Count Strings: " + stats.getCountString());
    }

    if (stats.getCountIntegers() == 0 && stats.getCountFloats() == 0 && stats.getCountString() == 0) {
        System.out.println("All files are empty!");
    }


}
    public void printFullStatistics(Stats stats) {
        System.out.println("Full statistics");

        if (stats.getCountIntegers() > 0) {
            System.out.println("Count Integers: " + stats.getCountIntegers());
            System.out.println("Min: " + stats.getMinIntegers());
            System.out.println("Max: " + stats.getMaxIntegers());
            System.out.println("Avg: " + stats.getAverageIntegers());
        }

        if (stats.getCountFloats() > 0) {
            System.out.println("Count Floats: " + stats.getCountFloats());
            System.out.println("Min: " + stats.getMinFloats());
            System.out.println("Max: " + stats.getMaxFloats());
            System.out.println("Avg: " + stats.getAverageFloats());
        }

        if (stats.getCountString() > 0) {
            System.out.println("Count Strings: " + stats.getCountString());
            System.out.println("Min length: " + stats.getMinStringLength());
            System.out.println("Max length: " + stats.getMaxStringLength());
        }

        if (stats.getCountIntegers() == 0 && stats.getCountFloats() == 0 && stats.getCountString() == 0) {
            System.out.println("All files are empty!");
        }
    }
}