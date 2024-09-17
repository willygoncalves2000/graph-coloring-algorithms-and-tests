// Nome(s) Discente(s): Willy Gonçalves Campos  
// Matrícula(s): 0026645
// Data: 02 de setembro de 2024


// Declaro que sou o único autor e responsável por este programa. Todas as partes do programa, exceto as que //foram fornecidas pelo professor foram desenvolvidas por mim. Declaro também que
// sou responsável por todas  as eventuais cópias deste programa e que não distribui nem facilitei a //distribuição de cópias.


import java.util.Arrays;

public class GraphColoring {
    private int vertices; // Número de vértices do grafo
    private int[][] graph;  // Matriz de adjacência
    private int[] colors;   // Array para armazenar cores dos vértices
    private int chromaticNumber; // O número cromático (número mínimo de cores)

    public GraphColoring(int[][] graph) {
        this.vertices = graph.length;
        this.graph = graph;
        this.colors = new int[vertices];
        Arrays.fill(colors, -1); // Inicialmente, todos os vértices começam sem cores definidas
        this.chromaticNumber = vertices; // Inicialmente, o número cromático é igual ao número de vértices
    }

    // Verifica se a cor pode ser atribuída ao vértice v
    /* ENTRADA: Vértice 'v', Cor 'c' 
     * Verifica se os vértices vizinhos de 'v' possuem a cor 'c'
     * Se SIM, então retorna FALSE -> a cor 'c' não pode ser atribuída ao vértice 'v'
     * Se NÃO, então retorna TRUE -> a cor 'c' pode ser atribuída ao vértice 'v'
    */
    private boolean isSafe(int v, int c) {
        for (int i = 0; i < vertices; i++) {
            if (graph[v][i] == 1 && colors[i] == c) {
                return false;
            }
        }
        return true;
    }

    // Método recursvio para resolver o problema de coloração de grafos usando backtracking
    private boolean graphColoringUtil(int v, int k) {
        // Se todos os vértices foram coloridos, retornar true
        if (v == vertices) {
            return true;
        }

        // TENTAR TODAS AS CORES DE 1 ATÉ k
        /* No pior caso, cada vértice vai ter uma cor, logo o número de cores vai ser igual a 'k'.
         * Portanto vamos tentar colorir o grafo com 1 cor, depois com 2, e assim por diante, até 'k'
         */
        for (int c = 0; c < k; c++) {
            // Chama isSafe para verificar se a cor 'c' pode ser atribuída ao vértice 'v'
            /* Se SIM, então atribui a cor 'c' ao vértice 'v' */
            if (isSafe(v, c)) {
                colors[v] = c;

                // Recursivamente tenta colorir o próximo vértice
                if (graphColoringUtil(v + 1, k)) {
                    return true;
                }

                // Se colorir com a cor 'c' não leva a uma solução, "apaga" a cor do vértice
                colors[v] = -1;
            }
        }

        // Se nenhuma cor pode ser atribuída ao vértice 'v', retornar false
        return false;
    }

    // Método para encontrar o número cromático do grafo
    public int findChromaticNumber() {
        for (int k = 1; k <= vertices; k++) {
            // "Apaga" todas as cores de todos os vértices antes de uma nova tentativa de coloração
            Arrays.fill(colors, -1);
            // Tenta colorir o grafo começando do vértice '0' e usando 'k' cores
            if (graphColoringUtil(0, k)) {
                chromaticNumber = k;
                break;
            }
        }
        return chromaticNumber;
    }

    // Método para imprimir as cores do vértices
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
    //     int chromaticNumber = coloring.findChromaticNumber();
    //     System.out.println("Numero cromatico: " + chromaticNumber);
    //     coloring.printSolution();
    // }
}