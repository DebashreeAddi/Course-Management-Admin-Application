package scrame;


import java.util.Scanner;

public class CheckType {

    public static char getChar() {
        Scanner sc = new Scanner(System.in);

        char charChoice = '\u0000';
        try {
            String input = sc.nextLine();
            charChoice = input.charAt(0);
        }
        catch (Exception e){
        }
        return charChoice;
    }

    public static int getInt() {
        Scanner sc = new Scanner(System.in);

        int intChoice = 0;
        try {
           intChoice = sc.nextInt();
        }
        catch (Exception e){
            throw new NullPointerException ("Not an integer");
        }
        return intChoice;
    }

    public static String getString() {
        Scanner sc = new Scanner(System.in);

        String stringChoice = null;
        try {
            stringChoice = sc.nextLine();
        }
        catch (Exception e){
        }
        return stringChoice;
    }
}
