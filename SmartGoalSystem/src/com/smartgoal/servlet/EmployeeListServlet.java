package com.smartgoal.servlet;

import com.smartgoal.dao.UserDAO;
import com.smartgoal.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/EmployeeListServlet")
public class EmployeeListServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null || !"manager".equals(session.getAttribute("userRole"))) {
            response.sendRedirect("login.html");
            return;
        }
        
        List<User> employees = userDAO.getAllEmployees();
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Assign Task</title>");
        out.println("<link rel='stylesheet' href='css/style.css'>");
        out.println("<link href='https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700&display=swap' rel='stylesheet'>");
        out.println("</head><body>");
        out.println("<div class='glass-bg'></div>");
        
        out.println("<div class='container'>");
        out.println("<div class='form-container animate-fade-in'>");
        out.println("<a href='managerDashboard.html' class='back-link'>← Back to Dashboard</a>");
        out.println("<div class='form-box'>");
        out.println("<div class='form-header'>");
        out.println("<h2>Assign New Task</h2>");
        out.println("<p>Select an employee and define their next objective</p>");
        out.println("</div>");
        
        out.println("<form action='AssignmentServlet' method='POST'>");
        out.println("<div class='form-group'>");
        out.println("<label for='employeeId'>Select Employee:</label>");
        out.println("<select name='employeeId' id='employeeId' required>");
        out.println("<option value=''>-- Select an Employee --</option>");
        for (User u : employees) {
            out.println("<option value='" + u.getId() + "'>" + u.getName() + " (" + u.getEmail() + ")</option>");
        }
        out.println("</select>");
        out.println("</div>");
        
        out.println("<div class='form-group'>");
        out.println("<label for='task'>Task Description:</label>");
        out.println("<textarea name='task' id='task' rows='4' required></textarea>");
        out.println("</div>");
        
        out.println("<button type='submit' class='btn-success full-width'>Assign Task</button>");
        out.println("</form>");
        out.println("</div>");
        out.println("</div>");
        
        out.println("</div>");
        out.println("</body></html>");
    }
}
