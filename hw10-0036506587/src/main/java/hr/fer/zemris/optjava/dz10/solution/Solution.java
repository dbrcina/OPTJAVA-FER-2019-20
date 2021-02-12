package hr.fer.zemris.optjava.dz10.solution;

import java.util.Comparator;

public class Solution implements Comparable<Solution> {

    private double[] values;
    private double[] objectives;
    private int frontRank;
    private double crowdingDistance;

    private static final Comparator<Solution> CRWD_COMPARATOR =
            Comparator.comparingDouble(Solution::getFrontRank).reversed()
                    .thenComparing(Solution::getCrowdingDistance);

    public Solution(double[] values, double[] objectives) {
        this.values = values;
        this.objectives = objectives;
    }

    public double[] getValues() {
        return values;
    }

    public double[] getObjectives() {
        return objectives;
    }

    public int getFrontRank() {
        return frontRank;
    }

    public void setFrontRank(int frontRank) {
        this.frontRank = frontRank;
    }

    public double getCrowdingDistance() {
        return crowdingDistance;
    }

    public void setCrowdingDistance(double crowdingDistance) {
        this.crowdingDistance = crowdingDistance;
    }

    @Override
    public int compareTo(Solution other) {
        return CRWD_COMPARATOR.compare(this, other);
    }

}
