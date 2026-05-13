package com.smartgoal.servlet;

import com.smartgoal.dao.NotificationDAO;
import com.smartgoal.model.Notification;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/GetNotificationsServlet")
public class GetNotificationsServlet extends HttpServlet {
    private NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(401);
            return;
        }
        
        int userId = (Integer) session.getAttribute("userId");
        List<Notification> notifications = notificationDAO.getNotificationsByUser(userId);
        
        StringBuilder json = new StringBuilder();
        json.append("{\"notifications\": [");
        for (int i = 0; i < notifications.size(); i++) {
            Notification n = notifications.get(i);
            json.append("{")
                .append("\"id\":").append(n.getId()).append(",")
                .append("\"message\":\"").append(n.getMessage().replace("\"", "\\\"")).append("\",")
                .append("\"isRead\":").append(n.getIsRead() == 1).append(",")
                .append("\"timeAgo\":\"").append(n.getTimeAgo()).append("\"")
                .append("}");
            if (i < notifications.size() - 1) json.append(",");
        }
        json.append("]}");
        
        response.setContentType("application/json");
        response.getWriter().write(json.toString());
    }
}
