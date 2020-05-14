package com.mefollow.webschool.management.admin.domain.dto;

import java.util.ArrayList;
import java.util.List;

public class ChaptersDto {
    private int minAvailableOrderIndex;
    private List<Chapter> chapters = new ArrayList<>();

    public int getMinAvailableOrderIndex() {
        return minAvailableOrderIndex;
    }

    public void setMinAvailableOrderIndex(int minAvailableOrderIndex) {
        this.minAvailableOrderIndex = minAvailableOrderIndex;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public void addChapter(String id, String title, int orderIndex) {
        chapters.add(new Chapter(id, title, orderIndex));
    }

    private class Chapter {
        private String id;
        private String title;
        private int orderIndex;

        Chapter(String id, String title, int orderIndex) {
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
