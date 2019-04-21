package org.ifyounoseyounose.backend.smelldetector;

import org.ifyounoseyounose.backend.SmellDetectorManager;
import org.ifyounoseyounose.backend.smelldetectors.SmellDetector;

import org.ifyounoseyounose.backend.smelldetectors.ViolationOfDataHidingSmellDetector;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViolationOfDataHidingTest {

    @Test
    public void detectSmells() {
        SmellDetectorManager s = new SmellDetectorManager();

        List<File> l = new ArrayList<>();
        //l.add(new File("./src/test/java/smellycodedirectory/Yeet.java"));
        //l.add(new File("./src/test/java/smellycodedirectory/Yeet2.java"));
        //l.add(new File("./src/test/java/smellycodedirectory/seq/Yeet3.java"));
       // l.add(new File("./src/test/java/smellycodedirectory/SmellyLiterals.java"));
        l.add(new File("./src/main/java/org.ifyounoseyounose/backend/smelldetectors/DataOnlyClassesSmellDetector.java"));

        HashMap<String, Integer> smellDetectorObjects = new HashMap<>();
        smellDetectorObjects.put("ViolationOfDataHiding", 0);

        s.detectSmells(smellDetectorObjects, l);
        assert(true);
    }
}