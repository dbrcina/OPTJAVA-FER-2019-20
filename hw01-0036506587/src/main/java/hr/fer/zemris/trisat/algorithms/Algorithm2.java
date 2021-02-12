package hr.fer.zemris.trisat.algorithms;

import hr.fer.zemris.trisat.*;
import hr.fer.zemris.trisat.util.Util;

import java.util.*;

public class Algorithm2 {

	private static final int NUM_OF_ITERATIONS = (int) Math.pow(10, 4);

	public static BitVector doAlgorithm(SATFormula satFormula) {
		BitVector result = null;

		// initial values
		Random rand = new Random();
		BitVector varValue = new BitVector(rand, satFormula.getNumberOfVariables());
		int t = 0;
		int fitCurrent = Util.lowFit(satFormula, varValue);

		do {
			if (fitCurrent == satFormula.getNumberOfClauses()) {
				result = varValue;
				break;
			}

			// entry where key value is highest fit and value is set of bit vectors
			// that generate that fit
			Map.Entry<Integer, HashSet<BitVector>> bestNeighbors = Util.findNeighbors(satFormula, varValue).entrySet()
					.stream().findFirst().get();

			int fitNext = bestNeighbors.getKey();
			if (fitCurrent > fitNext) {
				TriSATSolver.terminate("Zapeli smo u lokalnom optimumu...");
			}

			// get one random neighbor
			varValue = new ArrayList<>(bestNeighbors.getValue()).get(rand.nextInt(bestNeighbors.getValue().size()));

			fitCurrent = fitNext;
			t++;
		} while (t < NUM_OF_ITERATIONS);

		return result;
	}

}
