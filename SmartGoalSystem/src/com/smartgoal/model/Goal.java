package com.smartgoal.model;

public class Goal {
    private int    goalId;
    private int    userId;
    private String title;
    private String description;
    private String deadline;
    private String status;
    private String priority;
    private String createdAt;
    private String managerNotes;

    public Goal() {}

    public Goal(int goalId, int userId, String title, String description,
                String deadline, String status, String priority, String createdAt, String managerNotes) {
        this.goalId      = goalId;
        this.userId      = userId;
        this.title       = title;
        this.description = description;
        this.deadline    = deadline;
        this.status      = status;
        this.priority    = priority;
        this.createdAt   = createdAt;
        this.managerNotes = managerNotes;
    }

    public int    getGoalId()                    { return goalId; }
    public void   setGoalId(int goalId)          { this.goalId = goalId; }

    public int    getUserId()                    { return userId; }
    public void   setUserId(int userId)          { this.userId = userId; }

    public String getTitle()                     { return title; }
    public void   setTitle(String title)         { this.title = title; }

    public String getDescription()                       { return description; }
    public void   setDescription(String description)     { this.description = description; }

    public String getDeadline()                  { return deadline; }
    public void   setDeadline(String deadline)   { this.deadline = deadline; }

    public String getStatus()                    { return status; }
    public void   setStatus(String status)       { this.status = status; }

    public String getPriority()                    { return priority; }
    public void   setPriority(String priority)     { this.priority = priority; }

    public String getCreatedAt()                 { return createdAt; }
    public void   setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getManagerNotes()                     { return managerNotes; }
    public void   setManagerNotes(String managerNotes) { this.managerNotes = managerNotes; }
}
