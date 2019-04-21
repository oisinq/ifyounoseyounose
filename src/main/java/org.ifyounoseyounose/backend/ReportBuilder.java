package org.ifyounoseyounose.backend;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.io.File;


/**
 * ReportBuilder - creates the list
 */
public class ReportBuilder {

    private ArrayList<File> java_files = new ArrayList<>();

    //public HashMap<File, HashMap<SmellDetector, List<Integer>>> generateReport(List<SmellDetector> smells, File directory) {
    public CompleteReport generateReport(HashMap<String,Integer> smells, File directory) {
        CompleteReport completeReport = new CompleteReport();

        File[] directory_files = directory.listFiles();

        ArrayList<File> javaFiles = getJavaFiles(directory_files);

        SmellDetectorManager manager = new SmellDetectorManager();
        List<FileReport> fileReports = manager.detectSmells(smells, javaFiles);

        for (FileReport report : fileReports) {
            completeReport.addFileReport(report);
        }

        return completeReport;
    }

    /**
     * Gets all the files in the directory that are .java files
     */
    private ArrayList<File> getJavaFiles(File[] directory) {

        for (File dir : directory) {
            if (dir.isDirectory()) {

                getJavaFiles(dir.listFiles());
            } else if(dir.getName().contains("module-info")){
                continue;//module info can't be parsed and causes a crash if parser tries to get it so we take it out
            } else if (dir.isFile() && dir.getName().endsWith(".java")) {
                java_files.add(dir);
            }
        }
        return java_files;
    }

    private void addStats(CompleteReport report) {
        // Todo
    }
}
