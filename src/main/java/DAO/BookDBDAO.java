package DAO;

import Model.Author;
import Model.Book;
import Model.Builder;
import View.InputManager;
import View.OutputManager;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookDBDAO implements IDAOBook{

    private final String url;
    private final String user;
    private final String password;
    private Map<String, Book> dicOfBooks;

    public BookDBDAO(String path) throws IOException {
        Properties prop = LoginData.readProperties(path);
        url = prop.getProperty("db.url");
        user = prop.getProperty("db.user");
        password = prop.getProperty("db.passwd");

    }

    @Override
    public void addBook(Builder newBook) {
        String query = "INSERT INTO books VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(query))
        {
            int idAuthor = addToAuthors(newBook.getFirstName(),newBook.getSurname());
            String publisherID = addToPublishers(newBook.getName());
            pst.setLong(1, newBook.getISBN());
            pst.setInt(2, idAuthor);
            pst.setString(3, newBook.getTitle());
            pst.setString(4, publisherID);
            pst.setInt(5, newBook.getPublicationYear());
            pst.setFloat(6, newBook.getPrice());
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(BookDBDAO.class.getName());
            lgr.log(Level.SEVERE,"You cannot insert a book ", ex);
        }

    }

    private String addToPublishers(String nameOfPublisher) throws SQLException{
        Connection con = DriverManager.getConnection(url, user, password);
        PreparedStatement pst = con.prepareStatement("select  ID from publishers WHERE name = ?");

        pst.setString(1, nameOfPublisher);

        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            return rs.getString(1);
        }
        else{
            throw new SQLException();
        }
    }

    @Override
    public boolean checkPublisher(String nameOfPublisher){
        try(
        Connection con = DriverManager.getConnection(url, user, password);
        PreparedStatement pst = con.prepareStatement("select  ID from publishers WHERE name = ?")) {
            pst.setString(1, nameOfPublisher);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        }catch(SQLException ex){
            return false;
        }
    }

    @Override
    public void createNewPublisher(String nameOfPublisher, String publisherID) {
        String query = "INSERT INTO publishers VALUES (?, ?)";
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(query))
        {
            pst.setString(2, nameOfPublisher);
            pst.setString(1, publisherID);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(BookDBDAO.class.getName());
            lgr.log(Level.SEVERE,"You cannot insert a book ", ex);
        }
    }

    private int addToAuthors(String name, String lastName) throws SQLException {
        Connection con = DriverManager.getConnection(url, user, password);
        PreparedStatement pst = con.prepareStatement(
                "select  ID from authors WHERE first_name = ? AND surname = ?");
        pst.setString(1, name);
        pst.setString(2, lastName);

        ResultSet rs = pst.executeQuery();
        con.close();

        if (rs.next()) {
            return rs.getInt(1);
        }
        else{
            return createNewAuthors(name, lastName);
        }

    }

    private int createNewAuthors(String name, String lastName) throws SQLException {
        String AddToUser_tableStatement = "INSERT INTO authors VALUES (DEFAULT, ?, ?)";

        Connection con = DriverManager.getConnection(url, user, password);
        PreparedStatement pst = con.prepareStatement(AddToUser_tableStatement);

        pst.setString(1, name);
        pst.setString(2, lastName);
        pst.executeUpdate();

        pst = con.prepareStatement("select  ID from authors  WHERE first_name= ? AND surname = ?");
        pst.setString(1, name);
        pst.setString(2, lastName);
        ResultSet rs = pst.executeQuery();
        con.close();

        rs.next();

        return rs.getInt(1);
    }

    @Override
    public void editBook(long ISBN, int price) {
        String updateStatement = ("UPDATE books SET price = ? WHERE \"ISBN\" = ?");
        try(Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement pst = con.prepareStatement(updateStatement))
        {
            pst.setLong(2, ISBN);
            pst.setInt(1, price);
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(BookDBDAO.class.getName());
            lgr.log(Level.SEVERE,"Nothing delete " + ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteBook(long ISBN) {
        String deleteFromUserStatement = ("DELETE FROM books WHERE \"ISBN\" = ?");
        try(Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement pst = con.prepareStatement(deleteFromUserStatement))
        {
            pst.setLong(1,ISBN);
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(BookDBDAO.class.getName());
            lgr.log(Level.SEVERE,"Nothing delete " + ex.getMessage(), ex);
        }
    }

    @Override
    public Map<String, Book> searchBooks(String surname) {
        dicOfBooks = new TreeMap<>();
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(
                     "select  \"ISBN\" ,first_name, surname, title, name ,  publication_year, price from books inner join authors on books.author_id=authors.ID inner join publishers on books.publisher_id=publishers.ID WHERE surname = ?"))
        {
            pst.setString(1, surname);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Book book = new Book(new Builder()
                        .withISBN(rs.getLong(0))
                        .withFirstName(rs.getString(2))
                        .withSurname(rs.getString(3))
                        .withTitle(rs.getString(4))
                        .withName(rs.getString(5))
                        .withPublicationYear(rs.getInt(6))
                        .withPrice(rs.getFloat(7)));

                dicOfBooks.put(book.getTitle() ,book);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(BookDBDAO.class.getName());
            lgr.log(Level.SEVERE,"Return failed books " + ex.getMessage(), ex);
        }
        return dicOfBooks;
    }

    @Override
    public Map<String, Book> getBooks(){
        dicOfBooks = new TreeMap<>();
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(
                     "select  \"ISBN\" ,first_name, surname, title, name ,  publication_year, price from books inner join authors on books.author_id=authors.ID inner join publishers on books.publisher_id=publishers.ID"
             );
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Book book = new Book(new Builder()
                        .withISBN(rs.getLong(1))
                        .withFirstName(rs.getString(2))
                        .withSurname(rs.getString(3))
                        .withTitle(rs.getString(4))
                        .withName(rs.getString(5))
                        .withPublicationYear(rs.getInt(6))
                        .withPrice(rs.getFloat(7)));

                dicOfBooks.put(book.getTitle() ,book);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(BookDBDAO.class.getName());
            lgr.log(Level.SEVERE,"Return failed books " + ex.getMessage(), ex);
        }
        return dicOfBooks;
    }

    @Override
    public Map<Author, Integer> getCountBooksByAuthor() {
        List<Author> authorsList = getAllAuthors();
        Map<Author, Integer> dicOfAuthors = new HashMap<>();
        for(Author singleAuthor: authorsList){
            String query = String.format("select  * from books inner join authors on books.author_id=authors.ID inner join publishers on books.publisher_id=publishers.ID WHERE first_name = '%s' AND surname = '%s'",singleAuthor.getFirstName(), singleAuthor.getSurname());
            int count = 0;
            try (Connection con = DriverManager.getConnection(url, user, password);
                 PreparedStatement pst = con.prepareStatement(query);
                 ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    count++;
                }
                dicOfAuthors.put(singleAuthor, count);
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(BookDBDAO.class.getName());
                lgr.log(Level.SEVERE,"Return failed books " + ex.getMessage(), ex);
            }
        }
        return dicOfAuthors;
    }

    @Override
    public Map<String, Book> getBooksFromLastTenYears() {
        int year = Calendar.getInstance().get(Calendar.YEAR) - 10;
        dicOfBooks = new TreeMap<>();
        String query = String.format("select  \"ISBN\" ,first_name, surname, title, name ,  publication_year, price from books inner join authors on books.author_id=authors.ID inner join publishers on books.publisher_id=publishers.ID WHERE publication_year >= %d", year);
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Book book = new Book(new Builder()
                        .withISBN(rs.getLong(1))
                        .withFirstName(rs.getString(2))
                        .withSurname(rs.getString(3))
                        .withTitle(rs.getString(4))
                        .withName(rs.getString(5))
                        .withPublicationYear(rs.getInt(6))
                        .withPrice(rs.getFloat(7)));

                dicOfBooks.put(book.getTitle() ,book);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(BookDBDAO.class.getName());
            lgr.log(Level.SEVERE,"Return failed books " + ex.getMessage(), ex);
        }
        return dicOfBooks;
    }

    @Override
    public float getSumOfLibrary() {
        float sum = 0;
        String query = "SELECT price FROM books";
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                sum += rs.getFloat(1);
                //listOfAuthors.add(new Author(rs.getString(1), rs.getString(2)));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(BookDBDAO.class.getName());
            lgr.log(Level.SEVERE,"Failed return all of prices " + ex.getMessage(), ex);
        }
        return sum;
    }

    @Override
    public List<Author> getAllAuthors() {
        List<Author> listOfAuthors = new ArrayList<>();
        String query = "SELECT first_name, surname FROM authors";
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                listOfAuthors.add(new Author(rs.getString(1), rs.getString(2)));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(BookDBDAO.class.getName());
            lgr.log(Level.SEVERE,"Failed get all authors " + ex.getMessage(), ex);
        }
        return listOfAuthors;
    }
}
