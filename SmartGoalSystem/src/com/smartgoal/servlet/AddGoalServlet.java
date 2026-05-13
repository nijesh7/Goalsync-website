package com.smartgoal.servlet;

import com.smartgoal.dao.GoalDAO;
import com.smartgoal.model.Goal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/AddGoalServlet")
public class AddGoalServlet extends HttpServlet {
    private GoalDAO goalDAO = new GoalDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.html");
            return;
        }
        
        int userId = (Integer) session.getAttribute("userId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String deadline = request.getParameter("deadline");
        String priority = request.getParameter("priority");
        
        Goal g = new Goal(0, userId, title, description, deadline, "pending", priority, null);
        int result = goalDAO.addGoal(g);
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        if (result > 0) {
            out.println("<html><body>");
            out.println("<script>alert('Goal added successfully!'); window.location='ViewGoalServlet';</script>");
            out.println("</body></html>");
        } else {
            out.println("<html><body>");
            out.println("<script>alert('Failed to add goal.'); window.location='addGoal.html';</script>");
            out.println("</body></html>");
        }
    }
}
