package br.com.graphtheoryufcg.a01.questao02;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import static br.com.graphtheoryufcg.JGraphUtil.*;

public class Quarteiroes {

    public static void main(String[] args) {
        final String PATH_TO_CSV = "./input/q02.csv";

        Graph<String, DefaultEdge> graph = importGraphFromCSVMatrix(PATH_TO_CSV);

        printGraph(graph);
    }

}
