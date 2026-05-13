package com.smartgoal.servlet;

import com.smartgoal.dao.UserDAO;
import com.smartgoal.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        User user = userDAO.validateUser(email, password);
        
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getName());
            session.setAttribute("userRole", user.getRole());
            
            if ("manager".equals(user.getRole())) {
                response.sendRedirect("managerDashboard.html");
            } else {
                response.sendRedirect("employeeDashboard.html");
            }
        } else {
            out.println("<html><body>");
            out.println("<script>alert('Invalid Email or Password'); window.location='login.html';</script>");
            out.println("</body></html>");
        }
    }
}
