package hr.fer.zemris.optjava.dz10.problem;

public class Problem1 extends MOOPProblem {

    public Problem1() {
        super(4,
                new double[]{-5.0, -5.0, -5.0, -5.0}, new double[]{5.0, 5.0, 5.0, 5.0},
                new double[]{0.0, 0.0, 0.0, 0.0}, new double[]{25.0, 25.0, 25.0, 25.0}
        );
    }

    @Override
    public void evaluateSolution(double[] solution, double[] objectives) {
        for (int i = 0; i < numberOfObjectives; i++) {
            if (solution[i] < minArgumentBonds[i] || solution[i] > maxArgumentBonds[i]) {
                throw new RuntimeException("Vrijednost argumenta je izvan granica");
            }
            objectives[i] = solution[i] * solution[i];
        }
    }

}
