package org.ifyounoseyounose.backend;

import java.util.HashMap;
import java.util.List;

public class ClassReport {

    HashMap<String , List<Integer>> smells = new HashMap<String, List<Integer>>();

    public ClassReport(String name)
    {
        String className = name;
    }
    public void addSmell(String smell, List<Integer> lines)
    {
        smells.put(smell, lines);
    }


}
