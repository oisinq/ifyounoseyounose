package org.ifyounoseyounose.backend;

import java.io.File;
import java.util.HashMap;

public class CompleteReport {
    private HashMap<File, FileReport> data = new HashMap<>();

    void addFileReport(FileReport report) {
       // System.out.println("Adding this: " + report.toString() + "\n cool? lol");
        data.put(report.getFile(), report);
    }

    public FileReport getAllDetectedSmells(File f) {
        System.out.println(f.toString() + ", ye");
        return data.get(f);
    }
}
