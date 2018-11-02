package com.gioaudino.thesis;

public class Cost {
    long cost;
    Partition.Algorithm algorithm;

    @Override
    public String toString() {
        return "{cost w/ " + algorithm + '}';
    }
}
