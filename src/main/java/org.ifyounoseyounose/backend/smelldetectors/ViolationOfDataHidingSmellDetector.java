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

        //count number of lines
        //System.out.println(classes.toString());
        //System.out.println(classes);
        for (Class current : classes.keySet()) {    //iterate through all the classes in the hashmap
        List<String> publicFields = new ArrayList<>();
        List<Integer> lines = new ArrayList<>(); //list of line numbers where smell was found
             Field[] fields = current.getDeclaredFields();
            for(Field f: fields) {
                int modifiers = f.getModifiers();

                // System.out.println(f);
                if (Modifier.isPublic(modifiers)) {  //check that the field is public
                    //System.out.println(f.getName());
                    publicFields.add(f.getName());
                }
                    try {
                        int count = 1;
                        FileReader targetStream = new FileReader(classes.get(current));
                        //System.out.println(classes.get(current));
                        BufferedReader bufferedReader =
                                new BufferedReader(targetStream);
                        String line = bufferedReader.readLine();

                        while (line != null) {
                            for (String name : publicFields){
                            if (line.contains(name) ){
                                //System.out.println(line);
                                publicFields.remove(name);
                                lines.add(count);
                            }
                            }
                            line = bufferedReader.readLine();
                            count++;
                        }

                    } catch (Exception e) {
                        
                        System.out.println("Invalid file");
                    }




                }
            smells.addToReport(classes.get(current), lines);
            }



        return smells;
    }
}
