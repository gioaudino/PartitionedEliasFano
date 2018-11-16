package com.gioaudino.thesis;

public class Window {
    private int[] values;
    long maxCost;
    private int start = 0;
    int end = 0;
    private int minP;
    private int maxP = 0;
    private final short log2Quantum;


    Window(int[] values, long maxCost, int log2Quantum) {
        this.values = values;
        this.maxCost = maxCost;
        this.log2Quantum = (short) log2Quantum;
        this.minP = this.getStart();
    }

    private long universe() {
        return this.maxP - this.minP + 1;
    }

    private long size() {
        return this.end - this.start;
    }

    void advanceStart() {
        minP = getStart() + 1;
        this.start++;
    }

    void advanceEnd() {
        maxP = getEnd();
        this.end++;
    }

    Cost getCost() {
        return getCost(this.size(), this.universe());
    }

    private Cost getCost(long size, long universe) {
        return CostEvaluation.evaluateCost(universe, size, log2Quantum);
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
                "currCost=" + this.getCost() +
                ", maxCost=" + maxCost +
                ", start=" + start +
                ", end=" + end +
                ", v.start=" + values[start] +
                ", v.end=" + (end < values.length ? values[end] : "--") +
                ", minP=" + minP +
                ", maxP=" + maxP +
                ", u=" + this.universe() +
                ", m=" + this.size() +
                '}';
    }
}
