// Nome(s) Discente(s): Willy Gonçalves Campos  
// Matrícula(s): 0026645
// Data: 02 de setembro de 2024


// Declaro que sou o único autor e responsável por este programa. Todas as partes do programa, exceto as que //foram fornecidas pelo professor foram desenvolvidas por mim. Declaro também que
// sou responsável por todas  as eventuais cópias deste programa e que não distribui nem facilitei a //distribuição de cópias.

import java.util.Arrays;

public class GreedyGraphColoring {
    private int vertices; // Número de vértices do grafo
    private int[][] graph;  // Matriz de adjacência
    private int[] colors;   // Array para armazenar cores dos vértices
    private int chromaticNumber; // O número cromático (número mínimo de cores)

    public GreedyGraphColoring(int[][] graph) {
        this.vertices = graph.length;
        this.graph = graph;
        this.colors = new int[vertices];
        Arrays.fill(colors, -1); // Inicialmente, todos os vértices começam sem cores definidas
        this.chromaticNumber = 0; // Inicialmente, o número cromático é zero
    }

    // Método para resolver o problema de coloração de grafos usando o algoritmo guloso
    /* A cor 0 é atribuida ao primeiro vértice
     * Para cada proximo vértice, verifica as cores dos vértices adjacentes e marca essas
     * como INDISPONIVEIS. 
     * Em seguida, atribui a menor cor disponível ao vértice atual
     * O número cromático é atualizado conforme necessário
     */
    public void greedyColoring() {
        // Atribuir a primeira cor ao primeiro vértice
        colors[0] = 0;
        chromaticNumber = 1; // Pelo menos uma cor é usada

        // Array temporário para armazenar cores disponíveis
        boolean[] available = new boolean[vertices];
        
        // Atribuir cores aos restantes vértices
        for (int u = 1; u < vertices; u++) {
            // Inicializar todas as cores como disponíveis
            Arrays.fill(available, true);
            
            // Processar todos os vértices adjacentes e marcar suas cores como indisponíveis
            for (int i = 0; i < vertices; i++) {
                if (graph[u][i] == 1 && colors[i] != -1) {
                    available[colors[i]] = false;
                }
            }
            
            // Encontrar a primeira cor disponível
            int cr;
            for (cr = 0; cr < vertices; cr++) {
                if (available[cr]) {
                    break;
                }
            }
            
            // Atribuir a cor encontrada ao vértice u
            colors[u] = cr;
            
            // Atualizar o número cromático se necessário
            if (cr + 1 > chromaticNumber) {
                chromaticNumber = cr + 1;
            }
        }
    }

    // Método para imprimir as cores dos vértices
    public void printSolution() {
        System.out.println("A coloracao do grafo usa " + chromaticNumber + " cores:");
        for (int i = 0; i < vertices; i++) {
            System.out.println("Vertice " + i + " -> Cor " + colors[i]);
        }
    }

    // public static void main(String[] args) {
    //     // Exemplo de uso
    //     int[][] graph = {
    //         {0, 1, 1, 1},
    //         {1, 0, 1, 0},
    //         {1, 1, 0, 1},
    //         {1, 0, 1, 0},
    //     };

    //     GraphColoring coloring = new GraphColoring(graph);
    //     coloring.greedyColoring();
    //     coloring.printSolution();
    // }
}
