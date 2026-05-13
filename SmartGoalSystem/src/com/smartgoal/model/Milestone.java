package com.smartgoal.model;

public class Milestone {
    private int milestoneId;
    private int goalId;
    private String description;
    private int isCompleted; // 0 or 1

    // Getters and Setters
    public int getMilestoneId() { return milestoneId; }
    public void setMilestoneId(int milestoneId) { this.milestoneId = milestoneId; }

    public int getGoalId() { return goalId; }
    public void setGoalId(int goalId) { this.goalId = goalId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getIsCompleted() { return isCompleted; }
    public void setIsCompleted(int isCompleted) { this.isCompleted = isCompleted; }
}
