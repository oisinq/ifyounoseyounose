package org.ifyounoseyounose.backend;

import java.util.ArrayList;
import java.util.List;

public class FileReport {
    private List<Integer> detectedLines;

    FileReport() {
        detectedLines = new ArrayList<>();
    }
    public void addDetections(List<Integer> newLines) {
        detectedLines.addAll(newLines);
    }
}