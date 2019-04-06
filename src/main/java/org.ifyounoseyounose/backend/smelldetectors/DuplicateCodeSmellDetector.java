package org.ifyounoseyounose.backend.smelldetectors;

import org.ifyounoseyounose.backend.SmellReport;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DuplicateCodeSmellDetector extends SmellDetector implements ManualParserSmellDetector {
    @Override
    public SmellReport detectSmell(List<File> sourceCode) {
        SmellReport smells = new SmellReport();// To be returned
        int count = 0;// Line number
        for(File f : sourceCode) {// Iterates through files
            HashMap<String, List<Integer>> temp = new HashMap<>();// Key is line contents, value is number of times line has appeared

            String line = null;
            count=0;
            try {

                FileReader targetStream = new FileReader(f);
                BufferedReader bufferedReader =
                        new BufferedReader(targetStream);

                while((line = bufferedReader.readLine()) != null) {
                    line = line.trim();

                    if (!line.equals("}") && !line.equals("{") && !line.equals("")) {// Checks lines are irrelevant
                        List<Integer> l = temp.get(line); //see if you already have a list for current key
                        if (l == null) { //if not create one and put it in the map
                            l = new ArrayList<Integer>();
                            temp.put(line, l);
                        }
                            temp.get(line).add(count);
                    }
                    count++;
                }

                if(!temp.isEmpty()) {//Ensures there is a line of code to add
                    for(List<Integer> i: temp.values()) {//Searches for lines that have occurred more than the limit
                            if(i.size()>=2) {
                                smells.addToReport(f, i);
                            }

                    }
                }
            }
            catch(Exception e)
            {
                System.out.println("Invalid file");
            }

        }

        return smells;
    }
}