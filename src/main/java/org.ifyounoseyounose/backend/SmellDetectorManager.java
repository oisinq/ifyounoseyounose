package org.ifyounoseyounose.backend;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.ifyounoseyounose.backend.smelldetectors.*;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

/**
 * SmellDetectorManager - 
 */
public class SmellDetectorManager {

    /**
     * Method called by ReportBuilder which in turn invokes each individual SmellDetector
     *
     * @param smellDetectorStrings A map where the key is the name of a SmellDetector and the value is the associated limit with that smell
     * @param files                A list of files we want to analyse
     * @return A list of FileReports containing info about what code smells were detected
     */
    public List<FileReport> detectSmells(HashMap<String, Integer> smellDetectorStrings, List<File> files) {

        List<SmellDetector> smellDetectors = getSmellDetectors(smellDetectorStrings);
        HashMap<CompilationUnit, File> compUnits = new HashMap<>();

        getCompilationUnits(files, compUnits);

        List<SmellReport> results = new ArrayList<>();

        // For each SmellDetector object, we run it so that it detects code smells
        for (SmellDetector smellDetector : smellDetectors) {
            SmellReport smellReport = runCodeSmellDetector(smellDetector, files, compUnits);
            results.add(smellReport);
        }

        return generateFileReports(files, results);
    }

    /**
     * Creates CompilationUnit, File pairs for each File passed through
     *
     * @param files     Files we want to create compilation units from
     * @param compUnits HashMap that stores the File, CompilationUnit combination
     */
    private void getCompilationUnits(List<File> files, HashMap<CompilationUnit, File> compUnits) {
        // For each file, we add a CompilationUnit to the compUnits hashmap, with the File as the key
        // This makes creating SmellReports much easier
        for (File f : files) {
            try {
                compUnits.put(StaticJavaParser.parse(f), f);
            } catch (FileNotFoundException e) {
                System.err.println("Cannot find file " + f.getPath());
            }
        }
    }

    /**
     * Given a HashMap containing pairs of SmellDetector names and limits, we create the appropriate SmellDetector
     * objects and return a list
     *
     * @param smellDetectorStrings The HashMap containing SmellDetector names and limits
     * @return The list of SmellDetectors we want to use to detect smells (with limits set)
     */
    private List<SmellDetector> getSmellDetectors(HashMap<String, Integer> smellDetectorStrings) {
        List<SmellDetector> smellDetectors = new ArrayList<>();

        for (String smellDetector : smellDetectorStrings.keySet()) {
            SmellDetector currentSmell = null;
            int limit = -1;
            switch (smellDetector) {
                case "ArrowHeaded":
                    currentSmell = new ArrowheadedIndentationSmellDetector();
                    limit = smellDetectorStrings.get("ArrowHeaded");
                    break;
                case "BloatedClass":
                    currentSmell = new BloatedClassSmellDetector();
                    limit = smellDetectorStrings.get("BloatedClass");
                    break;
                case "BloatedMethod":
                    currentSmell = new BloatedMethodCodeSmellDetector();
                    limit = smellDetectorStrings.get("BloatedMethod");
                    break;
                case "BloatedParameter":
                    currentSmell = new BloatedParamSmellDetector();
                    limit = smellDetectorStrings.get("BloatedParameter");
                    break;
                case "DataOnly":
                    currentSmell = new DataOnlyClassesSmellDetector();
                    limit = smellDetectorStrings.get("DataOnly");
                    break;
                case "DataHiding":
                    currentSmell = new ViolationOfDataHidingSmellDetector();
                    limit = smellDetectorStrings.get("DataHiding");
                    break;
                case "DeadCode":
                    currentSmell = new DeadCodeSmellDetector();
                    limit = smellDetectorStrings.get("DeadCode");
                    break;
                case "DuplicateCode":
                    currentSmell = new DuplicateCodeSmellDetector();
                    limit = smellDetectorStrings.get("DuplicateCode");
                    break;
                case "MessageChaining":
                    currentSmell = new MessageChainingSmellDetector();
                    limit = smellDetectorStrings.get("MessageChaining");
                    break;
                case "PrimitiveObsession":
                    currentSmell = new PrimitiveObsessionSmellDetector();
                    limit = smellDetectorStrings.get("PrimitiveObsession");
                    break;
                case "SwitchStatement":
                    currentSmell = new SwitchStatementSmellDetector();
                    limit = smellDetectorStrings.get("SwitchStatement");
                    break;
                case "TooManyLiterals":
                    currentSmell = new TooManyLiteralsSmellDetector();
                    limit = smellDetectorStrings.get("TooManyLiterals");
                    break;
                case "TemporaryFields":
                    currentSmell = new TemporaryFieldsSmellDetector();
                    limit = smellDetectorStrings.get("TemporaryFields");
                    break;
                case "SpeculativeGenerality":
                    currentSmell = new SpeculativeGeneralitySmellDetector();
                    limit = smellDetectorStrings.get("SpeculativeGenerality");
                    break;
                default:
                    System.err.println("Smell Detector not found!");
            }

            if (currentSmell instanceof LimitableSmellDetector) {
                ((LimitableSmellDetector) currentSmell).setLimit(limit);
            }

            smellDetectors.add(currentSmell);
        }

        return smellDetectors;
    }

    /**
     * Returns a list of Class objects that have been loaded into the classLoader
     *
     * @param classLoader              URLClassLoader, containing the new classes we have imported
     * @param compiledClassesDirectory This is the folder containing the new .compiled_classes
     * @param files                    This is the list of files in the folder imported
     * @return A list of Class objects
     */
    private HashMap<Class, File> getListOfClasses(URLClassLoader classLoader, File compiledClassesDirectory, List<File> files) {

        List<Path> pathsList = new ArrayList<>();
        HashMap<Class, File> classesMap = new HashMap<>();
        try (Stream<Path> paths = Files.walk(Paths.get(compiledClassesDirectory.toString()))) {
            paths.filter(Files::isRegularFile)
                    .forEach(pathsList::add);
        } catch (Exception e) {
            System.err.println("Error searching .compiled_classes for files");
        }

        for (Path p : pathsList) {
            addPathToClassesMap(p, files, classesMap, classLoader);
        }

        return classesMap;
    }

    private void addPathToClassesMap(Path p, List<File> files, HashMap<Class, File> classesMap, URLClassLoader classLoader) {
        try {
            String fullPath = p.toString();

            // This extracts the class and package name from the full path of the class in /.compiled_clasees/
            String output = fullPath.replaceAll("\\.compiled_classes/", "");
            output = output.replaceAll("\\.class", "");
            output = output.replaceAll("/", ".");

            for (File f : files) {
                // find the file to which the compiled class corresponds
                if (fullPath.contains(f.getName().replaceAll(".java", ""))) {
                    classesMap.put(classLoader.loadClass(output), f); // add the class and file to hashMap to be returned
                }
            }

        } catch (ClassNotFoundException e) {
            System.err.println("Cannot find class.");
        }
    }

    /**
     * Generates a list of FileReports, using the files and SmellReports
     *
     * @param files   List of files we are analyzing
     * @param results List of SmellReports generated by each SmellDetector object
     * @return The list of generated FileReports
     */
    private List<FileReport> generateFileReports(List<File> files, List<SmellReport> results) {
        List<FileReport> fileReports = new ArrayList<>();
        for (File f : files) {
            FileReport fileReport = new FileReport();
            // Each fileReport contains info about multiple SmellDetectors, so we need to extract that data here
            for (SmellReport smellReport : results) {
                // If there were no smelly lines detected, we don't need to add it to the FileReport
                if (!smellReport.isEmptyForFile(f)) {
                    fileReport.addSmellDetections(smellReport.getSmellName(), smellReport.getDetectionsByFile(f));
                    fileReport.setFile(f);
                    fileReports.add(fileReport);
                }
            }
        }
        return fileReports;
    }

    /**
     * Detects which type of SmellDetector we are dealing with, and calls the appropriate method to execute it
     * @param smellDetector The SmellDetector we want to call
     * @param files         The files we want to analyze
     * @param compUnits     The CompilationUnit and File pairs that we need to use with JavaParser SmellDetectors
     * @return A SmellReport object representing the smells found, and which lines they were found on
     */
    private SmellReport runCodeSmellDetector(SmellDetector smellDetector, List<File> files, HashMap<CompilationUnit, File> compUnits) {
        SmellReport smellReport;
        // Check what type of smellDetector it is by seeing what interface it inherits
        // and call smellDetector.detectSmell() on it with the parameters corresponding to its interface
        if (smellDetector instanceof JavaParserSmellDetector) {
            smellReport = ((JavaParserSmellDetector) smellDetector).detectSmell(compUnits);
        } else if (smellDetector instanceof ManualParserSmellDetector) {
            smellReport = ((ManualParserSmellDetector) smellDetector).detectSmell(files);
        } else if (smellDetector instanceof ReflectionSmellDetector) {
            smellReport = executeReflectionSmellDetector(smellDetector, files);
        } else {
            throw new RuntimeException("SmellDetector " + smellDetector.getSmellName() + " doesn't inherit from one of " +
                    "the specialised SmellDetector interfaces");
        }
        if (smellReport != null) {
            smellReport.setSmellName(smellDetector.getSmellName());
        }
        return smellReport;
    }

    /**
     * Executes the detectSmell() method on a SmellDetector object that implements the JavaParserSmellDetector interface
     * and saves the resulting SmellReport in the results List
     *
     * @param smellDetector A SmellDetector object that implements the JavaParserSmellDetector interface
     * @param files         The list of files that are being analyzed
     */
    private SmellReport executeReflectionSmellDetector(SmellDetector smellDetector, List<File> files) {
        compileJavaFiles(files);

        File compiledClassesDirectory = new File(".compiled_classes/");
        URL[] urlList = new URL[1];
        URLClassLoader classLoader = null;
        SmellReport result = new SmellReport();
        classLoader = addToClassLoader(urlList, compiledClassesDirectory, classLoader);

        if (classLoader != null) {
            HashMap<Class, File> classesMap = getListOfClasses(classLoader, compiledClassesDirectory, files);
            result = ((ReflectionSmellDetector) smellDetector).detectSmell(classesMap);
        }
        return result;
    }

    /**
     * Adds a class URL to the classLoader
     */
    private URLClassLoader addToClassLoader(URL[] urlList, File compiledClassesDirectory , URLClassLoader classLoader) {
        try {
            urlList[0] = compiledClassesDirectory.toURI().toURL();
            classLoader = URLClassLoader.newInstance(urlList);
        } catch (Exception e) {
            System.err.println("Failed to add .class files to the ClassLoader");
        }
        return classLoader;
    }
    /**
     * Takes a list of .java files, compiles them .class files and puts them in the .compiled_classes folder
     *
     * @param files List of .java files
     */
    private void compileJavaFiles(List<File> files) {
        String[] st = new String[files.size() + 2];

        st[0] = "-d";
        st[1] = ".compiled_classes";
        for (int i = 2; i < st.length; i++) {
            st[i] = files.get(i - 2).getPath();
        }

        JavaCompiler compile = ToolProvider.getSystemJavaCompiler();
        compile.run(null, null, null, st);
    }
}