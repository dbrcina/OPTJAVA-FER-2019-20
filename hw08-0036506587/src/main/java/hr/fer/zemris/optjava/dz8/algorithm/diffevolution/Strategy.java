package hr.fer.zemris.optjava.dz8.algorithm.diffevolution;

public enum Strategy {
    RAND_BIN("rand/\\d+/bin"),
    BEST_BIN("best/\\d+/bin"),
    TARGET_TO_BEST_BIN("target-to-best/\\d+/bin"),
    RAND_EITHER_OR("rand/\\d+/either-or");

    private String definition;

    Strategy(String definition) {
        this.definition = definition;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

}
