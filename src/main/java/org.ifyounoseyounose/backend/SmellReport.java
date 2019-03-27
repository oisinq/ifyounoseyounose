package org.ifyounoseyounose.backend;

import java.util.HashMap;
import java.util.List;

public class SmellReport {
    HashMap<Class , List<Integer>> smells = new HashMap<Class, List<Integer>>();// Contains a list of smelly lines for each class

    public void addToReport(Class current, List<Integer> lines){//Adds found lines to the report
        smells.put(current, lines);
    }

}
