package hr.fer.zemris.trisat.algorithms;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.BitVectorNGenerator;
import hr.fer.zemris.trisat.SATFormula;
import hr.fer.zemris.trisat.SATFormulaStats;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Algorithm3 {

    private static final int NUM_OF_ITERATIONS = (int) Math.pow(10, 4);
    private static final int NUM_OF_BEST = 2;

    public static BitVector doAlgorithm(SATFormula satFormula) {
        BitVector result = null;

        SATFormulaStats stats = new SATFormulaStats(satFormula);

        // initial values
        Random rand = new Random();
        BitVector varValue = new BitVector(rand, satFormula.getNumberOfVariables());
        int t = 0;

        while (t < NUM_OF_ITERATIONS) {
            stats.setAssignment(varValue, true);
            if (stats.isSatisfied()) {
                result = varValue;
                break;
            }
            List<BitVector> bestNeighbors = getBestNeighbors(stats, varValue);
            varValue = bestNeighbors.get(rand.nextInt(bestNeighbors.size()));
            t++;
        }

        return result;
    }

    private static List<BitVector> getBestNeighbors(SATFormulaStats stats, BitVector varValue) {
        List<BitVectorModel> models = new ArrayList<>();
        double maxFit = 0;
        BitVectorNGenerator nGenerator = new BitVectorNGenerator(varValue);
        for (BitVector neighbor : nGenerator) {
            stats.setAssignment(neighbor, false);
            double fit = stats.getNumberOfSatisfied() + stats.getPercentageBonus();
            if (fit > maxFit) {
                maxFit = fit;
            }
            models.add(new BitVectorModel(neighbor, fit));
        }
        return models.stream()
                .sorted((m1, m2) -> Double.compare(m2.fit, m1.fit))
                .map(m -> m.vector)
                .limit(NUM_OF_BEST)
                .collect(Collectors.toList());
    }

    private static final class BitVectorModel {
        private BitVector vector;
        private double fit;

        private BitVectorModel(BitVector vector, double fit) {
            this.vector = vector;
            this.fit = fit;
        }
    }

}
