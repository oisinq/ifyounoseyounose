package org.ifyounoseyounose.backend;

import org.ifyounoseyounose.backend.smelldetectors.SmellDetector;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.io.File;


/**
 * ReportBuilder - creates the list
 */
public class ReportBuilder {

    public ArrayList<File> java_files = new ArrayList<>();

    public HashMap<File, HashMap<SmellDetector, List<Integer>>> generateReport(List<SmellDetector> smells, File directory) {

        HashMap<SmellDetector, List<Integer>> linesMap = new HashMap<>();
        HashMap<File, HashMap<SmellDetector, List<Integer>>> detectMap = new HashMap<>();

        File[] directory_files = directory.listFiles();
        ArrayList<File> javaFiles;
        javaFiles = getJavaFiles(directory_files);

        SmellDetectorManager manager = new SmellDetectorManager();
        manager.detectSmells(smells, javaFiles);

        return detectMap;
    }

    /**
     * Gets all the files in the directory that are .java files
     */
    private ArrayList<File> getJavaFiles(File[] directory) {

        for (File dir : directory) {
            if (dir.isDirectory()) {

                getJavaFiles(dir.listFiles());

            } else if (dir.isFile() && dir.getName().endsWith(".java")) {
                java_files.add(dir);
            }
        }
        return java_files;
    }

}
