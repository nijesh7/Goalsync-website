package com.smartgoal.model;

public class Progress {
    private int    progressId;
    private int    goalId;
    private String status;
    private String updatedAt;

    public Progress() {}

    public int    getProgressId()                    { return progressId; }
    public void   setProgressId(int progressId)      { this.progressId = progressId; }

    public int    getGoalId()                        { return goalId; }
    public void   setGoalId(int goalId)              { this.goalId = goalId; }

    public String getStatus()                        { return status; }
    public void   setStatus(String status)           { this.status = status; }

    public String getUpdatedAt()                     { return updatedAt; }
    public void   setUpdatedAt(String updatedAt)     { this.updatedAt = updatedAt; }
}
