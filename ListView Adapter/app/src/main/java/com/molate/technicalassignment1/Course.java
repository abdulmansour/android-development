package com.molate.technicalassignment1;

import java.util.ArrayList;
import java.util.Random;

public class Course {
    private static int courseCount = 0;
    ArrayList<Assignment> assignments;
    private String courseName;
    private double average;

    public Course() {
        courseCount++;
        courseName = "Course " + courseCount;

        Random rand = new Random();
        int n = rand.nextInt(5) + 1; //generate from 1 to 5 assignments
        assignments = new ArrayList<Assignment>();
        Assignment assignment = new Assignment();
        for (int i = 0; i < n; i++) {
            int grade = rand.nextInt(100) + 1; //generate a grade value from 1 to 100
            assignment = new Assignment(grade);
            this.assignments.add(assignment);
        }
    }

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setAverage(int average) {
        this.average = average;
    }

    public double getAverage() {
        return average;
    }
}