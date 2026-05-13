package com.smartgoal.servlet;

import com.smartgoal.dao.GoalDAO;
import com.smartgoal.dao.MilestoneDAO;
import com.smartgoal.model.Goal;
import com.smartgoal.model.Milestone;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@WebServlet("/ViewGoalServlet")
public class ViewGoalServlet extends HttpServlet {
    private GoalDAO goalDAO = new GoalDAO();
    private MilestoneDAO milestoneDAO = new MilestoneDAO();

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
        
        String search = request.getParameter("search");
        String priority = request.getParameter("priority");
        String status = request.getParameter("status");
        
        List<Goal> goals;
        if ("manager".equals(role)) {
            goals = goalDAO.getAllGoals(search, priority, status);
        } else {
            goals = goalDAO.getGoalsByUser(userId, search, priority, status);
        }
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
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
            out.println("<a href='ViewGoalServlet' class='nav-item active'><span>All Goals</span></a>");
            out.println("<a href='EmployeeListServlet' class='nav-item'><span>Assign Task</span></a>");
        } else {
            out.println("<a href='employeeDashboard.html' class='nav-item'><span>Dashboard</span></a>");
            out.println("<a href='ViewGoalServlet' class='nav-item active'><span>My Goals</span></a>");
            out.println("<a href='addGoal.html' class='nav-item'><span>New Goal</span></a>");
        }
        out.println("<a href='ViewAssignmentServlet' class='nav-item'><span>" + ("manager".equals(role) ? "Given Tasks" : "Assignments") + "</span></a>");
        out.println("</nav>");
        out.println("<div class='sidebar-footer'>");
        out.println("<a href='LogoutServlet' class='nav-item' style='color: var(--danger);'><span>Logout</span></a>");
        out.println("</div>");
        out.println("</aside>");

        // Main Content
        out.println("<main class='main-content'>");
        out.println("<div class='dashboard-header animate-fade-in'>");
        out.println("<h2>" + ("manager".equals(role) ? "Team Goals" : "My Goals") + "</h2>");
        out.println("</div>");
        
        // Filter Bar
        out.println("<div class='filter-bar animate-fade-in'>");
        out.println("<form action='ViewGoalServlet' method='get' class='filter-form'>");
        out.println("<input type='text' name='search' placeholder='Search by title...' value='" + (search != null ? search : "") + "' class='filter-input'>");
        out.println("<select name='priority' class='filter-select'>");
        out.println("<option value=''>All Priority</option>");
        out.println("<option value='high' " + ("high".equals(priority) ? "selected" : "") + ">High</option>");
        out.println("<option value='medium' " + ("medium".equals(priority) ? "selected" : "") + ">Medium</option>");
        out.println("<option value='low' " + ("low".equals(priority) ? "selected" : "") + ">Low</option>");
        out.println("</select>");
        out.println("<select name='status' class='filter-select'>");
        out.println("<option value=''>All Status</option>");
        out.println("<option value='pending' " + ("pending".equals(status) ? "selected" : "") + ">Pending</option>");
        out.println("<option value='in_progress' " + ("in_progress".equals(status) ? "selected" : "") + ">In Progress</option>");
        out.println("<option value='completed' " + ("completed".equals(status) ? "selected" : "") + ">Completed</option>");
        out.println("</select>");
        out.println("<button type='submit' class='btn-primary'>Filter</button>");
        out.println("<a href='ViewGoalServlet' class='btn-secondary' style='text-decoration:none;'>Clear</a>");
        out.println("</form>");
        out.println("</div>");
        
        if (goals.isEmpty()) {
            out.println("<p style='color: var(--text-muted);'>No goals found.</p>");
        } else {
            out.println("<div class='table-container animate-fade-in'>");
            out.println("<table>");
            out.println("<thead><tr><th>ID</th>");
            if ("manager".equals(role)) {
                out.println("<th>Employee</th>");
            }
            out.println("<th>Title</th><th>Description</th><th>Deadline</th><th>Priority</th><th>Status</th><th>Progress</th><th>Action</th></tr></thead><tbody>");
            
            for (Goal g : goals) {
                out.println("<tr>");
                out.println("<td>" + g.getGoalId() + "</td>");
                if ("manager".equals(role)) {
                    out.println("<td>Employee " + g.getUserId() + "</td>");
                }
                out.println("<td><strong>" + g.getTitle() + "</strong>");
                
                // Milestones Display
                List<Milestone> milestones = milestoneDAO.getMilestonesByGoal(g.getGoalId());
                if (!milestones.isEmpty()) {
                    out.println("<div class='milestone-list'>");
                    for (Milestone m : milestones) {
                        out.println("<div class='milestone-item " + (m.getIsCompleted() == 1 ? "done" : "") + "'>");
                        out.println("<form action='UpdateMilestoneServlet' method='post' style='display:inline;'>");
                        out.println("<input type='hidden' name='milestoneId' value='" + m.getMilestoneId() + "'>");
                        out.println("<input type='hidden' name='isCompleted' value='" + (m.getIsCompleted() == 1 ? 0 : 1) + "'>");
                        out.println("<input type='checkbox' " + (m.getIsCompleted() == 1 ? "checked" : "") + " onchange='this.form.submit()'>");
                        out.println("</form>");
                        out.println("<span>" + m.getDescription() + "</span>");
                        out.println("</div>");
                    }
                    out.println("</div>");
                }
                
                // Add Milestone Form (only for employees or managers)
                out.println("<form action='AddMilestoneServlet' method='post' class='add-milestone-form'>");
                out.println("<input type='hidden' name='goalId' value='" + g.getGoalId() + "'>");
                out.println("<input type='text' name='description' placeholder='Add milestone...' required>");
                out.println("<button type='submit'>+</button>");
                out.println("</form>");
                
                out.println("</td>");
                out.println("<td style='color: var(--text-muted);'>" + g.getDescription() + "</td>");
                
                // Deadline and Countdown
                String deadlineStr = g.getDeadline();
                String countdown = "";
                if (deadlineStr != null && !deadlineStr.isEmpty()) {
                    try {
                        LocalDate dl = LocalDate.parse(deadlineStr);
                        long days = ChronoUnit.DAYS.between(LocalDate.now(), dl);
                        if (days < 0) countdown = "<br><span style='color:var(--danger); font-size:0.75rem;'>Overdue</span>";
                        else if (days == 0) countdown = "<br><span style='color:var(--warning); font-size:0.75rem;'>Due Today</span>";
                        else countdown = "<br><span style='color:var(--info); font-size:0.75rem;'>" + days + " days left</span>";
                    } catch (Exception e) {}
                }
                out.println("<td>" + deadlineStr + countdown + "</td>");
                out.println("<td><span class='priority-badge " + g.getPriority() + "'>" + g.getPriority().toUpperCase() + "</span></td>");
                out.println("<td><span class='badge " + g.getStatus() + "'>" + g.getStatus().replace("_", " ") + "</span></td>");
                
                // Progress calculation
                int percent = 0;
                if ("in_progress".equals(g.getStatus())) percent = 50;
                else if ("completed".equals(g.getStatus())) percent = 100;
                
                out.println("<td>");
                out.println("<div class='progress-container'>");
                out.println("<div class='progress-bar' style='width: " + percent + "%;'></div>");
                out.println("<span class='progress-text'>" + percent + "%</span>");
                out.println("</div>");
                out.println("</td>");
                
                out.println("<td>");
                out.println("<form action='ProgressServlet' method='post' style='display:flex; gap: 8px;'>");
                out.println("<input type='hidden' name='goalId' value='" + g.getGoalId() + "'>");
                out.println("<select name='status' style='padding: 4px 8px; font-size: 0.85rem; border-radius: 8px;'>");
                out.println("<option value='pending' " + ("pending".equals(g.getStatus()) ? "selected" : "") + ">Pending</option>");
                out.println("<option value='in_progress' " + ("in_progress".equals(g.getStatus()) ? "selected" : "") + ">In Progress</option>");
                out.println("<option value='completed' " + ("completed".equals(g.getStatus()) ? "selected" : "") + ">Completed</option>");
                out.println("</select>");
                out.println("<button type='submit' class='btn-primary' style='padding: 4px 12px; font-size: 0.8rem;'>Update</button>");
                out.println("</form>");
                out.println("</td>");
                
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
