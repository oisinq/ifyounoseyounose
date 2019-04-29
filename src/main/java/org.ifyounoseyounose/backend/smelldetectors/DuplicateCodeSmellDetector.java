package org.ifyounoseyounose.backend.smelldetectors;

import org.ifyounoseyounose.backend.SmellReport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class DuplicateCodeSmellDetector extends LimitableSmellDetector implements ManualParserSmellDetector, SmellDetector {
    @Override
    public SmellReport detectSmell(List<File> sourceCode) {
        SmellReport smells = new SmellReport();

        // A hashmap with key being the contents of the line, the value being a hashmap with the key
        // being the file and the value being the line number the content appeared on
        HashMap<String, HashMap<File, List<Integer>>> outerHashMap = new HashMap<>();

        for (File file : sourceCode) { // Iterates through files
            checkLines(file, outerHashMap);
        }

        if (!outerHashMap.isEmpty()) { // Ensures there is a line of code to add
            countAgainstLimit(outerHashMap.values(), smells);
        }
        return smells;
    }

    private void countAgainstLimit(Collection<HashMap<File, List<Integer>>> maps, SmellReport smells) {
        int maxLines;

        for (HashMap<File, List<Integer>> x : maps) { // Searches through the hashmaps
            maxLines = 0;
            for (List<Integer> u : x.values()) { // Adds up the total amount of times the lines has appeared
                maxLines = maxLines + u.size();
            }
            if (maxLines > limit) { // If greater then the limit, add to report
                for (File y : x.keySet()) {
                    smells.appendToList(y, x.get(y));
                }
            }
        }
    }

    private void checkLines(File file, HashMap<String, HashMap<File, List<Integer>>> outerHashMap) {
        String line;
        int lineNumber = 1;
        try {
            FileReader targetStream = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(targetStream);
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (!line.startsWith("try")&&!line.contains("catch")&&!line.equals("")&&!line.startsWith("}") && !line.startsWith("{")&& !line.startsWith("/") &&!line.startsWith("*")&&!line.startsWith("import")&&!line.startsWith("package")&& !line.startsWith("protected") &&!line.startsWith("final")&&!line.startsWith("private")&&!line.startsWith("public")&&!line.startsWith("@")&&!line.startsWith("return")&&!line.equals("break;")) { // Checks lines are irrelevant
                    outerHashMap.computeIfAbsent(line, k -> new HashMap<>());
                    List<Integer> list = (outerHashMap.get(line)).get(file); // See if you already have a list for current key
                    if (list == null) { // If not, create one and put it in the map
                        list = new ArrayList<>();
                        outerHashMap.get(line).put(file, list);
                    }
                    outerHashMap.get(line).get(file).add(lineNumber); // Adds the line number to the inner hashmap
                }
                lineNumber++;
            }
        } catch (IOException e) {
            System.err.println("Could not read file");
        }
    }

    public String getSmellName() {
        return "DuplicateCode";
    }
}