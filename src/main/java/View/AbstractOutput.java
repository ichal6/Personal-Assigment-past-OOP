package View;

import Model.Author;
import Model.Book;

import java.util.List;
import java.util.Map;

public abstract class AbstractOutput {
    public abstract void print(String s);

    public abstract void print(Map<String, Book> books);

    public abstract void printAuthors(Map<Author, Integer> authors);

    public abstract void print(List<Author> allAuthors);
}
