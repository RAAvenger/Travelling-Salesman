import java.util.LinkedList;

public class StepHillClimbing implements Comparable {
    int cost;
    int[] location;
    int[][] regionMap;

    public StepHillClimbing(int[] location, int[][] regionMap) {
        this.location = location.clone();
        this.regionMap = regionMap;
        this.cost = CalculateCostOfLocation();
    }

    /**
     * calculate cost of traveling cities in order of "location".
     *
     * @return cost of traveling in order from first city of "location" to last city.
     */
    public int CalculateCostOfLocation() {
        int city1, city2, cost = 0;
        for (int i = 0; i + 1 < location.length; i++) {
            city1 = location[i];
            city2 = location[i + 1];
            cost += regionMap[city1][city2];
        }
        cost += regionMap[location[location.length - 1]][location[0]];
        return cost;
    }

    /**
     * get best neighbor as next step.
     *
     * @return next step or null if no neighbor step is better than current step.
     */
    public StepHillClimbing GetNextStep() {
        LinkedList<StepHillClimbing> neighbors = GetNeighborLocations();
        StepHillClimbing best = this;
        for (StepHillClimbing neighbor : neighbors) {
            if (best.cost > neighbor.cost) {
                best = neighbor;
            }
        }
        return best == this ? null : best;
    }

    /**
     * create next possible steps using current step by swapping two cities.
     *
     * @return all neighbors of current location.
     */
    private LinkedList<StepHillClimbing> GetNeighborLocations() {
        LinkedList<StepHillClimbing> neighbors = new LinkedList<StepHillClimbing>();
        for (int i = 0; i < location.length; i++) {
            for (int j = i + 1; i < location.length; i++) {
                int[] newNeighborLocation = SwapCities(i, j, location);
                StepHillClimbing newNeighbor = new StepHillClimbing(newNeighborLocation, regionMap);
                neighbors.add(newNeighbor);
            }
        }
        return neighbors;
    }

    /**
     * get two city indexes and swap cities in current order then return new order;
     *
     * @param city1    index of first city.
     * @param city2    index of second city.
     * @param location current order of cities.
     * @return new order of cities.
     */
    private int[] SwapCities(int city1, int city2, int[] location) {
        int[] result = location.clone();
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
        for (int i = 0; i < location.length; i++)
            result += "" + location[i] + ", ";
        result += "" + location[0] + ", ";
        return result + "\b\b.";
    }
}