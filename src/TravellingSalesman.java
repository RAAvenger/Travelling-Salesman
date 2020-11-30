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
        LinkedList<LocalBeamSearch_LineOfLight> prevAnswers = new LinkedList<>();
        for (int resets = 5; resets > 0; resets--) {
            LinkedList<LocalBeamSearch_LineOfLight> startingBeam = new LinkedList<>(prevAnswers);
            /*//cheating part
            startingBeam.add(new LocalBeamSearch_LineOfLight(map, new int[]{
                    89, 87, 43, 11, 93, 79, 5, 47, 53, 7
                    , 45, 80, 28, 69, 66, 40, 71, 49, 46
                    , 64, 81, 60, 9, 88, 77, 26, 86, 92
                    , 18, 16, 0, 36, 98, 13, 50, 70, 38
                    , 25, 21, 29, 12, 59, 96, 61, 51, 85
                    , 52, 57, 14, 83, 1, 31, 73, 33, 27
                    , 58, 97, 91, 24, 19, 65, 10, 72, 23
                    , 48, 62, 55, 76, 42, 68, 75, 39, 90
                    , 30, 84, 67, 82, 6, 37, 35, 3, 20
                    , 56, 41, 78, 8, 74, 63, 2, 17, 99
                    , 4, 44, 95, 32, 22, 54, 94, 34, 15
            }));*/
            while (startingBeam.size() < beamWidth) {
                startingBeam.add(new LocalBeamSearch_LineOfLight(map, CreateRandomOrderOfCities(map)));
            }
            LocalBeamSearch_BeamOfLight beam = new LocalBeamSearch_BeamOfLight(map, startingBeam, beamWidth, randomGenerator);
            long startTime = new Date().getTime();
            int bestCost = beam.GetMinCostInBeam();
            int currentBeamSize = beamWidth;
            while (new Date().getTime() < startTime + (this.tryTimeSec * 1000)) {
                int randomFactor = (int) (((startTime + tryTimeSec * 1000) - new Date().getTime()) / (tryTimeSec));
                beam.CreateNextBeam(randomFactor / 2, currentBeamSize);
                /** when we cant find a better path we extend our searching area. */
                int beamBestCost = beam.GetMinCostInBeam();
                if (bestCost > beamBestCost) {
                    bestCost = beamBestCost;
                    currentBeamSize = beamWidth;
                } else {
                    currentBeamSize += 20;
                }
                System.out.println(beam.PrintBestPath());
            }
            System.out.println("__________________________________________________________________________________________");
            LocalBeamSearch_LineOfLight newBestPath = new LocalBeamSearch_LineOfLight(map, beam.GetBestPath());
            if (!prevAnswers.contains(newBestPath))
                prevAnswers.add(newBestPath);
        }
    }

    /**
     * solve travelling salesman by Genetic Algorithm.
     */
    private void Algorithm3(int[][] map) {
        Genetic_Population population;
        int generationPopulation = 10;
        LinkedList<Genetic_Chromosome> startingChromosoms = new LinkedList<>();
        while (startingChromosoms.size() < generationPopulation) {
            Genetic_Chromosome newChromosome = new Genetic_Chromosome(map, CreateRandomOrderOfCities(map), randomGenerator);
            if (!startingChromosoms.contains(newChromosome))
                startingChromosoms.add(newChromosome);
        }
        population = new Genetic_Population(map, startingChromosoms, generationPopulation, randomGenerator);
        long startTime = new Date().getTime();
//        while (new Date().getTime() < startTime + (this.tryTimeSec * 1000)) {
        while (true) {
            int randomFactor = (int) (((startTime + tryTimeSec * 1000) - new Date().getTime()) / (tryTimeSec));
            population.CreateNextGeneration(randomFactor);
            System.out.println(population.PrintBestPath());
        }
    }

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
