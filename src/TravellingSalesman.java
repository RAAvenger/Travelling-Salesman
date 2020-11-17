import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class TravellingSalesman {
    public static void main(String[] args) {
        TravellingSalesman This = new TravellingSalesman();
        int[][] map = This.ReadInput();
        Step bestPath = null;
        for (int i = 0; i < 10; i++) {
            Step currentStep = null;
            Step newStep = This.CreatRandomStep(map);
            do {
                currentStep = newStep;
                newStep = currentStep.GetNextStep();
            } while (newStep != null);
            bestPath = bestPath == null || bestPath.cost > currentStep.cost ? currentStep : bestPath;
        }
        System.out.println(bestPath.Print());
        return;
    }


    /**
     * creat a step with random order of cities.
     *
     * @param map matrix of distances.
     * @return new state wit random location;
     */
    private Step CreatRandomStep(int[][] map) {
        int[] location = new int[map[0].length];
        for (int i = 0; i < map[0].length; i++) {
            location[i] = i;
        }
        for (int i = 0; i < map[0].length; i++) {
            int index1 = new Random(new Date().getTime()).nextInt(map[0].length - 1);
            int index2 = new Random(new Date().getTime()).nextInt(map[0].length - 1);
            if (index1 == index2)
                index2 = new Random(new Date().getTime()).nextInt(map[0].length - 1);
            int temp = location[index1];
            location[index1] = location[index2];
            location[index2] = temp;
        }
        return new Step(location, map);
    }

    /**
     * read input and create matrix of city distances.
     *
     * @return matrix of distances.
     */
    public int[][] ReadInput() {
        Scanner scanner = new Scanner(System.in);
        int cityCount = scanner.nextInt();
        int[][] map = new int[cityCount][cityCount];
        for (int i = 0; i < cityCount; i++) {
            for (int j = 0; j < cityCount; j++) {
                int inputInt = scanner.nextInt();
                map[i][j] = inputInt == -1 ? Integer.MAX_VALUE : inputInt;
            }
        }
        return map;
    }
}
