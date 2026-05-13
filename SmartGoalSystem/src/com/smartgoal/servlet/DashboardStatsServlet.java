package com.smartgoal.servlet;

import com.smartgoal.dao.GoalDAO;
import com.smartgoal.model.Goal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/DashboardStatsServlet")
public class DashboardStatsServlet extends HttpServlet {
    private GoalDAO goalDAO = new GoalDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(401);
            return;
        }
        
        int userId = (Integer) session.getAttribute("userId");
        String role = (String) session.getAttribute("userRole");
        
        List<Goal> goals;
        if ("manager".equals(role)) {
            goals = goalDAO.getAllGoals();
        } else {
            goals = goalDAO.getGoalsByUser(userId);
        }
        
        int total = goals.size();
        int pending = 0;
        int inProgress = 0;
        int completed = 0;
        int highPriority = 0;
        
        for (Goal g : goals) {
            if ("pending".equals(g.getStatus())) pending++;
            else if ("in_progress".equals(g.getStatus())) inProgress++;
            else if ("completed".equals(g.getStatus())) completed++;
            
            if ("high".equals(g.getPriority())) highPriority++;
        }
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print("{");
        out.print("\"total\":" + total + ",");
        out.print("\"pending\":" + pending + ",");
        out.print("\"inProgress\":" + inProgress + ",");
        out.print("\"completed\":" + completed + ",");
        out.print("\"highPriority\":" + highPriority + ",");
        out.print("\"userName\":\"" + session.getAttribute("userName") + "\"");
        
        if ("manager".equals(role)) {
            List<String[]> leaderboard = goalDAO.getLeaderboard();
            out.print(",\"leaderboard\":[");
            for (int i = 0; i < leaderboard.size(); i++) {
                String[] entry = leaderboard.get(i);
                out.print("{\"name\":\"" + entry[0] + "\",\"count\":" + entry[1] + "}");
                if (i < leaderboard.size() - 1) out.print(",");
            }
            out.print("]");
        }
        
        out.print("}");
    }
}
