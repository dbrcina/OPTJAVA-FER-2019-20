package hr.fer.zemris.optjava.dz5.part2.model;

import java.util.HashSet;
import java.util.Set;

public class Pool {

    private Set<Unit> units = new HashSet<>();

    public Set<Unit> getUnits() {
        return units;
    }

    public boolean addUnit(Unit unit) {
        return units.add(unit);
    }

    public int size() {
        return units.size();
    }

}
