/* Labb 2 i DD2350 Algoritmer, datastrukturer och komplexitet    */
/* Se labbinstruktionerna i kursrummet i Canvas                  */
/* Ursprunglig författare: Viggo Kann KTH viggo@nada.kth.se      */


/* Den ursprungliga koden skriven av Viggo Khan är modifierad av Stefan Garrido */
/* och Jens Ekenblad enligt laborationsinstruktionerna. Modifierade funktioner är: */
/* partDist och ClosestWords. Tillagd funktion skriven av Garrido/Ekenblad: sameChars. */

import java.util.LinkedList;
import java.util.List;

public class ClosestWords {
  LinkedList<String> closestWords = null;

  int closestDistance = -1;

  //Global variabel som används i partDist
  String lastStoredWord = "";


  // Dynprog av partDist som tar beräkningsordning: Första raden - > sista raden.
  int partDist(String w1, String w2, int w1len, int w2len, int[][] M) {

    //skippPos ger oss ett värde på hur många steg vi kan skippa när matris M skapas.
    int skippPos = sameChars(w2, lastStoredWord);

    // Rad 1 -> w1len
    for(int i = 1; i <= w1len; i++) {

      // Kolumn 1 -> w2len
      for(int j = skippPos +1; j <= w2len; j++){

        //Vi beräknar fram operationer för utbyte, lägga till och borttagning av ett tecken
        //Den operationer som är minst sparas i matrisen m.
        int swap = M[i-1][j-1] + (w1.charAt(i-1) == w2.charAt(j-1) ? 0 : 1);
        int add =  M[i-1][j]+1;
        int remove =  M[i][j-1]+1;

        M[i][j] = Math.min(Math.min(swap, add), remove);

      }
    }
    //w2 som kommer från wordList i ClosestWord sparas i lastStoredWord för användning i sameChars.
    lastStoredWord = w2;
    //returnera den sista platsen i matrisen vilket representerar minst # operationer mellan w1 och w2
    return M[w1len][w2len];
  }

  // Funktionen kontrollerar hur många bokstäver som är likadana mellan den nya ordet i
  // wordList (se ClosestWords) och den senaste sparade i lastStoredWord. Detta möjligör att vi
  // kan spara tid när matris M skapas, detta då operationsberäkningarna är likadana i matrisen M för dom
  // tecken som är likadana
  int sameChars(String w2, String lastWord) {
    int len = Math.min(w2.length(), lastWord.length());
    for(int i = 0; i < len; i++)
      if(w2.charAt(i) != lastWord.charAt(i))
        return i;
    return 0;
  }

  int distance(String w1, String w2, int[][] M) {
    return partDist(w1, w2, w1.length(), w2.length(), M);
  }


  // Skapar en 40x40 matris eftersom inget ord är > 39 längd och som sedan återanvänds i programmet.
  public ClosestWords(String w, List<String> wordList) {

    //Då inget ord är 40 tecken eller längre skapas en matris i förväg med storlek 40*40
    int[][] M = new int[40][40];

    //Utkanten av matrisen fylls med i = 0 till i=39, detta för att täcka basfallen då en av strängarna är tomma.
    for(int i = 0; i < 40; i++) {
      M[i][0] = i;
      M[0][i] = i;
    }

    for (String s : wordList) {

      // Skippar de förslag som har längre editeringsavstånd än den beräknade closestDistance
      if(Math.abs(w.length() - s.length()) > closestDistance && closestDistance != -1) {
        continue;
      }

      int dist = distance(w, s, M);
      // System.out.println("d(" + w + "," + s + ")=" + dist);
      if (dist < closestDistance || closestDistance == -1) {
        closestDistance = dist;
        closestWords = new LinkedList<String>();
        closestWords.add(s);
      }
      else if (dist == closestDistance)
        closestWords.add(s);
    }
  }

  int getMinDistance() {
    return closestDistance;
  }

  List<String> getClosestWords() {
    return closestWords;
  }
}
