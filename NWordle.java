import java.util.*;

public class NWordle{
    // All colors used in the game
    static String GREEN = "\u001B[32m";
    static String YELLOW = "\u001B[33m";
    static String RESET = "\u001B[0m";
    static String RED = "\u001B[31m";  
    static String  GREEN_BACKGROUND = "\u001B[42m"; 
public static void main(String[] args){

    String currentGuess = "";
    int Guesses = 0;
    int Words = 0;
    boolean continues = false;
    Scanner Guess = new Scanner(System.in);
    while(continues == false){
    System.out.println("Do you want to guess a 4 letter word or a 5 letter word? And how many words do you want to guess? ");
    System.out.println(GREEN + "GREEN = Correctly Placed," + YELLOW + " YELLOW = Incorrect Placed" + RED + " RED = Letter not in word" + RESET);
    Guesses = Guess.nextInt();
    Words = Guess.nextInt();
    if (Guesses == 4 || Guesses == 5 && Words >= 1) {continues = true;}
    else {System.out.println("Invalid input. The word must either be 4 or 5 letters long");}
    }

    Guess.nextLine();

    ArrayList<String> megaWordList = Methods.theword_list("allWords.txt", Guesses);
    Object[] ArrayML = megaWordList.toArray();

    ArrayList<String> theWordList = Methods.theword_list("guessWords.txt", Guesses);
    ArrayList<String> allRandomWords = new ArrayList<String>();
    for (int i = 0; i < Words; i++){
        int index = (int)(Math.random()*theWordList.size());
        allRandomWords.add(theWordList.get(index).toUpperCase());
    }

    ArrayList<ArrayList<String>> allColorWords = new ArrayList<ArrayList<String>>();
    
    
    ArrayList<Character> allLettersList = new ArrayList<Character>(Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q','R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'));
    ArrayList<Character> usedLettersList = new ArrayList<Character>();
    ArrayList<Set<Character>> megaSet = new ArrayList<Set<Character>>();

    ArrayList<Boolean> Valids = new ArrayList<Boolean>();
    for(int i = 0; i < Words; i++) {
        ArrayList<String> x = new ArrayList<String>();
        allColorWords.add(x);
        Boolean y = false;
        Valids.add(y);
    }
    for(int x = 0; x < Guesses + Words; x++) {
        
        if (x < (Guesses + Words) && (Methods.AnyFalse(Valids))){
            boolean length = false;

            do {
            System.out.print("Guess " + (x+1) + ":");
            String Ans = Guess.nextLine();
            currentGuess = Ans.toUpperCase();
            boolean inList = Arrays.asList(ArrayML).contains(currentGuess);
            length = Methods.lengthCheck(currentGuess, Guesses);
            if (inList == false) {length = false;}
            if (length == false) {System.out.println("Invalid word length or word not in dictionary! Try again.");}
        } while (length == false);

            for(int i = 0; i < Words; i++) {
            String color = Methods.Checker(allRandomWords.get(i), currentGuess, Valids.get(i));
            allColorWords.get(i).add(color);

            if(allRandomWords.get(i).equals(currentGuess) && Valids.get(i) == false) {
                    Valids.set(i, true); 
                }
            }
            currentGuess = currentGuess.toUpperCase();
            for(int j = 0; j < currentGuess.length(); j++) {usedLettersList.add(currentGuess.charAt(j));}

            Set<Character> allSetLetters = new HashSet<>(allLettersList);
            allSetLetters.removeAll(usedLettersList);
            megaSet.add(allSetLetters);

            Methods.Display(allColorWords, megaSet);

        }
    }

    System.out.println();
    if(Methods.AnyFalse(Valids)) {System.out.println("You ran out of guesses! The words were " + allRandomWords);}
    else {System.out.println("Congratulations! You guessed all the words! ");}
    Guess.close();

}
}
