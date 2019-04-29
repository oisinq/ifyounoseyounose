package org.ifyounoseyounose.backend.smelldetectors;

import org.ifyounoseyounose.backend.SmellReport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class ViolationOfDataHidingSmellDetector implements ReflectionSmellDetector, SmellDetector {

    @Override
    public SmellReport detectSmell(HashMap<Class, File> classes) {

        SmellReport smells = new SmellReport();
        for (Class current : classes.keySet()) {    //iterate through all the classes in the hashmap
            List<String> publicFields = new ArrayList<>();
            Set<Integer> lines = new HashSet<>(); //list of line numbers where smell was found
            Field[] fields = current.getDeclaredFields();

            for (Field f : fields) {
                int modifiers = f.getModifiers();
                if (Modifier.isPublic(modifiers)) {  //check that the field is public
                    publicFields.add(f.getName());
                }
            }
            try {
                int lineNumber = 1;
                FileReader targetStream = new FileReader(classes.get(current));

                BufferedReader bufferedReader =
                        new BufferedReader(targetStream);
                String line = bufferedReader.readLine();

                while (line != null) {
                    for (String name : publicFields) {
                        if (line.contains(name) && line.contains("public")) { //if the line is a public declaration
                            lines.add(lineNumber); //add to line number list
                        }
                    }
                    line = bufferedReader.readLine();
                    lineNumber++;
                }
            } catch (Exception e) {
                System.err.println("Invalid file" + e.toString());

            }
            ArrayList<Integer> finalLines = new ArrayList<>(lines);
            smells.addToReport(classes.get(current), finalLines); //add the current file and list of lines to the report
        }
        return smells;
    }

    public String getSmellName() {
        return "DataHiding";
    }
}
