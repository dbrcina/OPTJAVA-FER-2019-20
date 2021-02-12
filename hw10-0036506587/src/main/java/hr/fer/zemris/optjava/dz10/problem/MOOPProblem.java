package hr.fer.zemris.optjava.dz10.problem;

public abstract class MOOPProblem {

    protected final int numberOfObjectives;
    protected final double[] minArgumentBonds;
    protected final double[] maxArgumentBonds;
    protected final double[] minObjectivesValueBonds;
    protected final double[] maxObjectivesValueBonds;

    protected MOOPProblem(
            int numberOfObjectives,
            double[] minArgumentBonds,
            double[] maxArgumentBonds,
            double[] minObjectivesValueBonds,
            double[] maxObjectivesValueBonds) {
        this.numberOfObjectives = numberOfObjectives;
        this.minArgumentBonds = minArgumentBonds;
        this.maxArgumentBonds = maxArgumentBonds;
        this.minObjectivesValueBonds = minObjectivesValueBonds;
        this.maxObjectivesValueBonds = maxObjectivesValueBonds;
    }

    public int getNumberOfObjectives() {
        return numberOfObjectives;
    }

    public double[] getMinArgumentBonds() {
        return minArgumentBonds;
    }

    public double[] getMaxArgumentBonds() {
        return maxArgumentBonds;
    }

    public double[] getMinObjectivesValueBonds() {
        return minObjectivesValueBonds;
    }

    public double[] getMaxObjectivesValueBonds() {
        return maxObjectivesValueBonds;
    }

    public abstract void evaluateSolution(double[] solution, double[] objectives);

}
