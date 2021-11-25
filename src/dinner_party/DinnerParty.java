package dinner_party;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DinnerParty {

    private static final Scanner scan = new Scanner(System.in);
    private final Map<Integer, Guest> nameList = new HashMap<>();

    public static void main(String[] args) {
        DinnerParty list = new DinnerParty();
        list.enterGuests();
    }

    private void enterGuests() {
        System.out.println("Enter the number of friends joining(including you):");
        int number = scan.nextInt();

        if(number <= 0) {
            System.out.println("No one is joining for the party");
            return;
        }

        System.out.println("Enter the name of every friend(including you), each on new line:");

        for(int i = 0; i < number; i++) {
            String name = scan.nextLine().trim();

            if(name.isEmpty()) {
                i--;
                continue;
            }
            Guest guest = new Guest(name);
            nameList.put(i, guest);
        }
        this.enterAmount();
    }

    private void enterAmount() {
        System.out.println("Enter the total amount:");
        double amount = scan.nextDouble();

        System.out.println("Do you want to use the \"Who is lucky?\" feature? Write Yes/No");
        String ans = scan.next().trim();

        Integer lucky = checkLucky(ans);
        int balance = lucky == -1 ? 0 : 1;
        amount /= nameList.size() - balance;

        for(var entry : nameList.entrySet()) {
            Integer key = entry.getKey();
            Guest g = entry.getValue();

            if(!key.equals(lucky)) {
                g.setBill(amount);
            }
        }
        System.out.println(nameList.values());
    }

    private Integer checkLucky(String ans) {
        if(ans.equalsIgnoreCase("Yes")) {
            Integer lucky = (int) (Math.random() * nameList.size());
            Guest g = nameList.get(lucky);
            System.out.printf("%s is the luck one.\n", g.getName());
            return lucky;
        }
        System.out.println("No one is going to be lucky.");
        return -1;
    }
}

class Guest {
    private final String name;
    private double bill;

    Guest(String name) {
        this.name = name;
        this.bill = 0.0;
    }

    public String getName() {
        return name;
    }

    public void setBill(double bill) {
        this.bill = bill;
    }

    @Override
    public String toString() {
        return String.format("%s = %.2f ", name, bill);
    }
}
