package org.ifyounoseyounose.backend;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileReport {
    //Todo need a better variable name for this.
    private HashMap<String, List<Integer>> detections = new HashMap<>();
    private File relatedFile;

    public void addSmellDetections(String smellName, List<Integer> newLines) {
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

    public List<Integer> getSmellDetections(String smellName) {
        return detections.get(smellName);
    }

    public String toString() {
        String output = "File: " + getFile().toString() + "\n";
        output += "Smells: \n";
        for (String s : detections.keySet()) {
            output += s + "\n" + detections.get(s).toString() + "\n";
        }
        return output;
    }
}