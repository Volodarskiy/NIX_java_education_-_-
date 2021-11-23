package nix.education.java.tictactoe;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Tictactoe {

    Map<String, Character> matrix = new LinkedHashMap<>();
    char Player = 'X';

    public static void main(String[] args) {
        Tictactoe game = new Tictactoe();
        game.waitCell();
    }

    public Tictactoe() {
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                matrix.put(String.format("%d %d", j, i), '_');
            }
        }
        this.getStatus();
    }

    public void getStatus() {
        String gameStatus = "---------\n| %c %c %c |\n| %c %c %c |\n| %c %c %c |\n---------\n";
        System.out.printf(gameStatus, matrix.get("1 1"), matrix.get("1 2"), matrix.get("1 3"), matrix.get("2 1"), matrix.get("2 2"), matrix.get("2 3"), matrix.get("3 1"), matrix.get("3 2"), matrix.get("3 3"));
    }

    public void waitCell() {
        final Scanner scan = new Scanner(System.in);

        while (!checkResult()) {
            System.out.printf("(Player %c) Enter the coordinates:\n", Player);

            String input = scan.nextLine().trim();
            this.checkCommand(input);
        }
    }

    private void checkCommand(String input){

        if (input.isEmpty()) {
            return;
        }

        switch (isNeedNumb(input)) {
            case -1:
                System.out.println("You should enter numbers!");
                return;
            case 0:
                System.out.println("Coordinates should be from 1 to 3!");
                return;
        }

        if (matrix.get(input) == '_') {
             matrix.put(input, Player);
             this.getStatus();
             Player = Player == 'X' ? '0':'X';
        } else {
                System.out.println("This cell is occupied! Choose another one!");
        }
    }

    private boolean checkResult() {
        StringBuilder resultString = new StringBuilder();
        char winner = this.isVictory();

        switch(winner){
            case 'n':
                if (matrix.containsValue('_')) {
                    return false;
                }
                resultString.append("Draw");
                break;
            case 'X', '0':
                resultString.append(winner).append(" wins");
                break;
            case 'i':
                resultString.append("Impossible!");
                break;
            default: return false;
        }
        System.out.println(resultString);
        return true;
    }

    private char isVictory() {
        char winner = 'n';

        for (int i = 1; i <= 3; i++) {
            String key1 = String.format("%d 1", i);
            String key2 = String.format("%d 2", i);
            String key3 = String.format("%d 3", i);
            String key1i = String.format("1 %d", i);
            String key2i = String.format("2 %d", i);
            String key3i = String.format("3 %d", i);

            if (matrix.get(key1) == matrix.get(key2) && matrix.get(key1) == matrix.get(key3)) {
                winner = setWinner(winner, matrix.get(key1));
            }
            if (matrix.get(key1i) == matrix.get(key2i) && matrix.get(key1i) == matrix.get(key3i)) {
                winner = setWinner(winner, matrix.get(key1i));
            }
            if (matrix.get("1 1") == matrix.get("2 2") && matrix.get("1 1") == matrix.get("3 3")) {
                winner = setWinner(winner, matrix.get("1 1"));
            }
            if (matrix.get("1 3") == matrix.get("2 2") && matrix.get("1 3") == matrix.get("3 1")) {
                winner = setWinner(winner, matrix.get("1 3"));
            }
        }

        return winner;
    }

    private char setWinner (char win, char result)
    {
        if(result == '_') {
            return 'n';
        }
        if(win == 'n' || win == result) {
            return result;
        }
        return 'i';
    }

    private int isNeedNumb (String mess) {
        int num = 1;
        for(int i = 0; i < mess.length(); i++) {
            if (mess.charAt(i) == ' ') {
                continue;
            }
            if (mess.charAt(i) > '9' || mess.charAt(i) < '0') {
                num = -1;
            }else if(mess.charAt(i) > '3' || mess.charAt(i) < '1' || !matrix.containsKey(mess)) {
                num = 0;
            }
        }
        return num;
    }
}
