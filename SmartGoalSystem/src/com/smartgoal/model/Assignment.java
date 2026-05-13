package com.smartgoal.model;

public class Assignment {
    private int    assignmentId;
    private int    managerId;
    private int    employeeId;
    private String task;
    private String managerName;
    private String employeeName;
    private String createdAt;

    public Assignment() {}

    public int    getAssignmentId()                          { return assignmentId; }
    public void   setAssignmentId(int assignmentId)          { this.assignmentId = assignmentId; }

    public int    getManagerId()                             { return managerId; }
    public void   setManagerId(int managerId)                { this.managerId = managerId; }

    public int    getEmployeeId()                            { return employeeId; }
    public void   setEmployeeId(int employeeId)              { this.employeeId = employeeId; }

    public String getTask()                                  { return task; }
    public void   setTask(String task)                       { this.task = task; }

    public String getManagerName()                           { return managerName; }
    public void   setManagerName(String managerName)         { this.managerName = managerName; }

    public String getEmployeeName()                          { return employeeName; }
    public void   setEmployeeName(String employeeName)       { this.employeeName = employeeName; }

    public String getCreatedAt()                             { return createdAt; }
    public void   setCreatedAt(String createdAt)             { this.createdAt = createdAt; }
}
