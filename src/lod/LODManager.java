package lod;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import dataProcessing.DataSet;

/**
 * Creates and manages multiple LODs, and holds the original data.
 * Identifies when to switch to larger/smaller LODs.
 * 
 * @author Anas Ahmed
 * @version 1.0
 */
public class LODManager {
    private final List<LODData> LODDataList;
    private final static int MAX_VISIBLE_DATAPOINTS = 12_000; // If visible data points exceeds this number, attempt to use smaller LOD.

    public LODManager(DataSet dataSet, int numLODs) {
        System.out.println("Creating " + numLODs + " LODs");
        LODDataList = new ArrayList<>(numLODs);

        // Create LODs asynchronously.
        List<CompletableFuture<LODData>> futures = new ArrayList<>();
        for (int i = 0; i < numLODs; i++) {
            final int finalIndex = i;
            CompletableFuture<LODData> future = CompletableFuture.supplyAsync(() -> {
                System.out.println("Creating LOD " + (finalIndex + 1) + "...");
                return new LODData(finalIndex + 1, dataSet);
            });
            futures.add(future);
        }
        
        // Wait for all LODs to complete and collect them.
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        for (CompletableFuture<LODData> future : futures) {
            LODDataList.add(future.join());
        }
        
        System.out.println("Finished generating LODs");
    }

    /**
     * Returns the suitable LOD based on current zoom level.
     * Estimates the number of visible data points and chooses the LOD that has an acceptable amount of visible points.
     * @param currentPixelScale the current distance in meters 1 pixel corresponds to in the current zoom level.
     * @param mapWidth The width of the map UI element in pixels.
     * @param mapHeight The height of the map UI element in pixels.
     * @return The LOD index of the LOD for the current zoom level.
     */
    public int getLODIndex(double currentPixelScale, double mapWidth, double mapHeight) {
        double physicalMapArea = (mapWidth * mapHeight) / (currentPixelScale * currentPixelScale);
        double idealGridLengthKM = Math.sqrt(physicalMapArea / (MAX_VISIBLE_DATAPOINTS)) / 1000;
        return Math.min((int) idealGridLengthKM, LODDataList.size() - 1);
    }

    /**
     * Returns the LOD stored at the given index.
     * @param LODIndex The index of the desired LOD (use LODManager.getLODIndex()).
     */
    public LODData getLODData(int LODIndex) {
        return LODDataList.get(LODIndex);
    }
}
