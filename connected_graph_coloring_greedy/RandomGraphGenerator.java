import java.util.Random;

public class RandomGraphGenerator {

    /**
     * Generates an adjacency matrix for a random graph.
     *
     * @param n the number of vertices in the graph
     * @param probability the probability of an edge existing between two vertices
     * @return an adjacency matrix representing the graph
     */
    public static int[][] generateConnectedRandomGraph(int n, double probability) {
        int[][] graph = new int[n][n];
        Random random = new Random();

        // Step 1: Ensure the graph is connected by generating a random Minimum Spanning Tree (MST)
        boolean[] connected = new boolean[n];
        connected[0] = true; // Start from vertex 0

        for (int i = 1; i < n; i++) {
            int v = i;
            // Connect the current vertex to any of the already connected vertices
            int u = random.nextInt(i); 
            graph[u][v] = 1;
            graph[v][u] = 1;
            connected[v] = true;
        }

        // Step 2: Add random edges based on the specified probability
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (graph[i][j] == 0 && random.nextDouble() < probability) {
                    graph[i][j] = 1;
                    graph[j][i] = 1;
                }
            }
        }

        return graph;
    }

    /**
     * Prints the adjacency matrix.
     *
     * @param graph the adjacency matrix of the graph
     */
    public static void printGraph(int[][] graph) {
        int n = graph.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(graph[i][j] + " ");
            }
            System.out.println();
        }
    }
}
