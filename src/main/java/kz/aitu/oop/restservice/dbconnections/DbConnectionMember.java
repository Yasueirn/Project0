package kz.aitu.oop.restservice.dbconnections;

import kz.aitu.oop.restservice.entities.LibraryMember;

import java.sql.*;
import java.util.ArrayList;

public class DbConnectionMember {

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

    public ArrayList<LibraryMember> getAllMembers(Connection con) throws SQLException{

        String query = "SELECT * FROM library_member";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        ArrayList<LibraryMember> members = new ArrayList<>();
        while (rs.next()){
            LibraryMember member = new LibraryMember();
            member.setId(rs.getInt("id"));
            member.setName(rs.getString("name"));
            member.setEmail(rs.getString("email"));
            member.setPhone(rs.getString("phone"));
            members.add(member);
        }
        st.close();
        closeConn(con);
        return members;
    }

    public LibraryMember createMember(Connection conn, LibraryMember member) throws SQLException{
        String query = "INSERT INTO library_member (name, email, phone) VALUES (?, ?, ?)";
        PreparedStatement st = conn.prepareStatement(query);
        st.setString(1, member.getName());
        st.setString(2, member.getEmail());
        st.setString(3, member.getPhone());

        int success = st.executeUpdate();
        st.close();
        closeConn(conn);
        if (success == 1){
            System.out.println("Member created successfully");
            return member;
        }

        return null;
    }

    public LibraryMember findMemberById(Connection conn, int id) throws SQLException {
        String query = "SELECT * FROM Library_Member WHERE id=?";
        PreparedStatement st = conn.prepareStatement(query);
        st.setInt(1, id);

        ResultSet rs = st.executeQuery();

        LibraryMember member = new LibraryMember();
        while (rs.next()) {
            member.setId(rs.getInt("id"));
            member.setName(rs.getString("name"));
            member.setEmail(rs.getString("email"));
            member.setPhone(rs.getString("phone"));
        }

        st.close();
        conn.close();
        return member;
    }


    public LibraryMember updateMember(Connection conn, int id, String name, String email, String phone) throws SQLException {
        String query = "UPDATE Library_Member SET name=?, email=?, phone=? WHERE id=?";
        PreparedStatement st = conn.prepareStatement(query);
        st.setString(1, name);
        st.setString(2, email);
        st.setString(3, phone);
        st.setInt(4, id);

        int success = st.executeUpdate();
        st.close();
        closeConn(conn);

        if (success == 1) {
            System.out.println("Library member updated successfully");
            return new LibraryMember(id, name, email, phone);
        }
        return null;
    }


    public LibraryMember deleteMember(Connection conn, LibraryMember member) throws SQLException {
        String query = "DELETE FROM Library_Member WHERE id=?";
        PreparedStatement st = conn.prepareStatement(query);
        st.setInt(1, member.getId());

        int success = st.executeUpdate();
        st.close();
        closeConn(conn);

        if (success == 1) {
            System.out.println("Library member deleted successfully");
            return member;
        }

        return null;
    }




}

