package com.github.ericguo.servlet.rest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TestRestServlet extends HttpServlet {
 

 
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
 
    out.println("GET request handling");
    out.println(request.getPathInfo());
    out.println(request.getParameterMap());
    try {
      RestRequest resourceValues = new RestRequest(request.getPathInfo());
      out.println(resourceValues.getId());
    } catch (ServletException e) {
      response.setStatus(400);
      response.resetBuffer();
      e.printStackTrace();
      out.println(e.toString());
    }
    out.close();
  }
 

 
}