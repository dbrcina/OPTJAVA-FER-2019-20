package hr.fer.zemris.trisat;

import java.util.Objects;
import java.util.Random;

/**
 * Model of {@link BitVector}.
 */
public class MutableBitVector extends BitVector {

	/**
	 * Delegates {@link BitVector#BitVector(Random, int)}.
	 *
	 * @param n size of vector.
	 */
	public MutableBitVector(int n) {
		super(n);
	}

	/**
	 * Delegates {@link BitVector#BitVector(boolean...)}.
	 *
	 * @param bits bits.
	 */
	public MutableBitVector(boolean... bits) {
		super(bits);
	}

	/**
	 * Sets provided <i>value</i> at position <i>index</i>.
	 *
	 * @param index index.
	 * @param value value.
	 * @throws IndexOutOfBoundsException if <i>index</i> is invalid.
	 */
	public void set(int index, boolean value) {
		Objects.checkIndex(index, bits.length);
		bits[index] = value;
	}
}
