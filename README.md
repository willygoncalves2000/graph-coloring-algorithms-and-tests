# Graph Coloring Algorithms
This repository contains an implementation of two graph coloring algorithms:

* **Backtracking Graph Coloring Algorithm** - An exact coloring algorithm that explores all possibilities to ensure an optimal solution.
* **Greedy Graph Coloring Algorithm** - A heuristic coloring algorithm that uses a greedy approach to find an approximate solution.
Additionally, the repository includes a test folder that compares the performance of the two algorithms under the same conditions.

**coloracao_grafos_conexos_greedy/**: Contains the implementation of the graph generation algorithm, the greedy graph coloring algorithm, and a test that measures how many graphs the greedy algorithm solves within 180 minutes, starting from a graph with 1 vertex and incrementing the number of vertices with each new graph.

**coloracao_grafos_conexos_backtracking/**: Contains the implementation of the graph generation algorithm, the backtracking graph coloring algorithm, and a test that measures how many graphs the backtracking algorithm solves within 180 minutes, starting from a graph with 1 vertex and incrementing the number of vertices with each new graph.

**teste_coloracao_greedy_vs_backtracking/**: Contains a comparison test between the greedy and backtracking algorithms, using the same conditions and inputs.