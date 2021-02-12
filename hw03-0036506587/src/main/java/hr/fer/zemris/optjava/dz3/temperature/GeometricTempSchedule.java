package hr.fer.zemris.optjava.dz3.temperature;

/**
 * An implementation of {@link ITempSchedule} interface. It provides geometrical schedule:
 * <pre>Tk = alpha^k * T0</pre>
 */
public class GeometricTempSchedule implements ITempSchedule {

    private final double alpha;
    private double tCurrent;
    private final int innerLimit;
    private final int outerLimit;

    /**
     * Constructor.
     *
     * @param alpha      alpha.
     * @param tInitial   initial temperature.
     * @param innerLimit inner loop limit.
     * @param outerLimit outer loop limit.
     */
    public GeometricTempSchedule(
            double alpha,
            double tInitial,
            int innerLimit,
            int outerLimit) {
        this.alpha = alpha;
        this.tCurrent = tInitial;
        this.innerLimit = innerLimit;
        this.outerLimit = outerLimit;
    }

    @Override
    public double getNextTemperature() {
        tCurrent *= alpha;
        return tCurrent;
    }

    @Override
    public int getInnerLoopCounter() {
        return innerLimit;
    }

    @Override
    public int getOuterLoopCounter() {
        return outerLimit;
    }

}
