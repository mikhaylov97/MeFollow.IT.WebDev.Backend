package com.mefollow.webschool.management.admin.domain.dto;

import java.util.ArrayList;
import java.util.List;

public class RulesDto {
    private List<Rule> htmlRules = new ArrayList<>();
    private List<Rule> cssRules = new ArrayList<>();
    private List<Rule> jsRules = new ArrayList<>();

    public List<Rule> getHtmlRules() {
        return htmlRules;
    }

    public void setHtmlRules(List<Rule> htmlRules) {
        this.htmlRules = htmlRules;
    }

    public List<Rule> getCssRules() {
        return cssRules;
    }

    public void setCssRules(List<Rule> cssRules) {
        this.cssRules = cssRules;
    }

    public List<Rule> getJsRules() {
        return jsRules;
    }

    public void setJsRules(List<Rule> jsRules) {
        this.jsRules = jsRules;
    }

    public void addHtmlRule(String id, String title, int orderIndex) {
        htmlRules.add(new Rule(id, title, orderIndex));
    }

    public void addCssRule(String id, String title, int orderIndex) {
        cssRules.add(new Rule(id, title, orderIndex));
    }

    public void addJsRule(String id, String title, int orderIndex) {
        jsRules.add(new Rule(id, title, orderIndex));
    }

    private class Rule {
        private String id;
        private String title;
        private int orderIndex;

        public Rule(String id, String title, int orderIndex) {
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
