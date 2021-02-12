package hr.fer.zemris.optjava.dz2.functions;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Function1Test {

    @Test
    void testGetValue() {
        Function1 function1 = new Function1();
        double value = function1.getValue(new Array2DRowRealMatrix(new double[][]{
                {2, 3}
        }));
        Assertions.assertEquals(8, value);
    }

    @Test
    void testGradient() {
        Function1 function1 = new Function1();
        RealMatrix gradient = function1.gradient(new Array2DRowRealMatrix(new double[][]{
                {2, 2}
        }));
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {4.0},
                {2.0}
        });
        Assertions.assertEquals(expected, gradient);
    }
}
