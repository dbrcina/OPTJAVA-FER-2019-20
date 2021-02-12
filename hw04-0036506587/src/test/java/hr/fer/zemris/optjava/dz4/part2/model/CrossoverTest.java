package hr.fer.zemris.optjava.dz4.part2.model;

import hr.fer.zemris.optjava.dz4.part2.model.unit.Box;
import hr.fer.zemris.optjava.dz4.part2.model.crossover.CrossoverOperation;
import hr.fer.zemris.optjava.dz4.part2.model.gene.Column;
import hr.fer.zemris.optjava.dz4.part2.model.item.Stick;
import hr.fer.zemris.optjava.dz4.part2.model.mutation.MutationOperation;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class CrossoverTest {

    private List<Integer> sticksLength1 = Arrays.asList(15, 1, 20, 5, 4, 14, 7, 10, 1, 3, 13
            , 1, 5, 13, 7, 2, 16, 4, 1, 5, 4, 5, 6, 1, 3, 1, 1, 1, 16, 13, 1, 1);
    private List<Integer> sticksLength2 = Arrays.asList(5, 1, 16, 15, 1, 1, 6, 5, 1, 13, 3
            , 13, 1, 1, 14, 1, 1, 10, 2, 1, 7, 4, 5, 5, 4, 4, 16, 1, 13, 20, 7, 3);

    @Test
    void testCrossover() {
        Box box1 = initialize(sticksLength1.stream()
                .mapToInt(i -> i)
                .mapToObj(Stick::new)
                .collect(Collectors.toList())
        );
        Box box2 = initialize(sticksLength2.stream()
                .mapToInt(i -> i)
                .mapToObj(Stick::new)
                .collect(Collectors.toList())
        );
        /*Box b = new Box();
        Column c = new Column(20);
        c.addStick(new Stick(7));
        c.addStick(new Stick(10));
        var a = b.removeSticks(new Stick(13), c);*/

        Box child = CrossoverOperation.crossover(new Random(), new Box[]{box1, box2});
        MutationOperation.mutate(new Random(), child, 0.3);
        System.out.println(child);
    }

    private Box initialize(List<Stick> sticks) {
        List<Stick> temp = new ArrayList<>(sticks);
        Box box = new Box();
        for (int j = 0; j < sticks.size(); j++) {
            Column column = new Column(20);
            List<Integer> sticksToRemove = new ArrayList<>();
            for (int k = 0; k < temp.size(); k++) {
                Stick stick = temp.get(k);
                if (!column.addStick(stick)) break;
                sticksToRemove.add(k);
            }
            box.addColumn(column);
            Collections.reverse(sticksToRemove);
            for (int index : sticksToRemove) {
                temp.remove(index);
            }
            if (temp.isEmpty()) break;
        }
        return box;
    }

}
