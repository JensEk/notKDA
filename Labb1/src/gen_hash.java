import java.util.Arrays;
import java.util.Locale;

public class gen_hash {


    // Latmanshashar första 3 bokstäverna med små bokstäver
    public static int latHash (String word){

            /* end = 3 gör att vi alltid tar de 3 första bokstäver i ett ord med längd > 3
            om längd är <= 3 så tar vi den faktiska längden.
            */
        int end = 3;
        if (word.length() <= 3)
            end = word.length();

        String firstLetters = word.substring(0, end);

        char[] letters = {' ', ' ', ' '};

            /* for-loop fyller i char[] letters från höger till vänster beroende på längd av word,
            tex: "a" -> [_ , _, a] ; "ab" -> [_ , a, b] ; "abc" -> [a , b, c]
             */
        for (int i = 0; i < firstLetters.length(); i++) {
            letters[i] = firstLetters.toLowerCase(Locale.ROOT).charAt(i);
        }

        //returnera ett hashvärde på teckenkobinationen i letters enligt given funktion
        return eval_let(letters[0]) * 900 + eval_let(letters[1]) * 30 + eval_let(letters[2]);
    }


    // Returnera varje enskild tecken-värde
    private static int eval_let ( char let) {

            switch (let) {
                case ' ':
                    return 0;
                case (char)229:
                    return 27;
                case (char)228:
                    return 28;
                case (char)246:
                    return 29;
                default:
                    return (let - 'a' + 1);
        }

    }


}
