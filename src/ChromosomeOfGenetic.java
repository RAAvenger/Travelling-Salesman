public class ChromosomeOfGenetic implements Comparable {
    int fitness;
    int[] genes;
    int[][] regionMap;

    public ChromosomeOfGenetic(int[][] map, int[] genes) {
        this.genes = genes;
        regionMap = map;
        fitness = CaculateFitness();
    }

    /**
     * calculate cost of travelling from first city to last city and going back to first city.
     *
     * @return cost of travelling.
     */
    public int CaculateFitness() {
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

    @Override
    public int compareTo(Object o) {
        ChromosomeOfGenetic object = (ChromosomeOfGenetic) o;
        return fitness - object.fitness;
    }

    /**
     * creat a string to show step cost and path.
     *
     * @return string.
     */
    public String Print() {
        String result = "Cost: " + this.fitness + "\nPath: ";
        for (int i = 0; i < genes.length; i++)
            result += "" + genes[i] + ", ";
        result += "" + genes[0] + ", ";
        return result + "\b\b.";
    }
}
