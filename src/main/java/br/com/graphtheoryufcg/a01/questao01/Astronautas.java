package br.com.graphtheoryufcg.a01.questao01;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import static br.com.graphtheoryufcg.JGraphUtil.*;

public class Astronautas {

    public static void main(String[] args) {
        final String PATH_TO_CSV = "./input/q01.csv";

        Graph<String, DefaultEdge> graph = importGraphFromCSVMatrix(PATH_TO_CSV);
        Graph<String, DefaultEdge> compGraph = getCompGraph(graph);

        printEdgesFromGraph(compGraph);
    }


}
