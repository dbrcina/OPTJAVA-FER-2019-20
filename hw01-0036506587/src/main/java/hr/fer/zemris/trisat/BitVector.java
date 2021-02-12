package hr.fer.zemris.trisat;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * It provides read-only values of variable.
 */
public class BitVector {

	protected boolean[] bits;

	/**
	 * Constructor used for creating a vector whose values are generated randomly.
	 *
	 * @param rand         {@link Random} object
	 * @param numberOfBits size of vector.
	 */
	public BitVector(Random rand, int numberOfBits) {
		bits = new boolean[numberOfBits];
		IntStream.range(0, numberOfBits).forEach(i -> bits[i] = rand.nextBoolean());
	}

	/**
	 * Constructor used for creating a vector whose values are based on <i>bits</i>
	 * values.
	 *
	 * @param bits bits.
	 */
	public BitVector(boolean... bits) {
		this.bits = bits.clone();
	}

	/**
	 * Delegates {@link BitVector#BitVector(Random, int)}.
	 *
	 * @param n size of vector.
	 */
	public BitVector(int n) {
		this(new Random(), n);
	}

	/**
	 * Retrieves value from position <i>index</i>.
	 *
	 * @param index index
	 * @return value from position <i>index</i>.
	 * @throws IndexOutOfBoundsException if <i>index</i> is invalid.
	 */
	public boolean get(int index) {
		Objects.checkIndex(index, bits.length);
		return bits[index];
	}

	/**
	 * @return vectors size.
	 */
	public int getSize() {
		return bits.length;
	}

	/**
	 * @return bits.
	 */
	public boolean[] getBits() {
		return bits;
	}

	/**
	 * Copies current vector into an instance of {@link MutableBitVector}.
	 *
	 * @return an instance of {@link MutableBitVector}.
	 */
	public MutableBitVector copy() {
		return new MutableBitVector(bits);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (boolean variable : bits) {
			sb.append(variable ? 1 : 0);
		}
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		BitVector bitVector = (BitVector) obj;
		return Arrays.equals(bits, bitVector.bits);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(bits);
	}
}
