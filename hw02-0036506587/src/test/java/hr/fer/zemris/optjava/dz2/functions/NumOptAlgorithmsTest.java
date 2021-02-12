package hr.fer.zemris.optjava.dz2.functions;

import hr.fer.zemris.optjava.dz2.NumOptAlgorithms;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.jupiter.api.Test;

class NumOptAlgorithmsTest {

	@Test
    void testMinGradientDescent() {
        IHFunction function = new Function2();
        RealMatrix point = new Array2DRowRealMatrix(new double[][]{{-5, 5}});
        System.out.println(NumOptAlgorithms.minGradientDescent(function, 10000, null));
    }

    @Test
    void testMinNewton() {
        IHFunction function = new Function2();
        RealMatrix point = new Array2DRowRealMatrix(new double[][]{{-5, 5}});
        System.out.println(NumOptAlgorithms.minNewton(function, 1, point));
    }
}
