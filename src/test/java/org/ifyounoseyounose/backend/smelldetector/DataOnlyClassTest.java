package org.ifyounoseyounose.backend.smelldetector;

import org.ifyounoseyounose.backend.SmellDetectorManager;
import org.ifyounoseyounose.backend.smelldetectors.DataOnlyClassesSmellDetector;
import org.ifyounoseyounose.backend.smelldetectors.PrimitiveObsessionSmellDetector;
import org.ifyounoseyounose.backend.smelldetectors.SmellDetector;
import org.ifyounoseyounose.backend.smelldetectors.TooManyLiteralsSmellDetector;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataOnlyClassTest {

    @Test
    public void detectSmells() {
        SmellDetectorManager s = new SmellDetectorManager();

        List<File> l = new ArrayList<>();
        //l.add(new File("./src/test/java/smellycodedirectory/Yeet.java"));
        //l.add(new File("./src/test/java/smellycodedirectory/Yeet2.java"));
        //l.add(new File("./src/test/java/smellycodedirectory/seq/Yeet3.java"));
        //l.add(new File("./src/test/java/smellycodedirectory/SmellyLiterals.java"));
        //l.add(new File("./src/test/java/smellycodedirectory/SmellyPrimitives.java"));
        l.add(new File("./src/test/java/smellycodedirectory/SmellyDataOnly.java"));
        List<SmellDetector> smellDetectors = new ArrayList<>();
        smellDetectors.add(new DataOnlyClassesSmellDetector());


        s.detectSmells(smellDetectors, l);
        assert(true);
    }
}
