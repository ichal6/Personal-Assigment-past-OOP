package DAO;

import Model.Book;
import View.InputManager;
import View.OutputManager;

import java.io.IOException;
import java.sql.*;
import java.util.*;
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
    public void addBook(String[] newBook) {
        int idAuthor = addToAuthors(newBook[1],newBook[2]);
        String publisherID = addToPublishers(newBook[4]);
        String AddToUser_tableStatement = "INSERT INTO books VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(AddToUser_tableStatement))
        {
            pst.setLong(1, Long.parseLong(newBook[0]));
            pst.setInt(2, idAuthor);
            pst.setString(3, newBook[3]);
            pst.setString(4, publisherID);
            pst.setInt(5, Integer.parseInt(newBook[5]));
            pst.setFloat(6, Float.parseFloat(newBook[6]));
            pst.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private String addToPublishers(String nameOfPublisher) {
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement("select  ID from publishers WHERE name = ?")

        ) {
            pst.setString(1, nameOfPublisher);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getString(1);
            }
            else{
                InputManager input = new InputManager(new OutputManager());
                String publisherID = input.getStringInput("Please provide new id for publisher");
                createNewPublisher(nameOfPublisher, publisherID);
                return publisherID;
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(BookDBDAO.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return "";
    }

    private void createNewPublisher(String nameOfPublisher, String publisherID) {
        String AddToUser_tableStatement = "INSERT INTO publishers VALUES (?, ?)";
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(AddToUser_tableStatement))
        {
            pst.setString(2, nameOfPublisher);
            pst.setString(1, publisherID);
            pst.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }


    }

    private int addToAuthors(String name, String lastName) {
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement("select  ID from authors WHERE first_name = ? AND surname = ?")

        ) {
            pst.setString(1, name);
            pst.setString(2, lastName);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            else{
                return createNewAuthors(name, lastName);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(BookDBDAO.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return 0;
    }

    private int createNewAuthors(String name, String lastName){
        String AddToUser_tableStatement = "INSERT INTO authors VALUES (DEFAULT, ?, ?)";
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(AddToUser_tableStatement))
        {
            pst.setString(1, name);
            pst.setString(2, lastName);
            pst.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }


        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement("select  ID from authors  WHERE first_name= ? AND surname = ?")

        ) {
            pst.setString(1, name);
            pst.setString(2, lastName);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(BookDBDAO.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return 0;
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

        } catch (SQLException throwables) {
          throwables.printStackTrace();

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

        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }
    }

    @Override
    public Map<String, Book> searchBooks(String surname) {
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement("select  \"ISBN\" ,first_name, surname, title, name ,  publication_year, price from books inner join authors on books.author_id=authors.ID inner join publishers on books.publisher_id=publishers.ID WHERE surname = ?")

        ) {
            pst.setString(1, surname);
            ResultSet rs = pst.executeQuery();

            int attributesNumber = 7;
            dicOfBooks = new TreeMap<>();
            String[] bookAttributes = new String[attributesNumber];

            while (rs.next()) {
                for(int index = 0;index < attributesNumber; index++){
                    bookAttributes[index] = rs.getString(index+1);
                }
                Book book = new Book(bookAttributes);
                dicOfBooks.put(bookAttributes[3] ,book);
                con.close();
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(BookDBDAO.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return dicOfBooks;
    }

    @Override
    public Map<String, Book> getBooks() {
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement("select  \"ISBN\" ,first_name, surname, title, name ,  publication_year, price from books inner join authors on books.author_id=authors.ID inner join publishers on books.publisher_id=publishers.ID");
             ResultSet rs = pst.executeQuery()) {

            int attributesNumber = 7;
            dicOfBooks = new TreeMap<>();
            String[] bookAttributes = new String[attributesNumber];

            while (rs.next()) {
                for(int index = 0;index < attributesNumber; index++){
                    bookAttributes[index] = rs.getString(index+1);
                }
                Book book = new Book(bookAttributes);
                dicOfBooks.put(bookAttributes[3] ,book);
                con.close();
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(BookDBDAO.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return dicOfBooks;
    }
}
