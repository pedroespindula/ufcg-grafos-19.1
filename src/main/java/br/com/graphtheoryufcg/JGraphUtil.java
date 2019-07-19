package br.com.graphtheoryufcg;

import org.jgrapht.Graph;
import org.jgrapht.GraphMapping;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.cycle.PatonCycleBase;
import org.jgrapht.alg.isomorphism.VF2SubgraphIsomorphismInspector;
import org.jgrapht.alg.scoring.BetweennessCentrality;
import org.jgrapht.alg.scoring.ClosenessCentrality;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.generate.ComplementGraphGenerator;
import org.jgrapht.generate.CompleteGraphGenerator;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.io.*;
import org.jgrapht.util.SupplierUtil;

import java.io.*;
import java.util.*;

public class JGraphUtil {

    /**
     * Creates and returns a complementary graph based on one passed as parameter.
     * @param graph the graph that will be base for the complementary graph.
     * @return the complementary graph.
     */
    public static Graph<String, DefaultEdge> createCompGraph(Graph<String, DefaultEdge> graph) {
        Graph<String, DefaultEdge> compGraph = createSimpleGraph();
        ComplementGraphGenerator<String, DefaultEdge> complementGraphGenerator = new ComplementGraphGenerator<>(graph);
        complementGraphGenerator.generateGraph(compGraph);
        return compGraph;
    }

    // Importation related

    /**
     * Imports a graph from a GML file.
     * @param path the file path to the GML.
     * @return the builded graph.
     */
    public static Graph importGraphGML(String path) {
      return importGraph(createSimpleGraph(), path, createDefaultGMLImporter());
    }

    /**
     * Creates and returns a default GML importer.
     * @return the gml importer.
     */
    private static GmlImporter<String, DefaultEdge> createDefaultGMLImporter() {
        return new GmlImporter<>(getAttributeVertexStringProvider("label"), getDefaultEdgeProvider());
    }

    /**
     * Imports a graph from a CSV file that contains an adjacency matrix.
     * @param filePath the file path to the CSV.
     * @return the graph.
     */
    public static Graph<String, DefaultEdge> importGraphFromCSVMatrix(String filePath) {
        return importGraph(createSimpleGraph(), filePath, createDefaultCSVMatrixImporter());
    }

    /**
     * Creates and returns the default CSVMatrixImporter.
     * @return the CSVMatrixImporter with default values
     */
    private static CSVImporter<String, DefaultEdge> createDefaultCSVMatrixImporter() {
        CSVImporter<String, DefaultEdge> csvImporter = createDefaultCSVImporter();
        setDefaultCSVImporterMatrixValues(csvImporter);
        return csvImporter;
    }

    /**
     * Imports a graph from a CSV file using a CSVImporter and a base graph that were given.
     * @param baseGraph the base graph to be written.
     * @param filePath the filepath.
     * @param importer the importer.
     * @return the base graph after receiving the importation.
     */
    private static Graph<String, DefaultEdge> importGraph(Graph<String, DefaultEdge> baseGraph, String filePath, GraphImporter<String, DefaultEdge> importer) {
        try {
            importer.importGraph(baseGraph, createStringReader(filePath));
        } catch (ImportException e) {
            throw new RuntimeException("Importação do grafo falhou: " + e.getMessage());
        }
        return baseGraph;
    }

    /**
     * Creates and returns a String reader from a file.
     * @param filename the file path.
     * @return the String Reader.
     */
    private static Reader createStringReader(String filename) {
        StringBuilder contentBuilder = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("A leitura do arquivo falhou: " + e.getMessage());
        }

        return new StringReader(contentBuilder.toString());
    }

    /**
     * Set default CSVImporter Matrix values on the CSVImporter that was given.
     * @param csvImporter the csv Importer
     */
    private static void setDefaultCSVImporterMatrixValues(CSVImporter<String, DefaultEdge> csvImporter) {
        csvImporter.setFormat(CSVFormat.MATRIX);
        csvImporter.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_NODEID, true);
        csvImporter.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_ZERO_WHEN_NO_EDGE, false);
        csvImporter.setParameter(CSVFormat.Parameter.EDGE_WEIGHTS, false);
    }

    // Default providers

    /**
     * Returns a String vertex provider that only returns the label.
     * @return the default vertex provider.
     */
    private static VertexProvider<String> getDefaultVertexStringProvider() {
        return (label, attributes) -> attributes.get("label").getValue();
    }

    /**
     * Returns a String vertex provider that is associated with an attribute.
     * @param attribute the attribute key.
     * @return the attribute value.
     */
    private static VertexProvider<String> getAttributeVertexStringProvider(String attribute) {
        return (label, attributes) -> attributes.get(attribute).getValue();
    }

    /**
     * Returns a DefaultEdge edge provider that only return a new DefaultEdge.
     * @return the default edge provider.
     */
    private static EdgeProvider<String, DefaultEdge> getDefaultEdgeProvider() {
        return (from, to, label, attributes) -> new DefaultEdge();
    }

    /**
     * Creates and returns a default CSVImporter that uses the default vertex provider
     * and the default edge provider.
     * @return the default CSVImporter.
     */
    private static CSVImporter<String, DefaultEdge> createDefaultCSVImporter() {
        return new CSVImporter<>(getDefaultVertexStringProvider(), getDefaultEdgeProvider());
    }

    /**
     * Creates and returns a simple graph.
     * @return a new simple graph.
     */
    public static SimpleGraph<String, DefaultEdge> createSimpleGraph() {
        return new SimpleGraph<>(DefaultEdge.class);
    }

    /**
     * Creates a complete graph.
     * @param size the complete graph size.
     * @return the complete graph.
     */
    public static Graph<String, DefaultEdge> createCompleteGraph(int size) {
        Graph<String, DefaultEdge> completeGraph = new SimpleGraph<>(SupplierUtil.createStringSupplier(), SupplierUtil.createDefaultEdgeSupplier(), false);

        new CompleteGraphGenerator<String, DefaultEdge>(size).generateGraph(completeGraph);

        return completeGraph;
    }

    /**
     * Creates and returns a set from a GraphMapping.
     * @param mapping the mapping.
     * @param subgraph the subgraph.
     * @return the set created.
     */
    public static Set<String> createSetFromMapping(GraphMapping<String, DefaultEdge> mapping, Graph<String, DefaultEdge> subgraph) {
        Set<String> set = new HashSet<>();

        for (String vertex : subgraph.vertexSet()) {
            set.add(mapping.getVertexCorrespondence(vertex, false));
        }

        return set;
    }

    /**
     * Get all isomorphic vertex sets from a GraphMapping iterator.
     * @param iterator the graphMapping iterator.
     * @param subgraph the subgraph.
     * @return the sets of vertex sets
     */
    public static Set<Set<String>> getIsomorphicSets(Iterator<GraphMapping<String, DefaultEdge>> iterator, Graph<String, DefaultEdge> subgraph) {
        Set<Set<String>> blocks = new HashSet<>();

        while (iterator.hasNext()) {
            blocks.add(createSetFromMapping(iterator.next(), subgraph));
        }
        return blocks;
    }

    /**
     * Get an iterator of GraphMapping that contains all isomorphic subgraphs of the given one.
     * @param graph the base graph that will be search on.
     * @param subgraph the subgraph that will be used as search parameter.
     * @return the iterator of graphMapping.
     */
    public static Iterator<GraphMapping<String, DefaultEdge>> getSubgraphIsomorphisms(Graph<String, DefaultEdge> graph, Graph<String, DefaultEdge> subgraph) {
        return new VF2SubgraphIsomorphismInspector<>(graph, subgraph).getMappings();
    }

    /**
     * Prints all items of a collection.
     * @param collection the collection.
     */
    public static void printCollection(Collection collection) {
        for (Object o : collection) {
            System.out.println(o);
        }
    }

    /**
     * Return a random vertex from a set.
     * @param set the set.
     * @return the element.
     */
    public static String getRandomElement(Set<String> set) {
        int randomIndex = (int) Math.round(Math.random() * (set.size() - 1));

        return (String) set.toArray()[randomIndex];
    }

    /**
     * Returns the distance of the shortest distance between two nodes in a graph.
     * @param graph the graph.
     * @param source the source node.
     * @param target the target node.
     * @return the distance.
     */
    public static int getDistance(Graph<String, DefaultEdge> graph, String source, String target) {
        int result = 0;

        if (!source.equals(target)) {
            result = new DijkstraShortestPath<>(graph).getPath(source, target).getLength();
        }

        return result;
    }

    /**
     * Generate a tree
     * @param graph the graph given.
     * @param rootNode the root node.
     * @return the tree graph.
     */
    public static DefaultDirectedGraph<String, DefaultEdge> generateTree(Graph<String, DefaultEdge> graph, String rootNode) {
        if (!isTree(graph)) {
            throw new IllegalArgumentException("Grafo nao pode ser convertido em arvore");
        }

        DefaultDirectedGraph<String, DefaultEdge> result = new DefaultDirectedGraph<>(DefaultEdge.class);

        Graphs.addAllVertices(result, graph.vertexSet());

        for (DefaultEdge e : graph.edgeSet()) {
            String source = graph.getEdgeSource(e);
            String target = graph.getEdgeTarget(e);

            if (getDistance(graph, rootNode, source) < getDistance(graph, rootNode, target)) {
                result.addEdge(source, target);
            } else {
                result.addEdge(target, source);
            }
        }

        return result;
    }

    /**
     * Checks if the graph given is a tree.
     * @param graph the graph.
     * @return if it is a tree.
     */
    public static boolean isTree(Graph<String, DefaultEdge> graph) {
        ConnectivityInspector<String, DefaultEdge> connectivityInspector = new ConnectivityInspector<>(graph);
        boolean acyclic = new PatonCycleBase<>(graph).getCycleBasis().getLength() == 0;

        return connectivityInspector.isConnected() && acyclic;
    }

    /**
     * Print the tree with a given root.
     * @param tree the tree graph.
     * @param root the root node.
     */
    public static void printTree(DefaultDirectedGraph<String, DefaultEdge> tree, String root) {
        Set<Integer> separators = new HashSet<>();
        printNode(tree, root, 0, separators);
        System.out.println();
    }

    private static void printNode(DefaultDirectedGraph<String, DefaultEdge> tree, String node, int spaces, Set<Integer> sep) {
        System.out.println(node);
        Iterator<String> it = tree.outgoingEdgesOf(node)
                .stream()
                .map(tree::getEdgeTarget)
                .iterator();

        while (it.hasNext()) {
            String out = it.next();

            for (int i = 0; i < spaces; i++) {
                String separator = sep.contains(i) ? "│ " : "  ";
                System.out.print(separator);
            }

            if (it.hasNext()) {
                sep.add(spaces);
                System.out.print("├─");
            } else {
                sep.remove(spaces);
                System.out.print("└─");
            }
            printNode(tree, out, spaces + 1, sep);
        }
    }

    /**
     * Calculate the betweeness centrality to all nodes of a graph.
     * @param graph the graph.
     * @return the scores.
     */
    public static Map<String, Double> getBetweenessCentrality(Graph<String, DefaultEdge> graph) {
        return new BetweennessCentrality<>(graph).getScores();
    }

    /**
     * Calculate the closeness centrality to all nodes of a graph.
     * @param graph the graph.
     * @return the scores.
     */
    public static Map<String, Double> getClosenessCentrality(Graph<String, DefaultEdge> graph) {
      return new ClosenessCentrality<>(graph).getScores();
    }

    /**
     * Calculates the assortativity coefficient of a graph
     * @param graph the graph.
     * @param <V> the vertex class.
     * @param <E> the edge class.
     * @return the assortativy coefficient.
     */
    public static <V,E> double assortativityCoefficient (Graph <V, E> graph) {
        double edgeCount = graph.edgeSet().size();
        double n1 = 0;
        double n2 = 0;
        double dn = 0;

        for (E e : graph.edgeSet()) {
            int d1 = graph.degreeOf(graph.getEdgeSource(e));
            int d2 = graph.degreeOf(graph.getEdgeTarget(e));

            n1 += d1 * d2;
            n2 += d1 + d2;
            dn += d1 * d1 + d2 * d2;
        }

        n1 /= edgeCount;
        n2 = (n2 / (2 * edgeCount)) * (n2 / (2 * edgeCount));
        dn /= (2 * edgeCount);

        return (n1 - n2) / (dn - n2);
    }

}

