package hr.fer.zemris.trisat.algorithms;

import hr.fer.zemris.trisat.*;
import hr.fer.zemris.trisat.util.Util;

import java.util.*;

public class Algorithm4 {

	private static final int MAX_TRIES = 50;
	private static final int MAX_FLIPS = (int) Math.pow(10, 4);

	public static BitVector doAlgorithm(SATFormula satFormula) {
		BitVector result = null;

		// initial values
		int numberOfVariables = satFormula.getNumberOfVariables();
		int numberOfClauses = satFormula.getNumberOfClauses();
		Random rand = new Random();

		// algorithm
		for (int i = 0; i < MAX_TRIES; i++) {
			// random variable value
			BitVector varValue = new BitVector(rand, numberOfVariables);
			int lowFitCurrent = Util.lowFit(satFormula, varValue);

			for (int j = 0; j < MAX_FLIPS; j++) {
				if (lowFitCurrent == numberOfClauses) {
					result = varValue;
					break;
				}
				// entry where key value is highest fit and value is set of bit vectors
				// that generate that fit
				Map.Entry<Integer, HashSet<BitVector>> bestNeighbors = Util.findNeighbors(satFormula, varValue)
						.entrySet().stream().findFirst().get();
				int lowFitNext = bestNeighbors.getKey();
				if (lowFitCurrent > lowFitNext) {
					TriSATSolver.terminate("Zapeli smo u lokalnom optimumu...");
				}
				lowFitCurrent = lowFitNext;
				// get one random neighbor
				varValue = new ArrayList<>(bestNeighbors.getValue()).get(rand.nextInt(bestNeighbors.getValue().size()));
			}

			if (result != null)
				break;
		}

		return result;
	}

}
