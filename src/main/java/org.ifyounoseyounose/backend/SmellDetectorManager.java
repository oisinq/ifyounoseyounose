package org.ifyounoseyounose.backend;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class SmellDetectorManager {
    public List<SmellReport> detectSmells(List<SmellDetector> smellDetectors, List<File> files) {
        List<SmellReport> results = new ArrayList<>();

        // I go through every file, then every SmellDetector and pass each file to the relevant SmellDetector
        for (File file : files) {
            for (SmellDetector smellDetector : smellDetectors) {
                // Check what type of smellDetector it is by seeing what interface it inherits
                // and call smellDetector.detectSmell() on it with the parameters corresponding to its interface
                if (smellDetector instanceof JavaParserSmellDetector) {
                    try {
                        CompilationUnit cu = StaticJavaParser.parse(file);
                        // I have to do some casting here, I'm not sure if it's necessary
                        results.add(((JavaParserSmellDetector) smellDetector).detectSmell(cu));
                    } catch (FileNotFoundException e) {
                        System.err.println("JavaParser cannot find the .java file " + file.toString());
                    }
                } else if (smellDetector instanceof ManualParserSmellDetector) {
                    results.add(((ManualParserSmellDetector) smellDetector).detectSmell(file));
                } else if (smellDetector instanceof ReflectionSmellDetector) {
                   // ToolProvider.getSystemJavaCompiler();
                    //((ReflectionSmellDetector) smellDetector).detectSmell();
                }
            }
        }
        return results;
    }
}