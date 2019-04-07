package org.ifyounoseyounose.backend.smelldetectors;


import org.ifyounoseyounose.backend.SmellReport;

import java.lang.reflect.Modifier;
import java.util.*;

import java.lang.reflect.Field;
import java.io.File;


public class ViolationOfDataHidingSmellDetector extends SmellDetector implements ReflectionSmellDetector {

    @Override
    public SmellReport detectSmell(HashMap<Class, File> classes) {

        SmellReport smells = new SmellReport();
        List<Integer> lines = new ArrayList<>();

        for (Class current : classes.keySet()) {    //iterate through all the classes in the hashmap 
            Field[] fields = current.getDeclaredFields();

            for(Field f: fields){
                int modifiers = f.getModifiers();
                if(Modifier.isPublic(modifiers)){ //check that the field is public
                    smells.addToReport(classes.get(current), lines);
                }
            }

        }





        return smells;
    }
}
