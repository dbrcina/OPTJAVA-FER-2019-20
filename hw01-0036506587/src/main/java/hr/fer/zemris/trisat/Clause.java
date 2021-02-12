package hr.fer.zemris.trisat;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * It represents one clause in {@link SATFormula} formula.
 */
public class Clause {

	private final int[] indexes;

	/**
	 * Constructor used for initialization.
	 *
	 * @param indexes an array of indexes.
	 */
	public Clause(int[] indexes) {
		this.indexes = indexes.clone();
	}

	/**
	 * Retrieves number of literals.
	 *
	 * @return number of literals.
	 */
	public int getSize() {
		return indexes.length;
	}

	/**
	 * Retrieves literal from <i>index</i> position.
	 *
	 * @param index index.
	 * @return literal.
	 * @throws IndexOutOfBoundsException if <i>index</i> is invalid.
	 */
	public int getLiteral(int index) {
		Objects.checkIndex(index, indexes.length);
		return indexes[index];
	}

	/**
	 * Checks whether this clause is satisfied by <i>assignment</i>.
	 *
	 * @param assignment assignment.
	 * @return <i>true</i> if clause is satisfied, otherwise <i>false</i>.
	 */
	public boolean isSatisfied(BitVector assignment) {
		for (int index : indexes) {
			boolean value = assignment.get(Math.abs(index) - 1);
			if (index < 0)
				value = !value;
			if (value)
				return true;
		}
		return false;
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(" + ");
		for (int index : indexes) {
			sj.add("x" + index);
		}
		return "(" + sj + ")";
	}
}
