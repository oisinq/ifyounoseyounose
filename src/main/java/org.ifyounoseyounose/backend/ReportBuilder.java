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

    public CompleteReport generateReport(HashMap<String,Integer> smells, File directory) {
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
     */
    private ArrayList<File> getJavaFiles(File[] directory) {

        for (File dir : directory) {
            if (dir.isDirectory()) {
                getJavaFiles(dir.listFiles());
            } else if(dir.isFile() && dir.getName().endsWith(".java") && !dir.getName().contains("module-info")){
                //module info can't be parsed and causes a crash if parser tries to get it so we take it out
                java_files.add(dir);
            }
        }
        return java_files;
    }

    private void addStats(CompleteReport report) {
        // Todo
    }
}
