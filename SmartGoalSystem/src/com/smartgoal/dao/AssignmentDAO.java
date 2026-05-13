package com.smartgoal.dao;

import com.smartgoal.model.Assignment;
import com.smartgoal.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssignmentDAO {

    /** Manager assigns a task to an employee. */
    public int addAssignment(Assignment a) {
        String sql = "INSERT INTO assignments (manager_id, employee_id, task) VALUES (?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[]{"ASSIGNMENT_ID"})) {

            ps.setInt(1, a.getManagerId());
            ps.setInt(2, a.getEmployeeId());
            ps.setString(3, a.getTask());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /** Returns all assignments for an employee (visible to that employee). */
    public List<Assignment> getAssignmentsByEmployee(int employeeId) {
        List<Assignment> list = new ArrayList<>();
        String sql = "SELECT a.*, t.name AS manager_name, s.name AS employee_name " +
                     "FROM assignments a " +
                     "JOIN users t ON a.manager_id = t.id " +
                     "JOIN users s ON a.employee_id = s.id " +
                     "WHERE a.employee_id = ? ORDER BY a.created_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapAssignment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Returns all assignments created by a manager. */
    public List<Assignment> getAssignmentsByManager(int managerId) {
        List<Assignment> list = new ArrayList<>();
        String sql = "SELECT a.*, t.name AS manager_name, s.name AS employee_name " +
                     "FROM assignments a " +
                     "JOIN users t ON a.manager_id = t.id " +
                     "JOIN users s ON a.employee_id = s.id " +
                     "WHERE a.manager_id = ? ORDER BY a.created_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, managerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapAssignment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Returns ALL assignments (manager overview). */
    public List<Assignment> getAllAssignments() {
        List<Assignment> list = new ArrayList<>();
        String sql = "SELECT a.*, t.name AS manager_name, s.name AS employee_name " +
                     "FROM assignments a " +
                     "JOIN users t ON a.manager_id = t.id " +
                     "JOIN users s ON a.employee_id = s.id " +
                     "ORDER BY a.created_at DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapAssignment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Assignment mapAssignment(ResultSet rs) throws SQLException {
        Assignment a = new Assignment();
        a.setAssignmentId(rs.getInt("assignment_id"));
        a.setManagerId(rs.getInt("manager_id"));
        a.setEmployeeId(rs.getInt("employee_id"));
        a.setTask(rs.getString("task"));
        a.setManagerName(rs.getString("manager_name"));
        a.setEmployeeName(rs.getString("employee_name"));
        Timestamp ts = rs.getTimestamp("created_at");
        a.setCreatedAt(ts != null ? ts.toString() : "");
        return a;
    }
}
