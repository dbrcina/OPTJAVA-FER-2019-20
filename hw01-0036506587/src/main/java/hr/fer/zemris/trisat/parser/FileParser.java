package hr.fer.zemris.trisat.parser;

import hr.fer.zemris.trisat.Clause;
import hr.fer.zemris.trisat.SATFormula;
import hr.fer.zemris.trisat.TriSATSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Simple parser used for parsing <i>.cnf</i> file.
 */
public class FileParser {

	private static final String COMMENT = "c";
	private static final String VARIABLE_TOKEN = "p";
	@SuppressWarnings("unused")
	private static final String CLAUSE_TOKEN = "cnf";
	private static final String CLAUSE_TERMINATE = "0";
	private static final String DEFINITION_TERMINATE = "%";
	private static final Pattern COMPILE = Pattern.compile("\\s+");

	/**
	 * Parses provided <i>file</i> and generates sat formula if possible.
	 *
	 * @param file file.
	 * @return sat formula.
	 */
	public static SATFormula parse(Path file) {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(file);
		} catch (IOException e) {
			TriSATSolver.terminate("Error occurred while reading from a file " + file.getFileName());
		}
		return generateSATFormula(lines);
	}

	private static SATFormula generateSATFormula(Iterable<String> lines) {
		FileModel model = parseLines(lines);
		return new SATFormula(model.numberOfVariables, model.clauses);
	}

	private static FileModel parseLines(Iterable<String> lines) {
		int numberOfVariables = 0;
		List<Clause> clauses = new ArrayList<>();
		for (String line : lines) {
			String trimmedLine = line.trim();

			if (trimmedLine.startsWith(DEFINITION_TERMINATE))
				break;
			if (trimmedLine.startsWith(COMMENT))
				continue;

			// we expect definitions now...
			String[] parts = COMPILE.split(trimmedLine);

			if (trimmedLine.startsWith(VARIABLE_TOKEN)) {
				numberOfVariables = Integer.parseInt(parts[2]);
				continue;
			}

			if (!trimmedLine.endsWith(CLAUSE_TERMINATE)) {
				TriSATSolver.terminate("Clause needs to end with '0'!");
			}

			// map 3 variable values into array of indexes for a clause
			// ignore last element (0)
			int[] indexes = Arrays.stream(Arrays.copyOfRange(parts, 0, parts.length - 1)).mapToInt(Integer::parseInt)
					.toArray();
			clauses.add(new Clause(indexes));
		}

		return new FileModel(numberOfVariables, clauses.toArray(new Clause[clauses.size()]));
	}

	private static final class FileModel {
		private int numberOfVariables;
		private Clause[] clauses;

		private FileModel(int numberOfVariables, Clause[] clauses) {
			this.numberOfVariables = numberOfVariables;
			this.clauses = clauses;
		}
	}
}
