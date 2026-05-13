package com.smartgoal.servlet;

import com.smartgoal.dao.GoalDAO;
import com.smartgoal.dao.ProgressDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/ProgressServlet")
public class ProgressServlet extends HttpServlet {
    private GoalDAO goalDAO = new GoalDAO();
    private ProgressDAO progressDAO = new ProgressDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.html");
            return;
        }
        
        int goalId = Integer.parseInt(request.getParameter("goalId"));
        String status = request.getParameter("status");
        
        boolean updated = goalDAO.updateStatus(goalId, status);
        if (updated) {
            progressDAO.upsertProgress(goalId, status);
        }
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        if (updated) {
            out.println("<html><body>");
            out.println("<script>alert('Status updated successfully!'); window.location='ViewGoalServlet';</script>");
            out.println("</body></html>");
        } else {
            out.println("<html><body>");
            out.println("<script>alert('Failed to update status.'); window.location='ViewGoalServlet';</script>");
            out.println("</body></html>");
        }
    }
}
