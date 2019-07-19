package br.com.graphtheoryufcg.a01.questao01;

import br.com.graphtheoryufcg.JGraphUtil;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public class Astronautas {

    /**
     * Imports a graph from a file path, gets its complementary graph and then prints
     * all its edges.
     * @param args NOT USED.
     */
    public static void main(String[] args) {
        final String PATH_TO_CSV = "./input/a01/q01-example.csv";

        Graph<String, DefaultEdge> graph = JGraphUtil.importGraphFromCSVMatrix(PATH_TO_CSV);
        Graph<String, DefaultEdge> compGraph = JGraphUtil.createCompGraph(graph);

        JGraphUtil.printCollection(compGraph.edgeSet());
    }
}

