import java.util.Arrays;

public class GreedyGraphColoring {
    private int vertices; // Number of vertices in the graph
    private int[][] graph;  // Adjacency matrix
    private int[] colors;   // Array to store node colors
    private int chromaticNumber; // The chromatic number (minimum number of colors)

    public GreedyGraphColoring(int[][] graph) {
        this.vertices = graph.length;
        this.graph = graph;
        this.colors = new int[vertices];
        Arrays.fill(colors, -1); // Initially, all vertices start with undefined colors
        this.chromaticNumber = 0; // Initially, the chromatic number is zero
    }

    // Method to solve the graph coloring problem using the greedy algorithm
    /* Color 0 is assigned to the first node
     * For each subsequent node, check the colors of adjacent vertices and mark those
     * as UNAVAILABLE.
     * Then, assign the smallest available color to the current node
     * The chromatic number is updated as necessary
     */
    public void greedyColoring() {
        // Assign the first color to the first node
        colors[0] = 0;
        chromaticNumber = 1; // At least one color is used

        // Temporary array to store available colors
        boolean[] available = new boolean[vertices];
        
        // Assign colors to the remaining vertices
        for (int u = 1; u < vertices; u++) {
            // Initialize all colors as available
            Arrays.fill(available, true);
            
            // Process all adjacent vertices and mark their colors as unavailable
            for (int i = 0; i < vertices; i++) {
                if (graph[u][i] == 1 && colors[i] != -1) {
                    available[colors[i]] = false;
                }
            }
            
            // Find the first available color
            int cr;
            for (cr = 0; cr < vertices; cr++) {
                if (available[cr]) {
                    break;
                }
            }
            
            // Assign the found color to node u
            colors[u] = cr;
            
            // Update the chromatic number if necessary
            if (cr + 1 > chromaticNumber) {
                chromaticNumber = cr + 1;
            }
        }
    }

    // Method to print the colors of the vertices
    public void printSolution() {
        System.out.println("The graph coloring uses " + chromaticNumber + " colors:");
        for (int i = 0; i < vertices; i++) {
            System.out.println("Node " + i + " -> Color " + colors[i]);
        }
    }
}
