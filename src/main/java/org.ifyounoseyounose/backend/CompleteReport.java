package org.ifyounoseyounose.backend;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class CompleteReport {
    HashMap<File, FileReport> data = new HashMap<>();

    public void addFileReport(FileReport report) {
        data.put(report.getFile(), report);
    }

    public HashMap<String, List<Integer>> getDetectedLinesForSmell(File f, String smellName) {
        FileReport report = data.get(f);
        return report.getSmellDetections();
    }

    public FileReport getAllDetectedSmells(File f) {
        return data.get(f);
    }
}
