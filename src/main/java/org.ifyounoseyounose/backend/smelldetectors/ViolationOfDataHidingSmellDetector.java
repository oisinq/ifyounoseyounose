package org.ifyounoseyounose.backend.smelldetectors;


import javassist.*;
import org.ifyounoseyounose.backend.SmellReport;

import java.lang.reflect.Modifier;
import java.util.*;

import java.lang.reflect.Field;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

public class ViolationOfDataHidingSmellDetector extends SmellDetector implements ReflectionSmellDetector {

    @Override
    public SmellReport detectSmell(HashMap<Class, File> classes) {


        SmellReport smells = new SmellReport();


        for (Class current : classes.keySet()) {    //iterate through all the classes in the hashmap
        List<String> publicFields = new ArrayList<>();
        List<Integer> lines = new ArrayList<>(); //list of line numbers where smell was found
             Field[] fields = current.getDeclaredFields();
            for(Field f: fields) {
                int modifiers = f.getModifiers();

                // System.out.println(f);
                if (Modifier.isPublic(modifiers)) {  //check that the field is public

                    publicFields.add(f.getName());
                }
                    try {
                        int count = 1;
                        FileReader targetStream = new FileReader(classes.get(current));

                        BufferedReader bufferedReader =
                                new BufferedReader(targetStream);
                        String line = bufferedReader.readLine();

                        while (line != null) {
                            for (String name : publicFields){
                            if (line.contains(name) && line.contains("public")){ //if the line is a public declaration

                                publicFields.remove(name);  //remove the field from the list to avoid duplicates
                                lines.add(count); //add to line number list
                            }
                            }
                            line = bufferedReader.readLine();
                            count++;
                        }

                    } catch (Exception e) {

                        System.out.println("Invalid file");
                    }


                }
            smells.addToReport(classes.get(current), lines); //add the current file and list of lines to the report
            }



        return smells;
    }
}
