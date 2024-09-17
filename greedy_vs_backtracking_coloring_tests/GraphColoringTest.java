import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Random;

// Class to store the graph and associated probability
class GraphWithProbability {
    int[][] graph;
    double probability;

    public GraphWithProbability(int[][] graph, double probability) {
        this.graph = graph;
        this.probability = probability;
    }
}

public class GraphColoringTest {

    private static final int MAX_VERTICES = 50; // Maximum number of vertices for the test
    private static final int DURATION = 15; // Total execution duration for each algorithm (in minutes)


    private static int TOTAL_VERTICES_GREEDY = 0;
    private static int CHROMATIC_NUMBER_GREEDY = 0;

    private static int TOTAL_VERTICES_BACKTRACKING = 0;
    private static int CHROMATIC_NUMBER_BACKTRACKING = 0;

    private static long startTime;

    public static void main(String[] args) {
        // Store graphs and their probabilities to ensure they are the same for both algorithms
        GraphWithProbability[] storedGraphs = new GraphWithProbability[MAX_VERTICES + 1];

        // First, generate and store the graphs
        for (int n = 1; n <= MAX_VERTICES; n++) {
            storedGraphs[n] = generateGraphForBothAlgorithms(n); // Generate and store the graph with its probability
        }

        // Run the greedy algorithm for all graphs
        System.out.println("Starting test with Greedy algorithm...");
        // Store the start time
        startTime = System.currentTimeMillis();
        runAlgorithmTest("Greedy", storedGraphs);

        // Run the backtracking algorithm for all graphs
        System.out.println("\nStarting test with Backtracking algorithm...");
        startTime = System.currentTimeMillis();
        runAlgorithmTest("Backtracking", storedGraphs);

        // Display results
        System.out.println("\nTests completed.");
        System.out.println("\nAfter " + DURATION + " minutes:");
        System.out.println("Greedy Coloring Algorithm solved graphs from 1 to " + TOTAL_VERTICES_GREEDY + " vertices with a maximum chromatic number of " + CHROMATIC_NUMBER_GREEDY + " colors.");
        System.out.println("Backtracking Coloring Algorithm solved graphs from 1 to " + TOTAL_VERTICES_BACKTRACKING + " vertices with a maximum chromatic number of " + CHROMATIC_NUMBER_BACKTRACKING + " colors.");
    }

    private static GraphWithProbability generateGraphForBothAlgorithms(int n) {
        Random random = new Random();
        double[] probabilities = {0.1, 0.3, 0.5};
        double probability = probabilities[random.nextInt(probabilities.length)];
        System.out.println("Generating connected graph with " + n + " vertices and probability " + probability);

        // Generate the unique graph for both algorithms
        int[][] graph = RandomGraphGenerator.generateRandomConnectedGraph(n, probability);

        // Return the graph along with the used probability
        return new GraphWithProbability(graph, probability);
    }

    private static void runAlgorithmTest(String algorithm, GraphWithProbability[] storedGraphs) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        final boolean[] timeExpired = {false};

        // Start the coloring thread
        Thread coloringThread = new Thread(() -> {
            for (int n = 1; n <= MAX_VERTICES && !timeExpired[0]; n++) {
                System.out.println("Probability used to generate the graph: " + storedGraphs[n].probability);
                try {
                    if (algorithm.equals("Backtracking")) {
                        GraphColoringBacktracking backtracking = new GraphColoringBacktracking(storedGraphs[n].graph);
                        System.out.println("APPLYING COLORING...");
                        int chromaticNumber = backtracking.findChromaticNumber();
                        if (!Thread.currentThread().isInterrupted()) {
                            backtracking.printSolution();
                            System.out.println("Graph with " + n + " vertices solved with " + chromaticNumber + " colors (Backtracking).");
                            System.out.println("================ Time: " + ((System.currentTimeMillis() - startTime) / 1000) + " secs ================");
                            TOTAL_VERTICES_BACKTRACKING = n;
                            if (CHROMATIC_NUMBER_BACKTRACKING < chromaticNumber) {
                                CHROMATIC_NUMBER_BACKTRACKING = chromaticNumber;
                            }
                        }
                    } else if (algorithm.equals("Greedy")) {
                        System.out.println("APPLYING COLORING...");
                        GraphColoringGreedy greedy = new GraphColoringGreedy(storedGraphs[n].graph);
                        greedy.greedyColoring();
                        if (!Thread.currentThread().isInterrupted()) {
                            greedy.printSolution();
                            System.out.println("Graph with " + n + " vertices solved with " + greedy.getChromaticNumber() + " colors (Greedy).");
                            System.out.println("================ Time: " + ((System.currentTimeMillis() - startTime) / 1000) + " secs ================");
                            TOTAL_VERTICES_GREEDY = n;
                            if (CHROMATIC_NUMBER_GREEDY < greedy.getChromaticNumber()) {
                                CHROMATIC_NUMBER_GREEDY = greedy.getChromaticNumber();
                            }
                            
                        }
                    }
                } catch (InterruptedException e) {
                    System.out.println("Thread interrupted. Cancelling the coloring of the graph with " + n + " vertices.");
                    Thread.currentThread().interrupt(); // Preserve the interrupt status
                    break; // Exit the loop if the thread was interrupted
                }

                System.out.println("=============================================================");
            }
        });

        // Schedule thread interruption after the timeout
        scheduler.schedule(() -> {
            timeExpired[0] = true;
            if (coloringThread.isAlive()) {
                coloringThread.interrupt();
                // System.out.println("Execution time for " + algorithm + " expired. Interrupting and showing results.");
                try {
                    coloringThread.join(); // Wait for the thread to finish and show results immediately
                } catch (InterruptedException e) {
                    System.out.println("Execution interrupted during join.");
                    Thread.currentThread().interrupt();
                }
            }
            scheduler.shutdown();
        }, DURATION, TimeUnit.MINUTES);

        // Start and wait for the coloring thread to complete
        coloringThread.start();
        try {
            coloringThread.join();
        } catch (InterruptedException e) {
            System.out.println("Execution interrupted.");
            Thread.currentThread().interrupt();
        }
    }
}

class RandomGraphGenerator {
    public static int[][] generateRandomConnectedGraph(int n, double probability) {
        int[][] graph = new int[n][n];
        Random random = new Random();

        // First, create a tree to ensure the graph is connected
        for (int i = 1; i < n; i++) {
            int v = random.nextInt(i);
            graph[i][v] = 1;
            graph[v][i] = 1;
        }

        // Now, add additional edges with the given probability
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
}

class GraphColoringBacktracking {
    private int vertices;
    private int[][] graph;
    private int[] colors;
    private int chromaticNumber;

    public GraphColoringBacktracking(int[][] graph) {
        this.vertices = graph.length;
        this.graph = graph;
        this.colors = new int[vertices];
        Arrays.fill(colors, -1);
        this.chromaticNumber = vertices;
    }

    private boolean isSafe(int v, int c) {
        for (int i = 0; i < vertices; i++) {
            if (graph[v][i] == 1 && colors[i] == c) {
                return false;
            }
        }
        return true;
    }

    private boolean graphColoringUtil(int v, int k) throws InterruptedException {
        if (v == vertices || Thread.currentThread().isInterrupted()) {
            return true;
        }

        for (int c = 0; c < k; c++) {
            if (isSafe(v, c)) {
                colors[v] = c;

                if (graphColoringUtil(v + 1, k)) {
                    return true;
                }

                colors[v] = -1;
            }

            // Frequent interruption check
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException("Interruption detected during coloring of the graph with " + vertices + " vertices.");
            }
        }

        return false;
    }

    public int findChromaticNumber() throws InterruptedException {
        for (int k = 1; k <= vertices; k++) {
            Arrays.fill(colors, -1);
            if (graphColoringUtil(0, k)) {
                chromaticNumber = k;
                break;
            }
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Interruption detected during chromatic number calculation for " + vertices + " vertices.");
                throw new InterruptedException("Interruption detected during chromatic number calculation for " + vertices + " vertices.");
            }
        }
        return chromaticNumber;
    }

    public void printSolution() {
        System.out.println("Backtracking: The graph coloring uses " + chromaticNumber + " colors.");
    }
}

class GraphColoringGreedy {
    private int vertices;
    private int[][] graph;
    private int[] colors;
    private int chromaticNumber;

    public GraphColoringGreedy(int[][] graph) {
        this.vertices = graph.length;
        this.graph = graph;
        this.colors = new int[vertices];
        Arrays.fill(colors, -1);
        this.chromaticNumber = 0;
    }

    public void greedyColoring() {
        colors[0] = 0;
        chromaticNumber = 1;

        for (int v = 1; v < vertices; v++) {
            boolean[] available = new boolean[vertices];
            Arrays.fill(available, true);

            for (int i = 0; i < vertices; i++) {
                if (graph[v][i] == 1 && colors[i] != -1) {
                    available[colors[i]] = false;
                }
            }

            int color;
            for (color = 0; color < vertices; color++) {
                if (available[color]) {
                    break;
                }
            }

            colors[v] = color;
            chromaticNumber = Math.max(chromaticNumber, color + 1);
        }
    }

    public int getChromaticNumber() {
        return chromaticNumber;
    }

    public void printSolution() {
        System.out.println("Greedy: The graph coloring uses " + chromaticNumber + " colors.");
    }
}
