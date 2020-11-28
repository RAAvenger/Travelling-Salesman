import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

public class BeamOfLight {
    int beamWidth;
    int worstCostInBeam;
    LinkedList<LineOfLight> linesOfLight;
    LinkedList<LineOfLight> randomLinesOfLight;
    int[][] regionMap;

    public BeamOfLight(int[][] regionMap, LinkedList<LineOfLight> LinesOfLight, int beamWidth) throws Exception {
        if (LinesOfLight.size() != beamWidth)
            throw new Exception("LinesOfLight's size is noe equal to beamWidth.");
        this.regionMap = regionMap;
        this.linesOfLight = LinesOfLight;
        randomLinesOfLight = new LinkedList<>();
        this.beamWidth = beamWidth;
        worstCostInBeam = GetMaxCostInBeam();
    }

    /**
     * find maximum( worst ) const in current beam.
     *
     * @return max cost of line in linesOfLight.
     */
    private int GetMaxCostInBeam() {
        int max = 0;
        for (LineOfLight line : linesOfLight) {
            if (line.cost > max)
                max = line.cost;
        }
        return max;
    }

    private int GetMinCostInBeam() {
        int max = Integer.MAX_VALUE;
        for (LineOfLight line : linesOfLight) {
            if (line.cost < max)
                max = line.cost;
        }
        return max;
    }

    public void CreatNextBeam(int randomFactor, int runTime) {
        LinkedList<LineOfLight> tempLinesOfLight = (LinkedList<LineOfLight>) linesOfLight.clone();
        tempLinesOfLight.addAll((Collection<? extends LineOfLight>) randomLinesOfLight.clone());
        randomLinesOfLight.clear();
        for (LineOfLight line : tempLinesOfLight) {
            LinkedList<LineOfLight> newLines = line.GetCloseLinesOfLight();
            for (LineOfLight newLine : newLines) {
                if (newLine.cost < worstCostInBeam)
                    AddNewLineOfLightToBeam(newLine);
                else {
                    int score = Math.round(new Random().nextInt(runTime / 2) / randomFactor);
                    if (score != 0) {
                        randomLinesOfLight.add(newLine);
                    }
                }
            }
        }
    }

    private void AddNewLineOfLightToBeam(LineOfLight newLine) {
        for (int i = 0; i < beamWidth; i++) {
            if (linesOfLight.get(i).cost == worstCostInBeam) {
                linesOfLight.remove(i);
                worstCostInBeam = GetMaxCostInBeam();
                break;
            }
        }
        linesOfLight.add(newLine);
    }

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

