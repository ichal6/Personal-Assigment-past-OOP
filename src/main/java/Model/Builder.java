package Model;

public class Builder {
    private long ISBN;
    private String firstName;
    private String surname;
    private String title;
    private String name;
    private int publicationYear;
    private float price;

    public Builder withISBN(long ISBN){
        this.ISBN = ISBN;
        return this;
    }
    public Builder withFirstName(String firstName){
        this.firstName = firstName;
        return this;
    }
    public Builder withSurname(String surname){
        this.surname = surname;
        return this;
    }

    public Builder withName(String name){
        this.name = name;
        return this;
    }

    public Builder withPublicationYear(int publicationYear){
        this.publicationYear = publicationYear;
        return this;
    }

    public Builder withPrice(float price){
        this.price = price;
        return this;
    }

    public Builder withTitle(String title){
        this.title = title;
        return this;
    }

    public Book build(){
        return new Book(this) {
        };
    }

    public long getISBN() {
        return ISBN;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public float getPrice() {
        return price;
    }

}


