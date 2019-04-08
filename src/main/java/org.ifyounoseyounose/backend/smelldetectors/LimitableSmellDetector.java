package org.ifyounoseyounose.backend.smelldetectors;

public class LimitableSmellDetector extends SmellDetector {
    private int limit = 0;

    LimitableSmellDetector(int limit) {
        setLimit(limit);
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
