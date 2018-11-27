package com.gioaudino.thesis;

public class CostEvaluation {

    private final static long FIXED_COST = 64 + 64 + 64;

    public static Cost evaluateCost(long universe, long size, short log2Quantum) {
        Cost cost = new Cost();
        if (universe == size) {
            cost.cost = FIXED_COST;
            cost.algorithm = Partition.Algorithm.NONE;
            return cost;
        }
        long efCost = eliasFanoCompressionCost(universe, size, log2Quantum);
        long bitVectorCost = universe;
        if (efCost < bitVectorCost) {
            cost.cost = efCost + FIXED_COST;
            cost.algorithm = Partition.Algorithm.ELIASFANO;
            return cost;
        }
        cost.cost = bitVectorCost + FIXED_COST;
        cost.algorithm = Partition.Algorithm.BITVECTOR;
        return cost;

    }

    private static long eliasFanoCompressionCost(long universe, long size, short log2Quantum) {
        long msb = 63 - Long.numberOfLeadingZeros(universe / size);
        long lowerBits = universe > size ? msb : 0;
        long higherBitsLength = size + (universe >> lowerBits) + 2;
        long pointers0 = (higherBitsLength - size) >> log2Quantum;
        long pointerSize = (long) Math.ceil(log2(higherBitsLength));
        long pointers1Offset = pointers0 * pointerSize;
        long higherBitsOffsets = pointers1Offset + pointers0 * pointerSize;
        long lowerBitsOffset = higherBitsOffsets + higherBitsLength;

        return lowerBitsOffset + size * lowerBits;
    }

    private static double log2(double arg) {
        return Math.log(arg) / Math.log(2);
    }
}
