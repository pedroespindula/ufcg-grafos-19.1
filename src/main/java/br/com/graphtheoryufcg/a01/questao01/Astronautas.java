package br.com.graphtheoryufcg.a01.questao01;

import org.jgrapht.Graph;
import org.jgrapht.GraphMapping;
import org.jgrapht.alg.isomorphism.VF2SubgraphIsomorphismInspector;
import org.jgrapht.generate.ComplementGraphGenerator;
import org.jgrapht.generate.CompleteGraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.io.*;
import org.jgrapht.util.SupplierUtil;

import java.io.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

class JGraphUtil {

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
     * @param csvImporter the csv importer.
     * @return the base graph after receiving the importation.
     */
    private static Graph<String, DefaultEdge> importGraph(Graph<String, DefaultEdge> baseGraph, String filePath, CSVImporter<String, DefaultEdge> csvImporter) {
        try {
            csvImporter.importGraph(baseGraph, createStringReader(filePath));
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
        return (label, attributes) -> label;
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
     * Prints all items of a collection
     * @param collection the collection.
     */
    public static void printCollection(Collection collection) {
        for (Object o : collection) {
            System.out.println(o);
        }
    }
}
