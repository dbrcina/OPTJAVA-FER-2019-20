package hr.fer.zemris.trisat.algorithms;

import hr.fer.zemris.trisat.*;
import hr.fer.zemris.trisat.util.Util;

import java.util.*;

public class Algorithm5 {

	private static final int MAX_TRIES = 50;
	private static final int MAX_FLIPS = (int) Math.pow(10, 4);
	private static final double PROBABILITY_P = 0.5;

	public static BitVector doAlgorithm(SATFormula satFormula) {
		BitVector result = null;

		// initial values
		int numberOfVariables = satFormula.getNumberOfVariables();
		int numberOfClauses = satFormula.getNumberOfClauses();
		Random rand = new Random();

		// algorithm
		for (int i = 0; i < MAX_TRIES; i++) {
			// random variable value
			MutableBitVector varValue = new MutableBitVector(numberOfVariables);

			for (int j = 0; j < MAX_FLIPS; j++) {
				if (Util.lowFit(satFormula, varValue) == numberOfClauses) {
					result = varValue;
					break;
				}

				Clause unsatisfiedClause = getRandomUnsatisfiedClause(satFormula, varValue, rand);

				if (rand.nextDouble() <= PROBABILITY_P) {
					int index = unsatisfiedClause.getLiteral(rand.nextInt(unsatisfiedClause.getSize()));
					if (index < 0)
						index *= -1;
					index--;
					varValue.set(index, !varValue.get(index));
				} else {
					BitVector flippedVarValue = getFlippedVarValue(satFormula, varValue, unsatisfiedClause);
					varValue = flippedVarValue.copy();
				}
			}

			if (result != null)
				break;
		}

		return result;
	}

	private static BitVector getFlippedVarValue(SATFormula satFormula, MutableBitVector varValue,
			Clause unsatisfiedClause) {
		List<BitVector> vectors = new ArrayList<>();
		for (int i = 0; i < unsatisfiedClause.getSize(); i++) {
			int index = unsatisfiedClause.getLiteral(i);
			if (index < 0)
				index *= -1;
			index--;
			varValue.set(index, !varValue.get(index));
			vectors.add(varValue);
			varValue.set(index, !varValue.get(index));
		}

		return getMostAccurateVarValue(satFormula, vectors);
	}

	private static BitVector getMostAccurateVarValue(SATFormula satFormula, List<BitVector> vectors) {
		return vectors.stream().max((Comparator.comparingInt(v -> Util.lowFit(satFormula, v)))).get();
	}

	private static Clause getRandomUnsatisfiedClause(SATFormula satFormula, BitVector minterm, Random rand) {
		List<Clause> unsatisfiedClauses = new ArrayList<>();
		for (int i = 0; i < satFormula.getNumberOfClauses(); i++) {
			Clause clause = satFormula.getClause(i);
			if (!clause.isSatisfied(minterm)) {
				unsatisfiedClauses.add(clause);
			}
		}
		return unsatisfiedClauses.get(rand.nextInt(unsatisfiedClauses.size()));
	}

}
