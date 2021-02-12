package hr.fer.zemris.optjava.dz5.part2.parser;

import hr.fer.zemris.optjava.dz5.part2.model.Unit;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileParser {

    private Path file;

    public FileParser(Path file) {
        this.file = file;
    }

    public Unit parse() throws IOException {
        List<String> lines = Files.readAllLines(file).stream()
                .filter(line -> !line.isEmpty())
                .collect(Collectors.toList());
        int n = Integer.parseInt(lines.get(0).trim());
        List<String> distancesList = lines.subList(1, n + 1);
        List<String> goodsList = lines.subList(n + 1, 2 * n + 1);
        RealMatrix distances = parseMatrix(distancesList);
        RealMatrix goods = parseMatrix(goodsList);
        return new Unit(distances, goods);
    }

    private RealMatrix parseMatrix(List<String> matrixList) {
        int n = matrixList.size();
        RealMatrix matrix = new Array2DRowRealMatrix(n, n);
        for (int i = 0; i < n; i++) {
            String[] row = Arrays.stream(matrixList.get(i).split("\\s+"))
                    .filter(l -> !l.isEmpty())
                    .toArray(String[]::new);
            matrix.setRow(i, Arrays.stream(row)
                    .mapToDouble(Double::parseDouble)
                    .toArray()
            );
        }
        return matrix;
    }

}
