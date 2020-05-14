package com.mefollow.webschool.management.admin.domain.dto;

import java.util.ArrayList;
import java.util.List;

public class TopicsDto {
    private int minAvailableOrderIndex;
    private List<Topic> topics = new ArrayList<>();

    public int getMinAvailableOrderIndex() {
        return minAvailableOrderIndex;
    }

    public void setMinAvailableOrderIndex(int minAvailableOrderIndex) {
        this.minAvailableOrderIndex = minAvailableOrderIndex;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public void addTopic(String topicId, String topicTitle, int orderIndex) {
        topics.add(new Topic(topicId, topicTitle, orderIndex));
    }

    private class Topic {
        private String id;
        private String title;
        private int orderIndex;

        public Topic(String id, String title, int orderIndex) {
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
