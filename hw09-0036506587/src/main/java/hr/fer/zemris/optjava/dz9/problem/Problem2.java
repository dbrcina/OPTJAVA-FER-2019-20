package hr.fer.zemris.optjava.dz9.problem;

public class Problem2 extends MOOPProblem {

    public Problem2() {
        super(2,
                new double[]{0.1, 0.0}, new double[]{1.0, 5.0},
                new double[]{0.1, 1.0}, new double[]{1.0, 60.0}
        );
    }

    @Override
    public void evaluateSolution(double[] solution, double[] objectives) {
        for (int i = 0; i < numberOfObjectives; i++) {
            if (solution[i] < minArgumentBonds[i] || solution[i] > maxArgumentBonds[i]) {
                throw new RuntimeException("Vrijednost argumenta je izvan granica");
            }
        }
        objectives[0] = solution[0];
        objectives[1] = (1 + solution[1]) / solution[0];
    }

}
