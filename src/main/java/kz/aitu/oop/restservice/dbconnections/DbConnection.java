package kz.aitu.oop.restservice.dbconnections;
import kz.aitu.oop.restservice.entities.Book;

import java.sql.*;
import java.util.ArrayList;

public class DbConnection {

    private String url = "jdbc:postgresql://localhost:5432/Library_bd";
    private String user = "postgres";
    private String password = "1111";

    public Connection connect() throws Exception {

        Connection con = DriverManager.getConnection(url, user, password);
        System.out.println("Connected to PostgreSQL database");
        return con;
    }

    public int closeConn(Connection con) throws SQLException {
        if(con != null) {
            con.close();
            System.out.println("Connection closed");
            return 0;
        }
        System.out.println("Connection is null");
        return 1;
    }

    public ArrayList<Book> getAllBooks(Connection con) throws SQLException{

        String query = "SELECT * FROM Book";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        ArrayList<Book> books = new ArrayList<>();
        while (rs.next()){
            Book book1 = new Book();
            book1.setTitle(rs.getString("title"));
            book1.setAuthor(rs.getString("author"));
            book1.setIsbn(rs.getString("isbn"));
            book1.setAvailable(rs.getBoolean("is_available"));
            books.add(book1);
        }
        st.close();
        closeConn(con);
        return books;
    }

    public Book findBookByISBN(Connection conn, String isbn) throws SQLException{

        String query = "SELECT * FROM Book WHERE isbn=?";
        PreparedStatement st = conn.prepareStatement(query);
        st.setString(1, isbn);

        ResultSet rs = st.executeQuery();

        ArrayList<Book> books = new ArrayList<>();
        Book book1 = new Book();
        while (rs.next()){
            book1.setTitle(rs.getString("title"));
            book1.setAuthor(rs.getString("author"));
            book1.setIsbn(rs.getString("isbn"));
            book1.setAvailable(rs.getBoolean("is_available"));
            books.add(book1);

        }

        st.close();
        closeConn(conn);
        return book1;
    }

    public Book createBook(Connection conn, Book book) throws SQLException{
        String query = "INSERT INTO Book (title, author, isbn, is_available) VALUES (?, ?, ?, ?)";
        PreparedStatement st = conn.prepareStatement(query);
        st.setString(1, book.getTitle());
        st.setString(2, book.getAuthor());
        st.setString(3, book.getIsbn());
        st.setBoolean(4, book.isAvailable());

        int success = st.executeUpdate();
        st.close();
        closeConn(conn);
        if (success == 1){
            System.out.println("Book created successfully");
            return book;
        }

        return null;
    }

    public Book updateBook(Connection conn, Book b, String title, String author, String isbn, String oldIsbn) throws SQLException{
        String query = "UPDATE Book Set title=?, author=?, isbn=? WHERE isbn=?";
        PreparedStatement st = conn.prepareStatement(query);
        st.setString(1, title);
        st.setString(2, author);
        st.setString(3, isbn);
        st.setString(4, oldIsbn);

        int success = st.executeUpdate();
        st.close();
        closeConn(conn);
        if (success == 1){
            System.out.println("Book updated successfully");
            return b;

        }
        return null;
    }

    public Book updateBookAvailability(Connection conn, Book b, String isbn, Boolean status) throws SQLException{
        String query = "UPDATE Book SET title=?, author=?, is_available=? WHERE isbn=?";
        PreparedStatement st = conn.prepareStatement(query);
        st.setString(1, b.getTitle());
        st.setString(2, b.getAuthor());
        st.setBoolean(3, status);
        st.setString(4, isbn);

        int success = st.executeUpdate();
        st.close();
        closeConn(conn);

        if (success == 1){
            System.out.println("Book updated successfully");
            return b;
        }

        return null;
    }

    public Book deleteBook(Connection conn, Book book) throws SQLException{
        String query = "DELETE FROM Book WHERE isbn=?";
        PreparedStatement st = conn.prepareStatement(query);
        st.setString(1, book.getIsbn());

        int success = st.executeUpdate();
        st.close();
        closeConn(conn);
        if (success == 1){
            System.out.println("Book deleted successfully");
            return book;
        }

        return null;

    }

}
