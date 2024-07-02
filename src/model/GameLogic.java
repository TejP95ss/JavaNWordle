package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GameLogic {
  // All font and background colors used in the game
  static String GREEN = "\u001B[32m";
  static String YELLOW = "\u001B[33m";
  static String RESET = "\u001B[0m";
  static String RED = "\u001B[31m";
  static String GREEN_BACKGROUND = "\u001B[42m";

  int numberOfWords = 0;
  int wordLength = 0;

  ArrayList<String> megaWordList = new ArrayList<>();
  ArrayList<String> theWordList = new ArrayList<>();
  ArrayList<ArrayList<String>> allColorWords = new ArrayList<>();
  ArrayList<Boolean> validWords = new ArrayList<>();
  ArrayList<String> allRandomWords = new ArrayList<>();

  public GameLogic() {
  }

  // colorizes all words to match the users cumulative guessing and also checks if the guess is equal to a word
  public void colorize(String currentGuess) {
    for (int i = 0; i < numberOfWords; i++) {
      String color = this.Checker(allRandomWords.get(i), currentGuess, validWords.get(i));
      allColorWords.get(i).add(color);
      if (allRandomWords.get(i).equals(currentGuess) && !validWords.get(i)) {
        validWords.set(i, true);
      }
    }
  }

  // sets the word length and number of words being guessed for this particular round.
  public void setWordsLength(int numberOfWords, int wordLength) {
    this.numberOfWords = numberOfWords;
    this.wordLength = wordLength;
    this.megaWordList = this.theWordList("res/allWords.txt", wordLength); // all possible words.
    this.theWordList = this.theWordList("res/guessWords.txt", wordLength); // guess words.
    for (int i = 0; i < numberOfWords; i++) {
      ArrayList<String> x = new ArrayList<>();
      this.allColorWords.add(x);
      this.validWords.add(false);
      int index = (int) (Math.random() * this.theWordList.size());
      this.allRandomWords.add(this.theWordList.get(index).toUpperCase());
    }
  }


  // checks if the guess's length is correct and that the guess is an actual word
  public boolean validCheck(String currentGuess, int GuessLength) {
    return currentGuess.length() == GuessLength && this.megaWordList.contains(currentGuess);
  }

  // Returns the colored word based on location of the letters in the guess and the actual word
  public String Checker(String word, String currentGuess, boolean Validity) {
    currentGuess = currentGuess.toUpperCase();
    String varWord, newGuess, mainColor;
    varWord = newGuess = mainColor = "";
    ArrayList<Integer> newAllWordLetterPosition = new ArrayList<Integer>();

    // Creates new words of the original word and the guess with matching letters replaced with '1'
    for (int j = 0; j < currentGuess.length(); j++) {
      char letter = currentGuess.charAt(j);
      int value = word.substring(j).indexOf(letter);
      if (value != -1) {
        value += j;
      }
      if (value == j) {
        newGuess += '1';
        varWord += '1';
      } else {
        newGuess += currentGuess.charAt(j);
        varWord += word.charAt(j);
      }
    }

    // Creates a new ArrayList matching the index of each letter of newColors with the original word
    for (int j = 0; j < currentGuess.length(); j++) {
      char letter = newGuess.charAt(j);
      int value = varWord.indexOf(letter);
      if ('1' != varWord.charAt(j) && value != -1) {
        newAllWordLetterPosition.add(-2);
      } else {
        newAllWordLetterPosition.add(value);
      }
      if (value != -1) {
        varWord = varWord.substring(0, value) + '0' + varWord.substring(value + 1);
      }
    }

    // Assigns colors to each letter of the guess based on the previous variables
    for (int j = 0; j < currentGuess.length(); j++) {
      if (newGuess.charAt(j) == '1') {
        mainColor += GREEN + currentGuess.charAt(j) + RESET;
      } else if (newAllWordLetterPosition.get(j) == -1) {
        mainColor += RED + currentGuess.charAt(j) + RESET;
      } else {
        mainColor += YELLOW + currentGuess.charAt(j) + RESET;
      }
    }

    if (currentGuess.equals(word)) {
      String newMainColor = "";
      for (int k = 0; k < currentGuess.length(); k++) {
        newMainColor += GREEN_BACKGROUND + currentGuess.charAt(k) + RESET;
      }
      mainColor = newMainColor;
    }

    if (Validity) {
      return currentGuess;
    } else return mainColor;
  }

  // Returns all words of the same length as the inputted word length from a text file of words.
  public ArrayList<String> theWordList(String filename, int wordLength) {
    try {
      String[] words = new String(Files.readAllBytes(Paths.get(filename))).split(" ");
      ArrayList<String> actualWords = new ArrayList<>();
      for (String word : words) {
        if (word.length() == wordLength) {
          actualWords.add(word);
        }
      }
      return actualWords;
    } catch (Exception e) {
      System.out.println("Error 404: File \"words.txt\" not found");
      return null;
    }
  }

  // Displays the word with hints using colors, and also shows the unused letters in an ArrayList
  public void display(ArrayList<Set<Character>> allArray, Appendable app) {
    ArrayList<String> intStrings = new ArrayList<>();
    for (int j = 0; j < this.allColorWords.get(0).size() + this.allColorWords.get(0).get(0).length(); j++) {
      if (j <= 8) {
        intStrings.add("Guess 0" + (j + 1) + ": ");
      } else {
        intStrings.add("Guess " + (j + 1) + ": ");
      }
    }
    writeMessage("\033[H\033[2J", app);
    System.out.flush();
    for (int j = 0; j < this.allColorWords.get(0).size(); j++) {
      writeMessage(intStrings.get(j), app);
      for (ArrayList<String> allColorWord : this.allColorWords) {
        writeMessage(allColorWord.get(j) + " ", app);
      }
      writeMessage(" Unused Letters: " + allArray.get(j) + "\n", app);
    }
  }

  // Returns true if any boolean values in an array of booleans is false.
  public boolean AnyFalse() {
    return this.validWords.stream().anyMatch(b -> !b);
  }

  // Prints out the appropriate message based on whether the user guessed all words correctly or not
  public void showResults(Appendable app) {
    writeMessage("\n", app);
    if (this.AnyFalse()) {
      writeMessage("You ran out of guesses! The words were \n" + allRandomWords, app);
    } else {
      writeMessage("Congratulations! You guessed all the words! \n", app);
    }
  }

  // consolidates appending strings to the appendable into a method to avoid repetition.
  private void writeMessage(String msg, Appendable app) {
    try {
      app.append(msg);
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }
}