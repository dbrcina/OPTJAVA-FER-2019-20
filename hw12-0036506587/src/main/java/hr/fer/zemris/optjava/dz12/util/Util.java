package hr.fer.zemris.optjava.dz12.util;

import hr.fer.zemris.optjava.dz12.engine.GPEngine;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Util {

    public static GPEngine createGPEngine(Path file, int maxActions) throws Exception {
        try (BufferedReader br = Files.newBufferedReader(file)) {
            // first line is definition of platforms width and height
            int[] platformWidthHeight = Arrays.stream(br.readLine().trim().toUpperCase().split("X"))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            // platforms field definition...
            boolean[][] platform = new boolean[platformWidthHeight[0]][platformWidthHeight[1]];
            String line;
            int row = 1;
            while ((line = br.readLine()) != null && row <= platformWidthHeight[1]) {
                String[] rowLine = line.split("");
                if (rowLine.length != platformWidthHeight[0]) {
                    throw new RuntimeException("Width of " + row + ".row is different from definition");
                }
                for (int i = 0; i < platformWidthHeight[0]; i++) {
                    platform[row - 1][i] = rowLine[i].equals("1");
                }
                row++;
            }
            if (row - 1 != platformWidthHeight[1]) {
                throw new RuntimeException("Height of the platform is different from definition");
            }
            return new GPEngine(platform, maxActions);
        }
    }

}
