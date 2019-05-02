package org.ifyounoseyounose.backend.smelldetectors;

import org.ifyounoseyounose.backend.SmellReport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * BloatedClassSmellDetector - SmellDetector that manually evaluates if a class is bloated or not
 */
public class BloatedClassSmellDetector extends LimitableSmellDetector implements ManualParserSmellDetector, SmellDetector {

    public BloatedClassSmellDetector() {
        super(34);
    }

    @Override
    public SmellReport detectSmell(List<File> files) {

        SmellReport smells = new SmellReport();

        // We run this loop for each file in the list, checkits its length
        for (File current : files) {
            int lineNumber = 0;
            int removeLines = 0;
            List<Integer> lines = new ArrayList<>();
            try {
                FileReader targetStream = new FileReader(current);

                BufferedReader bufferedReader =
                        new BufferedReader(targetStream);
                String line = bufferedReader.readLine();

                while (line != null) {
                    line = line.trim();

                    if (line.startsWith("//") || line.startsWith("*") || line.length() == 0) { // We ignore lines with comments or empty lines
                        removeLines++;
                    }

                    line = bufferedReader.readLine();
                    lineNumber++; // We add to the line count
                }

            } catch (Exception e) {
                System.err.println("Invalid file" + e.toString()); // If we can't read the file, we throw an exception
            }

            if (limit <= lineNumber - removeLines) {
                lines.add(0); // highlight line 1 (to signify that the class is smelly)
            }
            smells.addToReport(current, lines);
        }
        return smells;
    }


    public String getSmellName() {
        return "BloatedClass";
    }
}
