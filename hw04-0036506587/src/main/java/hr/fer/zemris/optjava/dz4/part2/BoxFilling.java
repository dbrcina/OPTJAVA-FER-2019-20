package hr.fer.zemris.optjava.dz4.part2;

import hr.fer.zemris.optjava.dz4.part2.algorithm.HGGA;
import hr.fer.zemris.optjava.dz4.part2.model.unit.Box;
import hr.fer.zemris.optjava.dz4.part2.model.item.Stick;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Simulation program.
 */
public class BoxFilling {

    /**
     * Main entry of this program.
     *
     * @param args arguments givne through command line.
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 8) {
            System.out.println("Program očekuje 7 argumenata");
            return;
        }

        // arguments from command line
        Path file = Paths.get(args[0]);
        int populationSize = Integer.parseInt(args[1]);
        int n = Integer.parseInt(args[2]);
        int m = Integer.parseInt(args[3]);
        boolean p = Boolean.parseBoolean(args[4]);
        int iterations = Integer.parseInt(args[5]);
        int s = Integer.parseInt(args[6]);
        double sigma = Double.parseDouble(args[7]);

        // parsing box capacity
        String fileName = file.getFileName().toString();
        int index1 = fileName.indexOf("-") + 1;
        int index2 = fileName.indexOf("-", index1);
        int height = Integer.parseInt(fileName.substring(index1, index2));

        // parsing sticks sticks
        List<String> lines = Files.readAllLines(file).stream()
                .map(line -> line.replaceAll("[\\[\\]]", ""))
                .collect(Collectors.toList());
        List<Stick> sticks = Arrays.stream(lines.get(0).split(",\\s+"))
                .mapToInt(Integer::parseInt)
                .mapToObj(Stick::new)
                .collect(Collectors.toList());

        Box solution = new HGGA(
                height,
                populationSize,
                n,
                m,
                p,
                iterations,
                s,
                sigma,
                sticks
        ).run();
        System.out.println("Najbolje rješenje: ");
        System.out.println(solution);
    }
}
