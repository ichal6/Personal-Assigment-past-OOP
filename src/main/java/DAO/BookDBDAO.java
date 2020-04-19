package DAO;

import Model.Book;

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
    public void addBook(Book book) {


    }

    @Override
    public void editBook(Book book) {

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
             //PreparedStatement pst = con.prepareStatement("SELECT  FROM user_table INNER JOIN accountdetails ON user_table.account_details_id = accountdetails.accountdetails_id WHERE admin_user = '1' AND first_name = ? OR last_name = ? OR login = ?")
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
