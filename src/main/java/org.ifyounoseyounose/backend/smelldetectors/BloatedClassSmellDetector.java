package org.ifyounoseyounose.backend.smelldetectors;

import org.ifyounoseyounose.backend.SmellReport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BloatedClassSmellDetector extends LimitableSmellDetector implements ReflectionSmellDetector{

    public BloatedClassSmellDetector(int limit) {
        super(limit);
    }

    public BloatedClassSmellDetector() {
        super(20);
    }
    @Override
    public SmellReport detectSmell(HashMap<Class, File> classes) {

        SmellReport smells = new SmellReport();


        for (Class current : classes.keySet()) {
            int lineNumber = 0;
            List<Integer> lines = new ArrayList<Integer>();
            try {

                FileReader targetStream = new FileReader(classes.get(current));

                BufferedReader bufferedReader =
                        new BufferedReader(targetStream);
                String line = bufferedReader.readLine();
                while (line != null) {
                    line = bufferedReader.readLine();
                    lineNumber++;
                        }


            } catch (Exception e) {
                System.err.println("Invalid file" + e.toString());
            }

            if (limit < lineNumber) {
                lines.add(0); //add zero to highlight the class declaration 
                System.out.println(lineNumber);

            }

            smells.addToReport(classes.get(current), lines);
        }

            return smells;

    }
}
