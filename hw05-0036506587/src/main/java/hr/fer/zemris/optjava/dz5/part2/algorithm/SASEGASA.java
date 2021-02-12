package hr.fer.zemris.optjava.dz5.part2.algorithm;

import hr.fer.zemris.optjava.dz5.part2.function.FunctionImpl;
import hr.fer.zemris.optjava.dz5.part2.function.IFunction;
import hr.fer.zemris.optjava.dz5.part2.model.Unit;
import hr.fer.zemris.optjava.dz5.part2.parser.FileParser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class SASEGASA {

    private Path file;
    private int popSize;
    private int subPops;
    private FileParser parser;
    private Random rand = new Random();
    private final IFunction<Unit> function = new FunctionImpl();
    private final GAOffspringSelection selection = new GAOffspringSelection(1000, function);

    public SASEGASA(Path file, int popSize, int subPops) {
        this.file = file;
        this.popSize = popSize;
        this.subPops = subPops;
        this.parser = new FileParser(file);
    }

    public Unit run() throws IOException {
        List<Unit> population = initialPopulation();
        while (subPops > 1) {
            int div = popSize / subPops;
            int mod = popSize % subPops;
            int fromIndex = 0;
            for (int i = 0; i < subPops; i++) {
                if (i == subPops - 1 && mod != 0) {
                    selection.run(population.subList(fromIndex, fromIndex + mod));
                    break;
                }
                selection.run(population.subList(fromIndex, fromIndex + div));
                fromIndex += div;
            }
            subPops--;
            System.out.println(population.get(0));
        }
        Collections.sort(population);
        return population.get(0);
    }

    private List<Unit> initialPopulation() throws IOException {
        List<Unit> population = new ArrayList<>();
        Unit initialUnit = initialUnit();
        population.add(initialUnit);
        for (int i = 1; i < popSize; i++) {
            Unit unit = initialUnit.shuffle(rand);
            unit.setFitness(function.valutAt(unit));
            population.add(unit);
            initialUnit = unit;
        }
        return population;
    }

    private Unit initialUnit() throws IOException {
        Unit unit = parser.parse();
        unit.setPermutation(initialPermutation(unit.dimension()));
        unit.setFitness(function.valutAt(unit));
        return unit;
    }

    private int[] initialPermutation(int dimension) {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < dimension; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        return numbers.stream().mapToInt(i -> i).toArray();
    }

}
