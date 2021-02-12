package hr.fer.zemris.trisat;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Model which stashes some statistics about sat formula.
 */
public class SATFormulaStats {

	private static final double PERCENTAGE_CONSTANT_UP = 0.01;
	private static final double PERCENTAGE_CONSTANT_DOWN = 0.1;
	private static final double PERCENTAGE_UNIT_AMOUNT = 50;

	private SATFormula satFormula;
	private double[] post;
	private int numberOfSatisfied;
	private int percentageBonus;

	/**
	 * Constructor used for initialization.
	 *
	 * @param satFormula formula.
	 */
	public SATFormulaStats(SATFormula satFormula) {
		this.satFormula = satFormula;
		post = new double[satFormula.getNumberOfClauses()];
	}

	/**
	 * Updates percentages if <i>updatePercentages</i> is set to <i>true</i>,
	 * otherwise it updates percentage bonus.
	 *
	 * @param assignment        assignment.
	 * @param updatePercentages boolean flag.
	 */
	public void setAssignment(BitVector assignment, boolean updatePercentages) {
		numberOfSatisfied = 0;
		Consumer<Integer> satisfied;
		Consumer<Integer> notSatisfied;

		if (updatePercentages) {
			satisfied = i -> post[i] += (1 - post[i]) * PERCENTAGE_CONSTANT_UP;
			notSatisfied = i -> post[i] += (0 - post[i]) * PERCENTAGE_CONSTANT_DOWN;
		} else {
			percentageBonus = 0;
			satisfied = i -> percentageBonus += PERCENTAGE_UNIT_AMOUNT * (1 - post[i]);
			notSatisfied = i -> percentageBonus -= PERCENTAGE_UNIT_AMOUNT * (1 - post[i]);
		}
		doUpdate(assignment, satisfied, notSatisfied);
	}

	private void doUpdate(BitVector assignment, Consumer<Integer> satisfied, Consumer<Integer> notSatisfied) {
		for (int i = 0; i < post.length; i++) {
			Clause clause = satFormula.getClause(i);
			if (clause.isSatisfied(assignment)) {
				numberOfSatisfied++;
				satisfied.accept(i);
			} else {
				notSatisfied.accept(i);
			}
		}
	}

	/**
	 * @return number of satisfied clauses.
	 */
	public int getNumberOfSatisfied() {
		return numberOfSatisfied;
	}

	/**
	 * @return <i>true</i> if clause is satisfied, otherwise <i>false</i>.
	 */
	public boolean isSatisfied() {
		return numberOfSatisfied == satFormula.getNumberOfClauses();
	}

	/**
	 * @return percentage bonus.
	 */
	public int getPercentageBonus() {
		return percentageBonus;
	}

	/**
	 * @param index index.
	 * @return percentage from position <i>index</i>.
	 * @throws IndexOutOfBoundsException if <i>index</i> is invalid.
	 */
	public double getPercentage(int index) {
		Objects.checkIndex(index, post.length);
		return post[index];
	}

}
