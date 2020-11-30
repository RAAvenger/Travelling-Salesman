import java.util.Date;
import java.util.Random;

public class Genetic_Chromosome implements Comparable {
    int unfitness;
    int relativeFitness;
    int[] genes;
    int[][] regionMap;
    private Random randomGenerator = new Random(new Date().getTime());

    public Genetic_Chromosome(int[][] map, int[] genes) {
        this.genes = genes;
        regionMap = map;
        unfitness = CalculateUnfitness();
    }

    /**
     * calculate cost of travelling from first city to last city and going back to first city.
     *
     * @return cost of travelling.
     */
    public int CalculateUnfitness() {
        int city1, city2, cost = 0;
        for (int i = 0; i + 1 < genes.length; i++) {
            city1 = genes[i];
            city2 = genes[i + 1];
            cost += regionMap[city1][city2];
        }
        cost += regionMap[genes[genes.length - 1]][genes[0]];
        return cost;
    }

    /**
     * force mutation by swapping two genes.
     */
    public void ForceMutation() {
        int firstGeneIndex = randomGenerator.nextInt(genes.length);
        int secondGeneIndex;
        do {
            secondGeneIndex = randomGenerator.nextInt(genes.length);
        } while (firstGeneIndex == secondGeneIndex);
        this.genes = SwapCities(firstGeneIndex, secondGeneIndex, genes);
    }

    /**
     * get two city indexes and swap cities in current order then return new order;
     *
     * @param city1       index of first city.
     * @param city2       index of second city.
     * @param lineOfLight current order of cities.
     * @return new order of cities.
     */
    private int[] SwapCities(int city1, int city2, int[] lineOfLight) {
        int[] result = lineOfLight.clone();
        int temp = result[city1];
        result[city1] = result[city2];
        result[city2] = temp;
        return result;
    }

    /**
     * create a string to show step cost and path.
     *
     * @return string.
     */
    public String Print() {
        String result = "Cost: " + this.unfitness + "\nPath: ";
        for (int i = 0; i < genes.length; i++)
            result += "" + genes[i] + ", ";
        result += "" + genes[0] + ", ";
        return result + "\b\b.";
    }

    @Override
    public int compareTo(Object o) {
        Genetic_Chromosome object = (Genetic_Chromosome) o;
        return unfitness - object.unfitness;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        Genetic_Chromosome c = (Genetic_Chromosome) obj;
        for (int i = 0; i < genes.length; i++) {
            if (c.genes[i] != genes[i])
                return false;
        }
        return true;

    }
}
