package com.smartgoal.servlet;

import com.smartgoal.dao.AssignmentDAO;
import com.smartgoal.model.Assignment;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/ViewAssignmentServlet")
public class ViewAssignmentServlet extends HttpServlet {
    private AssignmentDAO assignmentDAO = new AssignmentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.html");
            return;
        }
        
        int userId = (Integer) session.getAttribute("userId");
        String role = (String) session.getAttribute("userRole");
        
        List<Assignment> assignments;
        if ("manager".equals(role)) {
            assignments = assignmentDAO.getAssignmentsByManager(userId);
        } else {
            assignments = assignmentDAO.getAssignmentsByEmployee(userId);
        }
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Goal Sync - Assignments</title>");
        out.println("<link rel='stylesheet' href='css/style.css'>");
        out.println("<link href='https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700&display=swap' rel='stylesheet'>");
        out.println("</head><body>");
        out.println("<div class='glass-bg'></div>");
        
        out.println("<div class='layout-with-sidebar'>");
        
        // Sidebar
        out.println("<aside class='sidebar animate-fade-in'>");
        out.println("<div class='sidebar-brand'>Goal Sync</div>");
        out.println("<nav class='sidebar-nav'>");
        if ("manager".equals(role)) {
            out.println("<a href='managerDashboard.html' class='nav-item'><span>Dashboard</span></a>");
            out.println("<a href='ViewGoalServlet' class='nav-item'><span>All Goals</span></a>");
            out.println("<a href='EmployeeListServlet' class='nav-item'><span>Assign Task</span></a>");
            out.println("<a href='ViewAssignmentServlet' class='nav-item active'><span>Given Tasks</span></a>");
        } else {
            out.println("<a href='employeeDashboard.html' class='nav-item'><span>Dashboard</span></a>");
            out.println("<a href='ViewGoalServlet' class='nav-item'><span>My Goals</span></a>");
            out.println("<a href='addGoal.html' class='nav-item'><span>New Goal</span></a>");
            out.println("<a href='ViewAssignmentServlet' class='nav-item active'><span>Assignments</span></a>");
        }
        out.println("</nav>");
        out.println("<div class='sidebar-footer'>");
        out.println("<a href='LogoutServlet' class='nav-item' style='color: var(--danger);'><span>Logout</span></a>");
        out.println("</div>");
        out.println("</aside>");

        // Main Content
        out.println("<main class='main-content'>");
        
        if ("manager".equals(role)) {
            out.println("<div style='display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px;'>");
            out.println("<h2>Team Assignments</h2>");
            out.println("<a href='EmployeeListServlet' class='btn-success'>+ Add New Task</a>");
            out.println("</div>");
        } else {
            out.println("<h2>My Assigned Tasks</h2>");
        }
        
        if (assignments.isEmpty()) {
            out.println("<p style='color: var(--text-muted);'>No assignments found.</p>");
        } else {
            out.println("<div class='table-container animate-fade-in'>");
            out.println("<table>");
            out.println("<thead><tr><th>ID</th><th>Manager</th><th>Employee</th><th>Task</th><th>Assigned On</th></tr></thead>");
            out.println("<tbody>");
            for (Assignment a : assignments) {
                out.println("<tr>");
                out.println("<td>" + a.getAssignmentId() + "</td>");
                out.println("<td><strong>" + a.getManagerName() + "</strong></td>");
                out.println("<td>" + a.getEmployeeName() + "</td>");
                out.println("<td style='color: #cbd5e1;'>" + a.getTask() + "</td>");
                out.println("<td style='color: var(--text-muted); font-size: 0.9rem;'>" + a.getCreatedAt() + "</td>");
                out.println("</tr>");
            }
            out.println("</tbody></table>");
            out.println("</div>");
        }
        
        out.println("</main>"); // Close main-content
        out.println("</div>"); // Close layout-with-sidebar
        out.println("</body></html>");
    }
}
