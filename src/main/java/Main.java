import Controller.ControllerLibrary;
import DAO.BookDBDAO;
import View.InputManager;
import View.OutputManager;

public class Main {
    public static void main(String[] args) {
        ControllerLibrary controllerLibrary = new ControllerLibrary(new OutputManager(), new InputManager(), new BookDBDAO());
    }
}
