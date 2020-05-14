package com.mefollow.webschool.management.admin.domain.dto;

import java.util.ArrayList;
import java.util.List;

public class CoursesDto {
    private List<Course> courses = new ArrayList<>();

    public void addCourse(String courseId, String courseTitle) {
        courses.add(new Course(courseId, courseTitle));
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    private class Course {
        private String id;
        private String title;

        Course(String id, String title) {
            this.id = id;
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
