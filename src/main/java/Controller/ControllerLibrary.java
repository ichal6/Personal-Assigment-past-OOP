package Controller;

import DAO.IDAOBook;
import Model.Builder;
import View.AbstractInput;
import View.AbstractOutput;

public class ControllerLibrary {
    private AbstractOutput output;
    private AbstractInput input;
    private IDAOBook dao;
    private final int EXIT = 0;
    private final int ADD_NEW_BOOK = 1;
    private final int EDIT_BOOK = 2;
    private final int DELETE_BOOK = 3;
    private final int SEARCH_BOOKS = 4;
    private final int DISPLAY_ALL_BOOKS = 5;
    private final int COUNT_BOOKS_BY_AUTHORS = 6;
    private final int BOOKS_FROM_LAST_TEN_YEARS = 7;
    private final int SUM_ALL_PRICE_OF_BOOKS = 8;
    private final int DISPLAY_FULL_NAME_OF_AUTHORS = 9;


    public ControllerLibrary(AbstractOutput output, AbstractInput input, IDAOBook bookDBDAO) {
        this.output = output;
        this.input = input;
        this.dao = bookDBDAO;
    }

    public void run(){

        boolean isRun;
        do{
            displayMenu();
            int optionNumber = input.getIntFromUser("Please provide options:");
            isRun = switchOptions(optionNumber);
        }while(isRun);
    }

    private void displayMenu(){
        output.print("0. exit\n1. Add new Book\n2. Edit book\n3. Delete book\n" +
                "4. search books\n5. Display all Books\n6. How many books author created\n" +
                "7. All books written in the last ten years\n8. Sum of prices all books\n" +
                "9. Display full name of author.");
    }

    private boolean switchOptions(int optionNumber){
        switch (optionNumber){
            case EXIT:
                return false;
            case ADD_NEW_BOOK:
                addBook();
                break;
            case EDIT_BOOK:
                long ISBN = input.getLongFromUser("Please provide ISBN to edit");
                float price =  input.getFloatFromUser("Please provide new price of product");
                dao.editBook(ISBN ,price);
                break;
            case DELETE_BOOK:
                long ISBN_search = input.getLongFromUser("Please provide ISBN to delete:");
                dao.deleteBook(ISBN_search);
                break;
            case SEARCH_BOOKS:
                String surname = input.getStringInput("Please provide surname of author");
                output.print(dao.searchBooks(surname));
                break;
            case DISPLAY_ALL_BOOKS:
                output.print(dao.getBooks());
                break;
            case COUNT_BOOKS_BY_AUTHORS:
                output.printAuthors(dao.getCountBooksByAuthor());
                break;
            case BOOKS_FROM_LAST_TEN_YEARS:
                output.print(dao.getBooksFromLastTenYears());
                break;
            case SUM_ALL_PRICE_OF_BOOKS:
                output.print(String.format("Sum all price of books = %.2f", dao.getSumOfLibrary()));
                break;
            case DISPLAY_FULL_NAME_OF_AUTHORS:
                output.print(dao.getAllAuthors());
                break;
        }
        return true;
    }

    private void addBook(){
        try {
            dao.addBook(createBook());
        }catch(NumberFormatException ex){
            output.print("You provide wrong data!");
        }
    }

    private Builder createBook() throws NumberFormatException{
        String[] data = new String[7];
        String[] questions = {"ISBN", "first name of author", "last name of author","title" ,"publisher", "Year of publication", "price"};
        Builder builder = new Builder();
        for(int index = 0; index < questions.length; index++){
            data[index] = input.getStringInput(String.format("Please provide %s:", questions[index]));
            if(data[index].length() == 0){
                output.print("You cannot provide empty value!");
                index--;
            }
        }

        builder.withISBN(Long.parseLong(data[0]))
                .withFirstName(data[1])
                .withSurname(data[2])
                .withTitle(data[3])
                .withName(data[4])
                .withPublicationYear(Integer.parseInt(data[5]))
                .withPrice(Float.parseFloat(data[6]));

        while (!dao.checkPublisher(builder.getName())) {
            String publisherID = input.getStringInput("Please provide new id for publisher");
            dao.createNewPublisher(builder.getName(), publisherID);
        }

        return builder;
    }


}
