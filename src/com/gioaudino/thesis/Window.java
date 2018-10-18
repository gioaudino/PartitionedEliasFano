package com.gioaudino.thesis;

public class Window {
    int[] values;
    long maxCost;
    int start = 0;
    int end = 0;
    int minP;
    int maxP = 0;


    public Window(int[] values, long maxCost) {
        this.values = values;
        this.maxCost = maxCost;
        this.minP = this.getStart();
    }

    public long universe() {
        return this.maxP - this.minP + 1;
    }

    public long size() {
        return this.end - this.start;
    }

    public void advanceStart() {
        minP = getStart() + 1;
        ++this.start;
    }

    public void advanceEnd() {
        maxP = getEnd();
        this.end++;
    }

    public long getCost() {
        return getCost(this.size(), this.universe());
    }

    public long getCost(long size, long universe) {
        return CostEvaluation.evaluateCost(universe, size);
    }

    public int getStart() {
        return this.values[this.start];
    }

    public int getEnd() {
        return this.values[this.end];
    }

    @Override
    public String toString() {
        return "Window{" +
                "currCost=" + this.getCost() +
                ", maxCost=" + maxCost +
                ", start=" + start +
                ", end=" + end +
                ", v.start=" + values[start] +
                ", v.end=" + values[end] +
                ", minP=" + minP +
                ", maxP=" + maxP +
                ", u=" + this.universe() +
                ", m=" + this.size() +
                '}';
    }
}
