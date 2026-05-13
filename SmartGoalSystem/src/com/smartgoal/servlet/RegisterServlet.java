package com.smartgoal.servlet;

import com.smartgoal.dao.UserDAO;
import com.smartgoal.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role"); // 'employee' or 'manager'
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        if (userDAO.emailExists(email)) {
            out.println("<html><body>");
            out.println("<script>alert('Email already registered!'); window.location='register.html';</script>");
            out.println("</body></html>");
            return;
        }
        
        User u = new User(0, name, email, password, role);
        int result = userDAO.registerUser(u);
        
        if (result > 0) {
            out.println("<html><body>");
            out.println("<script>alert('Registration successful! Please login.'); window.location='login.html';</script>");
            out.println("</body></html>");
        } else {
            out.println("<html><body>");
            out.println("<script>alert('Registration failed. Try again.'); window.location='register.html';</script>");
            out.println("</body></html>");
        }
    }
}
