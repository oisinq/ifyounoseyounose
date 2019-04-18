package org.ifyounoseyounose.backend;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SmellReport {
    String smellName = "";

    private HashMap<File, List<Integer>> smells = new HashMap<>(); // Contains a list of smelly lines for each class

    public void addToReport(File current, List<Integer> lines){ // Adds found lines to the report
        smells.put(current, lines);
    }

    public List<Integer> getDetectionsByFile(File file) {
        return smells.get(file);
    }

    public String toString() {
        return Arrays.asList(smells).toString();
    }

    public String getSmellName() {
        return smellName;
    }

    public void setSmellName(String name) {
        smellName = name;
    }

    public boolean isEmptyForFile(File f) {
        return !smells.containsKey(f) || smells.get(f).isEmpty();
    }
}