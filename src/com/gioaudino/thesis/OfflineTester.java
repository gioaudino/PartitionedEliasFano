package com.gioaudino.thesis;

import it.unimi.dsi.webgraph.ImmutableGraph;
import it.unimi.dsi.webgraph.NodeIterator;

import java.io.IOException;
import java.util.Arrays;

public class OfflineTester {
    public static void main(String[] args) throws IOException {
        System.out.println("Loading graph 1 <" + args[0] + ">");
        ImmutableGraph graph = ImmutableGraph.load(args[0]);
        System.out.println("Loading graph 2 <" + args[1] + ">");
        ImmutableGraph graph2 = ImmutableGraph.load(args[1]);

        System.out.println("Nodes:\n\tg1: " + graph.numNodes() + " - g2: " + graph2.numNodes());

        System.out.println(Arrays.toString(graph.successorArray(0)));
        System.out.println(Arrays.toString(graph2.successorArray(0)));
//        check(graph, graph2);

        System.out.println(String.format("Graphs <%s> and <%s> are %s", args[0], args[1], graph.equals(graph2) ? "EQUAL" : "NOT EQUAL"));
    }

    static void check(ImmutableGraph a, ImmutableGraph b) {
        int n = a.numNodes();
        if (n != b.numNodes()) {
            System.out.println("NODES");
        } else {
            NodeIterator i = a.nodeIterator();
            NodeIterator j = b.nodeIterator();

            while (n-- != 0) {
                int in = i.nextInt();
                int jn = j.nextInt();
                int d, c;
                if ((d = i.outdegree()) != j.outdegree()) {
                    System.out.println("OUTDEGREES: A: " + d + " B: " + j.outdegree());
                }

                int[] s = i.successorArray();
                int[] t = j.successorArray();
                c = d;
                while (d-- != 0) {
                    if (s[d] != t[d]) {
                        System.out.println("SUCCESSOR " + (c - d) + " of node: " + in + " A: " + s[d] + " B: " + t[d]);
                    }
                }
            }
        }

    }

}
