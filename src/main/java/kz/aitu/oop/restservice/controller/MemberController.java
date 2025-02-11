package kz.aitu.oop.restservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.aitu.oop.restservice.dbconnections.DbConnectionMember;
import kz.aitu.oop.restservice.entities.LibraryMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.util.ArrayList;

@RestController
public class MemberController {
    @Autowired
    private ObjectMapper obMapper;

    @GetMapping("/index/allMembers")
    public String allMembers(){
        DbConnectionMember myConnection = new DbConnectionMember();
        Connection conn = null;
        ArrayList<LibraryMember> members = new ArrayList<LibraryMember>();
        try{
            conn = myConnection.connect();
            members = myConnection.getAllMembers(conn);
        }catch(Exception e){
            System.out.println("Something went wrong " + e.toString());
        }

        String jsonText = null;
        try{
            jsonText = obMapper.writeValueAsString(members);
        }catch(JsonProcessingException e){
            System.out.println("Something went wrong with book " + e.toString());
        }

        return jsonText;
    }

    @PostMapping("/index/createMember")
    public String createMember(@RequestParam String name, @RequestParam String email, @RequestParam String phone){
        DbConnectionMember myConnection = new DbConnectionMember();
        Connection conn = null;
        LibraryMember member = new LibraryMember(name, email, phone);
        String jsonText = null;
        try{
            conn = myConnection.connect();
        }catch(Exception e){
            System.out.println("Something went wrong " + e.toString());
        }

        try{
            jsonText = obMapper.writeValueAsString(myConnection.createMember(conn, member));
        }catch (Exception e){
            System.out.println("Something went wrong " + e.toString());
        }

        return jsonText;
    }

    @PostMapping("/index/findMember")
    public String findMemberById(@RequestParam int id){
        DbConnectionMember myConnection = new DbConnectionMember();
        Connection conn = null;
        LibraryMember member = null;
        try{
            conn = myConnection.connect();
        }catch(Exception e){
            System.out.println("Something went wrong " + e.toString());
        }

        try{
            member = myConnection.findMemberById(conn, id);
        }catch(Exception e){
            System.out.println("Something went wrong " + e.toString());
        }


        String jsonText = null;
        try{
            jsonText = obMapper.writeValueAsString(member);
        }catch(JsonProcessingException e){
            System.out.println("Something went wrong " + e.toString());
        }

        return jsonText;
    }

    @PostMapping("/index/updateMember")
    public String updateMember(@RequestParam int id, @RequestParam String name, @RequestParam String email, @RequestParam String phone){
        DbConnectionMember myConnection = new DbConnectionMember();
        Connection conn = null;
        LibraryMember member = null;
        String jsonText = null;
        try{
            conn = myConnection.connect();
            member = myConnection.findMemberById(conn, id);
            member.setName(name);
            member.setEmail(email);
            member.setPhone(phone);
            conn = myConnection.connect();
            myConnection.updateMember(conn, id, name, email, phone);
        }catch (Exception e){
            System.out.println("Something went wrong " + e.toString());
        }

        try{
            jsonText = obMapper.writeValueAsString(member);
        }catch(JsonProcessingException e){
            System.out.println("Something went wrong2222 " + e.toString());
        }

        return jsonText;
    }

    @PostMapping("/index/deleteMember")
    public String deleteMember(@RequestParam int id){
        DbConnectionMember myConnection = new DbConnectionMember();
        Connection conn = null;
        LibraryMember member = null;
        String jsonText = null;
        try{
            conn = myConnection.connect();
            member = myConnection.findMemberById(conn, id);
            System.out.println(member);
            conn = myConnection.connect();
            myConnection.deleteMember(conn, member);

        }catch (Exception e){
            System.out.println("Something went wrong " + e.toString());
        }

        try{
            jsonText = obMapper.writeValueAsString(member);
        }catch(JsonProcessingException e){
            System.out.println("Something went wrong " + e.toString());
        }

        return jsonText;
    }
}
