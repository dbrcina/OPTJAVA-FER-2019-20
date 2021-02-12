package hr.fer.zemris.trisat;

import java.util.Iterator;
import java.util.List;

/**
 * Model used for generating vectors neighborhood.
 */
public class BitVectorNGenerator implements Iterable<MutableBitVector> {

	private BitVector assignment;

	/**
	 * Constructor used for initialization.
	 *
	 * @param assignment assignment.
	 */
	public BitVectorNGenerator(BitVector assignment) {
		this.assignment = assignment;
	}

	/**
	 * Creates neighborhood of vector.
	 *
	 * @return an array of neighbors.
	 */
	public MutableBitVector[] createNeighborhood() {
		MutableBitVector[] neighbors = new MutableBitVector[assignment.getSize()];
		for (int i = 0; i < neighbors.length; i++) {
			MutableBitVector neighbor = new MutableBitVector(assignment.getBits());
			neighbor.set(i, !neighbor.get(i));
			neighbors[i] = neighbor;
		}
		return neighbors;
	}

	@Override
	public Iterator<MutableBitVector> iterator() {
		return List.of(createNeighborhood()).iterator();
	}

}
