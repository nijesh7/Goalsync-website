package com.smartgoal.servlet;

import com.smartgoal.dao.AssignmentDAO;
import com.smartgoal.model.Assignment;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/AssignmentServlet")
public class AssignmentServlet extends HttpServlet {
    private AssignmentDAO assignmentDAO = new AssignmentDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null || !"manager".equals(session.getAttribute("userRole"))) {
            response.sendRedirect("login.html");
            return;
        }
        
        int managerId = (Integer) session.getAttribute("userId");
        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
        String task = request.getParameter("task");
        
        Assignment a = new Assignment();
        a.setManagerId(managerId);
        a.setEmployeeId(employeeId);
        a.setTask(task);
        
        int result = assignmentDAO.addAssignment(a);
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        if (result > 0) {
            out.println("<html><body>");
            out.println("<script>alert('Task assigned successfully!'); window.location='ViewAssignmentServlet';</script>");
            out.println("</body></html>");
        } else {
            out.println("<html><body>");
            out.println("<script>alert('Failed to assign task.'); window.location='assignments.html';</script>");
            out.println("</body></html>");
        }
    }
}
