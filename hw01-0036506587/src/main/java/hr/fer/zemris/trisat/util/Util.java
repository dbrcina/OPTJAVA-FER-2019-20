package hr.fer.zemris.trisat.util;

import hr.fer.zemris.trisat.*;

import java.util.*;
import java.util.regex.Pattern;

public class Util {

	private static final Pattern COMPILE = Pattern.compile("");

	/**
	 * Calculates fit function for provided <i>x</i> as determined by lowest lvl, by
	 * {@link Clause#isSatisfied(BitVector)} method.
	 *
	 * @param satFormula formula.
	 * @param x          vector.
	 * @return calculated fit.
	 */
	public static int lowFit(SATFormula satFormula, BitVector x) {
		int result = 0;
		int numberOfClauses = satFormula.getNumberOfClauses();
		for (int i = 0; i < numberOfClauses; i++) {
			Clause clause = satFormula.getClause(i);
			if (clause.isSatisfied(x))
				result++;
		}
		return result;
	}

	/**
	 * Finds all neighbors of <i>varValue</i> as determined by
	 * {@link BitVectorNGenerator} class.
	 *
	 * @param satFormula formula.
	 * @param varValue   var value.
	 * @return map of results where key is fit value and value is set of vectors
	 *         that satisfied that fit. Map is sorted in reverse order.
	 */
	public static Map<Integer, HashSet<BitVector>> findNeighbors(SATFormula satFormula, BitVector varValue) {
		Map<Integer, HashSet<BitVector>> fitVectorMap = new TreeMap<>(Collections.reverseOrder());
		BitVectorNGenerator nGenerator = new BitVectorNGenerator(varValue);
		for (BitVector neighbor : nGenerator) {
			int fit = lowFit(satFormula, neighbor);
			fitVectorMap.merge(fit, new HashSet<>(List.of(neighbor)), (oldV, newV) -> {
				oldV.add(neighbor);
				return oldV;
			});
		}
		return fitVectorMap;
	}

	/**
	 * Generates bit boolean array from provided <i>number</i>.
	 *
	 * @param number number.
	 * @param size   size.
	 * @return boolean array.
	 */
	public static boolean[] bitArrayFromNumber(int number, int size) {
		String[] bitStringArray = COMPILE.split(Integer.toBinaryString(number));
		int bitStringArrayLen = bitStringArray.length;
		boolean[] bitBooleanArray = new boolean[Math.max(size, bitStringArrayLen)];
		for (int i = 0; i < bitStringArrayLen; i++) {
			bitBooleanArray[i] = !"0".equals(bitStringArray[bitStringArrayLen - 1 - i]);
		}
		return bitBooleanArray;
	}

}
