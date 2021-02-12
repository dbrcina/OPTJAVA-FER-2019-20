package hr.fer.zemris.optjava.dz5.part2;

import hr.fer.zemris.optjava.dz5.part2.algorithm.SASEGASA;
import hr.fer.zemris.optjava.dz5.part2.model.Unit;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GeneticAlgorithm {

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Program očekuje 3 argumenta");
        }

        Path file = Paths.get(args[0]);
        int popSize = Integer.parseInt(args[1]);
        int subPops = Integer.parseInt(args[2]);

        Unit solution = new SASEGASA(file, popSize, subPops).run();

        System.out.println("Najbolje rjesenje:");
        System.out.println(solution);
    }

}
