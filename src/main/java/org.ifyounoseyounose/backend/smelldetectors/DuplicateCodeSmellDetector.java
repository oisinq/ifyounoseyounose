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
        HashMap<String, Integer> temp = new HashMap<>();
        for(File f : sourceCode) {// Iterates through files
            List<Integer> current = new ArrayList<>();// Smelly line numbers
            String line = null;
            count=0;
            try {
                FileReader targetStream = new FileReader(f);
                BufferedReader bufferedReader =
                        new BufferedReader(targetStream);
                while((line = bufferedReader.readLine()) != null) {
                    if(temp.containsKey(line))// Regular expression check
                    {
                        current.add(count);
                        temp.put(line, temp.get(line)+1);
                    }
                    else
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