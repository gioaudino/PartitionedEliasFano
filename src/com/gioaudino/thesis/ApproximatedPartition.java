package com.gioaudino.thesis;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ApproximatedPartition {

    final static double EPS_1 = 0.03;
    final static double EPS_2 = 0.3;

    public static List<Partition> createApproximatedPartition(int[] nodes) {
        final int size = nodes.length;
        if (size == 0)
            return new ArrayList<>();
        final Cost singleBlock = CostEvaluation.evaluateCost(nodes[nodes.length - 1] - nodes[0] + 1, size);
        final long singleBlockCost = singleBlock.cost;
        List<Window> windows = new ArrayList<>();
        long minimumCost = CostEvaluation.evaluateCost(1, 1).cost;
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
        steps[steps.length-1].how = singleBlock.algorithm;

        for (int i = 0; i < size; i++) {
            int lastEnd = i + 1;
            for (Window window : windows) {
                while (window.end < lastEnd)
                    window.advanceEnd();

                while (true) {
                    Cost windowCost = window.getCost();
                    if (steps[window.end].weight > steps[i].weight + windowCost.cost) {
                        steps[window.end].weight = steps[i].weight + windowCost.cost;
                        steps[window.end].from = i;
                        steps[window.end].how = windowCost.algorithm;
                    }
                    lastEnd = window.end;
                    if (window.end == size || windowCost.cost >= window.maxCost) break;
                    window.advanceEnd();
                }
                window.advanceStart();
            }
        }
        int n = steps.length - 1;
        LinkedList<Partition> partition = new LinkedList<>();
        while (steps[n].from != null) {
            partition.addFirst(new Partition(steps[n].from, n, steps[n].how, steps[steps.length - 1].weight));
//            if (steps[n].from == null) break;
            n = steps[n].from;
        }

        return partition;

    }

}
