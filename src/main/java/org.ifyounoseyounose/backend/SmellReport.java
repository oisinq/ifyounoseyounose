package org.ifyounoseyounose.backend;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class SmellReport {

    private HashMap<File, List<Integer>> smells = new HashMap<>();// Contains a list of smelly lines for each class

    public void addToReport(File current, List<Integer> lines){//Adds found lines to the report
        smells.put(current, lines);
    }
}