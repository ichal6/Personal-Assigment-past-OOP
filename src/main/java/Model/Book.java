package Model;

public class Book {
    private long ISBN;
    private Author author;
    private String title;
    private Publisher publisher;
    private int publicationYear;
    private float price;

    public Book(String[] data) {
        this.ISBN = Long.parseLong(data[0]);
        this.author = new Author(data[1], data[2]);
        this.title = data[3];
        this.publisher = new Publisher(data[4]);
        this.publicationYear = Integer.parseInt(data[5]);
        this.price = Float.parseFloat(data[6]);
    }

    @Override
    public String toString() {
        String returnValue;
        returnValue = String.format("| %-5d | %-20s | %-20s | %-20s | %-20s  %-20s | %-20s ",
                ISBN,
                author.getFirstName(),
                author.getSurname(),
                title,
                publisher.getName(),
                publicationYear,
                price);
        return returnValue;
    }
}
