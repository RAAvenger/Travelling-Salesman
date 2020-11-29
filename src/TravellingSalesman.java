import java.util.Date;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class TravellingSalesman {
    int tryTimeSec = 10;
    private Random randomGenerator = new Random(new Date().getTime());

    public static void main(String[] args) throws Exception {
        TravellingSalesman This = new TravellingSalesman();
        int[][] map = This.ReadInput();
//        This.Algorithm1(map);
        This.Algorithm2(map);
//        This.Algorithm3(map);
    }

    /**
     * solve travelling salesman by Hill Climbing Algorithm.
     */
    private void Algorithm1(int[][] map) {
        HillClimbing_Step bestPath = null;
        long startTime = new Date().getTime();
        while (new Date().getTime() < startTime + (this.tryTimeSec * 1000)) {
            HillClimbing_Step currentStep = null;
            HillClimbing_Step newStep = new HillClimbing_Step(this.CreateRandomOrderOfCities(map), map);
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
        int beamWidth = 10;
//        for (int resets = 3; resets > 0; resets--) {
        LinkedList<LocalBeamSearch_LineOfLight> startingBeam = new LinkedList<LocalBeamSearch_LineOfLight>();
        for (int i = 0; i < beamWidth; i++) {
            startingBeam.add(new LocalBeamSearch_LineOfLight(map, CreateRandomOrderOfCities(map)));
        }
        LocalBeamSearch_BeamOfLight beam = new LocalBeamSearch_BeamOfLight(map, startingBeam, beamWidth, randomGenerator);
        long startTime = new Date().getTime();
        while (new Date().getTime() < startTime + (this.tryTimeSec * 1000)) {
            int randomFactor = (int) (((startTime + tryTimeSec * 1000) - new Date().getTime()) / (tryTimeSec));
            beam.CreateNextBeam(randomFactor / 3);
        }
        System.out.println(beam.PrintBestPath());
//        }
    }

//    /**
//     * solve travelling salesman by Genetic Algorithm.
//     */
//    private void Algorithm3(int[][] map) {
//        Genetic_Population population;
//        LinkedList<Genetic_Chromosome> startingChromosoms = new LinkedList<>();
//        for (int i = 0; i < 10; i++) {
//            startingChromosoms.add(new Genetic_Chromosome(map, CreateRandomOrderOfCities(map)));
//        }
//        population = new Genetic_Population(map, startingChromosoms, 10);
//        long startTime = new Date().getTime();
////        while (new Date().getTime() < startTime + (this.tryTimeSec * 1000)) {
//        while (true) {
//            population.CreateNextGeneration((int) (((tryTimeSec * 1000 - (new Date().getTime())) / tryTimeSec * 1000) * 1000));
//        }
////        System.out.println(bestPath.Print());
//    }

    /**
     * create a step with random order of cities.
     *
     * @param map matrix of distances.
     * @return new state wit random location;
     */
    private int[] CreateRandomOrderOfCities(int[][] map) {
        int[] location = new int[map[0].length];
        for (int i = 0; i < map[0].length; i++) {
            location[i] = i;
        }
        for (int i = 0; i < map[0].length; i++) {
            int index2 = randomGenerator.nextInt(map[0].length);
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
