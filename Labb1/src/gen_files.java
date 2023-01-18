import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class gen_files {

    // listA med aaa -> hashkey -> listI
    int[] listA = new int[30*30*30];

    //LIstI1 innehåller strängar som keys och en arrayList med alla positioner i Korpus
    LinkedHashMap<String, ArrayList<Integer>> listI1 = new LinkedHashMap<>();




    // Funktion plockar in alla ord från fil skapad av tokenizer och lagrar i en hashmap
    public void gen_i (BufferedReader input) throws IOException {

        String word = "";  // Startvariabler till gen_i
        String line = input.readLine();

        while(line != null) {

            byte[] utf8Bytes = line.getBytes(StandardCharsets.ISO_8859_1);
            String converted = new String(utf8Bytes, StandardCharsets.ISO_8859_1).toLowerCase(Locale.ROOT);

           // String line = input.toString();  //tar en rad i taget i tokenizer filen
            String[] s = converted.split(" ");



            // Kollar om det är nytt ord och lägger in det i listI1 och sedan adderar indexpos i listI2
            if(!word.equals(s[0])) {
                word = s[0];
                listI1.put(word, new ArrayList<>());
                listI1.get(word).add(Integer.parseInt(s[1]));

            }
            else {
                listI1.get(s[0]).add(Integer.parseInt(s[1]));
            }

            line = input.readLine();
        }

        input.close();

    }

    // Läser in varje ord från listI och mappar 3 första bokstäver mot position i lista I.
    public void gen_a () {

        int offset = 0;


        // Iterera varje key/value par i listI
        for (Map.Entry<String, ArrayList<Integer>> next_Map : listI1.entrySet()) {

            int hashPos = gen_hash.latHash(next_Map.getKey());

            if (listA[hashPos] == 0 && hashPos != 0)
                listA[hashPos] = offset;

            /* offset beräknas enligt:
            sizeOf(int) = 4 byte för att representera längd av det sökta ordet
            sizeOf(int) = 4 byte för att representera #förekomster av ett ord
            sizeOf(int) = 4 byte för att representera position i I2 filen
            sizeOf(sträng) = dom faktiska # bytes av ett ord
            */
            byte [] word = next_Map.getKey().getBytes(StandardCharsets.ISO_8859_1);
            offset += 1 + 4 + 4 + word.length; // läser int atm

        }
    }

    // Läser in varje ord från Korpus filen och genererar alla filer
    public void gen_fil(BufferedReader input) throws IOException {



        gen_i(input);
        gen_a();

        DataOutputStream A_file = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("\\A_file.bin")));
        DataOutputStream I1_file = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("\\I1_file.bin")));
        DataOutputStream I2_file = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("\\I2_file.bin")));

        //BufferedWriter A_file  = new BufferedWriter(new FileWriter("\\A_file.bin"));
        //BufferedWriter I1_file  = new BufferedWriter(new FileWriter("\\I1_file.bin"));
        //BufferedWriter I2_file  = new BufferedWriter(new FileWriter("\\I2_file.bin"));


        int offsetI2 = 0;

        for (Map.Entry<String, ArrayList<Integer> > next_Map1 : listI1.entrySet()) {

            byte [] word = next_Map1.getKey().getBytes(StandardCharsets.ISO_8859_1);

            I1_file.writeByte(word.length); // Längd på ordet
            I1_file.writeInt(next_Map1.getValue().size()); // Antal förekomster
            I1_file.writeInt(offsetI2); // I2 offset index
            I1_file.write(word); // Själva ordet

            // Iterera över hela arraylisten med varje ords position och skriv till I2 filen
            ArrayList<Integer> value = next_Map1.getValue();
            for(int I2_pos : value){
                I2_file.writeInt(I2_pos);
            }

           offsetI2 += next_Map1.getValue().size() * 4;
        }

        I1_file.close();
        I2_file.close();

        for (int j : listA) {
            A_file.writeInt(j);
        }

        A_file.close();

    }
}
