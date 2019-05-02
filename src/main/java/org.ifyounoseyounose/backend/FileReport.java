package org.ifyounoseyounose.backend;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * FileReport - an object representing the smells detected in a file (across all executed SmellDetectors)
 */
public class FileReport {
    private HashMap<String, List<Integer>> detections = new HashMap<>();
    private File relatedFile;

    /**
     * Adds new smelly lines for a given SmellDetector name
     */
    void addSmellDetections(String smellName, List<Integer> newLines) {
        detections.put(smellName, newLines);
    }

    /**
     * Sets the File associated with this FileReport
     */
    public void setFile(File f) {
        relatedFile = f;
    }

    /**
     * Gets the File associated with this FileReport
     */
    public File getFile() {
        return relatedFile;
    }

    /**
     * Returns a hashmap of the smells detected by the program
     */
    public HashMap<String, List<Integer>> getSmellDetections() {
        return detections;
    }

    /**
     * Counts how many smelly lines are in the File (Note: if a line contains two smells, it will be counted twice)
     * (used for displaying stats to the user)
     */
    public int getSmellyLinesCount() {
        Set<Integer> smellyLines = new HashSet<>();

        // Goes through each SmellReport and adds up the number of smelly lines
        for (String smellName : detections.keySet()) {
            List<Integer> lineNumbers = detections.get(smellName);
            smellyLines.addAll(lineNumbers);
        }

        if (smellyLines.contains(0)) { // Line number 0 is used to signify the whole file is smelly, so we must return the full length of the file
            try {
                return (int) Files.lines(relatedFile.toPath()).count();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return smellyLines.size();
    }

    /**
     * Gets the percentage of lines in the File that are smelly
     * (used for displaying stats to the user)
     */
    private double getSmellyLinePercentage() {
        try {
            int numberOfSmellyLines = getSmellyLinesCount();
            long totalNumberOfLines = Files.lines(relatedFile.toPath()).count();
            return 100.0 * numberOfSmellyLines / totalNumberOfLines;
        } catch (Exception e) {
            System.err.println("Cannot open file at path " + relatedFile.toPath().toString());
        }
        return -1.0;
    }

     /**
     * Returns a list of all the Smells present in the file with their frequency
     * (used for displaying stats to the user)
     */
    private Map<String, Integer> getSmellyLineCountPerSmell() {
        Map<String, Integer> lineCountPerSmell = new HashMap<>();
        for (String smellName : detections.keySet()) {
            lineCountPerSmell.put(smellName, detections.get(smellName).size());
        }
        return lineCountPerSmell;
    }

    /**
     * Returns a list of all the Smells present in the file with their frequency, ordered by most frequent
     * (used for displaying stats to the user)
     */
    public List<Map.Entry<String, Integer>> getListOfSmellsByCount() {
        Set<Map.Entry<String, Integer>> setOfEntries = getSmellyLineCountPerSmell().entrySet();

        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(setOfEntries);

        // Since HashMaps are unordered, me need to return a List of Entry objects instead (this is a list of String, Integer pairs)
        sortedEntries.sort(Comparator.comparing(Map.Entry::getValue));
        Collections.reverse(sortedEntries);

        return sortedEntries;
    }

    /**
     * Returns a set of all the Smells present in the file (used for displaying stats to the user)
     * (used for displaying stats to the user)
     */
    Set<String> getPresentSmells() {
        return detections.keySet();
    }

    /**
     * Checks if the FileReport actually has any contents
     * (used for displaying stats to the user)
     */
    boolean isEmpty() {
        for (List<Integer> smellyLines : detections.values()) {
            if (!smellyLines.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * A String representation of the contents of the FileReport.
     * (used for displaying stats to the user)
     */
    public String toString() {
        // I'm using a StringBuilder to increase efficiency, because appending strings with += in for loops is not recommended
        StringBuilder output = new StringBuilder("File: " + getFile().toString() + "\n");
        output.append("Smelly percentage:").append(getSmellyLinePercentage()).append("\n");
        for (String s : detections.keySet()) {
            output.append("Smell: ").append(s).append("\n").append("Lines ");
            List<Integer> lines = detections.get(s);
            for (int i = 0; i < lines.size() - 2; i++) {
                output.append(lines.get(i)).append(", ");
            }
            output.append("& ").append(lines.get(lines.size() - 1)).append("\n");
        }
        return output.toString();
    }
}