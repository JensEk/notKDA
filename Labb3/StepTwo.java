/*
 * StepTwo skriven av Stefan Garrido och Jens Ekenblad
 * */


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StepTwo {

    public static Kattio io;
    private static int V, S, T; // integers motsvarar #noder, källan och sänkan
    private static List<Tuple<Integer,Integer,Integer>> tupleList;

    public static void main(String[] args) {

        io = new Kattio(System.in, System.out);
        Node2[] graph = initNodes();
        int maxFlow = MaxFlow(graph, S, T);

        io.println(V + "\n" + S + " " + T + " " + maxFlow);

        //Beräkna antal positiva kanter i varje edge och spara dessa edges i en tuple list
        int posEdges= 0;
        for(int i = 1; i < V+1; i++){
            for (Edge2 edge: graph[i].edges) {
                if(edge.flow > 0){
                    posEdges++;
                    Tuple<Integer, Integer, Integer> newTuple = new Tuple<>(edge.u,edge.v,edge.flow);
                    tupleList.add(newTuple);
                }
            }
        }

        io.println(posEdges);

        //printa ut alla sparade kanter med positiv flöde
        for (Tuple<Integer, Integer, Integer> tuple: tupleList) {
            io.println(tuple.getL() + " " + tuple.getR() + " " + tuple.getF());
        }

        io.close();
    }

    public static Node2[] initNodes(){
        int e;
        V = io.getInt();    // Motsvarar ett heltal som anger antalet hörn i V.
        S = io.getInt();    // Motsvarar ett heltal som anger källan
        T = io.getInt();    // Motsvarar ett heltal som anger sänkan
        e = io.getInt();    // Motsvarar ett heltal som anger antal kanter

        Node2[] graph = new Node2[V+1];
        tupleList = new ArrayList<>(e+1);

        // Initiera varje nod
        for (int i = 1; i < V+1; i++)
            graph[i] = new Node2();

        // Initiera varje edge
        for (int i = 0; i < e; i++) {
            int u,v,c;
            u = io.getInt();
            v = io.getInt();
            c = io.getInt();

            //edge b är inte med i input grafen, den finns för att hjälpa oss lösa problemet med flöde
            Edge2 a = new Edge2(u , v , 0 , c);
            Edge2 b = new Edge2(v , u , 0 , 0);

            //sätt a till reverse av b samt b till reverse av a
            a.pointToReverse(b);
            b.pointToReverse(a);

            //Spara edges i Node graph.
            graph[u].edges.add(a);
            graph[v].edges.add(b);
        }
        io.flush();

        return graph;
    }

    public static int MaxFlow(Node2[] graph, int source, int destination){
        int maxFlow = 0;

        while (true) {

            //Array används för att spara en väg till sänkan.
            // (parent[i] är en edge som används för att nå noden i
            Edge2[] parent = new Edge2[V+1];

            LinkedList<Node2> q = new LinkedList<>();
            q.add(graph[source]);

            //Kör bfs tills kön är tom
            while (!q.isEmpty()) {
                Node2 current = q.remove(0);

                //Kontrollera att edge ej har besökts, att edge ej pekar
                // mot källan samt att det finns kapacitet att skicka flöde igenom
                for (int i = 0; i < current.edges.size(); i++)

                    if (parent[current.edges.get(i).v] == null &&
                            current.edges.get(i).v != source &&
                            current.edges.get(i).capacity > current.edges.get(i).flow) {
                        parent[current.edges.get(i).v] = current.edges.get(i);
                        q.add(graph[current.edges.get(i).v]);
                    }
            }

            //Om sänkan ej har nåtts så finns det ingen väg dit -> break while loopen.
            if (parent[destination] == null)
                break;

            // Om sänkan har nåtts så beräknar vi hur mycket flöde som kan pushas igenom den hittade vägen.
            int flow = Integer.MAX_VALUE;

            //Hitta minsta maximala flödet genom att kontrollera varje edge i den hittade vägen
            //och uppdatera flow under vägen.
            Edge2 edge = parent[destination];
            while(edge != null){
                flow = Math.min(flow , edge.capacity - edge.flow);
                edge = parent[edge.u];
            }

            //Addera flöde i "riktning mot källan" och subtrahera flöde från den omvända vägen i en kant.
            edge = parent[destination];
            while(edge != null){
                edge.flow += flow;
                edge.reverse.flow -= flow;
                edge = parent[edge.u];
            }

            maxFlow += flow;
        }

        return maxFlow;
    }

}

//Klass är en lista med edges, innehåller även dom omvända edges!
class Node {
    ArrayList<Edge2> edges = new ArrayList<>();
}


//klass skapar ett objekt av typen edge, där varje edge innehåller:
//nod u, nod v samt flödet och kapacitet mellan nod u och v.
//edge reverse innehåller samma information men är den omvända kanten mellan två noder.
class Edge {
    int u , v , flow , capacity;
    Edge2 reverse;
    public Edge(int u , int v , int flow , int capacity) {
        this.u = u;
        this.v = v;
        this.flow = flow;
        this.capacity = capacity;
    }

    public void pointToReverse(Edge2 e) {
        reverse = e;
    }

}

class Tuple<L,R, F> {
    private L l;
    private R r;
    private F f;
    public Tuple(L l, R r, F f){
        this.l = l;
        this.r = r;
        this.f = f;
    }
    public L getL(){ return l; }
    public R getR(){ return r; }
    public F getF(){ return f; }
}
