package com.gioaudino.thesis;

import it.unimi.dsi.webgraph.ArrayListMutableGraph;
import it.unimi.dsi.webgraph.ImmutableGraph;
import it.unimi.dsi.webgraph.PEFGraph;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class PEFGraphTest {
    private static final String FILENAME = "pef-temp";
    private static final String REAL_GRAPH = "/users/gioaudino/tesi/peftest/uk-2007-05@100000";


    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private String ABSOLUTE_FILENAME;

    @Before
    public void setUp() {
        ABSOLUTE_FILENAME = folder.getRoot().getAbsolutePath() + "/" + FILENAME;
    }


    @Test
    public void testPEF() throws IOException {
        ArrayListMutableGraph g = new ArrayListMutableGraph(6, new int[][]{
                {0, 0}, {0, 1}, {0, 2}, {0, 3},
                {1, 0}, {1, 2}, {1, 3}, {1, 5},
                {2, 4},
                {3, 4}, {3, 5},
                {4, 2}, {4, 4}, {4, 5},
                {5, 0}, {5, 5}
        });

        PEFGraph.store(g.immutableView(), ABSOLUTE_FILENAME);
        PEFGraph graph = PEFGraph.load(ABSOLUTE_FILENAME);

        assertEquals(g.immutableView(), graph);
    }

    @Test
    public void testBiggerPEF() throws IOException {
        ArrayListMutableGraph g = new ArrayListMutableGraph(9, new int[][]{
                {0, 0}, {0, 1}, {0, 2}, {0, 3},
                {1, 0}, {1, 2}, {1, 3}, {1, 5},
                {2, 4}, {2, 7}, {2, 8},
                {3, 4}, {3, 5}, {3, 6},
                {4, 2}, {4, 4}, {4, 5},
                {5, 0}, {5, 5},
                {6, 0}, {6, 2}, {6, 3},
                // no successors for node 7
                {8, 0}, {8, 4}, {8, 8}

        });

        PEFGraph.store(g.immutableView(), ABSOLUTE_FILENAME);
        PEFGraph graph = PEFGraph.load(ABSOLUTE_FILENAME);

        assertEquals(g.immutableView(), graph);
    }

    @Test
    public void testOnRealGraph() throws IOException {
        ImmutableGraph or = ImmutableGraph.load(REAL_GRAPH);
        PEFGraph.store(or, ABSOLUTE_FILENAME);
        PEFGraph pef = PEFGraph.load(ABSOLUTE_FILENAME);
        assertEquals(or, pef);
    }
}
