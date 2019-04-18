package org.ifyounoseyounose.backend.smelldetector;

import org.ifyounoseyounose.backend.SmellDetectorManager;
import org.ifyounoseyounose.backend.smelldetectors.ArrowheadedIndentationSmellDetector;
import org.ifyounoseyounose.backend.smelldetectors.DuplicateCodeSmellDetector;
import org.ifyounoseyounose.backend.smelldetectors.SmellDetector;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArrowheadedIndentationTest {

    @Test
    public void detectSmells() {
        SmellDetectorManager s = new SmellDetectorManager();

        List<File> l = new ArrayList<>();
        l.add(new File("./src/test/java/smellycodedirectory/DuplicatedCode.java"));
        l.add(new File("./src/test/java/smellycodedirectory/SmellyArrowheadedIndentation.java"));


        HashMap<String, Integer> smellDetectorObjects = new HashMap<>();
        smellDetectorObjects.put("ArrowHeaded", 3);

        s.detectSmells(smellDetectorObjects, l);
        assert(true);
    }
}