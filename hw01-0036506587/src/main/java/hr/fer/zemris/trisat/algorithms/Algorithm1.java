package hr.fer.zemris.trisat.algorithms;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.SATFormula;
import hr.fer.zemris.trisat.util.Util;

public class Algorithm1 {

	public static BitVector doAlgorithm(SATFormula satFormula) {
		BitVector result = null;
		int numberOfVariables = satFormula.getNumberOfVariables();

		int rows = (int) Math.pow(2, numberOfVariables);
		for (int i = 0; i < rows; i++) {
			BitVector varValue = new BitVector(Util.bitArrayFromNumber(i, numberOfVariables));
			if (satFormula.isSatisfied(varValue)) {
				System.out.println(varValue);
				if (result == null) {
					result = varValue;
				}
			}
		}

		return result;
	}
}
