import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;

public class Genetic_Population {
    LinkedList<Genetic_Chromosome> generation;
    int[][] regionMap;
    int generationPopulation;
    private Random randomGenerator;

    public Genetic_Population(int[][] regionMap, LinkedList<Genetic_Chromosome> firstGeneration, int generationPopulation, Random randomGenerator) {
        this.regionMap = regionMap;
        generation = firstGeneration;
        this.generationPopulation = generationPopulation;
        this.randomGenerator = randomGenerator;
    }

    /**
     * create next generation of Chromosomes using current generation.
     *
     * @param mutationPossibilityFactor possibility factor of happening mutation.
     */
    public void CreateNextGeneration(int mutationPossibilityFactor) {
        LinkedList<Genetic_Chromosome> nextGeneration = new LinkedList<>();
        int[][] parents = new int[generation.size() / 2][2];
        for (int parentCounter = 0; parentCounter < generation.size() / 2; parentCounter++) {
            do {
                parents[parentCounter] = ChooseParents();
            } while (PairExists(parents, parentCounter - 1, parents[parentCounter]));
            LinkedList<Genetic_Chromosome> newChildren = CrossOver(generation.get(parents[parentCounter][0]).genes, generation.get(parents[parentCounter][1]).genes);
            for (int counter = 0; counter < newChildren.size(); counter++) {
                int score = randomGenerator.nextInt(1000);
                if (score < mutationPossibilityFactor) {
                    newChildren.get(counter).ForceMutation();
                }
            }
            nextGeneration.addAll(newChildren);
        }
        generation.addAll(nextGeneration);
        ChooseBestChromosomes();
    }

    /**
     * Return two chromosomes as parents.
     * Chromosomes with lower unfitness( higher relative fitness ) have more chance to be chosen.
     *
     * @return pair of indexes of chromosomes in current generation.
     */
    private int[] ChooseParents() {
        int relativeFitnessSum = 0, maxUnfitness = 0;
        for (Genetic_Chromosome chromosome : generation) {
            if (maxUnfitness < chromosome.unfitness)
                maxUnfitness = chromosome.unfitness;
        }
        for (Genetic_Chromosome chromosome : generation) {
            chromosome.relativeFitness = maxUnfitness + 10 - chromosome.unfitness;
            relativeFitnessSum += chromosome.relativeFitness;
        }
        int[] parents = new int[2];
        for (int i = 0, score = randomGenerator.nextInt(relativeFitnessSum), floorRelativeFitnessOfCurrentChromosome = 0; i < generation.size(); i++) {
            if (score >= floorRelativeFitnessOfCurrentChromosome && score < floorRelativeFitnessOfCurrentChromosome + generation.get(i).relativeFitness) {
                parents[0] = i;
                break;
            } else
                floorRelativeFitnessOfCurrentChromosome += generation.get(i).relativeFitness;
        }
        do {
            for (int i = 0, score = randomGenerator.nextInt(relativeFitnessSum), floorRelativeFitnessOfCurrentChromosome = 0; i < generation.size(); i++) {
                if (score >= floorRelativeFitnessOfCurrentChromosome && score < floorRelativeFitnessOfCurrentChromosome + generation.get(i).relativeFitness) {
                    parents[1] = i;
                    break;
                } else
                    floorRelativeFitnessOfCurrentChromosome += generation.get(i).relativeFitness;
            }
        } while (parents[0] == parents[1]);
        return parents;
    }

    /**
     * choose best chromosomes of current generation and next generation and replace current generation with best chromosomes.
     */
    private void ChooseBestChromosomes() {
        PriorityQueue<Genetic_Chromosome> priorityQueue = new PriorityQueue<Genetic_Chromosome>(generation);
        LinkedList<Genetic_Chromosome> bestChromosomes = new LinkedList<>();
        while (bestChromosomes.size() < generationPopulation && !priorityQueue.isEmpty()) {
            Genetic_Chromosome chromosome = priorityQueue.poll();
            if (!bestChromosomes.contains(chromosome))
                bestChromosomes.add(chromosome);
        }
        generation = bestChromosomes;
    }

    /**
     * search given pair list for given pair.
     *
     * @param pairsList  array of integer pair.
     * @param listLength length of array.
     * @param pair       pair of integers.
     * @return "true" if pair exists in list otherwise false.
     */
    private boolean PairExists(int[][] pairsList, int listLength, int[] pair) {
        for (int i = 0; i <= listLength; i++) {
            if ((pairsList[i][0] == pair[0] && pairsList[i][1] == pair[1]) || (pairsList[i][0] == pair[1] && pairsList[i][1] == pair[0])) {
                return true;
            }
        }
        return false;
    }

    /**
     * get two chromosomes and create new chromosomes from them.
     *
     * @param firstChromosome  index of first chromosome.
     * @param secondChromosome index of second chromosome.
     * @return linked list of new chromosomes.
     */
    private LinkedList<Genetic_Chromosome> CrossOver(int[] firstChromosome, int[] secondChromosome) {
        LinkedList<Genetic_Chromosome> newChromosomes = new LinkedList<>();
        LinkedList<int[][]> swappableParts = FindSwappableParts(firstChromosome, secondChromosome, 2);
        for (int[][] part : swappableParts) {
            /** create two new chromosomes by swapping part[0]( indexes of part[0][0] to part[0][1] ) from first chromosome
             * with part[1]( indexes of part[1][0] to part[1][1] ) of second chromosome and vise versa.*/
            int[] NewChromosomeGenes = firstChromosome.clone();
            for (int firstIndex = part[0][0], secondIndex = part[1][0]; firstIndex < part[0][1]; firstIndex++, secondIndex++) {
                NewChromosomeGenes[firstIndex] = secondChromosome[secondIndex];
            }
            Genetic_Chromosome newChromosome = new Genetic_Chromosome(regionMap, NewChromosomeGenes, randomGenerator);
            if (!newChromosomes.contains(newChromosome))
                newChromosomes.add(newChromosome);
            NewChromosomeGenes = secondChromosome.clone();
            for (int firstIndex = part[0][0], secondIndex = part[1][0]; firstIndex < part[0][1]; firstIndex++, secondIndex++) {
                NewChromosomeGenes[secondIndex] = firstChromosome[firstIndex];
            }
            newChromosome = new Genetic_Chromosome(regionMap, NewChromosomeGenes, randomGenerator);
            if (!newChromosomes.contains(newChromosome))
                newChromosomes.add(newChromosome);
        }
        return newChromosomes;
    }

    /**
     * search in two chromosomes for pair of parts that first part from first chromosome can be swapped with second part from second chromosome.
     *
     * @param firstChromosome  genes from first chromosome.
     * @param secondChromosome genes from second chromosome.
     * @param maxPartsCount    maximum number of parts to return.
     * @return linked list of swappable pair of parts( ex: int{{{1,5},{6,10}},...} means chromosome1[1:5] can be swapped with chromosome2[6:10].
     */
    private LinkedList<int[][]> FindSwappableParts(int[] firstChromosome, int[] secondChromosome, int maxPartsCount) {
        boolean[][] map = CreateSimilarityMapOfArrays(firstChromosome, secondChromosome);
        LinkedList<int[][]> candidatePartsList = new LinkedList<>();
        for (
                int basePartSize = (map.length + 1) / 2, i = 0, partSize = basePartSize + i;
                partSize > 0 && partSize < map.length;
                i = i < 0 ? i * -1 : (i + 1) * -1, partSize = basePartSize + i
        ) {
            for (int y = 0; y + partSize < map.length; y++) {
                for (int x = 0; x + partSize < map.length; x++) {
                    if (IsSwappable(map, partSize, x, y))
                        candidatePartsList.add(new int[][]{{x, x + partSize}, {y, y + partSize}});
                    if (candidatePartsList.size() == maxPartsCount + 10) {
                        LinkedList<Integer> chosenIndexes = new LinkedList<Integer>();
                        while (chosenIndexes.size() < maxPartsCount) {
                            int randomIndex = randomGenerator.nextInt(candidatePartsList.size());
                            if (!chosenIndexes.contains(randomIndex))
                                chosenIndexes.add(randomIndex);
                        }
                        LinkedList<int[][]> chosenPartsList = new LinkedList<int[][]>();
                        for (int index : chosenIndexes) {
                            chosenPartsList.add(candidatePartsList.get(index));
                        }
                        return chosenPartsList;
                    }
                }
            }

        }
        return candidatePartsList;
    }

    /**
     * check a part of map from xStart and yStart to xStart+partSize and yStart+partSize
     * and if there was partSize number of "true" values this part is swappable.
     *
     * @param map      matrix of booleans that represent equality or not equality of values of two arrays in each index.
     * @param partSize size of the part( matrix ) that will be checked.
     * @param xStart   start index of first array.
     * @param yStart   start index of second array.
     * @return "true" if two parts are swappable, otherwise "false".
     */
    private boolean IsSwappable(boolean[][] map, int partSize, int xStart, int yStart) {
        int temp = 0;
        for (int y = yStart; y < yStart + partSize; y++) {
            for (int x = xStart; x < xStart + partSize; x++) {
                if (map[y][x])
                    temp++;
            }
        }
        return temp == partSize ? true : false;
    }

    /**
     * create a map to find swappable area,
     * in this map if value of firstArray[i] is equal to value of SecondArray[j] then map[i][j] is "true" otherwise it's "false".
     *
     * @param firstArray  array of integers that have no two similar values and values are in range of [0 : arrayLength-1].
     * @param secondArray array of integers that have no two similar values and values are in range of [0 : arrayLength-1].
     * @return matrix of booleans that represent equality or not equality of values of two arrays in each index.
     */
    private boolean[][] CreateSimilarityMapOfArrays(int[] firstArray, int[] secondArray) {
        int[] indexOfFirstArray = new int[firstArray.length];
        for (int i = 0; i < firstArray.length; i++)
            indexOfFirstArray[firstArray[i]] = i;
        boolean[][] map = new boolean[firstArray.length][firstArray.length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = j == indexOfFirstArray[secondArray[i]];
            }
        }
        return map;
    }

    /**
     * find best path( path with minimum cost ) then return its cost and path as a string.
     */
    public String PrintBestPath() {
        PriorityQueue<Genetic_Chromosome> priorityQueue = new PriorityQueue<>(generation);
        return priorityQueue.poll().Print();
    }
}
