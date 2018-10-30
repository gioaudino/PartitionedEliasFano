package com.gioaudino.thesis;

public class CostEvaluation {

    private final static long FIXED_COST = 64 + 64 + 64;
    private final static long DOUBLE_BOUND_FIXED_COST = FIXED_COST + 64;

    public static long evaluateCost(long universe, long size) {
        return evaluateCost(universe, size, false);
    }

    public static long evaluateCost(long universe, long size, boolean hasDoubleBound) {
        if (universe == size)
            return hasDoubleBound ? DOUBLE_BOUND_FIXED_COST : FIXED_COST;
        long efCost = eliasFanoCompressionCost(universe, size) + 1;
        long bitVectorCost = bitVectorCompressionCost(universe, size) + 1;
        return Math.min(efCost, bitVectorCost) + (hasDoubleBound ? DOUBLE_BOUND_FIXED_COST : FIXED_COST);
    }

    static long eliasFanoCompressionCost(long universe, long size) {
        long msb = 63 - Long.numberOfLeadingZeros(universe / size);
        long lowerBits = universe > size ? msb : 0;
        long higherBitsLength = size + (universe >> lowerBits) + 2;
        short logSampling0 = 9; // ?
        long pointers0 = (higherBitsLength - size) >> logSampling0;
        long pointerSize = (long) Math.ceil(log2(higherBitsLength));
        long baseOffset = 0;
        long pointers0Ofsset = baseOffset;
        long pointers1Offset = pointers0Ofsset + pointers0 * pointerSize;
        long higherBitsOffsets = pointers1Offset + pointers0 * pointerSize;
        long lowerBitsOffset = higherBitsOffsets + higherBitsLength;

        return lowerBitsOffset + size * lowerBits;
    }

    private static long bitVectorCompressionCost(long universe, long size) {
        short logRank1Sampling = 9;
        long rank1Samples = universe >> logRank1Sampling;
        long rank1SampleSize = (long) Math.ceil(log2(size + 1));
        long baseOffset = 0;
        long rank1SamplesOffset = baseOffset;
        long pointer_size = (long) Math.ceil(log2(universe));
        long pointers1 = size >> logRank1Sampling;
        long pointers1Offset = rank1SamplesOffset + rank1Samples * rank1SampleSize;
        long bitsOffset = pointers1Offset + pointers1 * pointer_size;

        return bitsOffset + universe;
    }

    private static double log2(double arg) {
        return Math.log(arg) / Math.log(2);
    }
}
