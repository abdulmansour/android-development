package com.molate.technicalassignment1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Random;

public class gradeActivity extends AppCompatActivity {

    private ListView courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        prepareViews();

        Intent intent = getIntent();

        ArrayList<Course> courses = new ArrayList<Course>();

        Random rand = new Random();
        int n = rand.nextInt(5) + 1; //generate from 1 to 5 courses
        for (int i = 0; i < n; i++) {
            Course course = new Course();
            System.out.println(course.getCourseName());
            int totalGrade = 0;
            int average = 0;
            for (int j = 0; j < course.assignments.size(); j++) {
                Assignment assignment = course.assignments.get(j);
                System.out.println(assignment.getAssignmentName() + "\t" + assignment.getAssignmentGrade());
                totalGrade += assignment.getAssignmentGrade();
            }
            average = totalGrade/course.assignments.size();
            course.setAverage(average);
            courses.add(course);
            System.out.println("Average " + "\t" + average);
            System.out.println();
        }

        CourseAdapter adapter = new CourseAdapter(getApplicationContext(),R.layout.row_view, courses);
        courseList.setAdapter(adapter);

    }

    private void prepareViews() {
        courseList = findViewById(R.id.courseList);
    }


}
