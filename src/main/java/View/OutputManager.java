package View;

import Model.Book;

import java.util.Map;

public class OutputManager extends AbstractOutput{
    @Override
    public void print(String s) {
        System.out.println(s);
    }

    @Override
    public void print(Map<String, Book> books) {
        for (Map.Entry<String,Book> book: books.entrySet() ) {
            System.out.println(book.getValue().toString());
        }
    }
}
