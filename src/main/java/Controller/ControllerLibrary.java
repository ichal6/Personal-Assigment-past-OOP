package Controller;

import DAO.BookDBDAO;
import Model.Builder;
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

    public void run(){

        boolean isRun;
        do{
            displayMenu();
            int optionNumber = input.getInputFromUser("Please provide options:");
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
            case 0:
                return false;
            case 1:
                try {
                    dao.addBook(createBook());
                }catch(NumberFormatException ex){
                    output.print("You provide wrong data!");
                }
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
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                output.print(dao.getAllAuthors());
                break;
        }
        return true;
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
