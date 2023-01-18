import java.util.ArrayList;
import java.util.List;

class Heuristik {

    public static Kattio io;
    static int n, s, k; // n = antal roller, s = scener , k = antal skådisar
    private static List<Integer> [] roles;  //OBS! 0 index går ej att använda
    private static List<Integer> [] scenes;
    private static List<Integer> [] actors; //OBS! 0 index går ej att använda

    public static void main(String[] args) {

        io = new Kattio(System.in, System.out);
        read();     //funktionen sköter inläsning av indata enligt format för labb 4
        solve();    // funktionen löser själva rollbesättningsproblemet mha role_check och actor_check
        print();    // funktion printar ut resultatet enligt angiven utdataformat
        io.close();
    }

    static void read() {

        // get indata
        n = io.getInt();
        s = io.getInt();
        k = io.getInt();

        //Inititerar en ny lista för roles, scenes och actors.
        roles = new List[n];
        scenes = new List[s];
        actors = new List[k + n + 1];

        //initiera em ny ArrayList i varje plats för roles, scenes och actors.
        for(int i = 0; i < n; i++) {
            roles[i] = new ArrayList<>();
        }
        for(int i = 0; i < s; i++) {
            scenes[i] = new ArrayList<>();
        }
        for(int i = 0; i < (k+n+1); i++) {
            actors[i] = new ArrayList<>();
        }

        int a,b;    //Variabler används vid io inläsning

        // Motsvarar inläsning av n rader av indatan.
        // a = en roll och b = alla skådespelare som kan spela rollen.
        // inläsning av roller sker från roll 1....n
        for(int i = 0; i < n; i++) {
            a = io.getInt();
            for(int j = 0; j < a; j++) {
                b = io.getInt();
                roles[i].add(b);
            }
        }

        // Motsvarar inläsning av s rader av indatan.
        // a = en scen och b = alla roller som är med i scenen
        // inläsning av scener sker från scen 1....s
        for(int i = 0; i < s; i++) {
            a = io.getInt();
            for(int j = 0; j < a; j++) {
                b = io.getInt();
                scenes[i].add(b);
            }
        }

        io.flush();
    }

    // Kontrollerar om två roller spelar mot varandra i samma scen.
    // Returnera True om roller ej spelar mot varandra annars False
    static boolean role_check(int d1, int d2) {
        for(int i = 0; i < scenes.length; i++) {
            if(scenes[i].contains(d1))
                if(scenes[i].contains(d2))
                    return false;
        }
        return true;
    }

    // Funktion kollar om en skådespelare (actor) kan spela en roll (role).
    // Kontrollen görs mha role_check.
    static boolean actor_check(int actor, int role) {

        //Sant om actor är diva 1 eller 2.
        if(actor == 1 || actor == 2) {

            for(int i = 0; i < actors[1].size(); i++) {
                if(!role_check(actors[1].get(i) + 1, role))
                    return false;
            }

            for(int i = 0; i < actors[2].size(); i++) {
                if(!role_check(actors[2].get(i) + 1, role))
                    return false;
            }

        //else gäller för kontroll av resterande skådespelare
        } else {

            for(int i = 0; i < actors[actor].size(); i++) {
                if(!role_check(actors[actor].get(i) + 1, role))
                    return false;
            }
        }
        return true;
    }


    static void solve(){

        //Initiera Arraylist för diva 1 och 2.
        ArrayList<Integer> diva1 = new ArrayList<>();
        ArrayList<Integer> diva2 = new ArrayList<>();

        //Kontrollerar alla roller som divorna kan spela och läggs in i respektive ArrayList
        for(int i = 0; i < n; i++){
            if(roles[i].contains(1)){
                diva1.add(i);
            }
            if(roles[i].contains(2)){
                diva2.add(i);
            }
        }

        // Kollar så att divornas respektive roller inte spelar mot varandra i samma scen
        for(int i = 0; i < diva1.size(); i++){
            for(int j = 0; j < diva2.size();j++){

                //om divornas roller ej spelar mot varandra returnerar role_check true
                if(role_check(diva1.get(i) + 1, diva2.get(j) + 1)){

                    //lägg roler för respektive diva i actors
                    actors[1].add(diva1.get(i));
                    actors[2].add(diva2.get(j));

                    // markera roller som tagna genom att kalla på clear()
                    // på rollerna, ingen annan kan spela dessa.
                    roles[diva1.get(i)].clear();
                    roles[diva2.get(j)].clear();
                    break;  //hittat roller åt divorna -> break!
                }
            }

            // Om diva1 och diva2 har fått åtminstone 1 roll tilldelad
            if(!actors[1].isEmpty())
                break;
        }

        // Tilldelar varje skådis en roll och tar bort resterande skådisalternativ till den specifika rollen
        // vi markerar tagning av rollen genom att kalla clear().
        for(int i = 0; i < roles.length; i++){
            for(int j = 0; j < roles[i].size(); j++){
                int actorNumber = roles[i].get(j);

                if(actor_check(actorNumber, i+1)){
                    actors[actorNumber].add(i);
                    roles[i].clear();
                }
            }
        }

        //för varje roll som inte har kunnat tilldelas en skådis lägger vi en superskådis.
        //nr för superskådis (int a) börjar vid antal skådespelare + 1
        int a = k + 1;
        for(int i = 0; i < n; i ++){
            if(!roles[i].isEmpty()){
                actors[a++].add(i);
            }
        }


    }

    /*
   Funktion nedan printar ut enligt följande format:
   Rad ett: antal skådespelare som fått roller.
   En rad för varje skådespelare (som fått roller) med
   skådespelarens nummer, antalet roller skådespelaren tilldelats
   samt numren på dessa roller.
    */
    static void print() {

        int actor_count = 0;
        for(int i = 0; i < actors.length; i++) {
            if(!actors[i].isEmpty())
                actor_count++;
        }

        io.println(actor_count);
        for(int i = 0; i < actors.length; i++){
            if(!actors[i].isEmpty()) {
                io.print(i + " " + actors[i].size() + " ");
                for(int j = 0; j < actors[i].size(); j++){
                    io.println((actors[i].get(j) + 1));
                }
            }
        }
    }

}

