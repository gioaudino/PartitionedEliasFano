package com.gioaudino.thesis;

public class DoubleBoundWindow {
    private int[] values;
    long maxCost;
    private int start = 0;
    int end = 0;


    DoubleBoundWindow(int[] values, long maxCost) {
        this.values = values;
        this.maxCost = maxCost;
    }

    private long universe() {
        return this.values[this.end - 1] - this.values[this.start] + 1;
    }

    private long size() {
        return this.end - this.start;
    }

    void advanceStart() {
        ++this.start;
    }

    void advanceEnd() {
        this.end++;
    }

    long getCost() {
        return getCost(this.size(), this.universe());
    }

    private long getCost(long size, long universe) {
        return CostEvaluation.evaluateCost(universe, size, true).cost;
    }

    private int getStart() {
        return this.values[this.start];
    }

    private int getEnd() {
        return this.values[this.end];
    }

    @Override
    public String toString() {
        return "Window{" +
                "currCost=" + (this.size() > 0 ?this.getCost() : " n.a.") +
                ", maxCost=" + maxCost +
                ", start=" + start +
                ", end=" + end +
                ", v.start=" + values[start] +
                ", v.end=" + values[end] +
                ", u=" + this.universe() +
                ", m=" + this.size() +
                '}';
    }
}
