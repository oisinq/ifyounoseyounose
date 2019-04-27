package org.ifyounoseyounose.backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

public class CompleteReport {
    private HashMap<File, FileReport> data = new HashMap<>();

    void addFileReport(FileReport report) {
        data.put(report.getFile(), report);
    }

    public FileReport getAllDetectedSmells(File f) {
        return data.get(f);
    }

    public Set<String> getPresentSmells() {
        Set<String> presentSmells = new HashSet<>();
        for (FileReport fileReport : data.values()) {
            presentSmells.addAll(fileReport.getPresentSmells());
        }
        return presentSmells;
    }

    public Set<File> getCleanFiles() {
        Set<File> cleanFiles = new HashSet<>();
        for (File file : data.keySet()) {
            if (data.get(file).isEmpty()) {
                cleanFiles.add(file);
            }
        }
        return cleanFiles;
    }

    public int getNumberOfSmellyLines() {
        int totalSmellyLines = 0;
        for (FileReport fileReport : data.values()) {
            totalSmellyLines += fileReport.getSmellyLinesCount();
        }
        return totalSmellyLines;
    }

    public double getPercentageOfSmellLines() {
        int totalNumberOfLines = 0;
        for (File file : data.keySet()) {
            try {
                totalNumberOfLines += Files.lines(file.toPath()).count();
            } catch (IOException e) {
                System.err.println("Cannot open file " + file.toPath().toString());
            }
        }
        return 100.0*getNumberOfSmellyLines()/totalNumberOfLines;
    }

    public HashMap<String, Integer> getNumberOfDetections() {
        HashMap<String, Integer> smellCounter = new HashMap<>();

        for (FileReport r : data.values()) {
            for (String smell : r.getPresentSmells()) {
                smellCounter.put(smell, smellCounter.getOrDefault(smell, 1));
            }
        }

        return smellCounter;
    }
}
