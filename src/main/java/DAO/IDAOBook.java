package DAO;

import Model.Book;
import Model.Builder;

import java.util.Map;

public interface IDAOBook {
    void addBook(Builder newBook);
    void editBook(long ISBN, int price);
    void deleteBook(long ISBN);
    Map<String,Book> searchBooks(String surname);
    Map<String,Book> getBooks();
    boolean checkPublisher(String nameOfPublisher);
    void createNewPublisher(String nameOfPublisher, String publisherID);
}
