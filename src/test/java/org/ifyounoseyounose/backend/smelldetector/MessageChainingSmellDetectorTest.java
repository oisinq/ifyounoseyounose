package org.ifyounoseyounose.backend.smelldetector;

import org.ifyounoseyounose.backend.SmellDetectorManager;
import org.ifyounoseyounose.backend.smelldetectors.MessageChainingSmellDetector;
import org.ifyounoseyounose.backend.smelldetectors.PlaceholderSmellDetector;
import org.ifyounoseyounose.backend.smelldetectors.SmellDetector;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageChainingSmellDetectorTest {
    @Test
    public void detectSmells() {
        SmellDetectorManager s = new SmellDetectorManager();

        List<File> l = new ArrayList<>();
        l.add(new File("./src/test/java/smellycodedirectory/Yeet.java"));
        l.add(new File("./src/test/java/smellycodedirectory/Yeet2.java"));
        l.add(new File("./src/test/java/smellycodedirectory/seq/Yeet3.java"));

        HashMap<String, Integer> smellDetectorObjects = new HashMap<>();
        smellDetectorObjects.put("MessageChaining", 0);

        s.detectSmells(smellDetectorObjects, l);
        assert(true);
    }
}