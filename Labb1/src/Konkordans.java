import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;

public class Konkordans {

    public static void main (String[] args) throws IOException {

        if(args.length != 1) {
            System.out.println("Inmatat fel antal argument");
            System.exit(0);
        }

        String searchWord = args[0].toLowerCase(Locale.ROOT);


        int hash_searchWord = gen_hash.latHash(searchWord);

        // read A lista med hashen och returnera pos i I listan
        RandomAccessFile readA = new RandomAccessFile("\\A_file.bin", "r");


        // Ger första och sista positionen i I1 listan som vi ska söka efter med det specifika ordet.
        int pos_A = hash_searchWord * 4;
        readA.seek(pos_A);
        int pos_I1Low = readA.readInt();

        int pos_I1High = 0;
        while(pos_I1High == 0) {
            if(readA.getFilePointer() < readA.length())
                pos_I1High = readA.readInt();
            else
                pos_I1High = pos_I1Low;

        }

        searchForWord(pos_I1Low, pos_I1High, searchWord);


    }



    public static void searchForWord (int posLow, int posHigh ,String word) throws IOException {


        DataInputStream readI2 = new DataInputStream(new BufferedInputStream(new FileInputStream("\\I2_file.bin")));
        RandomAccessFile readI1 = new RandomAccessFile("\\I1_file.bin", "r");
        RandomAccessFile readKorp = new RandomAccessFile("./korpus", "r");

        readI1.skipBytes(posLow);
        byte length_pointer;

        if(posLow == posHigh) {
            posHigh = (int)readI1.length();
            System.out.println(posHigh);
        }


        while(posLow < posHigh) {

            length_pointer = readI1.readByte();

            if(length_pointer != word.length()) {
                readI1.skipBytes(4 + 4 + length_pointer);
                // If -> kolla att vi inte är på EOF, isf break

            }
            else {

                int nrOfOcc = readI1.readInt();
                int posI2 = readI1.readInt();
                byte[] foundWord = new byte[length_pointer];

                for(int i = 0; i < length_pointer; i++) {
                    foundWord[i] = readI1.readByte();
                }

                String fWord = new String(foundWord, StandardCharsets.ISO_8859_1);

                if(word.equals(fWord)) {

                    System.out.println("Det finns " + nrOfOcc + " förekomster av ordet");

                    // Används i fallet då vi har >25 förekomster,
                    if(nrOfOcc > 25) {
                        System.out.println("Ska alla förekomster skrivas ut?" +
                                "\nSkriv 'nej' för att avsluta, tryck annars på 'Enter' ");
                        Scanner input = new Scanner(System.in);
                        String answer = input.nextLine();

                        if(answer.equals("nej")){
                            System.out.println("Program avslutad");
                            System.exit(0);
                        }
                    }

                    readI2.skipBytes(posI2);

                    int readPos = 0;

                    for(int i = 0; i < nrOfOcc; i++) {
                        readPos = readI2.readInt();
                        printKorp(readPos, readKorp, length_pointer);
                    }

                    System.exit(0);

                }
            }
            posLow += 1 + 4 + 4 + length_pointer;
        }
        System.out.println("Ord finns ej");

    }

    // Funktion för att skriva ut det valda ordet i Korpus filen tillsammans med 30 byte före och efter
    public static void printKorp(int pos, RandomAccessFile korp, byte length) throws IOException {

        int a = 0;
        long b = korp.length();
        int c;
        byte read;

        // Kollar om + 30 positioner efter där vi står går EOF
        if(pos + 30 > b) {
            c = (int) ((pos+30) % b);
        }
        else {
            c = 30;
        }

        // Kollar om 30 bytes före där vi står är mindre än 0.
        // Läser sedan in en Byte i taget för att kolla ev. newline-tecken(#10) och ersätter det med blankspace(#9) och printar ut arrayens innehåll
        if(pos - 30 < 0) {
            a -= (pos - 30);


            byte[] array = new byte[a + length + 30];
            korp.seek(0);

            for(int i = 0; i < array.length; i++) {

                read = korp.readByte();
                if(read == 10)
                    array[i] = 32;
                else
                    array[i] = read;
            }
            String str = new String(array, StandardCharsets.ISO_8859_1);
            System.out.println(str);
        }
        else {
            a = pos -30;
            byte[] array = new byte[30 + length + c];
            korp.seek(a);
            for(int i = 0; i < array.length; i++) {

                read = korp.readByte();
                if(read == 10)
                    array[i] = 32;
                else
                    array[i] = read;
            }
            String str = new String(array, StandardCharsets.ISO_8859_1);
            System.out.println(str);
        }
    }
}
