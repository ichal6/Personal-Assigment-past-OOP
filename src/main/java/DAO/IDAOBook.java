package DAO;

import Model.Book;

import java.util.List;
import java.util.Map;

public interface IDAOBook {
    void addBook(Book newBook);
    void editBook(Book newBook);
    void deleteBook(Book newBook);
    List<Book> searchBooks();
    Map<String,Book> getBooks();
}
