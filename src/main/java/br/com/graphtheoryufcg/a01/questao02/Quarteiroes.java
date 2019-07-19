package br.com.graphtheoryufcg.a01.questao02;

import br.com.graphtheoryufcg.JGraphUtil;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.Set;

public class Quarteiroes {

    /**
     * Imports a graph from a CSV file, gets all subgraph isomorphisms between the graph from the file
     * and a complete graph of size 3 and then prints it all.
     * @param args NOT USED.
     */
    public static void main(String[] args) {
        final String PATH_TO_CSV = "./input/a01/q02-example.csv";

        Graph<String, DefaultEdge> graph = JGraphUtil.importGraphFromCSVMatrix(PATH_TO_CSV);

        Graph<String, DefaultEdge> triangleGraph = JGraphUtil.createCompleteGraph(3);
        Set<Set<String>> inseparableBlocks = JGraphUtil.getIsomorphicSets(JGraphUtil.getSubgraphIsomorphisms(graph, triangleGraph), triangleGraph);

        JGraphUtil.printCollection(inseparableBlocks);
    }
}
