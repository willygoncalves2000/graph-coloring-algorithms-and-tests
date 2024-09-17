// Nome(s) Discente(s): Willy Gonçalves Campos  
// Matrícula(s): 0026645
// Data: 02 de setembro de 2024


// Declaro que sou o único autor e responsável por este programa. Todas as partes do programa, exceto as que //foram fornecidas pelo professor foram desenvolvidas por mim. Declaro também que
// sou responsável por todas  as eventuais cópias deste programa e que não distribui nem facilitei a //distribuição de cópias.

import java.util.Random;

public class RandomGraphGenerator {

    /**
     * Gera uma matriz de adjacência para um grafo aleatório.
     *
     * @param n número de vértices no grafo
     * @param probability probabilidade de existir uma aresta entre dois vértices
     * @return matriz de adjacência representando o grafo
     */
    public static int[][] generateConnectedRandomGraph(int n, double probability) {
        int[][] graph = new int[n][n];
        Random random = new Random();

        // Passo 1: Garantir que o grafo seja conexo gerando uma árvore de expansão mínima (MST) aleatória
        boolean[] connected = new boolean[n];
        connected[0] = true; // Começa do vértice 0

        for (int i = 1; i < n; i++) {
            int v = i;
            // Conecta o vértice atual a qualquer um dos vértices já conectados
            int u = random.nextInt(i); 
            graph[u][v] = 1;
            graph[v][u] = 1;
            connected[v] = true;
        }

        // Passo 2: Adicionar arestas aleatórias com base na probabilidade especificada
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
     * Imprime a matriz de adjacência.
     *
     * @param graph matriz de adjacência do grafo
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

    // public static void main(String[] args) {
    //     double[] probabilities = {0.1, 0.3, 0.5, 0.7, 0.9};
    //     Random random = new Random();

    //     for (int vertices = 1; vertices <= 10; vertices++) { // Supondo que 100 seja o limite de vértices, ajuste conforme necessário
    //         double probability = probabilities[random.nextInt(probabilities.length)];
    //         int[][] graph = generateConnectedRandomGraph(vertices, probability);

    //         // Aqui você chamaria o algoritmo de coloração com o grafo gerado
    //         // colorGraph(graph);

    //         // Exemplo: Exibir a probabilidade escolhida e o tamanho do grafo
    //         System.out.println("Grafo com " + vertices + " vértices e probabilidade " + probability);
    //         printGraph(graph);
    //     }
    // }    
    //     int n = 5; // Número de vértices
    //     double probability = 0.5; // Probabilidade de existir uma aresta entre dois vértices

    //     int[][] graph = generateRandomGraph(n, probability);

    //     System.out.println("Grafo aleatorio gerado (matriz de adjacencia):");
    //     printGraph(graph);
    // }
}
