package com.smartgoal.servlet;

import com.smartgoal.dao.MilestoneDAO;
import com.smartgoal.model.Milestone;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/AddMilestoneServlet")
public class AddMilestoneServlet extends HttpServlet {
    private MilestoneDAO milestoneDAO = new MilestoneDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        int goalId = Integer.parseInt(request.getParameter("goalId"));
        String description = request.getParameter("description");
        
        if (description != null && !description.trim().isEmpty()) {
            Milestone m = new Milestone();
            m.setGoalId(goalId);
            m.setDescription(description);
            milestoneDAO.addMilestone(m);
        }
        
        response.sendRedirect("ViewGoalServlet");
    }
}
