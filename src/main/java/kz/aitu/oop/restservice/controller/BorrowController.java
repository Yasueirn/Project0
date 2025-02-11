package kz.aitu.oop.restservice.controller;

import kz.aitu.oop.restservice.dbconnections.DbConnectionBorrow;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;

@RestController
public class BorrowController {

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
