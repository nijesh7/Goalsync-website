package com.smartgoal.dao;

import com.smartgoal.model.Goal;
import com.smartgoal.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GoalDAO {

    /** Insert a new goal; returns generated goal_id or -1. */
    public int addGoal(Goal goal) {
        String sql = "INSERT INTO goals (user_id, title, description, deadline, status, priority) VALUES (?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[]{"GOAL_ID"})) {

            ps.setInt(1, goal.getUserId());
            ps.setString(2, goal.getTitle());
            ps.setString(3, goal.getDescription());
            
            if (goal.getDeadline() != null && !goal.getDeadline().trim().isEmpty()) {
                ps.setDate(4, Date.valueOf(goal.getDeadline()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            
            ps.setString(5, goal.getStatus() == null ? "pending" : goal.getStatus());
            ps.setString(6, goal.getPriority() == null ? "medium" : goal.getPriority());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /** Returns all goals for a specific user. */
    public List<Goal> getGoalsByUser(int userId, String search, String priority, String status) {
        List<Goal> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM goals WHERE user_id = ?");
        
        if (search != null && !search.isEmpty()) sql.append(" AND LOWER(title) LIKE ?");
        if (priority != null && !priority.isEmpty()) sql.append(" AND priority = ?");
        if (status != null && !status.isEmpty()) sql.append(" AND status = ?");
        
        sql.append(" ORDER BY created_at DESC");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int idx = 1;
            ps.setInt(idx++, userId);
            if (search != null && !search.isEmpty()) ps.setString(idx++, "%" + search.toLowerCase() + "%");
            if (priority != null && !priority.isEmpty()) ps.setString(idx++, priority);
            if (status != null && !status.isEmpty()) ps.setString(idx++, status);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapGoal(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Overloaded for backward compatibility */
    public List<Goal> getGoalsByUser(int userId) {
        return getGoalsByUser(userId, null, null, null);
    }

    /** Returns ALL goals (for teacher view). */
    public List<Goal> getAllGoals(String search, String priority, String status) {
        List<Goal> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT g.*, u.name AS student_name FROM goals g JOIN users u ON g.user_id = u.id WHERE 1=1");
        
        if (search != null && !search.isEmpty()) sql.append(" AND LOWER(g.title) LIKE ?");
        if (priority != null && !priority.isEmpty()) sql.append(" AND g.priority = ?");
        if (status != null && !status.isEmpty()) sql.append(" AND g.status = ?");
        
        sql.append(" ORDER BY g.created_at DESC");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int idx = 1;
            if (search != null && !search.isEmpty()) ps.setString(idx++, "%" + search.toLowerCase() + "%");
            if (priority != null && !priority.isEmpty()) ps.setString(idx++, priority);
            if (status != null && !status.isEmpty()) ps.setString(idx++, status);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Goal g = mapGoal(rs);
                list.add(g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Overloaded for backward compatibility */
    public List<Goal> getAllGoals() {
        return getAllGoals(null, null, null);
    }

    /** Updates the status column for a goal. */
    public boolean updateStatus(int goalId, String status) {
        String sql = "UPDATE goals SET status = ? WHERE goal_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, goalId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Deletes a goal by ID. */
    public boolean deleteGoal(int goalId) {
        String sql = "DELETE FROM goals WHERE goal_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, goalId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String[]> getLeaderboard() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT u.name, COUNT(g.goal_id) as completed_count " +
                     "FROM users u JOIN goals g ON u.id = g.user_id " +
                     "WHERE g.status = 'completed' " +
                     "GROUP BY u.name ORDER BY completed_count DESC FETCH FIRST 5 ROWS ONLY";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new String[]{rs.getString("name"), String.valueOf(rs.getInt("completed_count"))});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Goal mapGoal(ResultSet rs) throws SQLException {
        Goal g = new Goal();
        g.setGoalId(rs.getInt("goal_id"));
        g.setUserId(rs.getInt("user_id"));
        g.setTitle(rs.getString("title"));
        g.setDescription(rs.getString("description"));
        // deadline may be null
        Date d = rs.getDate("deadline");
        g.setDeadline(d != null ? d.toString() : "");
        g.setStatus(rs.getString("status"));
        g.setPriority(rs.getString("priority"));
        Timestamp ts = rs.getTimestamp("created_at");
        g.setCreatedAt(ts != null ? ts.toString() : "");
        return g;
    }
}
