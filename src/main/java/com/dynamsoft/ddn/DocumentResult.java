package com.dynamsoft.ddn;

public class DocumentResult {
    public int confidence;
    public int x1, y1, x2, y2, x3, y3, x4, y4;

    public DocumentResult(int confidence, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        this.confidence = confidence;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
        this.x4 = x4;
        this.y4 = y4;
    }
}
