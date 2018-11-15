package com.gioaudino.thesis;

public class Cost {
    long cost;
    public Partition.Algorithm algorithm;

    @Override
    public String toString() {
        return "{cost w/ " + algorithm + '}';
    }
}
