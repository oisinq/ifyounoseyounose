package org.ifyounoseyounose.backend.smelldetectors;

import org.ifyounoseyounose.backend.SmellReport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BloatedClassSmellDetector extends LimitableSmellDetector implements ManualParserSmellDetector, SmellDetector {

    public BloatedClassSmellDetector(int limit) {
        super(limit);
    }

    public BloatedClassSmellDetector() {
        super(20);
    }
    @Override
    public SmellReport detectSmell(List<File> files) {

        SmellReport smells = new SmellReport();


        for (File current : files) {
            int lineNumber = 0;
            List<Integer> lines = new ArrayList<>();
            try {
                
                FileReader targetStream = new FileReader(current);

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

            }

            smells.addToReport(current, lines);
        }

            return smells;

    }


    public String getSmellName() {
        return "BloatedClass";
    }
}
