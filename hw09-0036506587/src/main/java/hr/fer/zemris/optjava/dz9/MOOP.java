package hr.fer.zemris.optjava.dz9;

import hr.fer.zemris.optjava.dz9.algorithm.NSGA;
import hr.fer.zemris.optjava.dz9.problem.MOOPProblem;
import hr.fer.zemris.optjava.dz9.problem.Problem1;
import hr.fer.zemris.optjava.dz9.problem.Problem2;

public class MOOP {

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Program ocekuje 4 argumenta");
            return;
        }

        String fja = args[0];
        int velpop = Integer.parseInt(args[1]);
        String vrsta = args[2];
        int maxiter = Integer.parseInt(args[3]);

        MOOPProblem problem = fja.equals("1") ? new Problem1() : new Problem2();

        new NSGA(problem, velpop, vrsta, maxiter).run();
    }
}
