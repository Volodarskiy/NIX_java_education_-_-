package feature.banking_system;

import java.sql.*;
import java.util.Scanner;

public class BankingSystem {

    private static final Scanner scan = new Scanner(System.in);
    private static final Connect db = new Connect();
    private Account user;

    public static void main(String[] args) {
        Main.main(args);
    }

    public void enterMenu() {
        db.createTable();

        boolean exit;

        do {
            if(user == null) {
                exit = this.unLogMenu();
            }
            else {
                exit = this.clientMenu();
            }
        }
        while (!exit);
    }

    private boolean unLogMenu() {
        System.out.println("\n1.Create an account \n2.Log into account \n0.Exit");
        String command = scan.next();

        if (command.isEmpty()) {
            return false;
        }

        switch (command.trim()) {
            case "2":
                this.logIn();
                break;
            case "1":
                this.addClient();
                break;
            case "0":
                this.chooseExit();
                return true;
            default:
                System.out.printf("[Error] Unknown command: %s\n", command);
        }
        return false;
    }

    private void addClient() {
        Account client = new Account();

        while (db.cardInBase(client.getSerial()) != null) {
            client.generateID();
        }
        db.inputAcc(client);
    }

    private void logIn() {
        System.out.println("Enter your card number:");
        String card = scan.next();
        ResultSet result = db.cardInBase(card);

        if(result != null) {
            System.out.println("Enter your PIN:");
            String pin = scan.next();
            String pinFromBase = db.getStr(result, "pin");

            if (pinFromBase.equals(pin)) {
                this.user = new Account(card, pin, db.getBalance(result));
                System.out.println("You have successfully logged in!");
            } else {
                System.out.println("[Error] Wrong PIN!");
            }
        }
        else {
            System.out.println("[Error] Wrong card number!");
        }
    }

    private boolean clientMenu() {
        System.out.println("\n1.Balance \n2.Add income \n3.Do transfer \n4.Close account \n5.Log out \n0.Exit");

        String command = scan.next();

        switch (command.trim()) {
            case "1":
                double balance = this.user.getBalance();
                System.out.printf("Balance: %.2f\n", balance);
                break;
            case "2":
                doIncome(this.user);
                break;
            case "3":
                doTransfer(this.user);
                break;
            case "4":
                db.deleteCard(this.user);
                this.user = null;
                break;
            case "5":
                this.user = null;
                break;
            case "0":
                this.chooseExit();
                return true;
            default:
                System.out.printf("[Error] Unknown command: %s\n", command);
        }
        return false;
    }

    private void doIncome(Account user) {
        System.out.println("Enter income:");
        double addSum = scan.nextDouble();

        user.setBalance(user.getBalance() + addSum);
        db.updateBalance(user);
        System.out.println("Income successfully completed");
    }

    private void doTransfer(Account user) {
        System.out.println("Enter card number:");
        String card = scan.next();
        ResultSet result = db.cardInBase(card);

        if(result != null) {
            Account trUser = new Account(card, db.getStr(result, "pin"), db.getBalance(result));

            System.out.println("Enter how much money you want to transfer:");
            double sum = scan.nextDouble();

            if(user.getBalance() - sum < 0) {
                System.out.println("Not enough money!");
                return;
            }
            user.setBalance(user.getBalance() - sum);
            trUser.setBalance(trUser.getBalance() + sum);

            db.updateBalance(user);
            db.updateBalance(trUser);
            System.out.println("Transfer successfully completed");
        }
        else {
            System.out.println("Wrong card number!");
        }
    }

    private void chooseExit() {
        db.closeConnect();
        System.out.println("Bye!");
    }
    static class Main {

        public static void main(String[] args) {
            Thread BankSys = new Thread(new CustomThread(), "Banking System");
            BankSys.start();
        }
    }
}

class Account {
    private String id;
    private final String pin;
    private double balance;

    public Account() {
        generateID();
        this.pin = getRandomID(10000);
        this.balance = 0;

        System.out.printf("Your card has been created \nYour card number: %s \nYour card PIN: %s\n", this.id, this.pin);
    }

    public Account(String card, String pin, double balance) {
        id = card;
        this.pin = pin;
        this.balance = balance;
    }

    public void generateID() {
        String bin = "400000";
        String ai = getRandomID(1000000000);
        String checksum = getCheckSum(bin+ai);

        this.id = bin+ai+checksum;
    }

    private String getRandomID(int num)
    {
        StringBuilder sb = new StringBuilder();

        int length = Integer.toString(num-1).length();
        sb.append("0".repeat(length));
        String genID = Integer.toString((int) (Math.random() * num));

        sb.replace(length-genID.length(), length, genID);

        return sb.toString();
    }

    private String getCheckSum(String input) {
        int checkSum = 0;

        for(int i = 0; i < input.length(); i++) {
            int num = (input.charAt(i) - '0');

            if(i % 2 == 0) {
                num *= 2;

                if(num >= 10) {
                    num -= 9;
                }
            }
            checkSum += num;
        }
        checkSum %= 10;
        if(checkSum > 0) {
            checkSum = 10 - checkSum;
        }
        return Integer.toString(checkSum);
    }

    public double getBalance() {
        return balance;
    }

    public String getSerial() {
        return id;
    }

    public String getPin() {
        return pin;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}

class Connect {

    private static final String URL = "jdbc:sqlite:card.db";
    private Connection con = null;

    public Connect() {
        this.connect();
    }

    public void connect() {
        try {
            con = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void closeConnect() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS cards(\nid integer PRIMARY KEY,\nnumber text NOT NULL,\npin text NOT NULL,\nbalance real)";

        try {
            Statement statement = con.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void inputAcc (Account account) {
        if(account.getSerial().isEmpty() || account.getPin().isEmpty()) {
            return;
        }
        String query = "INSERT INTO cards(number, pin, balance) VALUES(?,?,?)";

        try {
            PreparedStatement statement = con.prepareStatement(query);

            statement.setString(1, account.getSerial());
            statement.setString(2, account.getPin());
            statement.setDouble(3, account.getBalance());
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateBalance (Account account) {
        String query = "UPDATE cards SET balance = ? WHERE number = ?";

        try {
            PreparedStatement statement = con.prepareStatement(query);

            statement.setDouble(1, account.getBalance());
            statement.setString(2, account.getSerial());
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteCard (Account account) {
        String query = "DELETE FROM cards WHERE number = ?";

        try {
            PreparedStatement statement = con.prepareStatement(query);

            statement.setString(1, account.getSerial());
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ResultSet cardInBase(String number) {
        String query = String.format("SELECT * FROM cards WHERE number = '%s'", number);

        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if(resultSet.next()) {
                return resultSet;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public String getStr (ResultSet result, String column) {

        if(result != null) {
            try {
                return result.getString(column);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    public double getBalance (ResultSet result) {
        double b = 0.0;

        if(result != null) {
            try {
                b = result.getDouble("balance");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return b;
    }
}
class CustomThread implements Runnable {

    @Override
    public void run() {
        System.out.printf("%s started... \n", Thread.currentThread().getName());
        BankingSystem bank = new BankingSystem();
        bank.enterMenu();
    }
}