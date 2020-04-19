package View;

import java.util.Scanner;

public class InputManager extends AbstractInput {
    private AbstractOutput output;

    public InputManager(AbstractOutput output) {
        this.output = output;
    }

    @Override
    public int getInputFromUser(String message) {
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
}
