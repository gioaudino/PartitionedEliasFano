package com.gioaudino.thesis;

import it.unimi.dsi.webgraph.ImmutableGraph;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class PartitionedEliasFano {

    private static final int DEFAULT_SAMPLES = 1000;

    private final static DecimalFormat df3 = new DecimalFormat("0.###");
    private static int count = 0;

    public static void main(String... args) throws IOException {
        if (args.length < 1) {
            System.err.println("USAGE: " + PartitionedEliasFano.class.getName() + " <basename> [<samples>]");
            System.exit(1);
        }
        ImmutableGraph graph = ImmutableGraph.load(args[0]);
        if (args.length > 1 && args[1].equals("--all")) {
            runForAll(graph);
        }
        final int SAMPLES = args.length >= 2 ? Integer.parseInt(args[1]) : DEFAULT_SAMPLES;

        Random random = new Random();
        for (int i = 0; i < SAMPLES; i++) {
            int node = random.nextInt(graph.numNodes());
            while (graph.outdegree(node) < 100)
                node = random.nextInt(graph.numNodes());
            int[] successors = graph.successorArray(node);

//            if (successors.length > graph.outdegree(node))
            successors = Arrays.copyOfRange(successors, 0, graph.outdegree(node));
            System.out.println("Testing on " + args[0] + " on node " + node + " with out-degree " + successors.length);

            new ParallelRun(successors, node, i).run();
        }
        System.err.println(PartitionedEliasFano.count + " out of " + SAMPLES + " broke the upper bound - " + df3.format((double) PartitionedEliasFano.count / SAMPLES * 100) + "%");
    }

    private static void runForAll(ImmutableGraph graph) {
        for (int node = 0; node < graph.numNodes(); node++) {
            if (graph.outdegree(node) > 0) {
                int[] successors = graph.successorArray(node);
                successors = Arrays.copyOfRange(successors, 0, graph.outdegree(node));
                System.out.println("Testing on node " + node + " with out-degree " + successors.length);
                new ParallelRun(successors, node, node).run();
            }
        }
    }

    private static class ParallelRun extends Thread {

        private final int[] listToCompress;
        private final int node;
        private final int i;

        private ParallelRun(int[] listToCompress, int node, int i) {
            this.listToCompress = listToCompress;
            this.node = node;
            this.i = i;
        }

        @Override
        public void run() {
            try {
                boolean broke = PartitionedEliasFano.run(this.listToCompress, this.node);
                if (broke) {
                    System.err.println("Run " + this.i + " on node " + this.node + " with out-degree " + this.listToCompress.length + " broke the upper bound");
                    PartitionedEliasFano.count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                for (int j = 0; j < this.listToCompress.length; j++) {
                    System.out.print(this.listToCompress[j]);
                    if (j + 1 < this.listToCompress.length)
                        System.out.print(", ");
                }
                System.out.println();
            }
        }
    }

    static boolean run(int[] list, int node) {
        List<Integer> listShortestPath = getShortestPath(list);
        ApproximatedPartition opt = new ApproximatedPartition(list);
        List<Integer> windowSP = opt.createApproximatedPartition();

        DoubleBoundApproximatedPartition dbap = new DoubleBoundApproximatedPartition(list);
        List<Integer> doubleBound = dbap.createDoubleBoundApproximatedPartition();

        System.out.println("Graph and Window are equal? " + listShortestPath.equals(windowSP));

        Iterator<Integer> iter = listShortestPath.iterator();
        System.out.print("\nExact partition:\t\t\t\t\t\t");
        while (iter.hasNext()) {
            System.out.print(iter.next());
            if (iter.hasNext())
                System.out.print("\t->\t");
        }

        System.out.print("\nApproximated partition:\t\t\t\t\t");
        iter = windowSP.iterator();
        while (iter.hasNext()) {
            System.out.print(iter.next());
            if (iter.hasNext())
                System.out.print("\t->\t");
        }

        System.out.print("\nApproximated partition w/ left bound:\t");
        iter = doubleBound.iterator();
        while (iter.hasNext()) {
            System.out.print(iter.next());
            if (iter.hasNext())
                System.out.print("\t->\t");
        }

        boolean brokeLimit = printStats(node, list, listShortestPath, opt, dbap);

        System.out.println("\n-----------------------------------\n");
        return brokeLimit;
    }

    private static boolean printStats(int node, int[] list, List<Integer> graphShortestPath, ApproximatedPartition ap, DoubleBoundApproximatedPartition dbap) {
        long pefCost = getCompressionCost(graphShortestPath, list);
        long efCost = CostEvaluation.eliasFanoCompressionCost(list[list.length - 1] + 1, list.length);

        System.out.println("\nCost of compression: " + pefCost + " bits");
        System.out.println("EF cost: " + efCost + " bits");
        System.out.println("PartitionedEliasFano / EF = " + df3.format(((float) pefCost / efCost * 100)) + "%");

        long upperBound = (long) ((1 + ApproximatedPartition.EPS_1) * (1 + ApproximatedPartition.EPS_2) * pefCost);

        System.out.println("Upper bound for Approximated Partition cost: " + df3.format(upperBound));
        System.out.println("Approximated Partition cost: " + ap.getCost() + " bits");
        System.out.println("(1+ε1)(1+ε2) = " + df3.format((1 + ApproximatedPartition.EPS_1) * (1 + ApproximatedPartition.EPS_2)));
        System.out.println("Approximated Partition / PartitionedEliasFano = " + df3.format((double) ap.getCost() / pefCost));

        System.out.println("\nApproximated partition with left bound for universe cost: " + dbap.getCost() + " bits");
        System.out.println("Double bound approximated partition / Partitioned Elias Fano = " + df3.format((double) dbap.getCost() / pefCost));
        System.out.println("Double bound approximated partition / Standard Approximated Partition = " + df3.format((double) dbap.getCost() / ap.getCost()));

        System.out.println("\nfirst: " + list[0] + " - last: " + list[list.length - 1]);
        boolean brokeLimit = (ap.getCost() > upperBound);

        if (brokeLimit) {
            System.err.println("NODE " + node);
            for (int i = 0; i < list.length; i++) {
                System.out.print(i + (i + 1 < list.length ? ", " : ""));
            }
        }
        System.out.println();
        return brokeLimit;
    }


    private static List<Integer> getShortestPath(int[] list) {
        StepInPath[] steps = new StepInPath[list.length + 1];

        final long maxWeight = CostEvaluation.evaluateCost(list[list.length - 1] + 1, list.length);

        for (int i = 0; i < steps.length; i++) {
            steps[i] = new StepInPath();
            steps[i].weight = maxWeight;
        }
        steps[0].weight = 0L;

        for (int i = 1; i < steps.length; i++) {
            steps[i].from = 0;
            steps[i].weight = getWeight(list, 0, i);
        }

        for (int i = 1; i < list.length; i++) {
            for (int j = i + 1; j < list.length + 1; j++) {
                long intervalWeight = getWeight(list, i, j);
                if (steps[j].weight > steps[i].weight + intervalWeight) {
                    steps[j].weight = steps[i].weight + intervalWeight;
                    steps[j].from = i;
                }
            }
        }

        LinkedList<Integer> result = new LinkedList<>();
        int n = steps.length - 1;
        while (true) {
            result.addFirst(n);
            if (steps[n].from == null) break;
            n = steps[n].from;

        }
        return result;
    }

    private static long getWeight(int[] list, int from, int to) {
        int universe = list[to - 1] + 1;
            universe -= from > 0 ? list[from - 1] + 1 : list[from];
        int elements = to - from;

        return CostEvaluation.evaluateCost(universe, elements);
    }

    static long getCompressionCost(List<Integer> partition, int[] list) {
        long cost = 0;
        Iterator<Integer> iter = partition.iterator();
        int from = iter.next();
        while (iter.hasNext()) {
            int next = iter.next();
            cost += getWeight(list, from, next);
            from = next;
        }

        return cost;
    }


}
