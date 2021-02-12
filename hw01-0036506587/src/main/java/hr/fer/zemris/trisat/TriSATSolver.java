package hr.fer.zemris.trisat;

import hr.fer.zemris.trisat.algorithms.*;
import hr.fer.zemris.trisat.parser.FileParser;
import hr.fer.zemris.trisat.validation.Validator;

import java.nio.file.Path;

/**
 * Simulation class.
 */
public class TriSATSolver {

	/**
	 * Main entry of this program.
	 *
	 * @param args arguments from command line.
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			terminate("Program expects two arguments; algorithm number and .cnf file!");
		}

		int algorithm = Validator.validateNumber(args[0]);
		Path file = Validator.validateFilePath(args[1]);
		SATFormula satFormula = FileParser.parse(file);

		BitVector result = null;
		switch (algorithm) {
		case 1:
			result = Algorithm1.doAlgorithm(satFormula);
			break;
		case 2:
			result = Algorithm2.doAlgorithm(satFormula);
			break;
		case 3:
			result = Algorithm3.doAlgorithm(satFormula);
			break;
		case 4:
			result = Algorithm4.doAlgorithm(satFormula);
			break;
		case 5:
			result = Algorithm5.doAlgorithm(satFormula);
			break;
		case 6:
			result = Algorithm6.doAlgorithm(satFormula);
			break;
		default:
			terminate("Valid algorithm numbers are from [1,6]!");
		}

		if (result == null) {
			System.out.println("Svi resursi su potršeni, nema zadovoljivih rješenja.");
		} else {
			System.out.println("Jedno od zadovoljivih rješenja: " + result);
		}
	}

	/**
	 * Terminates execution of current program.
	 *
	 * @param msg message.
	 */
	public static void terminate(String msg) {
		System.out.println(msg);
		System.out.println("Terminating...");
		System.exit(-1);
	}
}
