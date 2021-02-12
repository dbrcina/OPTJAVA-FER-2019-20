package hr.fer.zemris.trisat.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.MutableBitVector;
import hr.fer.zemris.trisat.SATFormula;
import hr.fer.zemris.trisat.util.Util;

public class Algorithm6 {

    private static final int MAX_TRIES = 50;
    private static final int MAX_FLIPS = (int) Math.pow(10, 4);
    public static final double PERCENTAGE = 0.5;

    public static BitVector doAlgorithm(SATFormula satFormula) {
        BitVector result = null;

        // initial values
        int numberOfVariables = satFormula.getNumberOfVariables();
        int numberOfClauses = satFormula.getNumberOfClauses();
        Random rand = new Random();

        // algorithm
        for (int i = 0; i < MAX_TRIES; i++) {
            // random variable value
            BitVector varValue = new BitVector(rand, numberOfVariables);
            int lowFitCurrent = Util.lowFit(satFormula, varValue);

            for (int j = 0; j < MAX_FLIPS; j++) {
                if (lowFitCurrent == numberOfClauses) {
                    result = varValue;
                    break;
                }
                // entry where key value is highest fit and value is set of bit vectors
                // that generate that fit
                Map.Entry<Integer, HashSet<BitVector>> bestNeighbors = Util.findNeighbors(satFormula, varValue)
                        .entrySet().stream().findFirst().get();
                int lowFitNext = bestNeighbors.getKey();
                if (lowFitCurrent > lowFitNext) {
                    varValue = changeVarValuesRandomly(varValue, rand);
                    continue;
                }
                lowFitCurrent = lowFitNext;
                // get one random neighbor
                varValue = new ArrayList<>(bestNeighbors.getValue())
                        .get(rand.nextInt(bestNeighbors.getValue().size()));
            }

            if (result != null) break;
        }

        return result;
    }

    private static BitVector changeVarValuesRandomly(BitVector varValue, Random rand) {
        Set<Integer> indexes = randomlyGeneratedIndexes(varValue, rand);
        MutableBitVector vector = varValue.copy();
        indexes.forEach(i -> vector.set(i, !vector.get(i)));
        return vector;
    }

    private static Set<Integer> randomlyGeneratedIndexes(BitVector varValue, Random rand) {
        int numOfVarsToChange = (int) (varValue.getSize() * PERCENTAGE);
        Set<Integer> indexes = new HashSet<>();
        for (int i = 0; i < numOfVarsToChange; i++) {
            indexes.add(rand.nextInt(varValue.getSize()));
        }
        return indexes;
    }
}
