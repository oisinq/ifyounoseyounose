package org.ifyounoseyounose.backend;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

public class FileReport {
    //Todo need a better variable name for this.
    private HashMap<String, List<Integer>> detections = new HashMap<>();
    private File relatedFile;

    void addSmellDetections(String smellName, List<Integer> newLines) {
        detections.put(smellName, newLines);
    }

    public void setFile(File f) {
        relatedFile = f;
    }

    public File getFile() {
        return relatedFile;
    }

    public HashMap<String, List<Integer>> getSmellDetections() {
        return detections;
    }

    public int getSmellyLinesCount() {
        Set<Integer> smellyLines = new HashSet<>();

        for (String smellName : detections.keySet()) {
            List<Integer> lineNumbers = detections.get(smellName);
            smellyLines.addAll(lineNumbers);
        }

        return smellyLines.size();
    }

    public double getSmellyLinePercentage() {
        try {
            int numberOfSmellyLines = getSmellyLinesCount();
            long totalNumberOfLines = Files.lines(relatedFile.toPath()).count();
            return 100.0*numberOfSmellyLines/totalNumberOfLines;
        } catch (Exception e) {
            System.err.println("Cannot open file at path " + relatedFile.toPath().toString());
        }
        return -1.0;
    }

    public Map<String, Integer> getSmellyLineCountPerSmell() {
        Map<String, Integer> lineCountPerSmell = new HashMap<>();
        for (String smellName : detections.keySet()) {
            lineCountPerSmell.put(smellName, detections.get(smellName).size());
        }
        return lineCountPerSmell;
    }

    public Set<String> getPresentSmells() {
        return detections.keySet();
    }

    public boolean isEmpty() {
        for (List<Integer> l : detections.values()) {
            if (!l.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder output = new StringBuilder("File: " + getFile().toString() + "\n");
        for (String s : detections.keySet()) {
            output.append("Smell: ").append(s).append("\n").append("Lines ");
            List<Integer> lines = detections.get(s);
            for (int i = 0; i < lines.size()-2; i++) {
                output.append(lines.get(i)).append(", ");
            }
            output.append("& ").append(lines.get(lines.size() - 1)).append("\n");
        }
        return output.toString();
    }
}