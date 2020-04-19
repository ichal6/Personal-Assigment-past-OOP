package DAO;

import Model.Book;

import java.util.List;
import java.util.Map;

public interface IDAOBook {
    void addBook();
    void editBook();
    void deleteBook();
    List<Book> searchBooks();
    Map<String,Book> getBooks();
}
