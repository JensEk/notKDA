/*
 * StepOne skriven av Stefan Garrido och Jens Ekenblad
 * */

public class StepOne {

    private static Kattio io;

    public static void main(String[] args) {
        io = new Kattio(System.in, System.out);
        reduceFlow();
        io.close();
    }

    public static void reduceFlow() {
        int x, y, e;
        x = io.getInt();
        y = io.getInt();
        e = io.getInt();

        int s = 1;               //källan motsvarar nod nr 1
        int t = x + y + 2;       //sänkan motsvarar nod nr = (x + y + 2), dvs alla noder i grafen + källan + sänkan

        io.println(x + y + 2);   // Totalt antal hörn i i V
        io.println(s + " " + t); // Motsvarar två heltal s och t som anger vilka hörn som är källa respektive utlopp.
        io.println(x + y + e);   // Motsvarar ett tal som anger |E|, det vill säga antalet kanter i grafen.

        //från källan till alla noder i x
        for (int i = 2; i < x + 2; i++) {
            io.println(s + " " + i + " " + 1);
        }

        //alla kanter mellan x och y,dvs bipartit grafen
        for (int i = 0; i < e; ++i) {
            int a = io.getInt();
            int b = io.getInt();
            io.println((a + 1) + " " + (b + 1) + " " + 1);
        }

        //från alla y noder till sink
        for (int i = x + 2; i < t; i++) {
            io.println(i + " " + t + " " + 1);
        }

        io.flush();

        //Nedanför hanteras utdatan som kommer från den svarta lådan,
        //utdatan motsvarar flödesproblemet som sedan används för att lösa matchningsproblemet.

        int v, flow;
        v = io.getInt();    //Motsvarar ett heltal som anger antalet hörn i V.
        s = io.getInt();    // Motsvarar ett heltal som anger källan
        t = io.getInt();    // Motsvarar ett heltal som anger sänkan
        flow = io.getInt(); // Motsvarar flödet från s till t.
        e = io.getInt();    //Motsvarar ett heltal som anger antalet kanter med positivt flöde.

        //översätter lösningen på flödesproblemet  till en lösning på matchningsproblemet
        io.println(x + " " + y);
        io.println(flow);

        for (int i = 0; i < e; ++i) {
            int a, b, c;
            a = io.getInt();    //nod a
            b = io.getInt();    //nod b
            c = io.getInt();    //flödet mellan nod a och b

            //så länge noder inte motsvarande källan eller sänkan så printar vi ut lösningen för matchningsproblemet
            if (a != s && b != t) {
                io.println((a - 1) + " " + (b - 1));
            }
        }
    }
}

