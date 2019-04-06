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
        HashMap<String, Integer> temp = new HashMap<>();// Key is line contents, value is number of times line has appeared
        for(File f : sourceCode) {// Iterates through files
            List<Integer> current = new ArrayList<>();// Smelly line numbers
            String line = null;
            count=0;
            try {
                FileReader targetStream = new FileReader(f);
                BufferedReader bufferedReader =
                        new BufferedReader(targetStream);
                while((line = bufferedReader.readLine()) != null) {
                    line = line.trim();
                    if(temp.containsKey(line)&&temp.get(line)>2)//If number of times line has appeared exceeds limit add line to smell reort
                    {
                        current.add(count);
                        temp.put(line, temp.get(line)+1);
                    }
                    else if(temp.containsKey(line))// If line has been repeated but under limit. Increase hashmap value
                    {
                        temp.put(line, temp.get(line)+1);
                    }
                    else// If line has not appeared before add to hashmap
                    {
                        temp.put(line, 1);
                    }
                    count++;
                }
                if(!current.isEmpty()) {//Ensures there is a line of code to add
                    smells.addToReport(f, current);
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