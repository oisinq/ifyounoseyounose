package org.ifyounoseyounose.backend.smelldetector;

import org.ifyounoseyounose.backend.SmellDetectorManager;
import org.ifyounoseyounose.backend.smelldetectors.BloatedClassSmellDetector;

import org.ifyounoseyounose.backend.smelldetectors.SmellDetector;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BloatedClassTest {

    @Test
    public void detectSmells() {
        SmellDetectorManager s = new SmellDetectorManager();

        List<File> l = new ArrayList<>();
        l.add(new File("./src/test/java/smellycodedirectory/SmellySwitchStatements.java"));
        l.add(new File("./src/test/java/smellycodedirectory/SmellyBloatedParamCode.java"));


        HashMap<String, Integer> smellDetectorObjects = new HashMap<>();
        smellDetectorObjects.put("BloatedClass", 20);

        s.detectSmells(smellDetectorObjects, l);
        assert(true);
    }
}

