package com.bk.code.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import com.bk.code.service.CRUDService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/db")
public class CRUDController extends HttpServlet {

  private CRUDService dbService = new CRUDService();
  private static Connection connection = null;
  private final String dbUrl = "jdbc:mysql://localhost:3306/testFirst";

  @Override
  public void init() throws ServletException {
      try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection  = DriverManager.getConnection(dbUrl, "root", "pa55bird");
            } catch (ClassNotFoundException e) {
                
                e.printStackTrace();
                System.err.println("\n\nUnable to load classs\n\n");
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("connection creation failed!");
            }
  }


  
  
  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String status = dbService.deleteUser(req.getParameter("userEmail"));
    
    resp.getWriter().print("<h5 style='color:red'>"+status+"</h5>");
    
  }
  
  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    StringBuilder stringBuilder = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))) {
      String line;
      while ((line = reader.readLine()) != null) {
        stringBuilder.append(line);
      }
        }
        
        String body = stringBuilder.toString();
        System.out.println("PUT request body is :"+body);  
        Map<String, String> parameters = parseUrlEncodedBody(body);
        
        String status = "";
        try {
          status = dbService.updateUser(parameters);
        } catch (SQLException e) {
          status = "sql exception occured!";
          e.printStackTrace();
        }  

        response.getWriter().print(status);

    }

    private Map<String, String> parseUrlEncodedBody(String body) throws IOException {
      Map<String, String> parameters = new HashMap<>();
      String[] pairs = body.split("&");
      for (String pair : pairs) {
        int idx = pair.indexOf("=");
        String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8.name());
        String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8.name());
        parameters.put(key, value);
      }
        return parameters;
      }
      
      @Override
      protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
    String status = "";
    
    if(req.getParameter("userEmail") != null) {
      req.setAttribute("userEmail", req.getParameter("userEmail"));
      req.getRequestDispatcher("/WEB-INF/views/updateForm.jsp").forward(req, resp);
      return;
    }

    String[] values = {
        req.getParameter("username"),
        req.getParameter("email"),
        req.getParameter("password"),
        req.getParameter("firstName"),
        req.getParameter("lastName"),
        req.getParameter("dateOfBirth"),
      };
      try {
        status = dbService.insertUser(values);
      } catch (SQLException e) {
        status = "insertFailed";
        e.printStackTrace();
      }
      req.setAttribute("message", status);
      
      req.getRequestDispatcher("/WEB-INF/views/status.jsp")
      .forward(req, resp);
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    String result = null;
    // get writer
    PrintWriter writer = resp.getWriter();

    // set content type
    resp.setContentType("text/html");
    
    // FIXME blank enter key press

    if (req.getParameter("operation") != null) {
      req.setAttribute("message", "new user entry");
      RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/inputForm.jsp");
      rd.forward(req, resp);
      return;
    }

    if (req.getParameter("searchName") != null) {
      try {
        writer.print(dbService.getUserByName(req.getParameter("searchName")));
      } catch (SQLException e) {
        writer.print("something wrong in finding user by name");
        e.printStackTrace();
        writer.close();
        return;
      }
      return;
    }
    
    if (req.getParameter("searchEmail") != null) {
      try {
        writer.print(dbService.getUserByEmail(req.getParameter("searchEmail")));
      } catch (SQLException e) {
        writer.print("something wrong in finding user by email");
        e.printStackTrace();
        writer.close();
        return;
      }
      return;
    }

    try {
      result = dbService.getAllUsers();
    } catch (SQLException e) {
      
      e.printStackTrace();
    }

    writer.println(result);
  }

  public static Connection getConnection() {
    return connection;
  }

  @Override
  public void destroy() {
      try {
        connection.close();
      } catch (SQLException e) {
        System.out.println("connection closed");
        e.printStackTrace();
      }
  }
}
