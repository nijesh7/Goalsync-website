package com.smartgoal.servlet;

import com.smartgoal.dao.MilestoneDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/UpdateMilestoneServlet")
public class UpdateMilestoneServlet extends HttpServlet {
    private MilestoneDAO milestoneDAO = new MilestoneDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        int milestoneId = Integer.parseInt(request.getParameter("milestoneId"));
        int isCompleted = Integer.parseInt(request.getParameter("isCompleted"));
        
        milestoneDAO.updateStatus(milestoneId, isCompleted);
        
        response.sendRedirect("ViewGoalServlet");
    }
}
