import java.util.LinkedList;
import java.util.Random;

public class LocalBeamSearch_BeamOfLight {
    int beamWidth;
    int worstCostInBeam;
    LinkedList<LocalBeamSearch_LineOfLight> linesOfLight;
    LinkedList<LocalBeamSearch_LineOfLight> randomLinesOfLight;
    int[][] regionMap;
    private Random randomGenerator;

    public LocalBeamSearch_BeamOfLight(int[][] regionMap, LinkedList<LocalBeamSearch_LineOfLight> LinesOfLight, int beamWidth, Random randomGenerator) throws Exception {
        if (LinesOfLight.size() != beamWidth)
            throw new Exception("LinesOfLight's size is noe equal to beamWidth.");
        this.regionMap = regionMap;
        this.linesOfLight = LinesOfLight;
        randomLinesOfLight = new LinkedList<>();
        this.beamWidth = beamWidth;
        worstCostInBeam = GetMaxCostInBeam();
        this.randomGenerator = randomGenerator;
    }

    /**
     * find maximum( worst ) const in current beam.
     *
     * @return max cost of line in linesOfLight.
     */
    private int GetMaxCostInBeam() {
        int max = 0;
        for (LocalBeamSearch_LineOfLight line : linesOfLight) {
            if (line.cost > max)
                max = line.cost;
        }
        return max;
    }

    /**
     * creating next beam of light using current beam and random lines.
     *
     * @param randomFactor possibility of choosing a random line and adding it to the randomLinesOfLight list.
     */
    public void CreateNextBeam(int randomFactor) {
        LinkedList<LocalBeamSearch_LineOfLight> tempLinesOfLight = (LinkedList<LocalBeamSearch_LineOfLight>) linesOfLight.clone();
        tempLinesOfLight.addAll((LinkedList<LocalBeamSearch_LineOfLight>) randomLinesOfLight.clone());
        randomLinesOfLight.clear();
        for (LocalBeamSearch_LineOfLight line : tempLinesOfLight) {
            LinkedList<LocalBeamSearch_LineOfLight> newLines = line.GetCloseLinesOfLight();
            for (LocalBeamSearch_LineOfLight newLine : newLines) {
                if (newLine.cost < worstCostInBeam)
                    AddNewLineOfLightToBeam(newLine);
                else {
                    int score = randomGenerator.nextInt(1000);
                    if (score < randomFactor && randomLinesOfLight.size() < beamWidth * 10) {
                        randomLinesOfLight.add(newLine);
                    }
                }
            }
        }
    }

    /**
     * get new line and add it to beam then remove one of lines with max cost.
     *
     * @param newLine new line of light with better cost than max beam cost.
     */
    private void AddNewLineOfLightToBeam(LocalBeamSearch_LineOfLight newLine) {
        for (int i = 0; i < beamWidth; i++) {
            if (linesOfLight.get(i).cost == worstCostInBeam) {
                linesOfLight.remove(i);
                worstCostInBeam = GetMaxCostInBeam();
                break;
            }
        }
        linesOfLight.add(newLine);
    }

    /**
     * search beam and random lines for the best path( path with minimum cost ) then return its cost and path as a string.
     */
    public String PrintBestPath() {
        int index = 0, rIndex = -1;
        int bestCost = linesOfLight.getFirst().cost;
        for (int i = 1; i < beamWidth; i++) {
            if (linesOfLight.get(i).cost < bestCost) {
                bestCost = linesOfLight.get(i).cost;
                index = i;
            }
        }
        for (int i = 0; i < randomLinesOfLight.size(); i++) {
            if (randomLinesOfLight.get(i).cost < bestCost) {
                bestCost = linesOfLight.get(i).cost;
                rIndex = i;
            }
        }
        return rIndex != -1 ? randomLinesOfLight.get(rIndex).Print() : linesOfLight.get(index).Print();
    }
}

