package DAO;

import Model.Author;
import Model.Book;
import Model.Builder;

import java.util.List;
import java.util.Map;

public interface IDAOBook {
    void addBook(Builder newBook);
    void editBook(long ISBN, float price);
    void deleteBook(long ISBN);
    Map<String,Book> searchBooks(String surname);
    Map<String,Book> getBooks();
    Map<Author, Integer> getCountBooksByAuthor();
    Map<String, Book> getBooksFromLastTenYears();
    float getSumOfLibrary();
    List<Author> getAllAuthors();
    boolean checkPublisher(String nameOfPublisher);
    void createNewPublisher(String nameOfPublisher, String publisherID);
}
