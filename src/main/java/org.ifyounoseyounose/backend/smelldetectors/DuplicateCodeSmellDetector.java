package org.ifyounoseyounose.backend.smelldetectors;

import org.ifyounoseyounose.backend.SmellReport;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DuplicateCodeSmellDetector extends LimitableSmellDetector implements ManualParserSmellDetector  {
    @Override
    public SmellReport detectSmell(List<File> sourceCode) {
        SmellReport smells = new SmellReport();// To be returned
        int count = 1;// Line number
        HashMap<String, HashMap<File, List<Integer>>> temp = new HashMap<>();//A hashmap with key being the contents of the line, the value being a hashmap with the key being the file and the value being the line number the content appeared on

        for(File f : sourceCode) {// Iterates through files

            String line = null;
            count=1;
            try {

                FileReader targetStream = new FileReader(f);
                BufferedReader bufferedReader =
                        new BufferedReader(targetStream);

                while((line = bufferedReader.readLine()) != null) {
                    line = line.trim();

                    if (!line.equals("}") && !line.equals("{") && !line.equals("")) {// Checks lines are irrelevant
                        HashMap<File, List<Integer>> hold = temp.get(line); //see if you already have a hashmap for current key
                        System.out.println("test4");

                        if (hold == null) { //if not create one and put it in the map
                            hold = new HashMap<File, List<Integer>>();
                            temp.put(line, hold);
                        }
                        List<Integer> l = (temp.get(line)).get(f); //see if you already have a list for current key
                        System.out.println("test4");

                        if (l == null) { //if not create one and put it in the map
                            l = new ArrayList<Integer>();
                            temp.get(line).put(f, l);
                        }
                        temp.get(line).get(f).add(count);//Adds the line number to the inner hashmap
                    }
                    count++;
                }


            }
            catch(IOException z)
            {
                System.out.println("Invalid file");
            }



        }
        if(!temp.isEmpty()) {//Ensures there is a line of code to add

            int maxLines=0;
            for(HashMap<File, List<Integer>> x: temp.values()) {//Searches through the hashmaps
                maxLines=0;
                for(List<Integer> u: x.values())//Adds up the total amount of times the lines has appeared
                {
                    maxLines=maxLines+u.size();
                }
                if(maxLines>=limit) {//If greater then the limit add to report
                    for (File y : x.keySet()) {
                            smells.addToReport(y, x.get(y));
                    }
                }
            }

        }
        return smells;
    }
}