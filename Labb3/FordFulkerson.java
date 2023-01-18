import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class FordFulkerson
{
    private int[] parent;
    private Queue<Integer> queue;
    private int numberOfVertices;
    private boolean[] visited;

    public FordFulkerson(int numberOfVertices)
    {
        this.numberOfVertices = numberOfVertices;
        this.queue = new LinkedList<Integer>();
        parent = new int[numberOfVertices + 1];
        visited = new boolean[numberOfVertices + 1];
    }

    public boolean bfs(int source, int goal, int graph[][])
    {
        boolean pathFound = false;
        int destination, element;

        for(int vertex = 1; vertex <= numberOfVertices; vertex++)
        {
            parent[vertex] = -1;
            visited[vertex] = false;
        }

        queue.add(source);
        parent[source] = -1;
        visited[source] = true;

        while (!queue.isEmpty())
        {
            element = queue.remove();
            destination = 1;

            while (destination <= numberOfVertices)
            {
                if (graph[element][destination] > 0 &&  !visited[destination])
                {
                    parent[destination] = element;
                    queue.add(destination);
                    visited[destination] = true;
                }
                destination++;
            }
        }
        if(visited[goal])
        {
            pathFound = true;
        }
        return pathFound;
    }

    public int fordFulkerson(int graph[][], int source, int destination)
    {
        int u, v;
        int maxFlow = 0;
        int pathFlow;

        int[][] residualGraph = new int[numberOfVertices + 1][numberOfVertices + 1];
        for (int sourceVertex = 1; sourceVertex <= numberOfVertices; sourceVertex++)
        {
            for (int destinationVertex = 1; destinationVertex <= numberOfVertices; destinationVertex++)
            {
                residualGraph[sourceVertex][destinationVertex] = graph[sourceVertex-1][destinationVertex-1];
            }
        }

        while (bfs(source ,destination, residualGraph))
        {
            pathFlow = Integer.MAX_VALUE;
            for (v = destination; v != source; v = parent[v])
            {
                u = parent[v];
                pathFlow = Math.min(pathFlow, residualGraph[u][v]);
            }
            for (v = destination; v != source; v = parent[v])
            {
                u = parent[v];
                residualGraph[u][v] -= pathFlow;
                residualGraph[v][u] += pathFlow;
            }
            maxFlow += pathFlow;
        }

        return maxFlow;
    }

    public static void main(String...arg)
    {

        int numberOfNodes = 6;
        int source = 1;
        int sink = 6;
        int maxFlow;






        int[][] graph = new int[][] {
                { 0, 16, 13, 0, 0, 0 }, { 0, 0, 10, 12, 0, 0 },
                { 0, 4, 0, 0, 14, 0 },  { 0, 0, 9, 0, 0, 20 },
                { 0, 0, 0, 7, 0, 4 },   { 0, 0, 0, 0, 0, 0 } };



        FordFulkerson fordFulkerson = new FordFulkerson(6);
        maxFlow = fordFulkerson.fordFulkerson(graph, source, sink);
        System.out.println("The Max Flow is " + maxFlow);

    }
}