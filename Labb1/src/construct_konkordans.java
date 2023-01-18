import java.io.*;
import java.nio.charset.StandardCharsets;

public class construct_konkordans {


    public static void main(String[] args) throws IOException {

        long timeStart = System.nanoTime();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.ISO_8859_1));
        gen_files generate = new gen_files();
        generate.gen_fil(reader);

        long timeStop = System.nanoTime();

        System.out.println("Tid för gen-filer: " + (timeStop-timeStart)/1000000000);

    }
}




