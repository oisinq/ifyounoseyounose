package org.ifyounoseyounose.backend;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * ReportBuilder - gets a list of .java Files, sends them to SmellDetectorManger and creates a CompleteReport for the directory
 */
public class ReportBuilder {

    private ArrayList<File> java_files = new ArrayList<>();

    /**
     * Method called by ReportBuilder which in turn invokes each individual SmellDetector
     * @param smells               A map where the key is the name of a smell detector and the value is the associated limit with that smell
     * @param directory            The project directory that we want to analyse
     * @return A CompleteReport object, which contains information about each file and the detected code smells
     */
    public CompleteReport generateReport(HashMap<String, Integer> smells, File directory) {
        CompleteReport completeReport = new CompleteReport();
        File[] directory_files = directory.listFiles();

        if (directory_files != null) {
            ArrayList<File> javaFiles = getJavaFiles(directory_files);

            SmellDetectorManager manager = new SmellDetectorManager();
            List<FileReport> fileReports = manager.detectSmells(smells, javaFiles);

            for (FileReport report : fileReports) {
                completeReport.addFileReport(report);
            }
        }

        return completeReport;
    }

    /**
     * Gets all the files in the directory that are .java files
     * @param directory               The directory of the project we want to get .java files from
     * @return A list of all the .java files
     */
    private ArrayList<File> getJavaFiles(File[] directory) {

        for (File dir : directory) {
            if (dir.isDirectory()) {
                getJavaFiles(Objects.requireNonNull(dir.listFiles()));
            } else if (dir.isFile() && dir.getName().endsWith(".java") && !dir.getName().contains("module-info")) {
                // module-info.java can't be parsed and causes a crash if JavaParser tries to get it, so we take it out
                java_files.add(dir);
            }
        }
        return java_files;
    }
}
