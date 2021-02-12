package hr.fer.zemris.generic.ga.algorithm;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.crossover.UniformCrossing;
import hr.fer.zemris.generic.ga.evaluator.Evaluator;
import hr.fer.zemris.generic.ga.mutation.RandomMutation;
import hr.fer.zemris.generic.ga.selection.RouletteWheelSelection;
import hr.fer.zemris.generic.ga.solution.GASolution;
import hr.fer.zemris.generic.ga.solution.IntArraySolution;
import hr.fer.zemris.optjava.rng.EVOThread;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ParallelEvaluationGA {

    private final GrayScaleImage template;
    private final int rectangles;
    private final int popSize;
    private final int maxGens;
    private final double minFitness;
    private final int numberOfWorkers;

    private final IRNG rng = RNG.getRNG();
    private final RouletteWheelSelection selection = new RouletteWheelSelection();
    private final UniformCrossing crossing = new UniformCrossing();
    private final RandomMutation mutation = new RandomMutation();

    private final List<IntArraySolution> population = new ArrayList<>();
    private IntArraySolution best;

    private final Queue<IntArraySolution> unitsToBeEvaluated = new ConcurrentLinkedQueue<>();
    private final Queue<IntArraySolution> evaluatedUnits = new ConcurrentLinkedQueue<>();

    public ParallelEvaluationGA(
            GrayScaleImage template, int rectangles, int popSize, int maxGens, double minFitness) {
        this.template = template;
        this.rectangles = rectangles;
        this.popSize = popSize;
        this.maxGens = maxGens;
        this.minFitness = minFitness;
        this.numberOfWorkers = Runtime.getRuntime().availableProcessors();
    }

    public IntArraySolution run() {
        // initial population
        offerInitialPopulation();
        // initialize workers
        initWorkers();
        // do generations
        for (int generation = 0; generation < maxGens; generation++) {
            // get evaluated units
            setEvaluatedUnitsIntoPopulation();
            // population is filled with evaluated units
            // sort it in descending order by negative fitness
            population.sort(GASolution::compareTo);
            // find best solution
            IntArraySolution tempBest = population.get(0);
            // save best solution
            if (best == null || best.fitness < tempBest.fitness) {
                best = (IntArraySolution) tempBest.duplicate();
            }

            System.out.println((generation + 1) + ". iteracija: " + best.fitness);
            // loop condition
            if (best.fitness > minFitness) break;

            generateNextOffer();
        }
        killAllWorkers();
        return best;
    }

    private void offerInitialPopulation() {
        int dataLength = 1 + rectangles * 5;
        for (int i = 0; i < popSize; i++) {
            int[] data = new int[dataLength];
            data[0] = rng.nextInt(0, 256);
            for (int j = 1; j < dataLength; j += 5) {
                data[j] = rng.nextInt(0, template.getWidth());
                data[j + 1] = rng.nextInt(0, template.getHeight());
                data[j + 2] = rng.nextInt(1, template.getWidth());
                data[j + 3] = rng.nextInt(1, template.getHeight());
                data[j + 4] = rng.nextInt(0, 256);
            }
            unitsToBeEvaluated.offer(new IntArraySolution(data));
        }
    }

    private void initWorkers() {
        final EVOThread[] workers = new EVOThread[numberOfWorkers];
        final Runnable job = () -> {
            Evaluator evaluator = new Evaluator(template);
            while (true) {
                IntArraySolution solution = unitsToBeEvaluated.poll();
                if (solution == null) continue;
                if (solution.getData() == null) break;
                evaluator.evaluate(solution);
                evaluatedUnits.offer(solution);
            }
        };
        for (int i = 0; i < numberOfWorkers; i++) {
            workers[i] = new EVOThread(job);
        }
        Arrays.stream(workers).forEach(EVOThread::start);
    }

    private void setEvaluatedUnitsIntoPopulation() {
        population.clear();
        int counter = 0;
        while (counter < popSize) {
            IntArraySolution unit = evaluatedUnits.poll();
            if (unit == null) continue;
            population.add(unit);
            counter++;
        }
    }

    private void generateNextOffer() {
        // selection pressure
        unitsToBeEvaluated.offer(best);
        unitsToBeEvaluated.offer(population.get(1));
        int counter = 2;
        while (counter < popSize) {
            IntArraySolution[] parents = selection.select(population);
            IntArraySolution child = crossing.crossover(parents);
            mutation.mutate(child, template.getWidth(), template.getHeight());
            unitsToBeEvaluated.offer(child);
            counter++;
        }
    }

    private void killAllWorkers() {
        int counter = 0;
        while (counter < numberOfWorkers) {
            unitsToBeEvaluated.offer(new IntArraySolution(null));
            counter++;
        }
    }

}
