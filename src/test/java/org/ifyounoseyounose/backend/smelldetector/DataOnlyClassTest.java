package org.ifyounoseyounose.backend.smelldetector;

import org.ifyounoseyounose.backend.SmellDetectorManager;
import org.ifyounoseyounose.backend.smelldetectors.DataOnlyClassesSmellDetector;
import org.ifyounoseyounose.backend.smelldetectors.SmellDetector;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataOnlyClassTest {

    @Test
    public void detectSmells() {
        SmellDetectorManager s = new SmellDetectorManager();

        List<File> l = new ArrayList<>();

        l.add(new File("./src/test/java/smellycodedirectory/SmellyPrimitives.java"));
        l.add(new File("./src/test/java/smellycodedirectory/SmellyDataOnly.java"));
        l.add(new File("./src/test/java/smellycodedirectory/SmellyData2.java"));

        HashMap<String, Integer> smellDetectorObjects = new HashMap<>();
        smellDetectorObjects.put("DataOnly", 2);

        s.detectSmells(smellDetectorObjects, l);
        assert(true);
    }
}
