package hr.fer.zemris.optjava.dz3;

import hr.fer.zemris.optjava.dz3.decoder.IDecoder;
import hr.fer.zemris.optjava.dz3.function.IFunction;
import hr.fer.zemris.optjava.dz3.neighborhood.INeighborhood;
import hr.fer.zemris.optjava.dz3.solution.SingleObjectiveSolution;
import hr.fer.zemris.optjava.dz3.temperature.ITempSchedule;

import java.util.Arrays;
import java.util.Random;

public class SimulatedAnnealing<T extends SingleObjectiveSolution> implements IOptAlgorithm<T> {

    private static final Random RAND = new Random();
    private static final double DELTA = 1E-3;

    private IDecoder<T> decoder;
    private INeighborhood<T> neighborhood;
    private T startWith;
    private IFunction function;
    private ITempSchedule schedule;
    private boolean minimize;

    public SimulatedAnnealing(
            IDecoder<T> decoder,
            INeighborhood<T> neighborhood,
            T startWith,
            IFunction function,
            ITempSchedule schedule,
            boolean minimize) {
        this.decoder = decoder;
        this.neighborhood = neighborhood;
        this.startWith = startWith;
        this.function = function;
        this.schedule = schedule;
        this.minimize = minimize;
    }

    @Override
    public T run() {
        T solution = startWith;
        solution.fitness = function.valueAt(decoder.decode(solution));
        for (int i = 0; i < schedule.getOuterLoopCounter(); i++) {
            double temperature = schedule.getNextTemperature();
            for (int j = 0; j < schedule.getInnerLoopCounter(); j++) {
                T neighbor = neighborhood.randomNeighbor(solution);
                neighbor.fitness = function.valueAt(decoder.decode(neighbor));
                double difference = neighbor.fitness - solution.fitness;
                if (!minimize) difference *= -1;
                if (difference <= 0 || RAND.nextDouble() < Math.exp(-difference / temperature))
                    solution = neighbor;
                System.out.print("Vanjska petlja:" + i + ", ");
                System.out.print("I-ta temperatura:" + temperature + ", ");
                System.out.print("Unutarnja petlja:" + j + ", ");
                System.out.print("Vrijednost funkcije:" + solution.fitness + ", ");
                System.out.print("Vrijednost solutiona:" + Arrays.toString(decoder.decode(solution)));
                System.out.println();
            }
            if (Math.abs(solution.fitness) <= DELTA) break;
        }
        return solution;
    }
}
