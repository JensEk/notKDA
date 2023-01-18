/*
Graffärgning
    Indata: En oriktad graf och ett antal färger m. Isolerade hörn och dubbelkanter kan förekomma, inte öglor.
    Fråga: Kan hörnen i grafen färgas med högst m färger så att inga grannar har samma färg?

    Indataformat:
    Rad ett: tal V (antal hörn, tex: 1 <= V <= 300)
    Rad två: tal E (antal kanter, tex:0<= E <= 25000)
    Rad tre: mål m (max antal färger, tex: 1<= m <= 2^30)
    En rad för varje kant (E stycken) med kantens ändpunkter (hörnen numreras från 1 till V)
*/


public class Reduce {

    public static Kattio io;

    public static void main(String[] args) {
        io = new Kattio(System.in, System.out);

        int nodes, edges, color;

        nodes = io.getInt();    //motsvarar antal hörn V i grafräkning och roller i rollbesättningsprob.
        edges = io.getInt();    //motsvarar antal kanter E i grafräkning och scener i rollbesättning
        color = io.getInt();    //motsvarar mål m, dvs max antal färger i grafräkning och skådespelare i rollbesättning

        //om noder är mindre eller lika med antal färger så vet vi så kan vi utgå ifrån
        //basfallet för rollbesättningsproblemet och det motsvarar en ja-instans.
        if(nodes <= color){
            io.println("3\n2\n3");
            io.println("1 1\n1 2\n1 3");
            io.println("2 1 3\n2 2 3");
        }
        else {

            // 3 2 3 läggs till för att täcka minsta möjliga krav i rollbesättningsproblemet.
            //detta då en karpreduktion och problemsinstanen omvandlas till rollbesättningsproblemet.
            int roles = nodes + 3;
            io.println(roles + "\n" + (edges + nodes + 2) + "\n" + (color + 3));


            //sätter från roll 1 och uppåt och säger hur många skådespelare kan spela varje roll,
            // detta baserad på antal färger.
            // Nästlade for loops körs upp till antal nya roller utöver basfallet (1 1, 1 2, 1 3)
            for (int i = 4; i <= roles; i++) {
                io.print(color + " ");

                for (int j = 0; j < color; j++) {
                    io.print((j + 4) + " ");
                }
                io.print("\n");
            }

            //resterande roller ges till divorna + den extra skådespelaren, detta enligt
            //minsta möjliga krav i rollbesättningsproblemet.
            io.println("1 1\n1 2\n1 3");

            //roller som ska spelas i varje scen där villkoret för graffärningsproblemet uppnås
            for (int i = 1; i < roles; i++) {
                io.println(2 + " " + i + " " + roles);
            }

            //resterande scener som kopplas ihop med graffärgninsproblemet
            int x, y;
            for (int i = 0; i < edges; i++) {
                x = io.getInt();
                y = io.getInt();
                io.println(2 + " " + x + " " + y);
            }

        }
        io.close();
    }
}