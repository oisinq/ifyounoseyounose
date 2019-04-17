package org.ifyounoseyounose.backend;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class FinalReport {
    HashMap<File, FileReport> data = new HashMap<>();

    public void addFileReport(FileReport report) {
        data.put(report.getFile(), report);
    }

    public List<Integer> getDetectedLines(File f, String smellName) {
        FileReport report = data.get(f);
        return report.getSmellDetections(smellName);
    }

}
