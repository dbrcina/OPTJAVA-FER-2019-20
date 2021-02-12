package hr.fer.zemris.optjava.dz12;

import hr.fer.zemris.optjava.dz12.engine.GPEngine;
import hr.fer.zemris.optjava.dz12.ga.evaluator.Evaluator;
import hr.fer.zemris.optjava.dz12.ga.initializer.PopulationInitializer;
import hr.fer.zemris.optjava.dz12.ga.operators.crossover.SubtreeCrossover;
import hr.fer.zemris.optjava.dz12.ga.operators.mutation.SubtreeMutation;
import hr.fer.zemris.optjava.dz12.ga.operators.reproduction.NodeReproduction;
import hr.fer.zemris.optjava.dz12.ga.operators.selection.NTournamentSelection;
import hr.fer.zemris.optjava.dz12.ga.solution.Solution;
import hr.fer.zemris.optjava.dz12.gui.SimulationFrame;
import hr.fer.zemris.optjava.dz12.util.Util;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AntTrailGA {

    private Random rand = new Random();

    private int popSize;
    private int maxGens;
    private int minFitness;

    private PopulationInitializer initializer;
    private Evaluator evaluator;
    private NTournamentSelection selection;
    private SubtreeMutation mutation;
    private SubtreeCrossover crossover;
    private NodeReproduction reproduction;

    public AntTrailGA(int popSize, int maxGens, int minFitness, GPEngine engine) {
        this.popSize = popSize;
        this.maxGens = maxGens;
        this.minFitness = minFitness;
        initializer = new PopulationInitializer(rand);
        evaluator = new Evaluator(engine);
        selection = new NTournamentSelection(rand, 7);
        mutation = new SubtreeMutation(rand, evaluator, initializer);
        crossover = new SubtreeCrossover(rand, evaluator);
        reproduction = new NodeReproduction(selection);
    }

    public Solution run() {
        List<Solution> population = initializer.initialize(popSize, 2, 6);
        population.forEach(evaluator::evaluate);
        Solution best = population.stream().sorted().findFirst().get();

        for (int generation = 0; generation < maxGens; generation++) {
            System.out.println((generation + 1) + ". iteration, fitness: " + best.getFitness() + ", " +
                    "number of nodes: " + (best.getRoot().numberOfNodesBelow() + 1));
            if (best.getFitness() >= minFitness) break;

            // generate children
            List<Solution> children = new ArrayList<>();
            children.add(best);
            for (int i = 1; i < popSize; i++) {
                double random = rand.nextDouble();
                if (random < 0.01) children.add(reproduction.reproduce(population));
                if (random < 0.10) children.add(mutation.mutate(selection.select(population)));
                else children.add(crossover.crossover(
                        selection.select(population), selection.select(population)));
                Solution child = children.get(i);
                if (best.getFitness() == child.getFitness()) {
                    if (best.getRoot().numberOfNodesBelow() > child.getRoot().numberOfNodesBelow()) {
                        best = child;
                    }
                }
                if (best.getFitness() < child.getFitness()) {
                    best = child;
                }
            }
            population = children;
        }
        return best;

    }

    public static void main(String[] args) throws IOException {
        if (args.length != 5) {
            System.out.println("Program expects 5 arguments");
            return;
        }

        String platformDefinition = args[0];
        int maxGens = Integer.parseInt(args[1]);
        int popSize = Integer.parseInt(args[2]);
        int minFitness = Integer.parseInt(args[3]);
        String solutionFile = args[4];

        GPEngine engine = null;
        try {
            engine = Util.createGPEngine(Paths.get(platformDefinition), 600);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        AntTrailGA ga = new AntTrailGA(popSize, maxGens, minFitness, engine);
        Solution best = ga.run();

        PrintWriter pwr = new PrintWriter(Files.newOutputStream(Paths.get(solutionFile)));
        pwr.write(best.toString());
        pwr.close();

        GPEngine finalEngine = engine;
        SwingUtilities.invokeLater(() -> new SimulationFrame(best, finalEngine).setVisible(true));
    }

}
