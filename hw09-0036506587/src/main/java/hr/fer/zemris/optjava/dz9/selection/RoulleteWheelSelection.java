package hr.fer.zemris.optjava.dz9.selection;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

public class RoulleteWheelSelection {

    public static int selection(Random rand, double[] fitnesses) {
        List<Entry<Integer, Double>> listOfPairs = new ArrayList<>();
        for (int i = 0; i < fitnesses.length; i++) {
            listOfPairs.add(new SimpleEntry<>(i, fitnesses[i]));
        }
        listOfPairs.sort((p1, p2) -> Double.compare(p2.getValue(), p1.getValue()));

        double maxFitness = listOfPairs.get(0).getValue();
        double sum = 0;
        for (Entry<Integer, Double> entry : listOfPairs) {
            sum += entry.getValue() - maxFitness;
        }

        double previousProbability = 0;

        for (Entry<Integer, Double> entry : listOfPairs) {
            double value = entry.getValue();
            double probability = (value - maxFitness) / sum;
            previousProbability += probability;
            if (rand.nextDouble() < previousProbability) return entry.getKey();
        }

        return listOfPairs.get(listOfPairs.size() - 1).getKey();
    }
}
