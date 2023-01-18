/*
* StepThree skriven av Stefan Garrido och Jens Ekenblad
 * */

import java.util.ArrayList;
import java.util.LinkedList;

public class StepThree {

    private static Kattio io;
    private static int V, S, T, X, Y;

    public static void main(String[] args) {
        io = new Kattio(System.in, System.out);
        Node2[] graph = initNodes();             //graph innehåller objekt av typ Edge2, graph är en repr. av indatan
        int maxFlow = MaxFlow(graph, S, T);     //returnerar max flöde in i sänkan från källan

        io.println(X + " " + Y + "\n" + maxFlow);

        //sök igenom graph och skriv ut alla kanter med positiv flöde, men skriv ej ut källan och sänkan
        for (int i = 1; i < V + 1; i++){
            for (Edge2 edge : graph[i].edges){
                if (edge.flow > 0 && edge.u != S && edge.v != T){
                    io.println((edge.u-1) + " " + (edge.v-1));
                }
            }
        }
        io.close();
    }

    //Funktionen hanterar indata från System.in mha Kattio. Vi initierar nödvändiga datastrukturer
    //och skapar en repr. av varje kant mellan två noder i objektet Edge2. Dessa sparas i graph och returneras
    public static Node2[] initNodes(){
        int e;
        X = io.getInt();
        Y = io.getInt();
        e = io.getInt();      //antal kanter mellan X och Y

        V = X + Y + 2;       // Totalt antal hörn i V
        S = 1;               //källan motsvarar nod nr 1
        T = X + Y + 2;       //sänkan motsvarar nod nr = (x + y + 2), dvs alla noder i grafen + källan + sänkan

        Node2[] graph = new Node2[V+1];

        // Initiera varje nod
        for (int i = 1; i < V+1; i++)
            graph[i] = new Node2();

        //från källan till alla noder i x
        for (int i = 2; i < X + 2; i++) {

            //edge b är inte med i input grafen, den finns för att hjälpa oss lösa problemet med flöde
            Edge2 a = new Edge2(S , i , 0 , 1);
            Edge2 b = new Edge2(i , S , 0 , 0);

            //sätt så att a pekar till dess reverse b och pss b till dess reverse a
            a.pointToReverse(b);
            b.pointToReverse(a);

            //Spara edges i Node graph.
            graph[S].edges.add(a);
            graph[i].edges.add(b);
        }

        //alla kanter mellan x och y,dvs bipartit grafen
        for (int i = 0; i < e; ++i) {
            int u = io.getInt();
            int v = io.getInt();

            //edge b är inte med i input grafen, den finns för att hjälpa oss lösa problemet med flöde
            Edge2 a = new Edge2((u+1) , (v+1) , 0 , 1);
            Edge2 b = new Edge2((v+1) , (u+1) , 0 , 0);

            //sätt så att a pekar till dess reverse b och pss b till dess reverse a
            a.pointToReverse(b);
            b.pointToReverse(a);

            //Spara edges i Node graph.
            graph[(u+1)].edges.add(a);
            graph[(v+1)].edges.add(b);
        }

        //från alla y noder till sink
        for (int i = X + 2; i < T; i++) {

            //edge b är inte med i input grafen, den finns för att hjälpa oss lösa problemet med flöde
            Edge2 a = new Edge2(i , T , 0 , 1);
            Edge2 b = new Edge2(T , i , 0 , 0);

            //sätt så att a pekar till dess reverse b och pss b till dess reverse a
            a.pointToReverse(b);
            b.pointToReverse(a);

            //Spara edges i Node graph.
            graph[i].edges.add(a);
            graph[T].edges.add(b);
        }

        io.flush();

        return graph;
    }

    //Funktion tar emot graph samt källan och sänkan och returnerar en max flöde.
    public static int MaxFlow(Node2[] graph, int source, int destination){

        int maxFlow = 0;

        //körs så länge vi inte hittar en väg till sänkan.
        while (true) {

            //Array används för att spara en väg till sänkan.
            // parent[i] är en edge som används för att nå noden i
            Edge2[] parent = new Edge2[V+1];

            LinkedList<Node2> q = new LinkedList<>();
            q.add(graph[source]);

            //Kör bfs tills kön är tom
            while (!q.isEmpty()) {
                Node2 current = q.remove(0);

                //Kontrollera att edge.v (nod v i edge objektet) ej har besökts,
                // att edge.v ej är källan samt att det finns kapacitet att skicka flöde igenom
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
            //sätt flödet till ett enormt stort tal
            int flow = Integer.MAX_VALUE;

            //Hitta minsta maximala flödet genom att kontrollera varje edge i den hittade vägen
            //och uppdatera flow under vägen.
            Edge2 edge = parent[destination];
            while(edge != null){
                flow = Math.min(flow , edge.capacity - edge.flow);
                edge = parent[edge.u];
            }

            //Addera flöde i en edge och subtrahera flöde från den omvända edgen.
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

//Klass är en Arraylist som innehåller objekt av typen Edge2
class Node2 {
    ArrayList<Edge2> edges = new ArrayList<>();
}

//klass skapar ett objekt av typen edge, där varje edge innehåller:
//nod u, nod v samt flödet och kapacitet mellan nod u och v.
//edge reverse innehåller samma information men är den "omvända" kanten mellan två noder.
class Edge2 {

    int u , v , flow , capacity;
    Edge2 reverse;

    public Edge2(int u , int v , int flow , int capacity) {
        this.u = u;
        this.v = v;
        this.flow = flow;
        this.capacity = capacity;
    }

    public void pointToReverse(Edge2 e) {
        reverse = e;
    }
}


