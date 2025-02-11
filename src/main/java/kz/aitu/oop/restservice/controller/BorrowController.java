package kz.aitu.oop.restservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.aitu.oop.restservice.dbconnections.DbConnectionBorrow;
import kz.aitu.oop.restservice.entities.BorrowedBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.util.ArrayList;

@RestController
public class BorrowController {
    @Autowired
    private ObjectMapper obMapper;

    @GetMapping("/index/borrowedBooks")
    public String borrowedBooks(){
        DbConnectionBorrow myConnection = new DbConnectionBorrow();
        Connection conn = null;
        ArrayList<BorrowedBook> bbooks = new ArrayList<BorrowedBook>();
        try{
            conn = myConnection.connect();
            bbooks = myConnection.getInfo(conn);
        }catch(Exception e){
            System.out.println("Something went wrong " + e.toString());
        }

        String jsonText = null;
        try{
            jsonText = obMapper.writeValueAsString(bbooks);
        }catch(JsonProcessingException e){
            System.out.println("Something went wrong with book " + e.toString());
        }

        return jsonText;
    }



    @PostMapping("/index/borrowBook")
    public String borrowBook(@RequestParam int memberId, @RequestParam int bookId) {
        DbConnectionBorrow myConnection = new DbConnectionBorrow();
        Connection conn = null;
        boolean success = false;

        try {
            conn = myConnection.connect();
            success = myConnection.borrowBook(conn, memberId, bookId);
        } catch (Exception e) {
            System.out.println("Something went wrong " + e.toString());
        }

        return success ? "Book borrowed successfully" : "Failed to borrow book";
    }

    @PostMapping("/index/returnBook")
    public String returnBook(@RequestParam int memberId, @RequestParam int bookId) {
        DbConnectionBorrow myConnection = new DbConnectionBorrow();
        Connection conn = null;
        boolean success = false;

        try {
            conn = myConnection.connect();
            success = myConnection.returnBook(conn, memberId, bookId);
        } catch (Exception e) {
            System.out.println("Something went wrong " + e.toString());
        }

        return success ? "Book returned successfully" : "Failed to return book";
    }
}
