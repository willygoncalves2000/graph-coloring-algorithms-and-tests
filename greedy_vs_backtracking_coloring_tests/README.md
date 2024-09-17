# Performance Comparison of Graph Coloring Algorithms

To compare the results obtained by the Greedy Algorithm and the Backtracking Algorithm, a modification was made to the graph generation algorithm to ensure a lower maximum probability rate. When generating the graph, a random rate is assigned, which can be 0.1, 0.3, or 0.5. As previously mentioned, this rate represents the probability of an edge connecting two vertices. A higher rate will result in a denser graph. This modification was made to better compare the two algorithms (greedy and backtracking), as the backtracking-based coloring could take a long time to execute on a very dense graph, which would significantly impact its performance compared to the greedy algorithm.

To compare the two algorithms (greedy and backtracking), a test was conducted where each algorithm was run for 15 minutes, using the same graphs for both. Before starting the actual tests, the test algorithm generates a random graph for each number of vertices, starting with 1 vertex and going up to 50 vertices (MAX_VERTICES), varying the density probability rate between 0.1, 0.3, or 0.5, with this rate assigned randomly to each graph. The generated graphs are stored to ensure that the same graphs are provided to both coloring algorithms.

After generating and storing the graphs, the testing phase begins. First, the Greedy Coloring Algorithm is executed, aiming to color as many graphs as possible (out of a total of 50) within a 15-minute time frame. Once completed, the Backtracking-Based Coloring Algorithm starts, with 15 minutes to color as many graphs as it can.

## Results
| Backtracking | Greedy |
| --- | --- |
| Colored up to 42 graphs within the 15 minutes | Solved all 50 graphs very quickly (in less than 1 second). |
| Required a maximum chromatic number of 8 colors | Required a maximum of 14 colors |
| The graph with 48 vertices and a density rate of 0.5 was not solved by the backtracking algorithm | The graph with 48 vertices and a density rate of 0.5 required the most colors (14) |
| The highest chromatic number (8) was required for some graphs, including the one with 37 vertices and a density rate of 0.5, for which the algorithm took 116 seconds to solve | The graph with 37 vertices and a density rate of 0.5 was solved by the Greedy Algorithm using 11 colors |

## Conclusions
* The complexity of the Greedy Algorithm is more efficient than the exponential complexity of the Backtracking Algorithm, which has exponential complexity, typical of an NP-complete problem, as it indeed is.
* The Greedy Algorithm does not guarantee an optimal solution.
* The Greedy Algorithm provides a valid solution that is often satisfactory for practical problems.
* Backtracking becomes impractical for large inputs.

