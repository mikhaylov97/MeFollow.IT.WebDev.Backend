package com.mefollow.webschool.management.admin.domain.dto;

import java.util.ArrayList;
import java.util.List;

public class LessonsDto {
    private int minAvailableOrderIndex;
    private List<Lesson> lessons = new ArrayList<>();

    public int getMinAvailableOrderIndex() {
        return minAvailableOrderIndex;
    }

    public void setMinAvailableOrderIndex(int minAvailableOrderIndex) {
        this.minAvailableOrderIndex = minAvailableOrderIndex;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public void addLesson(String id, String title, int orderIndex) {
        lessons.add(new Lesson(id, title, orderIndex));
    }

    private class Lesson {
        private String id;
        private String title;
        private int orderIndex;

        Lesson(String id, String title, int orderIndex) {
            this.id = id;
            this.title = title;
            this.orderIndex = orderIndex;
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

        public int getOrderIndex() {
            return orderIndex;
        }

        public void setOrderIndex(int orderIndex) {
            this.orderIndex = orderIndex;
        }
    }
}
