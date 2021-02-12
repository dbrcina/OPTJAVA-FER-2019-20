package hr.fer.zemris.optjava.dz5.part1;

import hr.fer.zemris.optjava.dz5.part1.algorithm.RAPGA;

public class GeneticAlgorithm {

    private static final int MIN_POP = 2;
    private static final int MAX_POP = 1000;
    private static final int MAX_EFFORT = 1000;
    private static final int ITERATIONS = 150;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Program očekuje jedan argument");
            return;
        }
        int n = Integer.parseInt(args[0]);
        new RAPGA(MIN_POP, MAX_POP, MAX_EFFORT, n, ITERATIONS).run();
    }

}
