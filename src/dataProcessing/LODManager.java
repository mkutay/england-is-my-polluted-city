package dataProcessing;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates and manages multiple LODs, and holds the original data
 * Identifies when to switch to larger/smaller LODs
 */
public class LODManager {
    private final List<LODData> LODDataList;
    private final static int MAX_VISIBLE_DATAPOINTS = 1200; //If visible data points exceeds this number, attempt to use smaller LOD

    public LODManager(DataSet dataSet, int numLODs) {
        System.out.println("Creating " + numLODs + " LODs");
        LODDataList = new ArrayList<>(numLODs);
        for (int i = 0; i < numLODs; i++) {
            LODData LOD = new LODData(i+1, dataSet);
            LODDataList.add(LOD);
        }
        System.out.println("Finished generating LODs");
    }

    /**
     * Returns the suitable LOD based on current zoom level
     * Estimates the number of visible data points and chooses the LOD that has an acceptable amount of visible points
     * @param currentPixelScale the current distance in meters 1 pixel corresponds to in the current zoom level
     * @param mapWidth the width of the map UI element in pixels
     * @param mapHeight the height of the map UI element in pixels
     * @return the LOD index of the LOD for the current zoom level
     */
    public int getLODIndex(double currentPixelScale, double mapWidth, double mapHeight) {
        double physicalMapArea = mapWidth * mapHeight / currentPixelScale / currentPixelScale;
        double idealGridLengthKM = Math.sqrt(physicalMapArea/(MAX_VISIBLE_DATAPOINTS))/1000;
        return Math.min((int) idealGridLengthKM , LODDataList.size()-1);
    }

    /**
     * Returns the LOD stored at the given index
     * @param LODIndex the index of the desired LOD (use LODManager.getLODIndex())
     */
    public LODData getLODData(int LODIndex) {
        return LODDataList.get(LODIndex);
    }
}
