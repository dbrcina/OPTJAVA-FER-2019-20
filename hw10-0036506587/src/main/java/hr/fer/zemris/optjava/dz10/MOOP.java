package hr.fer.zemris.optjava.dz10;

import hr.fer.zemris.optjava.dz10.algorithm.NSGA2;
import hr.fer.zemris.optjava.dz10.problem.MOOPProblem;
import hr.fer.zemris.optjava.dz10.problem.Problem1;
import hr.fer.zemris.optjava.dz10.problem.Problem2;

public class MOOP {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Program ocekuje 3 argumenta");
            return;
        }

        String fja = args[0];
        int velpop = Integer.parseInt(args[1]);
        int maxiter = Integer.parseInt(args[2]);

        MOOPProblem problem = fja.equals("1") ? new Problem1() : new Problem2();

        new NSGA2(problem, velpop, maxiter).run();
    }
}
