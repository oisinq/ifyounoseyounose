package org.ifyounoseyounose.backend;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class CompleteReport {
    private HashMap<File, FileReport> data = new HashMap<>();

    public void addFileReport(FileReport report) {
       // System.out.println("Adding this: " + report.toString() + "\n cool? lol");
        data.put(report.getFile(), report);
    }

    public HashMap<String, List<Integer>> getDetectedLinesForSmell(File f, String smellName) {
        FileReport report = data.get(f);
        return report.getSmellDetections();
    }

    public FileReport getAllDetectedSmells(File f) {
        System.out.println(f.toString() + ", ye");
        return data.get(f);
    }
}
