import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Methods extends NWordle{

    public static boolean lengthCheck(String currentGuess, int GuessLength){
        boolean valid = false;
        if(currentGuess.length() == GuessLength) {valid = true;}
        return valid;
    }

    // Returns the colored word based on location of the letters in the guess and the actual word
    static String Checker(String word, String currentGuess, boolean Validity){
        currentGuess = currentGuess.toUpperCase();
        String varWord, newGuess, mainColor;
        varWord = newGuess = mainColor = "";
        ArrayList<Integer> newAllWordLetterPosition = new ArrayList<Integer>();

        // Creates new words of the original word and the guess with matching letters replaced with '1' 
        for(int j = 0; j < currentGuess.length(); j++) {
            char letter = currentGuess.charAt(j);
            int value = word.substring(j).indexOf(letter);
            if (value != -1) {value += j;}
            if (value == j) {
                newGuess += '1';
                varWord += '1';
            }
            else {newGuess += currentGuess.charAt(j);
                  varWord += word.charAt(j);  }
        }

        // Creates a new ArrayList matching the index of each letter of newColors with the original word
        for(int j = 0; j < currentGuess.length(); j++) {
            char letter = newGuess.charAt(j);
            int value = varWord.indexOf(letter);
            if('1' != varWord.charAt(j) && value != -1) {newAllWordLetterPosition.add(-2);}
            else {newAllWordLetterPosition.add(value);}
            if (value != -1) {
                varWord = varWord.substring(0, value) + '0' + varWord.substring(value + 1);
                }
        }

        // Assigns colors to each letter of the guess based on the previous variables
        for(int j = 0; j < currentGuess.length(); j++) {
            if (newGuess.charAt(j) == '1') {
                mainColor += GREEN + currentGuess.charAt(j) + RESET;
            }
            else if (newAllWordLetterPosition.get(j) == -1) {    
                mainColor += RED + currentGuess.charAt(j) + RESET;
            }
            else {mainColor += YELLOW + currentGuess.charAt(j) + RESET;}
        }

        if(currentGuess.equals(word)) {
            String newMainColor = "";
            for(int k = 0; k < currentGuess.length(); k++) {
                newMainColor += GREEN_BACKGROUND + currentGuess.charAt(k) + RESET;
            }
            mainColor = newMainColor;
        }

        if(Validity) {return currentGuess;}
        else return mainColor;
    }
    
    // Returns all words of the same length as the inputted word length from a text file of words.
    public static ArrayList<String> theword_list(String filename, int wordLength){
        try {
            String[] words = new String(Files.readAllBytes(Paths.get(filename))).split(" ");
            ArrayList<String> actualWords = new ArrayList<String>();
            for(int j = 0; j < words.length; j++){
                if (words[j].length() == wordLength) {actualWords.add(words[j]);}
                else {continue;}
            }
            return actualWords;
        }
        catch(Exception e){
            System.out.println("Error 404: File \"words.txt\" not found");
            return null;
        }
    }
    
    // Displays the word with hints using colors, and also shows the unused letters in an ArrayList
    public static void Display(ArrayList<ArrayList<String>> allColorWords, ArrayList<Set<Character>> allArray){
        ArrayList<String> intStrings = new ArrayList<>();
        for(int j = 0; j < allColorWords.get(0).size() + allColorWords.get(0).get(0).length(); j++){
            if(j >= 0 && j <= 8) {intStrings.add("Guess 0" + (j+1) + ": ");}
            else {intStrings.add("Guess " + String.valueOf(j+1) + ": ");}
        }
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
        for(int j = 0; j < allColorWords.get(0).size(); j++) {
            System.out.print(intStrings.get(j));
            for (int x = 0; x < allColorWords.size(); x++) {
                System.out.print(allColorWords.get(x).get(j) + " "); 
            }
            System.out.println(" Unused Letters: "+ allArray.get(j));
        }
    }

    // Returns true if any boolean values in a array of booleans is false.
    public static boolean AnyFalse(ArrayList<Boolean> Valids) {
        boolean value = false;
        for (int i = 0; i < Valids.size(); i++) {
            if (!Valids.get(i)) {
                value = true;
                return value;
            }
            else continue;
        }
        return value;
    }
}