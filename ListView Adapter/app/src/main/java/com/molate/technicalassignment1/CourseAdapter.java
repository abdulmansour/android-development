package com.molate.technicalassignment1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class CourseAdapter extends ArrayAdapter<Course> {
    Context contex;
    int ressource;
    ArrayList<Course> courses = null;

    public CourseAdapter(@NonNull Context context, int resource, ArrayList<Course> courses) {
        super(context, resource);
        this.contex = context;
        this.ressource = resource;
        this.courses = courses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Course course = courses.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(contex).inflate(ressource,parent,false);
        }
        TextView courseNameTextView = convertView.findViewById(R.id.courseName);
        TextView averageNameTextView = convertView.findViewById(R.id.averageName);

        courseNameTextView.setText(course.getCourseName());
        averageNameTextView.setText(Double.toString(course.getAverage()));

        return convertView;
    }
}
