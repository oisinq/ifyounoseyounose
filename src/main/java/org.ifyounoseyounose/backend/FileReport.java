package org.ifyounoseyounose.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileReport {
    private List<Integer> detectedLines;
    private HashMap<String, List<Integer>> newThing;

    FileReport() {
        detectedLines = new ArrayList<>();
        newThing = new HashMap<>();
    }
    public void addDetections(List<Integer> newLines) {
        if (newLines != null) {
            detectedLines.addAll(newLines);
        }
    }

    public void addSmellDetections(String smellName, List<Integer> newLines) {
        newThing.put(smellName, newLines);
    }
}