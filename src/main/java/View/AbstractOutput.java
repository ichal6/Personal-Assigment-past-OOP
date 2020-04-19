package View;

import Model.Book;

import java.util.Map;

public abstract class AbstractOutput {
    public abstract void print(String s);

    public abstract void print(Map<String, Book> books);
}
