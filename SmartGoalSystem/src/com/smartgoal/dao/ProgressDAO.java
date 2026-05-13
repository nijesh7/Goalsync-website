package com.smartgoal.dao;

import com.smartgoal.model.Progress;
import com.smartgoal.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgressDAO {

    /** Inserts or updates progress for a goal (upsert via replace). */
    public boolean upsertProgress(int goalId, String status) {
        // First check if a progress row exists
        String checkSql = "SELECT progress_id FROM progress WHERE goal_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement check = con.prepareStatement(checkSql)) {

            check.setInt(1, goalId);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                // Update existing
                String update = "UPDATE progress SET status = ? WHERE goal_id = ?";
                PreparedStatement ps = con.prepareStatement(update);
                ps.setString(1, status);
                ps.setInt(2, goalId);
                ps.executeUpdate();
            } else {
                // Insert new
                String insert = "INSERT INTO progress (goal_id, status) VALUES (?,?)";
                PreparedStatement ps = con.prepareStatement(insert);
                ps.setInt(1, goalId);
                ps.setString(2, status);
                ps.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Get all progress records. */
    public List<Progress> getAllProgress() {
        List<Progress> list = new ArrayList<>();
        String sql = "SELECT * FROM progress ORDER BY updated_at DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Progress p = new Progress();
                p.setProgressId(rs.getInt("progress_id"));
                p.setGoalId(rs.getInt("goal_id"));
                p.setStatus(rs.getString("status"));
                Timestamp ts = rs.getTimestamp("updated_at");
                p.setUpdatedAt(ts != null ? ts.toString() : "");
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
