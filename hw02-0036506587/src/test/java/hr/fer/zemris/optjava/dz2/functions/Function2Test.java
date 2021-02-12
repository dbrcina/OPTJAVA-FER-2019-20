package hr.fer.zemris.optjava.dz2.functions;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Function2Test {

    @Test
    void testGetValue() {
        Function2 function2 = new Function2();
        double value = function2.getValue(new Array2DRowRealMatrix(new double[][]{
                {2, 3}
        }));
        Assertions.assertEquals(11, value);
    }

    @Test
    void testGradient() {
        Function2 function2 = new Function2();
        RealMatrix gradient = function2.gradient(new Array2DRowRealMatrix(new double[][]{
                {3, 3}
        }));
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {4.0},
                {20.0}
        });
        Assertions.assertEquals(expected, gradient);
    }
}
