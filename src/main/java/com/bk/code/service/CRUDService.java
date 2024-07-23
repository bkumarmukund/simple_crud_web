package com.bk.code.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.bk.code.servlet.CRUDController;

public class CRUDService {
    
    private Connection con  = null;
    PreparedStatement ps = null;

    // READ
    private final String readAllUsersQuery = "select * from user";
    private final String readUsersByNameQuery = "select * from user where username like ?";
    private final String readUsersByEmailQuery = "select * from user where email like ?";
    //CREATE
    private final String readBeforeInsert = "select * from user where email=?";
    private final String insertUserQuery = "insert into user(username,email,password,first_name,last_name,date_of_birth) values(?,?,?,?,?,?)";
    //UPDATE
    private final String updateUserQuery = "update user set username=?, first_name=?, last_name=?, date_of_birth=? where email=?";
    //DELETE
    private final String deleteUserQuery = "delete from user where email=?";


    public String deleteUser(String email) {
        int result = 0;
        try {
            getStatement(deleteUserQuery);
            ps.setString(1, email);
            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return "database error!";
        }
        if (result == 0) return "nothing to delete!";
        return "deleted!";
    }

    public String updateUser(Map<String, String> parameters) throws SQLException {
        getStatement(readBeforeInsert);
        ps.setString(1, parameters.get("userEmail"));
        ResultSet rs = ps.executeQuery();

        // avoid non existant update
        if (!rs.next()) return "user is not registered!";

        getStatement(updateUserQuery);
        
        ps.setString(1, parameters.get("username"));
        ps.setString(2, parameters.get("firstName"));
        ps.setString(3, parameters.get("lastName"));
        ps.setString(4, parameters.get("dateOfBirth"));
        ps.setString(5, parameters.get("userEmail"));

        return (ps.executeUpdate()) == 0 ? "Record not updated" : "Record Updated!"; 
    }

    public String insertUser(String[] values) throws SQLException {
        getStatement(readBeforeInsert);
        ps.setString(1, values[1]);
        ResultSet rs = ps.executeQuery();

        // avoid duplicate entry
        if (rs.next()) return "duplicate value is not allowed!";

        getStatement(insertUserQuery);
        for (int i = 0; i < values.length; i++) {
            ps.setString(i+1, values[i]);
        }
        return (ps.executeUpdate()) == 0 ? "Failed to Save Record" : "Data saved!"; 
    }

    public String getAllUsers() throws SQLException {
        getStatement(readAllUsersQuery);
        return getUsers(readAllUsersQuery);
    }

    public String getUserByName(String nameInput) throws SQLException {
        // temp = null;
        getStatement(readUsersByNameQuery);
        ps.setString(1, "%"+nameInput+"%");
        return getUsers(readUsersByNameQuery);
    }

    public String getUserByEmail(String emailInput) throws SQLException {
        // temp = null;
        getStatement(readUsersByEmailQuery);
        ps.setString(1, "%"+emailInput+"%");
        return getUsers(readUsersByEmailQuery);
    }

    private String getUsers(String readQuery) throws SQLException {
        // con = getConnectionIfNotAvailable();
        // psRead = con.prepareStatement(readQuery);


        StringBuilder result = new StringBuilder();
        boolean resultNotFount = true;

        // ResultSet rs = psRead.executeQuery();
        ResultSet rs = ps.executeQuery();

        // get writer
        result.append("<tr>"+
        "<th>::Name::</th>"+
        "<th>::email::</th>"+
        "<th>::birth::</th>"+
        "<th></th>"+
        // "<td>::birthDate::</td>"+
        // "<td>::accountCreatedOn::</td>"+
        // "<td>::accountUpdatedOn::</td>"+
        "</tr>"
        );
        
        while (rs.next()) {
            resultNotFount = false;
            result.append("<tr>");
            result.append("<td>"+
                rs.getString("first_name")+" "
                +rs.getString("last_name")+"</td>"
            );
            result.append("<td>"+
            rs.getString("email")+
            "</td>"
            );
            result.append("<td>"+
            rs.getDate("date_of_birth")+
            "</td>"
            );
            result.append(
                "<td>"+
                "<button hx-post='db' hx-trigger='click' hx-target='#search-results' style='color:blue' name='userEmail' value="+
            rs.getString("email")+">update</button>"+
                "<button hx-delete='db' hx-trigger='click' hx-target='closest td' style='color:red' name='userEmail' value="+
            rs.getString("email")+">delete</button>"+
            "</td>"
            );
            result.append("</tr>");

        }

        if(resultNotFount) return "no data found!";
        return result.toString();

    }

    private void getStatement(String sqlString) throws SQLException {
        con = getConnectionIfNotAvailable();
        ps = con.prepareStatement(sqlString);
    }

    private Connection getConnectionIfNotAvailable() {
        return CRUDController.getConnection();
    }
}
