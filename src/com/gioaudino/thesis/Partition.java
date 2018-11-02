package com.gioaudino.thesis;

public class Partition {

    public int from;
    public int to;
    public Algorithm algorithm;
    public long allCost;

    public Partition(int from, int to, Algorithm algorithm, long allCost) {
        this.from = from;
        this.to = to;
        this.algorithm = algorithm;
        this.allCost = allCost;
    }

    @Override
    public String toString() {
        return "Partition{" +
                "from=" + from +
                ", to=" + to +
                ", algorithm=" + algorithm +
                '}';
    }

    public enum Algorithm {
        NONE,
        BITVECTOR,
        ELIASFANO
    }

}
