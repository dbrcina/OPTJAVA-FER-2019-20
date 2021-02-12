package hr.fer.zemris.optjava.dz3;

public class Util {

    public static String stringFromBooleanArray(boolean[] bits) {
        StringBuilder sb = new StringBuilder();
        for (boolean bit : bits) {
            sb.append(bit ? "1" : "0");
        }
        return sb.toString();
    }
}
