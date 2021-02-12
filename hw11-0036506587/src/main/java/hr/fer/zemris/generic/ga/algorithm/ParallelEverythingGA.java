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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ParallelEverythingGA {

    private final GrayScaleImage template;
    private final int rectangles;
    private final int popSize;
    private final int maxGens;
    private final double minFitness;
    private final int numberOfWorkers;

    private final List<IntArraySolution> population = new ArrayList<>();
    private IntArraySolution best;

    private final Queue<Integer> numberOfChildren = new ConcurrentLinkedQueue<>();
    private final Queue<IntArraySolution> evaluatedUnits = new ConcurrentLinkedQueue<>();

    public ParallelEverythingGA(
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
        initializePopulation();
        // initialize workers
        initWorkers();
        // do generations
        for (int generation = 0; generation < maxGens; generation++) {
            // send number of children
            sendNumberOfChildren();
            // get evaluated units
            List<IntArraySolution> evaluatedUnitsList = new ArrayList<>();
            for (int i = 0; i < popSize; ) {
                IntArraySolution unit = evaluatedUnits.poll();
                if (unit == null) continue;
                evaluatedUnitsList.add(unit);
                i++;
            }
            setEvaluatedUnitsIntoPopulation(evaluatedUnitsList);
            // population is filled with evaluated units
            // sort it in descending order by negative fitness
            population.sort(GASolution::compareTo);
            // find best solution
            IntArraySolution tempBest = population.get(0);
            // save best solution
            if (best == null || best.fitness < tempBest.fitness) {
                best = (IntArraySolution) tempBest.duplicate();
            } else {
                population.remove(0);
                population.add(0, best);
            }

            System.out.println((generation + 1) + ". iteracija: " + best.fitness);
            // loop condition
            if (best.fitness > minFitness) break;
        }
        killAllWorkers();
        return best;
    }

    private void initializePopulation() {
        Evaluator evaluator = new Evaluator(template);
        IRNG rng = RNG.getRNG();
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
            IntArraySolution solution = new IntArraySolution(data);
            evaluator.evaluate(solution);
            population.add(solution);
        }
    }

    private void initWorkers() {
        EVOThread[] workers = new EVOThread[numberOfWorkers];
        Runnable job = () -> {
            IRNG rng = RNG.getRNG();
            Evaluator evaluator = new Evaluator(template);
            RouletteWheelSelection selection = new RouletteWheelSelection();
            UniformCrossing crossing = new UniformCrossing();
            RandomMutation mutation = new RandomMutation();
            while (true) {
                Integer numOfChildren = numberOfChildren.poll();
                if (numOfChildren == null) continue;
                if (numOfChildren < 0) break;
                for (int i = 0; i < numOfChildren; i++) {
                    IntArraySolution[] parents = selection.select(population);
                    IntArraySolution child = crossing.crossover(parents);
                    mutation.mutate(child, template.getWidth(), template.getHeight());
                    evaluator.evaluate(child);
                    evaluatedUnits.offer(child);
                }
            }
        };
        for (int i = 0; i < numberOfWorkers; i++) {
            workers[i] = new EVOThread(job);
        }
        Arrays.stream(workers).forEach(EVOThread::start);
    }

    private void sendNumberOfChildren() {
        int numberOfJobs = popSize / numberOfWorkers;
        int residue = popSize % numberOfWorkers;
        for (int i = 0; i < numberOfJobs; i++) {
            numberOfChildren.offer(numberOfWorkers);
        }
        numberOfChildren.offer(residue);
    }

    private void setEvaluatedUnitsIntoPopulation(List<IntArraySolution> evaluatedUnitsList) {
        population.clear();
        population.addAll(evaluatedUnitsList);
    }

    private void killAllWorkers() {
        int counter = 0;
        while (counter < numberOfWorkers) {
            numberOfChildren.offer(-1);
            counter++;
        }
    }

}
