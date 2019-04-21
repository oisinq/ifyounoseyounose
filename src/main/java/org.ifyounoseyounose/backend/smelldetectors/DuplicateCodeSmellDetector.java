package org.ifyounoseyounose.backend.smelldetectors;

import org.ifyounoseyounose.backend.SmellReport;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DuplicateCodeSmellDetector extends LimitableSmellDetector implements ManualParserSmellDetector, SmellDetector {
    @Override
    public SmellReport detectSmell(List<File> sourceCode) {
        SmellReport smells = new SmellReport();
        int lineNumber;

        // A hashmap with key being the contents of the line, the value being a hashmap with the key
        // being the file and the value being the line number the content appeared on
        HashMap<String, HashMap<File, List<Integer>>> outerHashMap = new HashMap<>();

        for(File file : sourceCode) { // Iterates through files

            String line;
            lineNumber = 1;

            try {
                FileReader targetStream = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(targetStream);

                // This conditional gets the next line from bufferedReader, assigns its value to line then ensures it's not null
                while((line = bufferedReader.readLine()) != null) {
                    line = line.trim();
                    if(!line.startsWith("/")) {
                        if (!line.equals("}") && !line.equals("{") && !line.equals("") && !line.startsWith("/") &!line.startsWith("*")) { // Checks lines are irrelevant

                            HashMap<File, List<Integer>> innerHashMap = outerHashMap.get(line); //see if you already have a hashmap for current key

                            if (innerHashMap == null) { // If not, create one and put it in the map
                                innerHashMap = new HashMap<>();
                                outerHashMap.put(line, innerHashMap);
                            }

                            List<Integer> list = (outerHashMap.get(line)).get(file); // See if you already have a list for current key

                            if (list == null) { // If not, create one and put it in the map
                                list = new ArrayList<>();
                                outerHashMap.get(line).put(file, list);
                            }
                            outerHashMap.get(line).get(file).add(lineNumber); // Adds the line number to the inner hashmap
                        }
                    }
                    lineNumber++;
                }


            }
            catch(IOException e)
            {
                System.err.println("Could not read file");
            }
        }

        if(!outerHashMap.isEmpty()) { // Ensures there is a line of code to add
            int maxLines;

            for(HashMap<File, List<Integer>> x : outerHashMap.values()) { // Searches through the hashmaps
                maxLines = 0;
                for(List<Integer> u : x.values()) { // Adds up the total amount of times the lines has appeared
                    maxLines = maxLines + u.size();
                }
                if(maxLines >= limit) { // If greater then the limit, add to report
                    for (File y : x.keySet()) {
                        smells.addToReport(y, x.get(y));
                    }
                }
            }

        }
        return smells;
    }


    public String getSmellName() {
        return "DuplicateCode";
    }
}