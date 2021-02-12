package hr.fer.zemris.optjava.dz2;

import hr.fer.zemris.optjava.dz2.functions.Function1;
import hr.fer.zemris.optjava.dz2.functions.Function2;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static hr.fer.zemris.optjava.dz2.NumOptAlgorithms.minGradientDescent;
import static hr.fer.zemris.optjava.dz2.NumOptAlgorithms.minNewton;

public class Jednostavno {

    public static void main(String[] args) {
        if (args.length != 2 && args.length != 4) {
            System.out.println("Program očekuje 2 ili 4 argumenta!");
            return;
        }

        String taskNumber = args[0];
        int iterations = Integer.parseInt(args[1]);
        RealMatrix point = null;
        if (args.length == 4) {
            point = new Array2DRowRealMatrix(new double[][]{
                    {Double.parseDouble(args[2]), Double.parseDouble(args[3])}
            });
        }

        List<RealMatrix> results = null;
        switch (taskNumber) {
            case "1a":
                results = minGradientDescent(new Function1(), iterations, point);
                break;
            case "1b":
                results = minNewton(new Function1(), iterations, point);
                break;
            case "2a":
                results = minGradientDescent(new Function2(), iterations, point);
                break;
            case "2b":
                results = minNewton(new Function2(), iterations, point);
                break;
            default:
                System.out.println("Pogrešan broj zadatka");
                System.exit(-1);
        }

        lineChart(results, taskNumber);
    }

    private static void lineChart(List<RealMatrix> results, String taskNumber) {
        XYSeries dataset = new XYSeries("iteration");
        for (int i = 0; i < results.size(); i++) {
            double[] values = results.get(i).getRow(0);
            System.out.println("Rješenje u " + i + ". iteraciji: " +
                    "(" + values[0] + "," + values[1] + ")"
            );
            dataset.add(values[0], values[1]);
        }
        JFreeChart lineChartObject = ChartFactory.createXYLineChart(
                "Rezultati algoritma",
                "X1",
                "X2",
                new XYSeriesCollection(dataset),
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        int width = 640;
        int height = 480;
        File file = new File( taskNumber + ".jpeg");
        try {
            ChartUtilities.saveChartAsJPEG(file, lineChartObject, width, height);
        } catch (IOException ignorable) {
        }
    }
}
