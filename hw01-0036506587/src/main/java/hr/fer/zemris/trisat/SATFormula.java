package hr.fer.zemris.trisat;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Model of generic <i>sat</i> formula.
 */
public class SATFormula {

	private int numberOfVariables;
	private Clause[] clauses;

	/**
	 * Constructor used for initialization.
	 *
	 * @param numberOfVariables numOfVar.
	 * @param clauses           an array of clauses.
	 */
	public SATFormula(int numberOfVariables, Clause[] clauses) {
		this.numberOfVariables = numberOfVariables;
		this.clauses = clauses.clone();
	}

	/**
	 * @return number of variables.
	 */
	public int getNumberOfVariables() {
		return numberOfVariables;
	}

	/**
	 * @return number of clauses.
	 */
	public int getNumberOfClauses() {
		return clauses.length;
	}

	/**
	 * @param index index.
	 * @return clause from position <i>index</i>.
	 * @throws IndexOutOfBoundsException if <i>index</i> is invalid.
	 */
	public Clause getClause(int index) {
		Objects.checkIndex(index, clauses.length);
		return clauses[index];
	}

	/**
	 * Checks whether this formula is satisfied by <i>assignment</i>.
	 *
	 * @param assignment assignment.
	 * @return <i>true</i> if formula is satisfied, otherwise <i>false</i>.
	 */
	public boolean isSatisfied(BitVector assignment) {
		for (Clause clause : clauses) {
			if (!clause.isSatisfied(assignment))
				return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("f(");
		StringJoiner sj = new StringJoiner(",");
		for (int i = 0; i < numberOfVariables; i++) {
			sj.add("x" + (i + 1));
		}
		sb.append(sj).append(")=");

		for (Clause clause : clauses) {
			sb.append(clause);
		}

		return sb.toString();
	}
}
