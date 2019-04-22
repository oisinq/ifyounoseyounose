package org.ifyounoseyounose.backend;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SmellReport {
    private String smellName = "";

    private HashMap<File, List<Integer>> smells = new HashMap<>(); // Contains a list of smelly lines for each class

    public void addToReport(File current, List<Integer> lines){ // Adds found lines to the report
        smells.put(current, lines);
    }

    List<Integer> getDetectionsByFile(File file) {
        return smells.get(file);
    }

    public String toString() {
        return getSmellName() + ": \n" + Collections.singletonList(smells).toString();
    }

    public String getSmellName() {
        return smellName;
    }

    void setSmellName(String name) {
        smellName = name;
    }

    boolean isEmptyForFile(File f) {
        return !smells.containsKey(f) || smells.get(f).isEmpty();
    }
}