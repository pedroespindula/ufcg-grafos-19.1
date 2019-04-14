package br.com.graphtheoryufcg;

import org.jgrapht.Graph;
import org.jgrapht.generate.ComplementGraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.io.*;

import java.io.*;

public class JGraphUtil {

    public static void printEdgesFromGraph(Graph<String, DefaultEdge> graph) {
        for (DefaultEdge defaultEdge : graph.edgeSet()) {
            System.out.println(defaultEdge);
        }
    }

    public static void printGraph(Graph<String, DefaultEdge> graph) {
        System.out.println("Vertices: " + graph.vertexSet());
        System.out.println("Arestas: " + graph.edgeSet());
    }

    public static Graph<String, DefaultEdge> getCompGraph(Graph<String, DefaultEdge> graph) {
        Graph<String, DefaultEdge> compGraph = getSimpleGraph();
        ComplementGraphGenerator<String, DefaultEdge> complementGraphGenerator = new ComplementGraphGenerator<>(graph);
        complementGraphGenerator.generateGraph(compGraph);
        return compGraph;
    }


    public static Graph<String, DefaultEdge> importGraphFromCSVMatrix(String filePath) {
        return importGraph(getSimpleGraph(), filePath, getDefaultCSVMatrixImporter());
    }

    private static CSVImporter<String, DefaultEdge> getDefaultCSVMatrixImporter() {
        CSVImporter<String, DefaultEdge> csvImporter = getDefaultCSVImporter();
        setDefaultCSVImporterMatrixValues(csvImporter);
        return csvImporter;
    }

    private static Graph<String, DefaultEdge> importGraph(Graph<String, DefaultEdge> graph, String filePath, CSVImporter<String, DefaultEdge> csvImporter) {
        try {
            csvImporter.importGraph(graph, getStringReader(filePath));
        } catch (ImportException e) {
            throw new RuntimeException("Importação do grafo falhou: " + e.getMessage());
        }
        return graph;
    }

    private static Reader getStringReader(String filename) {
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

    private static void setDefaultCSVImporterMatrixValues(CSVImporter<String, DefaultEdge> csvImporter) {
        csvImporter.setFormat(CSVFormat.MATRIX);
        csvImporter.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_NODEID, true);
        csvImporter.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_ZERO_WHEN_NO_EDGE, false);
        csvImporter.setParameter(CSVFormat.Parameter.EDGE_WEIGHTS, false);
    }

    private static VertexProvider<String> getDefaultVertexStringProvider() {
        return (label, attributes) -> label;
    }

    private static EdgeProvider<String, DefaultEdge> getDefaultEdgeProvider() {
        return (from, to, label, attributes) -> new DefaultEdge();
    }

    private static CSVImporter<String, DefaultEdge> getDefaultCSVImporter() {
        return new CSVImporter<>(getDefaultVertexStringProvider(), getDefaultEdgeProvider());
    }

    private static SimpleGraph<String, DefaultEdge> getSimpleGraph() {
        return new SimpleGraph<>(DefaultEdge.class);
    }

}
