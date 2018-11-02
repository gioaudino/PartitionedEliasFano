package com.gioaudino.thesis;

public class StepInPath {
    long weight;
    Integer from = null;
    Partition.Algorithm how;

    @Override
    public String toString() {
        return "StepInPath{" +
                "weight=" + weight +
                ", from=" + from +
                '}';
    }
}
