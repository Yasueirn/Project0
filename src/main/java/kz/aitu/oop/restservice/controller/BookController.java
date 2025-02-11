package kz.aitu.oop.restservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.aitu.oop.restservice.dbconnections.DbConnectionBook;
import kz.aitu.oop.restservice.entities.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.util.ArrayList;

@RestController
public class BookController {
    @Autowired
    private ObjectMapper obMapper;


    @GetMapping("/index/allBooks")
    public String getallBooks(){
        DbConnectionBook myConnection = new DbConnectionBook();
        Connection conn = null;
        ArrayList<Book> books = new ArrayList<Book>();
        try{
            conn = myConnection.connect();
            books = myConnection.getAllBooks(conn);
        }catch(Exception e){
            System.out.println("Something went wrong " + e.toString());
        }

        String jsonText = null;
        try{
            jsonText = obMapper.writeValueAsString(books);
        }catch(JsonProcessingException e){
            System.out.println("Something went wrong with book " + e.toString());
        }

        return jsonText;
    }

    @PostMapping("/index/findBook")
    public String findBookByisbn(@RequestParam String isbn){
        DbConnectionBook myConnection = new DbConnectionBook();
        Connection conn = null;
        Book book1 = null;
        try{
            conn = myConnection.connect();
        }catch(Exception e){
            System.out.println("Something went wrong " + e.toString());
        }

        try{
            book1 = myConnection.findBookByISBN(conn, isbn);
        }catch(Exception e){
            System.out.println("Something went wrong " + e.toString());
        }


        String jsonText = null;
        try{
            jsonText = obMapper.writeValueAsString(book1);
        }catch(JsonProcessingException e){
            System.out.println("Something went wrong " + e.toString());
        }

        return jsonText;
    }

    @PostMapping("/index/createBook")
    public String createBook(@RequestParam String title, @RequestParam String author, @RequestParam String isbn){
        DbConnectionBook myConnection = new DbConnectionBook();
        Connection conn = null;
        Book book1 = new Book(title, author, isbn, true);
        String jsonText = null;
        try{
            conn = myConnection.connect();
        }catch(Exception e){
            System.out.println("Something went wrong " + e.toString());
        }

        try{
            jsonText = obMapper.writeValueAsString(myConnection.createBook(conn, book1));
        }catch (Exception e){
            System.out.println("Something went wrong " + e.toString());
        }

        return jsonText;
    }

    @PostMapping("/index/updateBook")
    public String updateBook(@RequestParam String title, @RequestParam String author, @RequestParam String isbn, @RequestParam String oldIsbn){
        DbConnectionBook myConnection = new DbConnectionBook();
        Connection conn = null;
        Book book1 = null;
        String jsonText = null;
        try{
            conn = myConnection.connect();
            book1 = myConnection.findBookByISBN(conn, oldIsbn);
            book1.setTitle(title);
            book1.setAuthor(author);
            book1.setIsbn(isbn);
            conn = myConnection.connect();
            myConnection.updateBook(conn, book1, title, author, isbn, oldIsbn);
        }catch (Exception e){
            System.out.println("Something went wrong " + e.toString());
        }

        try{
            jsonText = obMapper.writeValueAsString(book1);
        }catch(JsonProcessingException e){
            System.out.println("Something went wrong2222 " + e.toString());
        }

        return jsonText;
    }

    @PostMapping("/index/updateBookAvailability")
    public String updateBookAvailability(@RequestParam Boolean status, @RequestParam String isbn){
        DbConnectionBook myConnection = new DbConnectionBook();
        Connection conn = null;
        Book book1 = null;
        String jsonText = null;
        try {
            conn = myConnection.connect();
            book1 = myConnection.findBookByISBN(conn, isbn);
            book1.setAvailable(status);
            System.out.println(book1);
            conn = myConnection.connect();
            myConnection.updateBookAvailability(conn, book1, isbn, status);
        }catch (Exception e){
            System.out.println("Something went wrong11111 " + e.toString());
        }


        try{
            jsonText = obMapper.writeValueAsString(book1);
        }catch(JsonProcessingException e){
            System.out.println("Something went wrong2222 " + e.toString());
        }

        return jsonText;
    }

    @PostMapping("/index/deleteBook")
    public String deleteBook(@RequestParam String isbn){
        DbConnectionBook myConnection = new DbConnectionBook();
        Connection conn = null;
        Book book1 = null;
        String jsonText = null;
        try{
            conn = myConnection.connect();
            book1 = myConnection.findBookByISBN(conn, isbn);
            System.out.println(book1);
            conn = myConnection.connect();
            myConnection.deleteBook(conn, book1);

        }catch (Exception e){
            System.out.println("Something went wrong " + e.toString());
        }

        try{
            jsonText = obMapper.writeValueAsString(book1);
        }catch(JsonProcessingException e){
            System.out.println("Something went wrong " + e.toString());
        }

        return jsonText;
    }

}

