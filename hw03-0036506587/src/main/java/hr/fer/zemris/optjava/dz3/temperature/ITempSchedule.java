package hr.fer.zemris.optjava.dz3.temperature;

/**
 * Model of simple temperature scheduler which has information about inner and outer loop
 * counters along with next temperature value.
 */
public interface ITempSchedule {

    double getNextTemperature();

    int getInnerLoopCounter();

    int getOuterLoopCounter();

}
