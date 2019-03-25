package org.ifyounoseyounose.backend;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SmellDetectorManager {
    public List<Result> detectSmells(List<SmellDetector> smellDetectors, List<File> files) {
        List<Result> results = new ArrayList<>();

        for (File file : files) {
            for (SmellDetector smellDetector : smellDetectors) {
                // Check what type of smellDetector it is by seeing what interface it inherits
                // and call smellDetector.detectSmell() on it with the parameters corresponding to its interface
            }
        }
        return results;
    }
}