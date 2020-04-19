import Controller.ControllerLibrary;
import DAO.BookDBDAO;
import View.AbstractOutput;
import View.InputManager;
import View.OutputManager;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ControllerLibrary controllerLibrary = null;
        AbstractOutput output = new OutputManager();
        try {
            controllerLibrary = new ControllerLibrary(output, new InputManager(output), new BookDBDAO("src/main/resources/database.properties"));
            controllerLibrary.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
