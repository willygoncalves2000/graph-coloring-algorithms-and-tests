# A backtracking-based algorithm

## Resume
In this project, a backtracking-based algorithm is implemented.
Backtracking is a technique of exhaustive search, but more efficient than brute force, as it uses a smart trial-and-error approach and discards partial solutions that certainly won't work. 
This approach guarantees an optimal solution for the graph coloring problem, though the time required to find the solution can be extremely high, depending on the graph. I will delve into this topic in more detail later.
To test the algorithm, it will be executed for 180 minutes, during which it will attempt to color as many graphs as possible, starting with a graph that has 1 node and progressing to as many nodes as possible within that time. 

## Generating Graphs for Testing
### In-Depth Analysis of the Algorithm
The ``RandomGraphGenerator.java`` file is responsible for generating the graphs that will be used by the graph coloring algorithm. The algorithm implemented in this file generates only connected graphs. This is because disconnected graphs can be easily resolved and directly impact the execution time of the coloring algorithm, which could significantly influence the test results.<br><br>
To ensure the generation of only connected graphs, a random Minimum Spanning Tree (MST) is first generated, which guarantees that each node is connected to at least one other node in the tree.<br>
Subsequently, new connections are randomly generated between pairs of nodes based on a probability value, which is randomly selected from a set of predetermined values (0.1, 0.3, 0.5, 0.7, 0.9). Essentially, a random number is chosen, and if this number is less than the selected probability, a connection is established between the two nodes. 
The higher the probability value, the denser the graph will be, meaning there will be more connections between the nodes. As a result, the greater the number of connections between the vertices, the more time the algorithm will require to complete the graph coloring.

## Backtracking Graph Coloring Algorithm
The presented algorithm uses the backtracking technique to determine the chromatic number of the graph.
The algorithm receives an adjacency matrix that represents the graph. An array `colors`, stores the colors assigned to each node. Initially, all nodes have no defined color (a value of `-1` is assigned to all indices in the array).<br><br>
The `isSafe` function checks if it is safe to assign a specific color to a node by verifying whether any adjacent node already has that color. Whether any adjacent node has that color, the assignment is considered safe.<br><br>
The `graphColoringUtil` function is recursive. It tries to color the graph using a fixed number of colors (passed as the parameter `k`). The function traverses each node of the graph and tries to assign a color that is safe (as verified by the `isSafe` function), using the minimum number of colors possible.
If a color assignment fails to resolve the problem for the next node, the color is removed and another color is tried (hence the term "backtracking"). If all nodes are successfully colored, the function returns `true`, indicating that the coloring is complete.<br><br>
Finally, the `findChromaticNumber` function searches for the chromatic number of the graph by attempting to color the graph using between 1 and `nodes` colors (the total number of nodes). This is because, in the worst case, the number of colors required is equal to the number of nodes in the graph. At each iteration, this function attempts to color the graph with an increasing number of colors until it finds the minimum solution. The chromatic number is defined as the smallest number of colors needed to complete the coloring.

### About the Algorithm
This algorithm guarantees an optimal solution; in other words, it always provides the smallest possible chromatic number. However, it is important to note that optimizing the graph coloring problem is an NP-complete problem, which means that the best algorithm we know of has exponential time complexity. There are satisfactory algorithms to solve this problem in polynomial time, but they do not guarantee an optimal solution. However, they are quite useful in practice for graphs of moderate size.

## Test Results and Analysis
For the tests, were used `Schedulers` to measure how many graphs were colored at each time interval (10 minutes, 30 minutes, 60 minutes, 120 minutes and 180 minutes). <br>
And here are the results:<br><br>
| Interval | Number of Nodes | Number of Colored Graphs |
| --- | --- | --- |
| 10 minutes | up to 18 nodes | 18 graphs |
| 30 minutes | up to 18 nodes | 0 graphs |
| 60 minutes | up to 25 nodes | 7 graphs |
| 120 minutes | up to 25 nodes | 0 graphs|
| 180 minutes | up to 25 nodes | 0 graphs|

`Number of Nodes` refers to the number of nodes in the last graph colored by the end of this time interval. 
`Number of Colored Graphs` refers to the total number of graphs colored within this time range.

Therefore, by running this algorithm for up to 180 minutes, the largest instance of the problem for which an optimal solution was obtained had 25 nodes. During the test execution, a graph with 26 nodes was generated with a 90% density probability, resulting in a dense graph with many connections. This demonstrates how the number of connections can impact the performance of the coloring algorithm.

These results illustrate the typical behavior of exact algorithms for NP-complete problems, such as the graph coloring problem. The exponential complexity of these problems explains these limitations.

