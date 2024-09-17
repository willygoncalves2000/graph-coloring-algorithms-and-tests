// Nome(s) Discente(s): Willy Gonçalves Campos  
// Matrícula(s): 0026645
// Data: 02 de setembro de 2024


// Declaro que sou o único autor e responsável por este programa. Todas as partes do programa, exceto as que //foram fornecidas pelo professor foram desenvolvidas por mim. Declaro também que
// sou responsável por todas  as eventuais cópias deste programa e que não distribui nem facilitei a //distribuição de cópias.

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Random;

// Classe para armazenar o grafo e a probabilidade associada
class GraphWithProbability {
    int[][] graph;
    double probability;

    public GraphWithProbability(int[][] graph, double probability) {
        this.graph = graph;
        this.probability = probability;
    }
}

public class GraphColoringTest {

    private static final int MAX_VERTICES = 50; // Maximo numero de vertices para o teste
    private static final int DURATION = 15; // Duracao total de execucao para cada algoritmo (em minutos)


    private static int TOTAL_VERTICES_GREEDY = 0;
    private static int CHROMATIC_NUMBER_GREEDY = 0;

    private static int TOTAL_VERTICES_BACKTRACKING = 0;
    private static int CHROMATIC_NUMBER_BACKTRACKING = 0;

    private static long startTime;

    public static void main(String[] args) {
        // Armazenar grafos e suas probabilidades para garantir que sejam os mesmos para ambos os algoritmos
        GraphWithProbability[] storedGraphs = new GraphWithProbability[MAX_VERTICES + 1];

        // Primeiro, gerar e armazenar os grafos
        for (int n = 1; n <= MAX_VERTICES; n++) {
            storedGraphs[n] = generateGraphForBothAlgorithms(n); // Gerar e armazenar o grafo com sua probabilidade
        }

        // Executar o algoritmo guloso para todos os grafos
        System.out.println("Iniciando teste com algoritmo Guloso...");
        // Armazenar o tempo de início
        startTime = System.currentTimeMillis();
        runAlgorithmTest("Guloso", storedGraphs);

        // Executar o algoritmo de backtracking para todos os grafos
        System.out.println("\nIniciando teste com algoritmo de Backtracking...");
        startTime = System.currentTimeMillis();
        runAlgorithmTest("Backtracking", storedGraphs);

        // Exibir resultados
        System.out.println("\nTestes concluidos.");
        System.out.println("\nApos " + DURATION + " minutos:");
        System.out.println("Algoritmo de Coloracao Guloso resolveu grafos de 1 a " + TOTAL_VERTICES_GREEDY + " vertices com um numero cromatico maximo de " + CHROMATIC_NUMBER_GREEDY + " cores.");
        System.out.println("Algoritmo de Coloracao Backtracking resolveu grafos de 1 a " + TOTAL_VERTICES_BACKTRACKING + " vertices com um numero cromatico maximo de " + CHROMATIC_NUMBER_BACKTRACKING + " cores.");
    }

    private static GraphWithProbability generateGraphForBothAlgorithms(int n) {
        Random random = new Random();
        double[] probabilities = {0.1, 0.3, 0.5};
        double probability = probabilities[random.nextInt(probabilities.length)];
        System.out.println("Gerando grafo conexo com " + n + " vertices e probabilidade " + probability);

        // Gerar o grafo unico para ambos os algoritmos
        int[][] graph = RandomGraphGenerator.generateRandomConnectedGraph(n, probability);

        // Retornar o grafo junto com a probabilidade usada
        return new GraphWithProbability(graph, probability);
    }

    private static void runAlgorithmTest(String algorithm, GraphWithProbability[] storedGraphs) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        final boolean[] timeExpired = {false};

        // Iniciar a thread de coloração
        Thread coloringThread = new Thread(() -> {
            for (int n = 1; n <= MAX_VERTICES && !timeExpired[0]; n++) {
                System.out.println("Probabilidade usada para gerar o grafo: " + storedGraphs[n].probability);
                try {
                    if (algorithm.equals("Backtracking")) {
                        GraphColoringBacktracking backtracking = new GraphColoringBacktracking(storedGraphs[n].graph);
                        System.out.println("APLICANDO COLORACAO...");
                        int chromaticNumber = backtracking.findChromaticNumber();
                        if (!Thread.currentThread().isInterrupted()) {
                            backtracking.printSolution();
                            System.out.println("Grafo com " + n + " vertices resolvido com " + chromaticNumber + " cores (Backtracking).");
                            System.out.println("================ Time: " + ((System.currentTimeMillis() - startTime) / 1000) + " seg ================");
                            TOTAL_VERTICES_BACKTRACKING = n;
                            if (CHROMATIC_NUMBER_BACKTRACKING < chromaticNumber) {
                                CHROMATIC_NUMBER_BACKTRACKING = chromaticNumber;
                            }
                        }
                    } else if (algorithm.equals("Guloso")) {
                        System.out.println("APLICANDO COLORACAO...");
                        GraphColoringGreedy greedy = new GraphColoringGreedy(storedGraphs[n].graph);
                        greedy.greedyColoring();
                        if (!Thread.currentThread().isInterrupted()) {
                            greedy.printSolution();
                            System.out.println("Grafo com " + n + " vertices resolvido com " + greedy.getChromaticNumber() + " cores (Guloso).");
                            System.out.println("================ Time: " + ((System.currentTimeMillis() - startTime) / 1000) + " seg ================");
                            TOTAL_VERTICES_GREEDY = n;
                            if (CHROMATIC_NUMBER_GREEDY < greedy.getChromaticNumber()) {
                                CHROMATIC_NUMBER_GREEDY = greedy.getChromaticNumber();
                            }
                            
                        }
                    }
                } catch (InterruptedException e) {
                    System.out.println("Thread interrompida. Cancelando a coloracao do grafo com " + n + " vertices.");
                    Thread.currentThread().interrupt(); // Preservar o estado de interrupcao
                    break; // Sair do loop se a thread foi interrompida
                }

                System.out.println("=============================================================");
            }
        });

        // Agendar a interrupção da thread após o tempo limite
        scheduler.schedule(() -> {
            timeExpired[0] = true;
            if (coloringThread.isAlive()) {
                coloringThread.interrupt();
                // System.out.println("Tempo de execucao para " + algorithm + " esgotado. Interrompendo e mostrando resultados.");
                try {
                    coloringThread.join(); // Esperar a thread terminar e mostrar resultados imediatamente
                } catch (InterruptedException e) {
                    System.out.println("Execucao interrompida durante o join.");
                    Thread.currentThread().interrupt();
                }
            }
            scheduler.shutdown();
        }, DURATION, TimeUnit.MINUTES);

        // Iniciar e aguardar o término da thread de coloração
        coloringThread.start();
        try {
            coloringThread.join();
        } catch (InterruptedException e) {
            System.out.println("Execucao interrompida.");
            Thread.currentThread().interrupt();
        }
    }
}

class RandomGraphGenerator {
    public static int[][] generateRandomConnectedGraph(int n, double probability) {
        int[][] graph = new int[n][n];
        Random random = new Random();

        // Primeiro, criar uma arvore para garantir que o grafo seja conexo
        for (int i = 1; i < n; i++) {
            int v = random.nextInt(i);
            graph[i][v] = 1;
            graph[v][i] = 1;
        }

        // Agora, adicionar arestas adicionais com a probabilidade dada
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

            // Verificacao frequente de interrupcao
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException("Interrupcao detectada durante a coloracao do grafo com " + vertices + " vertices.");
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
                System.out.println("Interrupcao detectada durante o calculo do numero cromatico para " + vertices + " vertices.");
                throw new InterruptedException("Interrupcao detectada durante o calculo do numero cromatico para " + vertices + " vertices.");
            }
        }
        return chromaticNumber;
    }

    public void printSolution() {
        System.out.println("Backtracking: A coloracao do grafo usa " + chromaticNumber + " cores.");
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

    public void greedyColoring() throws InterruptedException {
        colors[0] = 0;
        chromaticNumber = 1;

        boolean[] available = new boolean[vertices];

        for (int u = 1; u < vertices; u++) {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Interrupcao detectada durante a coloracao gulosa para " + vertices + " vertices.");
                throw new InterruptedException("Interrupcao detectada durante a coloracao gulosa para " + vertices + " vertices.");
            }
            Arrays.fill(available, true);

            for (int i = 0; i < vertices; i++) {
                if (graph[u][i] == 1 && colors[i] != -1) {
                    available[colors[i]] = false;
                }
            }

            int cr;
            for (cr = 0; cr < vertices; cr++) {
                if (available[cr]) {
                    break;
                }
            }

            colors[u] = cr;

            if (cr + 1 > chromaticNumber) {
                chromaticNumber = cr + 1;
            }

            // Verificacao frequente de interrupcao
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Interrupcao detectada durante a coloracao gulosa para " + vertices + " vertices.");
                throw new InterruptedException("Interrupcao detectada durante a coloracao gulosa para " + vertices + " vertices.");
            }
        }
    }

    public int getChromaticNumber() {
        return chromaticNumber;
    }

    public void printSolution() {
        System.out.println("Guloso: A coloracao do grafo usa " + chromaticNumber + " cores.");
    }
}
