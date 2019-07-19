package br.com.graphtheoryufcg.a02;

import br.com.graphtheoryufcg.JGraphUtil;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.Scanner;
import java.util.stream.Collectors;

public class questao01 {

    /**
     * Game that reads a graph, turns it to a tree and challenges the user to guess the root of this tree.
     * The root is generated randomly based on the graphs nodes.
     * @param args
     */
    public static void main(String[] args) {

        printHeader();
        printInstructions();

        // Graph import
        final String CSV_PATH = "./input/a02/q01-example.csv";
        Graph<String, DefaultEdge> graph = JGraphUtil.importGraphFromCSVMatrix(CSV_PATH);

        // Tree generation.
        String rootNode = JGraphUtil.getRandomElement(graph.vertexSet());
        DefaultDirectedGraph<String, DefaultEdge> tree = JGraphUtil.generateTree(graph, rootNode);

        printLoadedGraph(tree);

        Scanner sc = new Scanner(System.in);

        // User entries
        int maxTries = Integer.parseInt(readLine(sc, "Indique o numero de tentativas: "));
        int tries = 1;
        String input = "";

        if (tries <= maxTries) {
            // Game loop
            do {
                input = readLine(sc, "Tentativa: ");

                if (tree.containsVertex(input) && !input.equals(rootNode)) {
                    printValidVertexInput(tree, input);
                    tries += 1;
                } else {
                    printInvalidVertexInput(tree);
                }


            } while (tries <= maxTries && !rootNode.equals(input.trim()));
        }

        printEndGame(rootNode, tree, input);
    }

    /**
     * Prints a header with phrase equals to the title of the game: "Guess the root".
     */
    private static void printHeader() {
        String header =
                " _____ _   _ _____ _____ _____   _____ _   _  _____  ______ _____  _____ _____ \n" +
                "|  __ \\ | | |  ___/  ___/  ___| |_   _| | | ||  ___| | ___ \\  _  ||  _  |_   _|\n" +
                "| |  \\/ | | | |__ \\ `--.\\ `--.    | | | |_| || |__   | |_/ / | | || | | | | |  \n" +
                "| | __| | | |  __| `--. \\`--. \\   | | |  _  ||  __|  |    /| | | || | | | | |  \n" +
                "| |_\\ \\ |_| | |___/\\__/ /\\__/ /   | | | | | || |___  | |\\ \\\\ \\_/ /\\ \\_/ / | |  \n" +
                " \\____/\\___/\\____/\\____/\\____/    \\_/ \\_| |_/\\____/  \\_| \\_|\\___/  \\___/  \\_/  \n" +
                "                                                                               \n";

        System.out.println(header);
    }

    /**
     * Prints one graph with its edges and its nodes.
     * @param graph the graph.
     */
    private static void printLoadedGraph(DefaultDirectedGraph<String, DefaultEdge> graph) {
        System.out.println("- O grafo carregado");
        System.out.println("Vertices: " + graph.vertexSet());
        System.out.println("Arestas: " + graph.edgeSet() + "\n");
    }

    /**
     * Print the game instructions.
     */
    private static void printInstructions() {
        String instructions =
                "- Instruções: " + "\n" +
                "Objetivo: Descobrir que vertice é a raiz de uma árvore em uma quantidade limitada de tentativas." + "\n" +
                "Como jogar: Voce digitara o no que voce acha que eh a raiz, e entao sera avaliado se voce acertou ou nao." + "\n";

        System.out.println(instructions);
    }

    /**
     * Prints the game result
     * @param rootNode the root node.
     * @param tree the tree.
     * @param lastInput the last input given.
     */
    private static void printEndGame(String rootNode, DefaultDirectedGraph<String, DefaultEdge> tree, String lastInput) {
        if (rootNode.equals(lastInput.trim())) {
            System.out.println("Voce acertou!");
        } else {
            System.out.println("Numero de tentativas excedido!");
        }

        JGraphUtil.printTree(tree, rootNode);
        System.out.println(tree);
    }

    /**
     * Print that the player has chosen a invalid vertex.
     * @param tree the tree.
     */
    private static void printInvalidVertexInput(DefaultDirectedGraph<String, DefaultEdge> tree) {
        System.out.println("A arvore nao contem este no. Nos possiveis: ");
        System.out.println(tree.vertexSet());
    }

    /**
     * Print that the player has chosen a valid vertex and shows its father and children.
     * @param tree the tree.
     * @param input the input.
     */
    private static void printValidVertexInput(DefaultDirectedGraph<String, DefaultEdge> tree, String input) {
        String father = getFather(tree, input);
        String children = getChildren(tree, input);

        String result = String.format("%s nao eh raiz. O pai de %s eh %s, e os filhos de %s são {%s}", input, input, father, input, children);
        System.out.println(result);
    }

    /**
     * Get all children of a node in a graph as a string.
     * @param tree the tree.
     * @param node the node.
     * @return all children as string.
     */
    private static String getChildren(DefaultDirectedGraph<String, DefaultEdge> tree, String node) {
        return tree.outgoingEdgesOf(node)
                            .stream()
                            .map(tree::getEdgeTarget)
                            .collect(Collectors.joining(", "));
    }

    /**
     * Get the father of a node given in a graph as string.
     * @param tree the tree.
     * @param node the node.
     * @return the father node as string.
     */
    private static String getFather(DefaultDirectedGraph<String, DefaultEdge> tree, String node) {
        DefaultEdge edgePai = (DefaultEdge) tree.incomingEdgesOf(node).toArray()[0];
        return tree.getEdgeSource(edgePai);
    }

    /**
     * Prints a message and reads a line.
     * @param scanner the scanner object.
     * @param string the string to be printed.
     * @return the user input.
     */
    private static String readLine(Scanner scanner, String string) {
        System.out.print(string);
        return scanner.nextLine();
    }
}
