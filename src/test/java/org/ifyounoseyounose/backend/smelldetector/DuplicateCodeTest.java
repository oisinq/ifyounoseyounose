package org.ifyounoseyounose.backend.smelldetector;

import org.ifyounoseyounose.backend.SmellDetectorManager;
import org.ifyounoseyounose.backend.smelldetectors.DuplicateCodeSmellDetector;
import org.ifyounoseyounose.backend.smelldetectors.SmellDetector;
import org.ifyounoseyounose.backend.smelldetectors.TooManyLiteralsSmellDetector;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DuplicateCodeTest {

    @Test
    public void detectSmells() {
        SmellDetectorManager s = new SmellDetectorManager();

        List<File> l = new ArrayList<>();
        l.add(new File("./src/test/java/smellycodedirectory/DuplicatedCode.java"));
        l.add(new File("./src/test/java/smellycodedirectory/Yeet.java"));


        HashMap<String, Integer> smellDetectorObjects = new HashMap<>();
        smellDetectorObjects.put("TooManyLiterals", 0);

        s.detectSmells(smellDetectorObjects, l);
        assert(true);
    }
}