import java.util.Arrays;

public class GraphColoring {
    private int vertices; // Number of vertices in the graph
    private int[][] graph;  // Adjacency matrix
    private int[] colors;   // Array to store vertex colors
    private int chromaticNumber; // The chromatic number (minimum number of colors)

    public GraphColoring(int[][] graph) {
        this.vertices = graph.length;
        this.graph = graph;
        this.colors = new int[vertices];
        Arrays.fill(colors, -1); // Initially, all vertices start with undefined colors
        this.chromaticNumber = vertices; // Initially, the chromatic number is equal to the number of vertices
    }

    // Checks if the color can be assigned to vertex v
    /* INPUT: Vertex 'v', Color 'c'
     * Checks if the neighboring vertices of 'v' have color 'c'
     * If YES, then returns FALSE -> color 'c' cannot be assigned to vertex 'v'
     * If NO, then returns TRUE -> color 'c' can be assigned to vertex 'v'
    */
    private boolean isSafe(int v, int c) {
        for (int i = 0; i < vertices; i++) {
            if (graph[v][i] == 1 && colors[i] == c) {
                return false;
            }
        }
        return true;
    }

    // Recursive method to solve the graph coloring problem using backtracking
    private boolean graphColoringUtil(int v, int k) {
        // If all vertices are colored, return true
        if (v == vertices) {
            return true;
        }

        // TRY ALL COLORS FROM 1 TO k
        /* In the worst case, each vertex will have one color, so the number of colors will be 'k'.
         * Therefore, we will try to color the graph with 1 color, then with 2, and so on, up to 'k'
         */
        for (int c = 0; c < k; c++) {
            // Calls isSafe to check if color 'c' can be assigned to vertex 'v'
            /* If YES, then assigns color 'c' to vertex 'v' */
            if (isSafe(v, c)) {
                colors[v] = c;

                // Recursively tries to color the next vertex
                if (graphColoringUtil(v + 1, k)) {
                    return true;
                }

                // If coloring with color 'c' does not lead to a solution, "unassign" the color from the vertex
                colors[v] = -1;
            }
        }

        // If no color can be assigned to vertex 'v', return false
        return false;
    }

    // Method to find the chromatic number of the graph
    public int findChromaticNumber() {
        for (int k = 1; k <= vertices; k++) {
            // "Unassigns" all colors from all vertices before a new coloring attempt
            Arrays.fill(colors, -1);
            // Attempts to color the graph starting from vertex '0' and using 'k' colors
            if (graphColoringUtil(0, k)) {
                chromaticNumber = k;
                break;
            }
        }
        return chromaticNumber;
    }

    // Method to print the vertex colors
    public void printSolution() {
        System.out.println("The graph coloring uses " + chromaticNumber + " colors:");
        for (int i = 0; i < vertices; i++) {
            System.out.println("Vertex " + i + " -> Color " + colors[i]);
        }
    }
}
