package nix.education.java.coffeemachine;

import java.util.Scanner;

public class CoffeeMachine {
    private double money;
    private int water, milk, coffee, cups;
    private static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        CoffeeMachine mach1 = new CoffeeMachine(400, 540, 120,9, 550.0);
        mach1.waitCommand((byte)0);
    }

    public void waitCommand(byte mess) {
        if(mess == 0) {
            System.out.println("\nWrite action(buy, fill, take, remaining, exit):");
        }
        String command = scan.nextLine();
        this.setCommand(command);
    }

    public void setCommand(String command) {

        byte mess = 0;

        switch (command.trim()) {
            case "buy":
                this.buy();
                break;
            case "fill":
                this.fill();
                break;
            case "take":
                this.take();
                break;
            case "remaining":
                System.out.printf("The coffee machine has: \n%d of water \n%d of milk \n%d of coffee beans \n%d of disposable cups \n%.0f of money\n", this.water, this.milk, this.coffee, this.cups, this.money);
                break;
            case "exit":
                mess = 2;
                break;
            default: mess = 1;
        }
        if(mess != 2) this.waitCommand(mess);
    }

    private void buy() {
        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
        String choice = (scan.next()).trim();

        if(!choice.equalsIgnoreCase("back"))
        {
            makeCoffee(choice);
        }
    }

    private void fill() {
        System.out.println("Write how many ml of water you want to add:");
        this.water += scan.nextInt();

        System.out.println("Write how many ml of milk you want to add:");
        this.milk += scan.nextInt();

        System.out.println("Write how many grams of coffee beans you want to add:");
        this.coffee += scan.nextInt();

        System.out.println("Write how many disposable coffee cups you want to add:");
        this.cups += scan.nextInt();

        System.out.printf("The coffee machine has: \n%d of water \n%d of milk \n%d of coffee beans \n%d of disposable cups \n%.0f of money\n", water, milk, coffee, cups, money);
    }

    private void take() {
        System.out.printf("I gave you %.0f\n", this.money);
        this.money = 0.0;
        System.out.printf("\nThe coffee machine has: \n%d of water \n%d of milk \n%d of coffee beans \n%d of disposable cups \n%.0f of money\n", this.water, this.milk, this.coffee, this.cups, this.money);
    }

    private void makeCoffee(String num)
    {
        if(!isByteInRange(num))
        {
            buy();
            return;
        }

        byte choice = Byte.parseByte(num);

        typeCoffee drink = typeCoffee.values()[choice-1];
        System.out.println(drink);

        if(water < drink.water) {
            System.out.println("Sorry, not enough water");
        }
        else if(milk < drink.milk) {
            System.out.println("Sorry, not enough milk");
        }
        else if(coffee < drink.coffee) {
            System.out.println("Sorry, not enough coffee beans");
        }
        else if(cups <= 0) {
            System.out.println("Sorry, not enough cups");
        }
        else {
            water -= drink.water;
            milk -= drink.milk;
            coffee -= drink.coffee;
            money += drink.price;
            cups--;
            System.out.println("I have enough resources, making you a coffee!");
        }
    }

    public CoffeeMachine(int water,  int milk, int coffee, int cups, double money) {
        this.water = water;
        this.milk = milk;
        this.coffee = coffee;
        this.cups = cups;
        this.money = money;

    }

    public enum typeCoffee {
        ESPRESSO (250, 0, 16, 4),
        LATTE (350, 75, 20, 7),
        CAPPUCCINO (200, 100, 12, 6);

        private final int water, milk, coffee, price;
        private static final int size = typeCoffee.values().length;

        typeCoffee(int water, int milk, int coffee, int price)
        {
            this.water = water;
            this.milk = milk;
            this.coffee = coffee;
            this.price = price;
        }
    }

    private static boolean isByteInRange(String input) {
        try {
            byte num = Byte.parseByte(input);

            return (num <= typeCoffee.size && num > 0);

        } catch (Exception ex) {
            return false;
        }
    }
}