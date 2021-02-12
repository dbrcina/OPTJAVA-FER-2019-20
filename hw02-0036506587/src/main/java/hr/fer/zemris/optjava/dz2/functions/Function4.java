package hr.fer.zemris.optjava.dz2.functions;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import static java.lang.Math.*;

// a = 7
// b = -3
// c = 2
// d = 1
// e = -3
// f = 3
public class Function4 implements IHFunction {

    private static final int NUMBER_OF_VARIABLES = 6;
    private static final double SCALE_VALUE = 1E10;

    private RealMatrix coefficients;
    private RealMatrix y;

    public Function4(RealMatrix coefficients, RealMatrix y) {
        this.coefficients = coefficients;
        this.y = y;
    }

    @Override
    public RealMatrix hesse(RealMatrix point) {
        double[][] hesse = new double[NUMBER_OF_VARIABLES][NUMBER_OF_VARIABLES];
        double a = point.getEntry(0, 0);
        double b = point.getEntry(0, 1);
        double c = point.getEntry(0, 2);
        double d = point.getEntry(0, 3);
        double e = point.getEntry(0, 4);
        double f = point.getEntry(0, 5);
        for (int i = 0; i < coefficients.getRowDimension(); i++) {
            double x1 = coefficients.getEntry(i, 0);
            double x2 = coefficients.getEntry(i, 1);
            double x3 = coefficients.getEntry(i, 2);
            double x4 = coefficients.getEntry(i, 3);
            double x5 = coefficients.getEntry(i, 4);
            double k = y.getEntry(i, 0);
            double pow = pow((cos(e * x4) + 1), 2);
            double v = b * x2 * pow(x1, 3) + a * x1 + f * x4 * x5 * x5 - k
                    + c * exp(d * x3) * (cos(e * x4) + 1);
            // first row
            hesse[0][0] += 2 * x1 * x1;
            hesse[0][1] += 2 * pow(x1, 4) * x2;
            hesse[0][2] += 2 * x1 * exp(d * x3) * (cos(e * x4) + 1);
            hesse[0][3] += 2 * c * x1 * x3 * exp(d * x3) * (cos(e * x4) + 1);
            hesse[0][4] += -2 * c * x1 * x4 * exp(d * x3) * sin(e * x4);
            hesse[0][5] += 2 * x1 * x4 * x5 * x5;
            // second row
            hesse[1][0] += hesse[0][1];
            hesse[1][1] += 2 * pow(x1, 6) * x2 * x2;
            hesse[1][2] += 2 * pow(x1, 3) * x2 * exp(d * x3) * (cos(e * x4) + 1);
            hesse[1][3] += 2 * c * pow(x1, 3) * x2 * x3 * exp(d * x3) * (cos(e * x4) + 1);
            hesse[1][4] += -2 * c * pow(x1, 3) * x2 * x4 * exp(d * x3) * sin(e * x4);
            hesse[1][5] += 2 * pow(x1, 3) * x2 * x4 * x5 * x5;
            // third row
            hesse[2][0] += hesse[0][2];
            hesse[2][1] += hesse[1][2];
            hesse[2][2] += 2 * exp(2 * d * x3) * pow;
            hesse[2][3] += 2 * c * x3 * exp(2 * d * x3) * pow
                    + 2 * x3 * exp(d * x3) * (cos(e * x4) + 1)
                    * v;
            hesse[2][4] += -2 * x4 * exp(d * x3) * sin(e * x4)
                    * v
                    - 2 * c * x4 * exp(2 * d * x3) * sin(e * x4) * (cos(e * x4) + 1);
            hesse[2][5] += 2 * x4 * x5 * x5 * exp(d * x3) * (cos(e * x4) + 1);
            // fourth row
            hesse[3][0] += hesse[0][3];
            hesse[3][1] += hesse[1][3];
            hesse[3][2] += hesse[2][3];
            hesse[3][3] += 2 * c * c * x3 * x3 * exp(2 * d * x3) * pow
                    + 2 * c * x3 * x3 * exp(d * x3) * (cos(e * x4) + 1) * v;
            hesse[3][4] += -2 * c * c * x3 * x4 * exp(2 * d * x3) * sin(e * x4) * (cos(e * x4) + 1)
                    - 2 * c * x3 * x4 * exp(d * x3) * sin(e * x4)
                    * v;
            hesse[3][5] += 2 * c * x3 * x4 * x5 * x5 * exp(d * x3) * (cos(e * x4) + 1);
            // fifth row
            hesse[4][0] += hesse[0][4];
            hesse[4][1] += hesse[1][4];
            hesse[4][2] += hesse[2][4];
            hesse[4][3] += hesse[3][4];
            hesse[4][4] += 2 * c * c * x4 * x4 * exp(2 * d * x3) * sin(pow(e * x4, 2))
                    - 2 * c * x4 * x4 * exp(d * x3) * cos(e * x4) * v;
            hesse[4][5] += -2 * c * x4 * x4 * x5 * x5 * exp(d * x3) * sin(e * x4);
            //sixth row
            hesse[5][0] += hesse[0][5];
            hesse[5][1] += hesse[1][5];
            hesse[5][2] += hesse[2][5];
            hesse[5][3] += hesse[3][5];
            hesse[5][4] += hesse[4][5];
            hesse[5][5] += 2 * x4 * x4 * pow(x5, 4);
        }
        return new Array2DRowRealMatrix(hesse);
    }

    @Override
    public int getNumberOfVariables() {
        return NUMBER_OF_VARIABLES;
    }

    @Override
    public double getValue(RealMatrix point) {
        double error = 0;
        double a = point.getEntry(0, 0);
        double b = point.getEntry(0, 1);
        double c = point.getEntry(0, 2);
        double d = point.getEntry(0, 3);
        double e = point.getEntry(0, 4);
        double f = point.getEntry(0, 5);
        for (int i = 0; i < coefficients.getRowDimension(); i++) {
            double x1 = coefficients.getEntry(i, 0);
            double x2 = coefficients.getEntry(i, 1);
            double x3 = coefficients.getEntry(i, 2);
            double x4 = coefficients.getEntry(i, 3);
            double x5 = coefficients.getEntry(i, 4);
            double yValue = y.getEntry(i, 0);
            double function = a * x1
                    + b * x1 * x1 * x1 * x2
                    + c * exp(d * x3) * (1 + cos(e * x4))
                    + f * x4 * x5 * x5
                    - yValue;
            error += function * function;
        }
        return error;
    }

    @Override
    public RealMatrix gradient(RealMatrix point) {
        double a = point.getEntry(0, 0);
        double b = point.getEntry(0, 1);
        double c = point.getEntry(0, 2);
        double d = point.getEntry(0, 3);
        double e = point.getEntry(0, 4);
        double f = point.getEntry(0, 5);
        double[] gradColumn = new double[6];
        for (int i = 0; i < coefficients.getRowDimension(); i++) {
            double x1 = coefficients.getEntry(i, 0);
            double x2 = coefficients.getEntry(i, 1);
            double x3 = coefficients.getEntry(i, 2);
            double x4 = coefficients.getEntry(i, 3);
            double x5 = coefficients.getEntry(i, 4);
            double yValue = y.getEntry(i, 0);
            double function = a * x1
                    + b * x1 * x1 * x1 * x2
                    + c * exp(d * x3) * (1 + cos(e * x4))
                    + f * x4 * x5 * x5
                    - yValue;
            gradColumn[0] += 2 * x1 * function;
            gradColumn[1] += 2 * x1 * x1 * x1 * x2 * function;
            gradColumn[2] += 2 * exp(d * x3) * (1 + cos(e * x4)) * function;
            gradColumn[3] += 2 * c * x3 * exp(d * x3) * (1 + cos(e * x4)) * function;
            gradColumn[4] += -2 * c * x4 * exp(d * x3) * sin(e * x4) * function;
            gradColumn[5] += 2 * x4 * x5 * x5 * function;
        }

        // gradColumn = Arrays.stream(gradColumn).map(v -> v / SCALE_VALUE).toArray();
        RealVector vector = new ArrayRealVector(gradColumn);
        vector.unitize();
        return new Array2DRowRealMatrix(new double[][]{vector.toArray()}).transpose();
    }

}
