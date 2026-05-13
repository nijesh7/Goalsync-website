package com.smartgoal.servlet;

import com.smartgoal.dao.GoalDAO;
import com.smartgoal.dao.NotificationDAO;
import com.smartgoal.model.Goal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/UpdateGoalNotesServlet")
public class UpdateGoalNotesServlet extends HttpServlet {
    private GoalDAO goalDAO = new GoalDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null || !"manager".equals(session.getAttribute("userRole"))) {
            response.sendRedirect("login.html");
            return;
        }
        
        int goalId = Integer.parseInt(request.getParameter("goalId"));
        String notes = request.getParameter("managerNotes");
        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
        
        boolean success = goalDAO.updateManagerNotes(goalId, notes);
        
        if (success) {
            new NotificationDAO().addNotification(employeeId, "Manager added feedback to your goal: " + (notes.length() > 30 ? notes.substring(0, 27) + "..." : notes));
        }
        
        response.sendRedirect("ViewGoalServlet");
    }
}
