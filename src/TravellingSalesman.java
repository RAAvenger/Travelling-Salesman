import java.util.Date;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class TravellingSalesman {
    int tryTimeSec = 10;

    public static void main(String[] args) throws Exception {
        TravellingSalesman This = new TravellingSalesman();
        int[][] map = This.ReadInput();
//        This.Algorithm1(map);
        This.Algorithm2(map);
    }

    /**
     * solve travelling salesman by Hill Climbing Algorithm.
     */
    private void Algorithm1(int[][] map) {
        StepHillClimbing bestPath = null;
        long startTime = new Date().getTime();
        while (new Date().getTime() < startTime + (this.tryTimeSec * 1000)) {
            StepHillClimbing currentStep = null;
            StepHillClimbing newStep = new StepHillClimbing(this.CreatRandomStep(map), map);
            do {
                currentStep = newStep;
                newStep = currentStep.GetNextStep();
            } while (newStep != null);
            bestPath = bestPath == null || bestPath.cost > currentStep.cost ? currentStep : bestPath;
        }
        System.out.println(bestPath.Print());
    }

    /**
     * solve travelling salesman by Local Beam Search Algorithm.
     */
    private void Algorithm2(int[][] map) throws Exception {
        int beamWidth = 100;
        for (int c = 0; c < 3; c++) {
            LinkedList<LineOfLight> startingBeam = new LinkedList<LineOfLight>();
            for (int i = 0; i < beamWidth; i++) {
                startingBeam.add(new LineOfLight(map, CreatRandomStep(map)));
            }
            BeamOfLight beam = new BeamOfLight(map, startingBeam, beamWidth);
            long startTime = new Date().getTime();
            while (new Date().getTime() < startTime + (this.tryTimeSec * 1000)) {
                beam.CreatNextBeam((int) (new Date().getTime()), tryTimeSec * 1000);
            }
            System.out.println(beam.PrintBestPath());
        }
    }

    /**
     * creat a step with random order of cities.
     *
     * @param map matrix of distances.
     * @return new state wit random location;
     */
    private int[] CreatRandomStep(int[][] map) {
        int[] location = new int[map[0].length];
        for (int i = 0; i < map[0].length; i++) {
            location[i] = i;
        }
        for (int i = 0; i < map[0].length; i++) {
            int index2 = new Random(new Date().getTime()).nextInt(map[0].length);
            int temp = location[i];
            location[i] = location[index2];
            location[index2] = temp;
        }
        return location;
    }

    /**
     * read input and create matrix of city distances.
     *
     * @return matrix of distances.
     */
    public int[][] ReadInput() {
        System.out.println("Input your numbers( you can use test inputs at \"test/\" directory. ) :");
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
