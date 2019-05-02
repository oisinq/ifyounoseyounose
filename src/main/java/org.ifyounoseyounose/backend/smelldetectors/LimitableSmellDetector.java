package org.ifyounoseyounose.backend.smelldetectors;

/**
 * LimitableSmellDetector - class extended by any SmellDetector that has a "limit" of some kind
 */
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
