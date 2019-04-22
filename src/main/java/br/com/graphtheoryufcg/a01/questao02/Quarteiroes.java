package br.com.graphtheoryufcg.a01.questao02;

import org.jgrapht.Graph;
import org.jgrapht.GraphMapping;
import org.jgrapht.alg.isomorphism.VF2GraphIsomorphismInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import java.util.Iterator;

import static br.com.graphtheoryufcg.JGraphUtil.*;

public class Quarteiroes {

    public static void main(String[] args) {
        final String PATH_TO_CSV = "./input/a01/q02.csv";

        Graph<String, DefaultEdge> graph = importGraphFromCSVMatrix(PATH_TO_CSV);

        Graph<String, DefaultEdge> result = findCityBlock(graph);
        printGraph(result);
    }

    public static Graph<String, DefaultEdge> findCityBlock(Graph<String, DefaultEdge> in) {
        Graph<String, DefaultEdge> triangleGraph = createTriangleGraph();

        Graph<String, DefaultEdge> letras = new SimpleGraph<>(DefaultEdge.class);

        triangleGraph.addVertex("1");
        triangleGraph.addVertex("2");
        triangleGraph.addVertex("3");

        triangleGraph.addEdge("a", "b");
        triangleGraph.addEdge("a", "c");
        triangleGraph.addEdge("c", "b");

        VF2GraphIsomorphismInspector<String, DefaultEdge> isomorphismInspector = new VF2GraphIsomorphismInspector<>(letras, triangleGraph);

        Iterator<GraphMapping<String, DefaultEdge>> iterator = isomorphismInspector.getMappings();

        while (iterator.hasNext()) {
            System.out.println(iterator.next() + " !");
        }

        return in;
    }

    private static Graph<String, DefaultEdge> createTriangleGraph() {
        Graph<String, DefaultEdge> triangleGraph = new SimpleGraph<>(DefaultEdge.class);

        triangleGraph.addVertex("1");
        triangleGraph.addVertex("2");
        triangleGraph.addVertex("3");

        triangleGraph.addEdge("1", "2");
        triangleGraph.addEdge("1", "3");
        triangleGraph.addEdge("3", "2");

        printGraph(triangleGraph);
        return triangleGraph;
    }
}
