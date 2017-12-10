import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AnnealingKnapsackProblemTest {

    @Test
    void fillRandomKnapsackAnnealing() {
        Random random = new Random();
        int maxWeight = 1000;
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            items.add(new Item(1 + random.nextInt(100), 1 + random.nextInt(100)));
        }

        int weight = 0;

        Fill fill = new AnnealingKnapsackProblem(items, maxWeight, 200, 10000).fillKnapsackAnnealing();
        System.out.println("Имитация отжига: " + fill.getCost());
        for (Item item : fill.getItems())
            weight += item.getWeight();
        System.out.println("Вес " + weight);

        weight = 0;
        Fill greedyFill = FillKt.fillKnapsackGreedy(maxWeight, items);
        System.out.println("Жадный: " + greedyFill.getCost());
        for (Item item : greedyFill.getItems())
            weight += item.getWeight();
        System.out.println("Вес " + weight);

        weight = 0;
        Fill dynamicFill = FillKt.fillKnapsackDynamic(maxWeight, items, new HashMap<>());
        System.out.println("Динамический: " + dynamicFill.getCost());
        for (Item item : dynamicFill.getItems())
            weight += item.getWeight();
        System.out.println("Вес " + weight);
    }
}