package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

import model.GameLogic;



public class SyncController {
  GameLogic logic;
  Readable read;
  Appendable app;
  int wordLength;
  int numberOfWords;
  Scanner scan;
  static String GREEN = "\u001B[32m";
  static String YELLOW = "\u001B[33m";
  static String RESET = "\u001B[0m";
  static String RED = "\u001B[31m";


  public SyncController(GameLogic logic, Readable read, Appendable app) {
    this.read = Objects.requireNonNull(read);
    this.app = Objects.requireNonNull(app);
    this.logic = logic;
    this.wordLength = 0;
    this.numberOfWords = 0;
    this.scan = new Scanner(read);
    this.askStartInputs();
  }

  // asks the starting inputs to correctly start the game with appropriate word lengths/guesses.
  private void askStartInputs() {
    boolean continues = true;
    while (continues) {
      writeMessage("Do you want to guess a 4 letter word or a 5 letter word? And how many words do you want to guess? \n");
      writeMessage(GREEN + "GREEN = Correctly Placed," + YELLOW + " YELLOW = Incorrect Placed" + RED + " RED = Letter not in word" + RESET + "\n");
      this.wordLength = scan.nextInt();
      this.numberOfWords = scan.nextInt();
      if ((this.wordLength == 4 || this.wordLength == 5) && this.numberOfWords >= 1) {
        continues = false;
      } else {
        writeMessage("Invalid input. The word must either be 4 or 5 letters long\n");
      }
    }
    scan.nextLine();
  }

  // starts running the main game until the user loses or guesses all words correctly.
  public void execute() {
    logic.setWordsLength(this.numberOfWords, this.wordLength);

    // Creates a ArrayList of all letters and then also of characters that the user has used so far
    ArrayList<Character> allLettersList = new ArrayList<>(Arrays.asList('A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z'));
    ArrayList<Character> usedLettersList = new ArrayList<>();
    ArrayList<Set<Character>> megaSet = new ArrayList<>();

    String currentGuess;

    // Starts running the main game
    for (int x = 0; x < wordLength + numberOfWords; x++) {
      // runs this if any of the words are left to be guessed correctly
      if (logic.AnyFalse()) {
        boolean validLength;
        // keeps asking the user for a guess as long as their input guess is invalid
        do {
          if (x < 9) {
            writeMessage("Guess 0" + (x + 1) + ": ");
          } else {
            writeMessage("Guess " + (x + 1) + ": ");
          }
          String Ans = scan.nextLine();
          currentGuess = Ans.toUpperCase();
          validLength = logic.validCheck(currentGuess, wordLength);
          if (!validLength) {
            writeMessage("Invalid word length or word not in dictionary! Try again.\n");
          }
        } while (!validLength);

        logic.colorize(currentGuess);

        // adds the letters of this guess to the list of used letters
        currentGuess = currentGuess.toUpperCase();
        for (int j = 0; j < currentGuess.length(); j++) {
          usedLettersList.add(currentGuess.charAt(j));
        }

        // removes the used letters so the user can see what letters they haven't guessed yet
        Set<Character> allSetLetters = new HashSet<>(allLettersList);
        allSetLetters.removeAll(usedLettersList);
        megaSet.add(allSetLetters);

        // displays the current state of the game
        logic.display(megaSet, this.app);
      }
    }

    logic.showResults(this.app);
    scan.close();
  }

  // consolidates appending strings to the appendable into a method to avoid repetition.
  private void writeMessage(String msg)  {
    try {
      this.app.append(msg);
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }
}

