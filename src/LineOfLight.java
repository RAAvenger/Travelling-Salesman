import java.util.LinkedList;

public class LineOfLight implements Comparable {
    int cost;
    int[] lineOfLight;
    int[][] regionMap;

    public LineOfLight(int[][] regionMap, int[] lineOfLight) {
        this.regionMap = regionMap;
        this.lineOfLight = lineOfLight;
        cost = CalculateCost();
    }

    /**
     * calculate cost of travelling from first city to last city and going back to first city.
     *
     * @return cost of travelling.
     */
    public int CalculateCost() {
        int city1, city2, cost = 0;
        for (int i = 0; i + 1 < lineOfLight.length; i++) {
            city1 = lineOfLight[i];
            city2 = lineOfLight[i + 1];
            cost += regionMap[city1][city2];
        }
        cost += regionMap[lineOfLight[lineOfLight.length - 1]][lineOfLight[0]];
        return cost;
    }

    /**
     * creat all lines of light close to this one.
     *
     * @return linkedList of lineOfLight.
     */
    public LinkedList<LineOfLight> GetCloseLinesOfLight() {
        LinkedList<LineOfLight> linesOfLight = new LinkedList<>();
        for (int i = 0; i < lineOfLight.length; i++) {
            for (int j = i + 1; i < lineOfLight.length; i++) {
                int[] newNeighborLocation = SwapCities(i, j, lineOfLight);
                linesOfLight.add(new LineOfLight(regionMap, newNeighborLocation));
            }
        }
        return linesOfLight;
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
        StepHillClimbing object = (StepHillClimbing) o;
        return cost - object.cost;
    }

    /**
     * creat a string to show step cost and path.
     *
     * @return string.
     */
    public String Print() {
        String result = "Cost: " + this.cost + "\nPath: ";
        for (int i = 0; i < lineOfLight.length; i++)
            result += "" + lineOfLight[i] + ", ";
        result += "" + lineOfLight[0] + ", ";
        return result + "\b\b.";
    }
}
