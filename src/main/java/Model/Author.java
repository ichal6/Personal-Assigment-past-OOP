package Model;

public class Author {
    private static int serial = 0;

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    private String firstName;
    private String surname;

    public Author(String firstName, String surname) {
        this.firstName = firstName;
        this.surname = surname;
        serial++;
    }
}
