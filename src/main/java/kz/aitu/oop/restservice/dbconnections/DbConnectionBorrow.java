package kz.aitu.oop.restservice.dbconnections;

import java.sql.SQLException;
import java.sql.*;

public class DbConnectionBorrow {

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

    public boolean borrowBook(Connection conn, int memberId, int bookId) throws SQLException {
        String checkQuery = "SELECT is_available FROM book WHERE id=?";
        PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
        checkStmt.setInt(1, bookId);
        ResultSet rs = checkStmt.executeQuery();

        if (rs.next() && !rs.getBoolean("is_available")) {
            System.out.println("Book is NOT available");
            return false; // Книга уже занята
        }

        String insertQuery = "INSERT INTO borrowed_books (member_id, book_id) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(insertQuery);
        stmt.setInt(1, memberId);
        stmt.setInt(2, bookId);
        int result = stmt.executeUpdate();

        if (result == 1) {
            String updateBookQuery = "UPDATE book SET is_available = FALSE WHERE id=?";
            PreparedStatement updateStmt = conn.prepareStatement(updateBookQuery);
            updateStmt.setInt(1, bookId);
            updateStmt.executeUpdate();
        }

        stmt.close();
        return result == 1;
    }

    public boolean returnBook(Connection conn, int memberId, int bookId) throws SQLException {
        String updateQuery = "UPDATE borrowed_books SET return_date = NOW() WHERE member_id = ? AND book_id = ? AND return_date IS NULL";
        PreparedStatement stmt = conn.prepareStatement(updateQuery);
        stmt.setInt(1, memberId);
        stmt.setInt(2, bookId);
        int result = stmt.executeUpdate();

        if (result == 1) {
            String updateBookQuery = "UPDATE book SET is_available = TRUE WHERE id=?";
            PreparedStatement updateStmt = conn.prepareStatement(updateBookQuery);
            updateStmt.setInt(1, bookId);
            updateStmt.executeUpdate();
        }

        stmt.close();
        return result == 1;
    }


}
