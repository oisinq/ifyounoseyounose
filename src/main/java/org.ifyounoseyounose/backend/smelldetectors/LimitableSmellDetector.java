package org.ifyounoseyounose.backend.smelldetectors;

public class LimitableSmellDetector {
    protected int limit;

    LimitableSmellDetector(int limit) {
        setLimit(limit);
    }

    LimitableSmellDetector() {
        limit = 0;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
