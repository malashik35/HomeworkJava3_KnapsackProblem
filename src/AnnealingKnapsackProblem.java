import kotlin.Pair;

import java.util.*;

public class AnnealingKnapsackProblem {
    private int tStart;
    private int iterationNumber;
    private List<Item> items;
    private int maxWeight;
    private boolean[] currentFill;
    private boolean[] mutatedFill;
    private boolean[] bestFill;
    private Random random = new Random();

    AnnealingKnapsackProblem(List<Item> items, int maxWeight, int tStart, int iterationNumber) {
        this.tStart = tStart;
        this.iterationNumber = iterationNumber;
        this.items = items;
        this.maxWeight = maxWeight;
        this.currentFill = new boolean[items.size()];
        this.mutatedFill = new boolean[items.size()];
    }

    public Fill fillKnapsackAnnealing() {
        currentFill = initialFill();

        for (int t = 1; t <= iterationNumber; t++) {
            mutate();

            int currentCost = evaluateCost(currentFill);
            int mutatedCost = evaluateCost(mutatedFill);
            int currentWeight = evaluateWeight(currentFill);
            int mutatedWeight = evaluateWeight(mutatedFill);

            if ((mutatedCost > currentCost) && (mutatedWeight <= maxWeight)) {
                currentFill = Arrays.copyOf(mutatedFill, mutatedFill.length);
            } else {
                double probabilityToMutate = random.nextDouble();
                double dE = Math.exp((double) -(currentCost - mutatedCost) / ((double) tStart / t));
                if ((mutatedWeight <= maxWeight) && (probabilityToMutate < dE)) {
                    currentFill = Arrays.copyOf(mutatedFill, mutatedFill.length);
                }
            }

            if (currentCost > evaluateCost(bestFill))
                bestFill = Arrays.copyOf(currentFill, currentFill.length);
        }

        int cost = 0;
        Set<Item> itemsInFill = new HashSet<>();
        for (int i = 0; i < bestFill.length; i++) {
            if (bestFill[i]) {
                itemsInFill.add(items.get(i));
                cost += items.get(i).getCost();
            }
        }

        return new Fill(cost, itemsInFill);
    }


    private boolean[] initialFill() {

        SortedMap<Item, Integer> mapWithGreedyCoefficients = new TreeMap<>(new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                if (Double.compare((double) o2.getCost() / o2.getWeight(),(double) o1.getCost() / o1.getWeight()) != 0) {
                    return Double.compare((double) o2.getCost() / o2.getWeight(), (double) o1.getCost() / o1.getWeight());
                } else {
                    return 1;
                }
            }
        });

        for (int originalIndex = 0; originalIndex < items.size(); originalIndex++) {
            Item currentItem = items.get(originalIndex);
            mapWithGreedyCoefficients.put
                    (currentItem, originalIndex);
        }

        int currentWeight = 0;

        for (Map.Entry<Item, Integer> entry : mapWithGreedyCoefficients.entrySet()) {
            if (currentWeight + entry.getKey().getWeight() <= maxWeight) {
                currentFill[entry.getValue()] = true;
                currentWeight += entry.getKey().getWeight();

                try {
                    assert ((entry.getKey().getWeight() == items.get(entry.getValue()).getWeight()) && (entry.getKey().getCost() == items.get(entry.getValue()).getCost()));
                } catch (AssertionError e) {
                    System.out.println("ex");
                }

            }
        }

        bestFill = Arrays.copyOf(currentFill, currentFill.length);
        return currentFill;
    }

    private void mutate() {
        mutatedFill = new boolean[currentFill.length];

        int mutateIteration = 5;
        for (int i = 0; i < mutateIteration; i++) {

            int start = random.nextInt(items.size());
            int finish = start + random.nextInt(items.size() - start);

            System.arraycopy(currentFill, 0, mutatedFill, 0, start);

            for (int k = finish; k >= start; k--) {
                mutatedFill[k] = currentFill[finish - k];
            }

            System.arraycopy(currentFill, finish, mutatedFill, finish, currentFill.length - finish);


        }
    }

    private int evaluateWeight(boolean[] array) {
        int weight = 0;

        for (int i = 0; i < array.length; i++) {
            if (array[i]) {
                weight += items.get(i).getWeight();
            }
        }
        return weight;
    }

    private int evaluateCost(boolean[] array) {
        int cost = 0;

        for (int i = 0; i < array.length; i++) {
            if (array[i]) {
                cost += items.get(i).getCost();
            }
        }
        return cost;
    }
}
