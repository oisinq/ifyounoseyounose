package org.ifyounoseyounose.backend;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileReport {
    private List<Integer> detectedLines;
    //Todo need a better variable name for this.
    private HashMap<String, List<Integer>> newThing;
    private File relatedFile;

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

    public void setFile(File f) {
        relatedFile = f;
    }

    public File getFile() {
        return relatedFile;
    }

    public List<Integer> getSmellDetections(String smellName) {
        return newThing.get(smellName);
    }
}