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

        Set<Set<String>> result = findCityBlocks(graph);

        System.out.println(result.toString());
    }

    private static Set<Set<String>> findCityBlocks(Graph<String, DefaultEdge> in) {
        Graph<String, DefaultEdge> triangleGraph = createTriangleGraph();

        VF2SubgraphIsomorphismInspector<String, DefaultEdge> isomorphismInspector =
                new VF2SubgraphIsomorphismInspector<>(in, triangleGraph);

        Iterator<GraphMapping<String, DefaultEdge>> iterator = isomorphismInspector.getMappings();

        Set<Set<String>> blocks = new HashSet<Set<String>>();

        while (iterator.hasNext()) {
            GraphMapping<String, DefaultEdge> mapping = iterator.next();
            Set<String> block = new HashSet<>();
            block.add(mapping.getVertexCorrespondence("1", false));
            block.add(mapping.getVertexCorrespondence("2", false));
            block.add(mapping.getVertexCorrespondence("3", false));
            blocks.add(block);
        }

        return blocks;
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
