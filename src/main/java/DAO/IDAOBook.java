package DAO;

import Model.Book;

import java.util.List;
import java.util.Map;

public interface IDAOBook {
    void addBook(String[] newBook);
    void editBook(long ISBN, int price);
    void deleteBook(long ISBN);
    Map<String,Book> searchBooks(String surname);
    Map<String,Book> getBooks();
}
