package org.ifyounoseyounose.backend.smelldetectors;

import org.ifyounoseyounose.backend.SmellReport;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MessageChainingSmellDetector extends LimitableSmellDetector implements ManualParserSmellDetector, SmellDetector {
    @Override
    public SmellReport detectSmell(List<File> sourceCode) {
        SmellReport smells = new SmellReport();// To be returned
        int count;// Line number
        for (File f : sourceCode) {// Iterates through files
            List<Integer> current = new ArrayList<>();// Smelly line numbers
            String line;
            count = 1;
            try {
                FileReader targetStream = new FileReader(f);
                BufferedReader bufferedReader = new BufferedReader(targetStream);
                while ((line = bufferedReader.readLine()) != null) {
                    checkRegex(line, count, current);
                    count++;
                }
                if (!current.isEmpty()) {//Ensures there is a line of code to add
                    smells.addToReport(f, current);
                }
            } catch (Exception e) {
                System.out.println("Invalid file");
            }
        }
        return smells;
    }

    private void checkRegex(String line, int count, List<Integer> current){
        if (!line.trim().startsWith("/") && !line.startsWith("*")) {
            if (line.matches("[^/].*(\\..*\\(.*\\)){"+limit+",};")) { // Regular expression check
                current.add(count);
            }
        }
    }

    public String getSmellName() {
        return "MessageChaining";
    }
}
