package com.gioaudino.thesis;

import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.ArrayList;
import java.util.List;

public class ApproximatedPartition {

    final static double EPS_1 = 0.03;
    final static double EPS_2 = 0.3;

    private int[] nodes;
    private int size;
    private IntArrayList partition;
    private long cost;

    public ApproximatedPartition(int[] nodes) {
        this.nodes = nodes;
        this.size = nodes.length;
    }

    public IntArrayList createApproximatedPartition() {
        final long singleBlockCost = CostEvaluation.evaluateCost(this.nodes[this.nodes.length - 1] + 1, this.size); //this.evaluateCost(this.nodes[this.nodes.length - 1], size);
        List<Window> windows = new ArrayList<>();
        long minimumCost = CostEvaluation.evaluateCost(1, 1);
        long costBound = minimumCost;
        while (costBound < minimumCost / EPS_1) {
            windows.add(new Window(nodes, costBound));
            if (costBound > singleBlockCost) break;
            costBound *= 1 + EPS_2;
        }

        StepInPath[] steps = new StepInPath[size + 1];
        for (int i = 0; i < steps.length; i++) {
            steps[i] = new StepInPath();
            steps[i].weight = singleBlockCost;
            steps[i].from = 0;
        }
        steps[0].from = null;
        steps[0].weight = 0L;

        for (int i = 0; i < this.size; i++) {
            int lastEnd = i + 1;
            for (Window window : windows) {
                while (window.end < lastEnd)
                    window.advanceEnd();

                while (true) {
                    long windowCost = window.getCost();
                    if (steps[window.end].weight > steps[i].weight + windowCost) {
                        steps[window.end].weight = steps[i].weight + windowCost;
                        steps[window.end].from = i;
                    }
                    lastEnd = window.end;
                    if (window.end == size || windowCost >= window.maxCost) break;
                    window.advanceEnd();
                }
                window.advanceStart();
            }
        }
        int n = steps.length - 1;
        this.partition = new IntArrayList();
        while (true) {
            this.partition.add(0, n);
            if (steps[n].from == null) break;
            n = steps[n].from;
        }
        this.cost = steps[steps.length - 1].weight;

        return this.partition;

    }

    long getCost() {
        return cost;
    }

}
