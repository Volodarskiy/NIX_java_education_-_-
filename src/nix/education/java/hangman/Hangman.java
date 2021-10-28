package nix.education.java.hangman;

import java.util.ArrayList;
import java.util.Scanner;

public class Hangman {
    private final Scanner scan = new Scanner(System.in);
    private String trueWord;
    private final StringBuilder emptyToWord = new StringBuilder();
    private int HP;

    public static void main(String[] args)
    {
        Hangman game = new Hangman(8);
        game.startMainMenu();
    }

    protected void startMainMenu()
    {
        System.out.print("Type \"play\" to play the game, \"exit\" to quit:");
        String command = scan.next();
        switch(command.trim()) {
            case "play":
                this.startGame();
                break;
            case "exit":
                break;
            default: this.startMainMenu();
        }
    }

    protected void startGame()
    {
        String rightWord = getRandWord();

        System.out.println("\nHANGMAN\nThe game will be available soon.\n");

        this.setEmptyToWord(rightWord);
        this.trueWord = rightWord;
        this.changeWord();
    }

    protected void changeWord()
    {
        String dashes = "-";

        this.emptyToWord.replace(0, this.emptyToWord.length(), dashes.repeat(this.emptyToWord.length()));
        this.guessWord();
    }
    protected void guessWord()
    {
        if(this.HP == 0)
        {
            System.out.println("Thanks for playing!\nWe'll see how well you did in the next stage");
            return;
        }

        System.out.printf("HANGMAN\n%s\nInput a letter:", this.emptyToWord.subSequence(0, this.trueWord.length()));

        String letter = (scan.next().trim());

        if(letter.length() != 1)
        {
            System.out.println("You should input a single letter.");
            this.guessWord();
            return;
        }

        if(!isLowerCaseEnglish(letter.charAt(0)))
        {
            System.out.println("Please enter a lower case English letter.");
            this.guessWord();
        }
        else checkLetter(letter);
    }

    private void checkLetter(String letter) {

        if(this.emptyToWord.indexOf(letter) != -1)
        {
            System.out.println("You've already guessed this letter.");
            this.guessWord();
        }
        else getResult(letter);

    }

    private void getResult(String letter) {

        StringBuilder sb = new StringBuilder(this.trueWord);
        int result = sb.indexOf(letter);

        if(result == -1) {
            this.HP--;
            System.out.println("That letter doesn't appear in the word");
        }
        else{
            while(result >= 0)
            {
                result = sb.indexOf(letter);
                if(result != -1)
                {
                    sb.replace(result, result + 1, " ");
                    this.emptyToWord.replace(result, result + 1, letter);
                }
                else break;
            }
        }
        this.emptyToWord.append(letter);

        if(this.emptyToWord.indexOf("-") != -1)
        {
            this.guessWord();
        }
        else System.out.printf("\nYou guessed word %s!\nYou survived!", this.emptyToWord.substring(0, this.trueWord.length()));
    }

    private String getRandWord() {

        ArrayList<String> wordList = new ArrayList<>();

        wordList.add("java");
        wordList.add("python");
        wordList.add("javascript");
        wordList.add("kotlin");

        return wordList.get(getRandomNumber(wordList.size()));
    }
    public Hangman(int Health)
    {
        this.HP = Health;
    }
    private int getRandomNumber(int num)    //Случайное число по num не включительно
    {
        return (int) (Math.random() * num);
    }

    private void setEmptyToWord(String word)
    {
        this.emptyToWord.replace(0, this.emptyToWord.length(), word);
    }
    private boolean isLowerCaseEnglish(char c){
        return c >= 'a' && c <= 'z';
    }
}
