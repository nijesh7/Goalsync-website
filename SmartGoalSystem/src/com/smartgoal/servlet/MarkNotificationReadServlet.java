package com.smartgoal.servlet;

import com.smartgoal.dao.NotificationDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/MarkNotificationReadServlet")
public class MarkNotificationReadServlet extends HttpServlet {
    private NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(401);
            return;
        }
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            notificationDAO.markAsRead(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        response.setStatus(200);
    }
}
