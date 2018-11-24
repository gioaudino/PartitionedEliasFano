package com.gioaudino.thesis;

public class ImprovedWindow {
    private int[] values;
    long maxCost;
    private int start = 0;
    int end = 0;
    private final short log2Quantum;

    ImprovedWindow(int[] values, long maxCost, int log2Quantum) {
        this.values = values;
        this.maxCost = maxCost;
        this.log2Quantum = (short) log2Quantum;
    }

    private long universe() {
        return this.values[this.end - 1] - this.values[this.start] + 1;
    }

    private long size() {
        return this.end - this.start;
    }

    void advanceStart() {
        this.start++;
    }

    void advanceEnd() {
        this.end++;
    }

    long getCost() {
        return getCost(this.size(), this.universe());
    }

    private long getCost(long size, long universe) {
        return CostEvaluation.evaluateCost(universe, size, log2Quantum, true).cost;
    }

    @Override
    public String toString() {
        return "Window{" +
                "currCost=" + (this.size() > 0 ? this.getCost() : " n.a.") +
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
