package com.smartgoal.dao;

import com.smartgoal.model.Milestone;
import com.smartgoal.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MilestoneDAO {

    public boolean addMilestone(Milestone m) {
        String sql = "INSERT INTO milestones (goal_id, description, is_completed) VALUES (?, ?, 0)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, m.getGoalId());
            ps.setString(2, m.getDescription());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Milestone> getMilestonesByGoal(int goalId) {
        List<Milestone> list = new ArrayList<>();
        String sql = "SELECT * FROM milestones WHERE goal_id = ? ORDER BY milestone_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, goalId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Milestone m = new Milestone();
                m.setMilestoneId(rs.getInt("milestone_id"));
                m.setGoalId(rs.getInt("goal_id"));
                m.setDescription(rs.getString("description"));
                m.setIsCompleted(rs.getInt("is_completed"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateStatus(int milestoneId, int isCompleted) {
        String sql = "UPDATE milestones SET is_completed = ? WHERE milestone_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, isCompleted);
            ps.setInt(2, milestoneId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteMilestone(int milestoneId) {
        String sql = "DELETE FROM milestones WHERE milestone_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, milestoneId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
