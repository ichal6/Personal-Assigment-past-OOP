package Model;

public class Author {
    private static int serial = 0;
    private String firstName;
    private String surname;

    public Author(String firstName, String surname) {
        this.firstName = firstName;
        this.surname = surname;
        serial++;
    }
}
