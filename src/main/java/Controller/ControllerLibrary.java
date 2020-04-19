package Controller;

import DAO.BookDBDAO;
import Model.Book;
import View.AbstractInput;
import View.AbstractOutput;

public class ControllerLibrary {
    private AbstractOutput output;
    private AbstractInput input;
    private BookDBDAO dao;


    public ControllerLibrary(AbstractOutput output, AbstractInput input, BookDBDAO bookDBDAO) {
        this.output = output;
        this.input = input;
        this.dao = bookDBDAO;
    }

    private void displayMenu(){
        output.print("1. Add new Book\n2. Edit book\n3. Delete book\n" +
                " 4. search books\n 5. Display all Books");
    }

    private void switchOptions(int optionNumber){
        switch (optionNumber){
            case 1:
                dao.addBook(createBook());

                break;
            case 2:
                long ISBN = input.getLongFromUser("Please provide ISBN to remove");
                int price =  input.getInputFromUser("Please provide new price of product");
                dao.editBook(ISBN ,price);
                break;
            case 3:
                long ISBN_search = input.getLongFromUser("Please provide ISBN:");
                dao.deleteBook(ISBN_search);
                break;
            case 4:
                String surname = input.getStringInput("Please provide surname of author");
                output.print(dao.searchBooks(surname));
                break;
            case 5:
                output.print(dao.getBooks());
                break;
        }
    }

    private String[] createBook(){
        String[] data = new String[7];
        String[] questions = {"ISBN", "first name of author", "last name of author","title" ,"publisher", "Year of publication", "price"};
        for(int index = 0; index < questions.length; index++){
            data[index] = input.getStringInput(String.format("Please provide %s:", questions[index]));
            if(data[index].length() == 0){
                output.print("You cannot provide empty value!");
                index--;
            }
        }
        return data;
    }

    public void run(){
        displayMenu();
        int optionNumber = input.getInputFromUser("Please provide options:");
        switchOptions(optionNumber);

    }
}
