package hr.fer.zemris.trisat.validation;

import hr.fer.zemris.trisat.TriSATSolver;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Simple class used for validation of input arguments.
 */
public class Validator {

	private static final String RESOURCES_FOLDER = "src/main/resources";
	private static final String FILE_EXTENSION = ".cnf";

	/**
	 * Validates number.
	 *
	 * @param number number.
	 * @return parsed number or terminates program with
	 *         {@link TriSATSolver#terminate(String)} method.
	 */
	public static int validateNumber(String number) {
		int parsedNumber = 0;
		try {
			parsedNumber = Integer.parseInt(number);
		} catch (NumberFormatException ex) {
			TriSATSolver.terminate(number + " is not a valid integer!");
		}
		return parsedNumber;
	}

	/**
	 * Validates file path.
	 *
	 * @param fileName file.
	 * @return file path or terminates program with
	 *         {@link TriSATSolver#terminate(String)} method.
	 */
	public static Path validateFilePath(String fileName) {
		if (!fileName.endsWith(FILE_EXTENSION)) {
			TriSATSolver.terminate("Program expects only .cnf files!");
		}
		Path path = null;
		try {
			path = Paths.get(RESOURCES_FOLDER + "/" + fileName);
		} catch (InvalidPathException ex) {
			TriSATSolver.terminate(fileName + " cannot be resolved into path!");
		}
		return path;
	}

}
