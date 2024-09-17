// Nome(s) Discente(s): Willy Gonçalves Campos  
// Matrícula(s): 0026645
// Data: 02 de setembro de 2024


// Declaro que sou o único autor e responsável por este programa. Todas as partes do programa, exceto as que //foram fornecidas pelo professor foram desenvolvidas por mim. Declaro também que
// sou responsável por todas  as eventuais cópias deste programa e que não distribui nem facilitei a //distribuição de cópias.

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class Main {

    private static int vertices = 1; // Número inicial de vértices
    private static int lastColoredVertices = 0; // Número de vértices do último grafo colorido com sucesso
    private static final int TOTAL_DURATION = 180; // Duração total em minutos
    private static final int[] INTERVALS = {10, 30, 60, 120, 180}; // Intervalos em minutos
    private static long startTime;
    private static boolean stopRequested = false;
    private static List<Integer> verticesAtIntervals = new ArrayList<>(); // Para armazenar os vértices nos intervalos

    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Armazenar o tempo de início
        startTime = System.currentTimeMillis();

        // Agendar a exibição do número de vértices em intervalos específicos
        for (int interval : INTERVALS) {
            scheduler.schedule(() -> {
                verticesAtIntervals.add(lastColoredVertices);
                System.out.println("Tempo: " + interval + " minutos - Numero de vertices: " + lastColoredVertices);
                
                // Se o intervalo for igual ao TOTAL_DURATION, encerrar a aplicação
                if (interval == TOTAL_DURATION) {
                    displayFinalResults();
                    scheduler.shutdown();
                    stopRequested = true;
                }
            }, interval, TimeUnit.MINUTES);
        }

        // Rodar a tarefa principal em um loop até que 180 minutos sejam atingidos
        while (!stopRequested && (System.currentTimeMillis() - startTime) < TOTAL_DURATION * 60 * 1000) {
            generateAndColorGraph();
            System.out.println("================ Time: " + ((System.currentTimeMillis() - startTime) / 1000) + " seg ================");
        }

        if (!scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    private static void generateAndColorGraph() {
        if (stopRequested) {
            return; // Se foi solicitado para parar, não continuar
        }
        
        Random random = new Random();
        double[] probabilities = {0.1, 0.3, 0.5, 0.7, 0.9}; // Probabilidade de existência de uma aresta
        double probability = probabilities[random.nextInt(probabilities.length)];

        // Gerar um grafo aleatório com o número atual de vértices
        System.out.println("Grafo Conexo com " + vertices + " vertices e probabilidade " + probability);
        int[][] randomGraph = RandomGraphGenerator.generateConnectedRandomGraph(vertices, probability);

        // Aplicar o algoritmo de coloração de grafos
        System.out.println("APLICANDO COLORACAO...");
        GraphColoring graphColoring = new GraphColoring(randomGraph);
        int chromaticNumber = graphColoring.findChromaticNumber();

        // Exibir o número cromático e a coloração dos vértices
        System.out.println("Numero de vertices: " + vertices);
        System.out.println("Numero cromatico: " + chromaticNumber);
        graphColoring.printSolution();

        // Atualizar o número de vértices coloridos com sucesso
        lastColoredVertices = vertices;

        // Aumentar o número de vértices para a próxima iteração
        vertices++;

        // Verificar se o tempo total foi atingido durante a coloração do grafo atual
        if ((System.currentTimeMillis() - startTime) >= TOTAL_DURATION * 60 * 1000) {
            stopRequested = true;
        }

        // Inserir uma pequena pausa antes de iniciar a próxima iteração, para permitir a exibição do estado atual
        try {
            Thread.sleep(500); // 0,5 segundos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void displayFinalResults() {
        System.out.println("Fim da execucao apos " + TOTAL_DURATION + " minutos.");

        // Exibir o número de vértices em cada intervalo de tempo
        for (int i = 0; i < INTERVALS.length; i++) {
            System.out.println("Numero de vertices no intervalo de " + INTERVALS[i] + " minutos: " + verticesAtIntervals.get(i));
        }
    }
}
