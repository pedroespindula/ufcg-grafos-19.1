package br.com.graphtheoryufcg.a01.questao02;

import org.jgrapht.Graph;
import org.jgrapht.GraphMapping;
import org.jgrapht.alg.isomorphism.VF2SubgraphIsomorphismInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.*;

import static br.com.graphtheoryufcg.JGraphUtil.*;

public class Quarteiroes {

    public static void main(String[] args) {
        final String PATH_TO_CSV = "./input/a01/q02.csv";

        Graph<String, DefaultEdge> graph = importGraphFromCSVMatrix(PATH_TO_CSV);

        System.out.println(findCityBlocks(graph));
    }

    private static Set<Set<String>> findCityBlocks(Graph<String, DefaultEdge> graph) {
        return buildBlocks(getMappings(graph, createTriangleGraph()));
    }

    private static Set<Set<String>> buildBlocks(Iterator<GraphMapping<String, DefaultEdge>> iterator) {
        Set<Set<String>> blocks = new HashSet<>();

        while (iterator.hasNext()) {
            blocks.add(buildBlock(iterator.next()));
        }
        return blocks;
    }

    private static Iterator<GraphMapping<String, DefaultEdge>> getMappings(Graph<String, DefaultEdge> graph, Graph<String, DefaultEdge> subgraph) {
        return new VF2SubgraphIsomorphismInspector<>(graph, subgraph).getMappings();
    }

    private static Set<String> buildBlock(GraphMapping<String, DefaultEdge> mapping) {
        Set<String> block = new HashSet<>();

        block.add(mapping.getVertexCorrespondence("1", false));
        block.add(mapping.getVertexCorrespondence("2", false));
        block.add(mapping.getVertexCorrespondence("3", false));

        return block;
    }

    private static Graph<String, DefaultEdge> createTriangleGraph() {
        Graph<String, DefaultEdge> triangleGraph = new SimpleGraph<>(DefaultEdge.class);

        triangleGraph.addVertex("1");
        triangleGraph.addVertex("2");
        triangleGraph.addVertex("3");

        triangleGraph.addEdge("1", "2");
        triangleGraph.addEdge("1", "3");
        triangleGraph.addEdge("3", "2");

        return triangleGraph;
    }
}
