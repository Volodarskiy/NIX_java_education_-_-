package nix.education.java.chatbot;

import java.util.Scanner;

public class ChatBot
{
    public static void main(String[] args)
    {
        String botName = "Spectrum021", clName;
        Scanner scan = new Scanner(System.in);
        short birth_year = (short) 2021;

        System.out.printf("Hello, my name is %s. \nI was created in %d. \nPlease, remind me your name.\n", botName, birth_year);
        clName = scan.nextLine();
        System.out.printf("What a great name you have, %s.\nLet me guess your age. \nEnter remainders of dividing your age by 3, 5 and 7.\n", clName);

        short remainder3, remainder5, remainder7, age;

        remainder3 = (short) (scan.nextShort() % 3);
        remainder5 = (short) (scan.nextShort() % 5);
        remainder7 = (short) (scan.nextShort() % 7);

        age = (short) ((remainder3 * 70 + remainder5 * 21 + remainder7 * 15) % 105);
        System.out.printf("Your age is %d; that's a good time to start programming! \nNow i will prove to you than I can count to any number you want. \n", age);
        int clCount = scan.nextInt();

        for (int i = 0; i <= clCount; i++)
        {
            System.out.println(i + " !");
        }
        System.out.println("What was the name originally given to the new programming language, which later became known as 'Java'?\n1. Snake\n2. Tree\n3. Java\n4. Oak\nUse answer option number.");
        String clAnswer;

        while(true)
        {
            clAnswer = (scan.nextLine()).trim();

            if(clAnswer.equals("4"))
            {
                System.out.println("It was Oak, right!\nGoodbye, have a nice day!");
                break;
            }
            else if(!clAnswer.isEmpty())
            {
                System.out.println("Nope, please try again!");
            }
        }
    }
}
