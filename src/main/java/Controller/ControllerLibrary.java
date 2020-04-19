package Controller;

import DAO.BookDBDAO;
import View.AbstractInput;
import View.AbstractOutput;

public class ControllerLibrary {
    AbstractOutput output;
    AbstractInput input;
    BookDBDAO dao;

    public ControllerLibrary(AbstractOutput output, AbstractInput input, BookDBDAO bookDBDAO) {
        this.output = output;
        this.input = input;
        this.dao = bookDBDAO;
    }

    public void run(){

    }
}
