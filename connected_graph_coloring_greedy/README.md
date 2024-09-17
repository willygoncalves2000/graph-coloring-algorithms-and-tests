# A backtracking-based algorithm

## Resume
In this project, a greedy algorithm has been implemented to solve the graph coloring problem. A greedy algorithm for graph coloring is an approach where the graph is colored by assigning colors to vertices one by one in a sequence, making a local optimal choice at each step. The algorithm tries to use the smallest possible number of colors without checking for a globally optimal solution. Starting with an uncolored graph, the algorithm picks a node, assigns the lowest available color (that hasn’t been used by its neighbors), and then moves on to the next node. This process continues until all vertices are colored

## Generating Graphs for Testing
### In-Depth Analysis of the Algorithm
The ``RandomGraphGenerator.java`` file is responsible for generating the graphs that will be used by the graph coloring algorithm. The algorithm implemented in this file generates only connected graphs. This is because disconnected graphs can be easily resolved and directly impact the execution time of the coloring algorithm, which could significantly influence the test results.<br><br>
To ensure the generation of only connected graphs, a random Minimum Spanning Tree (MST) is first generated, which guarantees that each node is connected to at least one other node in the tree.<br>
Subsequently, new connections are randomly generated between pairs of nodes based on a probability value, which is randomly selected from a set of predetermined values (0.1, 0.3, 0.5, 0.7, 0.9). Essentially, a random number is chosen, and if this number is less than the selected probability, a connection is established between the two nodes. 
The higher the probability value, the denser the graph will be, meaning there will be more connections between the nodes. As a result, the greater the number of connections between the vertices, the more time the algorithm will require to complete the graph coloring.

## Greedy-based Graph Coloring Algorithm
The algorithm begins by assigning a color (color `0`) to the first node. For each subsequent node, it checks the colors of neighboring nodes and marks those colors as unavailable. Then, it assigns the lowest available color to the current node and updates the chromatic number as needed.

### About the Algorithm
This algorithm is simple and efficient, but it does not guarantee an optimal solution; in other words, it does not ensure that the number of colors used is the minimum possible. The greedy-based algorithm provides a valid solution and is often satisfactory for practical problems, especially when performance is prioritized over optimality.

The time complexity of this algorithm is O(𝑉<sup>2</sup>), where 𝑉 represents the number of nodes. This complexity is more efficient than the exponential complexity of the backtracking-based algorithm.
