package com.smartgoal.servlet;

import com.smartgoal.dao.MilestoneDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/DeleteMilestoneServlet")
public class DeleteMilestoneServlet extends HttpServlet {
    private MilestoneDAO milestoneDAO = new MilestoneDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.html");
            return;
        }
        
        try {
            int milestoneId = Integer.parseInt(request.getParameter("milestoneId"));
            milestoneDAO.deleteMilestone(milestoneId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Redirect back to the goals view
        response.sendRedirect("ViewGoalServlet");
    }
}
