package org.ifyounoseyounose.backend;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class FileReport {
    //Todo need a better variable name for this.
    private HashMap<String, List<Integer>> detections = new HashMap<>();
    private File relatedFile;

    void addSmellDetections(String smellName, List<Integer> newLines) {
        detections.put(smellName, newLines);
    }

    public void setFile(File f) {
        relatedFile = f;
    }

    public File getFile() {
        return relatedFile;
    }

    public HashMap<String, List<Integer>> getSmellDetections() {
        return detections;
    }

    public String toString() {
        StringBuilder output = new StringBuilder("File: " + getFile().toString() + "\n" + "Smells: \n");
        for (String s : detections.keySet()) {
            output.append(s).append("\n").append(detections.get(s).toString()).append("\n");
        }
        return output.toString();
    }
}