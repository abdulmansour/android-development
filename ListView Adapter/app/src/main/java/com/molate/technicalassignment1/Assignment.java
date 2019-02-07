package com.molate.technicalassignment1;

import java.util.Random;

public class Assignment {
    private static int assignmentCount = 0;
    private int assignmentGrade;
    private String assignmentName;

    public Assignment() {
        assignmentCount = 0; //reset Assignment count
    }
    public Assignment(int assignmentGrade) {
        assignmentCount++;
        this.assignmentGrade = assignmentGrade;
        assignmentName = "Assignment " + assignmentCount;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public int getAssignmentGrade() {
        return assignmentGrade;
    }

}