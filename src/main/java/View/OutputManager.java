package View;

import Model.Author;
import Model.Book;

import java.util.List;
import java.util.Map;

public class OutputManager extends AbstractOutput{
    @Override
    public void print(String s) {
        System.out.println(s);
    }

    @Override
    public void print(Map<String, Book> books) {
        int width = 155;
        String label = String.format("| %-20s | %-20s | %-20s | %-30s | %-20s  |%-20s | %-20s \n%s",
                "ISBN",
                "first name of author",
                "last name of author",
                "title",
                "publisher",
                "publication of Year",
                "price",
                "-".repeat(width));
        System.out.println(label);
        for (Map.Entry<String,Book> book: books.entrySet() ) {
            System.out.println(book.getValue().toString());
        }
    }

    @Override
    public void printAuthors(Map<Author, Integer> authors) {
        for (Map.Entry<Author,Integer> author: authors.entrySet() ) {
            System.out.println(author.getKey().getFirstName() + " " + author.getKey().getSurname() + " " + author.getValue().toString());
        }
    }

    @Override
    public void print(List<Author> allAuthors) {
        System.out.println("Full name of authors:");
        for(Author author: allAuthors){
            System.out.println(author.getFirstName() + " " + author.getSurname());
        }
        System.out.println();
    }
}
