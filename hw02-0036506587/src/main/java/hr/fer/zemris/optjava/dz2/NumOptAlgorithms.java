package hr.fer.zemris.optjava.dz2;

import hr.fer.zemris.optjava.dz2.functions.IFunction;
import hr.fer.zemris.optjava.dz2.functions.IHFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

/**
 * Class which provides numeric optimization algorithms;<br>
 * <i>Gradient descent</i> and <i>Newton's method.</i>
 */
public class NumOptAlgorithms {

    private static final double LOWER_BOUND = -5;
    private static final double UPPER_BOUND = 5;
    private static final double DELTA = 1E-8;
    private static final int LAMBDA_ITERATIONS = 200;

    /**
     * <i>Gradient descent</i> is a <b>first-order</b> iterative optimization algorithm for
     * finding the minimum of a function.
     *
     * @param function   function.
     * @param iterations number of iterations.
     * @param point      initial point value.
     * @return list of results.
     */
    public static List<RealMatrix> minGradientDescent(
            IFunction function,
            int iterations,
            RealMatrix point) {
        // input is gradient and point(which is ignored in this case)
        // output is vector d
        BiFunction<RealMatrix, RealMatrix, RealMatrix> vectorD =
                (g, p) -> g.scalarMultiply(-1);
        return doAlgorithm(function, vectorD, iterations, point, false);
    }

    /**
     * <i>Newton's method in optimization</i> is an iterative method for finding the roots of
     * a differentiable function f, which are solutions to the equation f (x) = 0. In
     * optimization, Newton's method is applied to the derivative f ′ of a twice-differentiable
     * function f to find the roots of the derivative (solutions to f ′(x) = 0), also known as
     * the stationary points of f. These solutions may be minima, maxima, or saddle points
     *
     * @param function   function.
     * @param iterations number of iterations.
     * @param point      initial point value.
     * @return list of results.
     */
    public static List<RealMatrix> minNewton(
            IHFunction function,
            int iterations,
            RealMatrix point) {
        // input is hesse-matrix and gradient
        // output is vector d
        BiFunction<RealMatrix, RealMatrix, RealMatrix> vectorD =
                (h, g) -> MatrixUtils.inverse(h.scalarMultiply(-1)).multiply(g);
        return doAlgorithm(function, vectorD, iterations, point, true);
    }

    /**
     * If <i>isNewton</i> flag is <i>true</i>, <i>Newton's method</i> is used, otherwise
     * <i>Gradient descent method.</i>
     *
     * @param function   function.
     * @param bifunction bifunction for calculating vector d.
     * @param iterations number of iterations.
     * @param point      initial point value.
     * @param isNewton   boolean flag.
     * @return list of results.
     */
    private static List<RealMatrix> doAlgorithm(
            IFunction function,
            BiFunction<RealMatrix, RealMatrix, RealMatrix> bifunction,
            int iterations,
            RealMatrix point,
            boolean isNewton) {
        List<RealMatrix> results = new ArrayList<>();
        int numberOfVariables = function.getNumberOfVariables();

        RealMatrix currentPoint;
        if (point != null) {
            currentPoint = point.copy();
        } else {
            // find inital value if one is not provided
            currentPoint = initialValue(numberOfVariables);
        }
        // add current point into list of results
        results.add(currentPoint);
        // do algorithm
        for (int i = 0; i < iterations; i++) {
            RealMatrix gradient = function.gradient(currentPoint);
            if (isSatisfied(gradient, numberOfVariables)) break;
            RealMatrix d;
            if (isNewton) {
                d = bifunction.apply(((IHFunction) function).hesse(currentPoint), gradient);
            } else {
                d = bifunction.apply(gradient, currentPoint);
            }
            double lambda = lambda(currentPoint, d, function);
            currentPoint = updateCurrentPoint(currentPoint, d, lambda);
            results.add(currentPoint);
        }

        return results;
    }

    /**
     * Calculates lambda iteratively.
     *
     * @param currentPoint current point.
     * @param d            vector d.
     * @param function     function.
     * @return lambda.
     */
    private static double lambda(
            RealMatrix currentPoint,
            RealMatrix d,
            IFunction function) {
        double lambdaLower = 0;
        double lambdaUpper = lambdaUpper(currentPoint, d, function);
        int iterations = LAMBDA_ITERATIONS;
        double lambda = (lambdaLower + lambdaUpper) / 2;
        while (iterations > 0) {
            double value = thetaDerivation(currentPoint.copy(), d, function, lambda);
            if (Math.abs(value) <= DELTA) break;
            if (value > 0) lambdaUpper = lambda;
            else lambdaLower = lambda;
            iterations--;
            lambda = (lambdaLower + lambdaUpper) / 2;
        }
        return lambda;
    }

    /**
     * Calculates lambda upper iteratively.
     *
     * @param currentPoint current point.
     * @param d            vector d.
     * @param function     function.
     * @return lambda upper.
     */
    private static double lambdaUpper(
            RealMatrix currentPoint,
            RealMatrix d,
            IFunction function) {
        double lambdaUpper = 1;
        int iterations = LAMBDA_ITERATIONS;
        while (iterations >0) {
            double value = thetaDerivation(currentPoint.copy(), d, function, lambdaUpper);
            if (value >= 0.0) break;
            lambdaUpper *= 2;
            iterations--;
        }
        return lambdaUpper;
    }

    /**
     * Calculates derivation of <i>theta function</i> based on formula:
     * <pre>thetaDerivation = grad(currentPoint)[transposed] * d</pre>.
     *
     * @param currentPoint current point.
     * @param d            vector d.
     * @param function     function.
     * @param lambda       lambda.
     * @return theta derivation.
     */
    private static double thetaDerivation(
            RealMatrix currentPoint,
            RealMatrix d,
            IFunction function,
            double lambda) {
        currentPoint = updateCurrentPoint(currentPoint, d, lambda);
        RealMatrix currentGradient = function.gradient(currentPoint);
        return (currentGradient.transpose().multiply(d)).getEntry(0, 0);
    }

    /**
     * Updates <i>currentPoint</i> based on formula:
     * <pre>x1 = x0 + lambda * d</pre>
     * where <i>x0,x1</i> and <i>d</i> are vectors(matrix) and <i>lambda</i> is a scalar
     * value.
     *
     * @param currentPoint current point.
     * @param d            vector d.
     * @param lambda       lambda.
     * @return updated current point.
     */
    private static RealMatrix updateCurrentPoint(
            RealMatrix currentPoint,
            RealMatrix d,
            double lambda) {
        return currentPoint.add(d.transpose().scalarMultiply(lambda));
    }

    /**
     * @param gradient          gradient for certain point.
     * @param numberOfVariables number of variables.
     * @return <i>true</i> if gradient values are considered as minimum values, otherwise
     * <i>false</i>.
     */
    private static boolean isSatisfied(RealMatrix gradient, int numberOfVariables) {
        double[] minGradientValues = gradient.getColumn(0);
        long countSatisfied = Arrays.stream(minGradientValues)
                .filter(v -> Math.abs(v) <= DELTA)
                .count();
        return countSatisfied == numberOfVariables;
    }

    /**
     * Generates initial point value based on <i>numberOfVariables</i> and lower and upper
     * bonds.
     *
     * @param numberOfVariables number of variables.
     * @return initial value.
     */
    private static RealMatrix initialValue(int numberOfVariables) {
        double[] values = new double[numberOfVariables];
        Random rand = new Random();
        for (int i = 0; i < numberOfVariables; i++) {
            values[i] = LOWER_BOUND + rand.nextDouble() * 2 * UPPER_BOUND;
        }
        return new Array2DRowRealMatrix(new double[][]{values});
    }
}
