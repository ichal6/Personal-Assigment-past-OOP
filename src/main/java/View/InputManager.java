package View;

import java.util.Scanner;

public class InputManager extends AbstractInput {
    private AbstractOutput output;

    public InputManager(AbstractOutput output) {
        this.output = output;
    }

    @Override
    public int getIntFromUser(String message) {
        output.print(message);
        int input = 0;
        Scanner scanFromUser = new Scanner(System.in);

        while(!scanFromUser.hasNextInt()){
            output.print("Wrong input! Please insert the integer number.");
            scanFromUser.next();
        }
        input = scanFromUser.nextInt();
        return input;
    }

    @Override
    public String getStringInput(String message) {
        output.print(message);
        Scanner scanFromUser = new Scanner(System.in);
        String input = scanFromUser.nextLine();

        return input;
    }

    @Override
    public long getLongFromUser(String message) {
        output.print(message);
        long input = 0L;
        Scanner scanFromUser = new Scanner(System.in);

        while(!scanFromUser.hasNextLong()){
            output.print("Wrong input! Please insert the long number.");
            scanFromUser.next();
        }
        input = scanFromUser.nextLong();
        return input;
    }

    @Override
    public float getFloatFromUser(String message) {
        output.print(message);
        float input = 0.0F;
        Scanner scanFromUser = new Scanner(System.in);

        while(!scanFromUser.hasNextFloat()){
            output.print("Wrong input! Please insert the float number.");
            scanFromUser.next();
        }
        input = scanFromUser.nextFloat();
        return input;
    }
}
