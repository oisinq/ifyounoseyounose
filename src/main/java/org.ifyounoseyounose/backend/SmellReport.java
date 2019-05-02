package org.ifyounoseyounose.backend;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * SmellReport - an object that represents the smelly lines found by an individual SmellDetector across a project
 */
public class SmellReport {
    private String smellName = "";

    private HashMap<File, List<Integer>> smells = new HashMap<>(); // Contains a list of smelly lines for each class

    /**
     * Given a list of lines and the corresponding file, it adds this info to the object
     */
    public void addToReport(File current, List<Integer> lines) {
        smells.put(current, lines);
    }

    /**
     * When given a file, this returns a list of all the lines where smells are present
     */
    List<Integer> getDetectionsByFile(File file) {
        return smells.get(file);
    }

    /**
     * Returns the name of the SmellDetector associated with this object
     */
    public String getSmellName() {
        return smellName;
    }

     /**
     * Sets the smell name of the SmellDetector associated with this object
     */
    void setSmellName(String name) {
        smellName = name;
    }

    /**
     * Checks if any smell detections exist for File f for this SmellDetector
     */
    boolean isEmptyForFile(File f) {
        return !smells.containsKey(f) || smells.get(f).isEmpty();
    }

    /**
     * Adds a list of smelly lines for a specific file
     */
    public void appendToList(File current, List<Integer> alpha) {
        smells.computeIfAbsent(current, age -> new ArrayList<Integer>());

        for (Integer i : alpha) {
            smells.get(current).add(i);
        }
    }

    /**
     * Returns a string representation of the contents of the SmellReport
     */
    public String toString() {
        return getSmellName() + ": \n" + Collections.singletonList(smells).toString();
    }
}
