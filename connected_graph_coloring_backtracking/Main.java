import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class Main {

    private static int vertices = 1; // Initial number of vertices
    private static int lastColoredVertices = 0; // Number of vertices in the last successfully colored graph
    private static final int TOTAL_DURATION = 180; // Total duration in minutes
    private static final int[] INTERVALS = {10, 30, 60, 120, 180}; // Intervals in minutes
    private static long startTime;
    private static boolean stopRequested = false;
    private static List<Integer> verticesAtIntervals = new ArrayList<>(); // To store vertices at intervals

    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Store the start time
        startTime = System.currentTimeMillis();

        // Schedule the display of the number of vertices at specific intervals
        for (int interval : INTERVALS) {
            scheduler.schedule(() -> {
                verticesAtIntervals.add(lastColoredVertices);
                System.out.println("Time: " + interval + " minutes - Number of vertices: " + lastColoredVertices);
                
                // If the interval equals TOTAL_DURATION, end the application
                if (interval == TOTAL_DURATION) {
                    displayFinalResults();
                    scheduler.shutdown();
                    stopRequested = true;
                }
            }, interval, TimeUnit.MINUTES);
        }

        // Run the main task in a loop until 180 minutes are reached
        while (!stopRequested && (System.currentTimeMillis() - startTime) < TOTAL_DURATION * 60 * 1000) {
            generateAndColorGraph();
            System.out.println("================ Time: " + ((System.currentTimeMillis() - startTime) / 1000) + " sec ================");
        }

        if (!scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    private static void generateAndColorGraph() {
        if (stopRequested) {
            return; // If stop has been requested, do not continue
        }
        
        Random random = new Random();
        double[] probabilities = {0.1, 0.3, 0.5, 0.7, 0.9}; // Probability of an edge existing
        double probability = probabilities[random.nextInt(probabilities.length)];

        // Generate a random graph with the current number of vertices
        System.out.println("Connected Graph with " + vertices + " vertices and probability " + probability);
        int[][] randomGraph = RandomGraphGenerator.generateConnectedRandomGraph(vertices, probability);

        // Apply the graph coloring algorithm
        System.out.println("APPLYING COLORING...");
        GraphColoring graphColoring = new GraphColoring(randomGraph);
        int chromaticNumber = graphColoring.findChromaticNumber();

        // Display the chromatic number and the coloring of the vertices
        System.out.println("Number of vertices: " + vertices);
        System.out.println("Chromatic number: " + chromaticNumber);
        graphColoring.printSolution();

        // Update the number of successfully colored vertices
        lastColoredVertices = vertices;

        // Increase the number of vertices for the next iteration
        vertices++;

        // Check if the total time has been reached during the coloring of the current graph
        if ((System.currentTimeMillis() - startTime) >= TOTAL_DURATION * 60 * 1000) {
            stopRequested = true;
        }

        // Insert a short pause before starting the next iteration to allow for the current state to be displayed
        try {
            Thread.sleep(500); // 0.5 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void displayFinalResults() {
        System.out.println("End of execution after " + TOTAL_DURATION + " minutes.");

        // Display the number of vertices at each time interval
        for (int i = 0; i < INTERVALS.length; i++) {
            System.out.println("Number of vertices at the " + INTERVALS[i] + " minute interval: " + verticesAtIntervals.get(i));
        }
    }
}
