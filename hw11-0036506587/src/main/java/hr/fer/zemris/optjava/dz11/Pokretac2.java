package hr.fer.zemris.optjava.dz11;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.algorithm.ParallelEvaluationGA;
import hr.fer.zemris.generic.ga.algorithm.ParallelEverythingGA;
import hr.fer.zemris.generic.ga.evaluator.Evaluator;
import hr.fer.zemris.generic.ga.solution.IntArraySolution;
import hr.fer.zemris.optjava.dz11.util.Util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class Pokretac2 {

    public static void main(String[] args) throws IOException {
        if (args.length != 7) {
            System.out.println("Program očekuje 7 argumenata");
            return;
        }

        File templateFile = new File(args[0]);
        int rectangles = Integer.parseInt(args[1]);
        int popSize = Integer.parseInt(args[2]);
        int maxGens = Integer.parseInt(args[3]);
        double minFitness = Double.parseDouble(args[4]);
        File paramsOutput = new File(args[5]);
        File generatedImage = new File(args[6]);

        GrayScaleImage template = GrayScaleImage.load(templateFile);
        ParallelEverythingGA algorithm = new ParallelEverythingGA(
                template, rectangles, popSize, maxGens, minFitness);
        long start = System.nanoTime();
        IntArraySolution best = algorithm.run();
        long end = System.nanoTime();
        System.out.println(Util.formatInterval(TimeUnit.NANOSECONDS.toMillis(end - start)));

        PrintWriter pwr = new PrintWriter(paramsOutput);
        for (int value : best.getData()) pwr.println(value);
        pwr.close();

        GrayScaleImage image = new Evaluator(template).draw(best, null);
        image.save(generatedImage);
    }

}
