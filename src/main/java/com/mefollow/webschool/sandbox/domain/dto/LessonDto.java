package com.mefollow.webschool.sandbox.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mefollow.webschool.sandbox.domain.base.Chapter;
import com.mefollow.webschool.sandbox.domain.base.Lesson;

public class LessonDto {
    private String id;
    private String chapterId;
    private int lessonOrderIndex;
    private String lessonTitle;
    private String lessonDescription;
    private int chapterOrderIndex;
    private String chapterDescription;
    private int numberOfChapters;
    private boolean solved;
    private boolean displayTheory;
    private boolean enabledHtml;
    private boolean enabledCss;
    private boolean enabledJs;
    private String bundleId;
    private String bundleUrl;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String htmlContent;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cssContent;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String jsContent;

    public LessonDto(Lesson lesson, Chapter chapter, int numberOfChapters, boolean solved, boolean displayTheory, String bundleId, String bundleUrl) {
        this.id = lesson.getId();
        this.chapterId = chapter.getId();
        this.lessonOrderIndex = lesson.getOrderIndex();
        this.lessonTitle = lesson.getTitle();
        this.lessonDescription = lesson.getDescription();
        this.chapterOrderIndex = chapter.getOrderIndex();
        this.chapterDescription = chapter.getDescription();
        this.numberOfChapters = numberOfChapters;
        this.solved = solved;
        this.displayTheory = displayTheory;
        this.enabledHtml = chapter.isHtmlEnabled();
        this.enabledCss = chapter.isCssEnabled();
        this.enabledJs = chapter.isJsEnabled();
        this.bundleId = bundleId;
        this.bundleUrl = bundleUrl;
    }

    public String getId() {
        return id;
    }

    public LessonDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getChapterId() {
        return chapterId;
    }

    public LessonDto setChapterId(String chapterId) {
        this.chapterId = chapterId;
        return this;
    }

    public int getLessonOrderIndex() {
        return lessonOrderIndex;
    }

    public LessonDto setLessonOrderIndex(int lessonOrderIndex) {
        this.lessonOrderIndex = lessonOrderIndex;
        return this;
    }

    public String getLessonTitle() {
        return lessonTitle;
    }

    public LessonDto setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
        return this;
    }

    public String getLessonDescription() {
        return lessonDescription;
    }

    public LessonDto setLessonDescription(String lessonDescription) {
        this.lessonDescription = lessonDescription;
        return this;
    }

    public int getChapterOrderIndex() {
        return chapterOrderIndex;
    }

    public LessonDto setChapterOrderIndex(int chapterOrderIndex) {
        this.chapterOrderIndex = chapterOrderIndex;
        return this;
    }

    public String getChapterDescription() {
        return chapterDescription;
    }

    public LessonDto setChapterDescription(String chapterDescription) {
        this.chapterDescription = chapterDescription;
        return this;
    }

    public int getNumberOfChapters() {
        return numberOfChapters;
    }

    public LessonDto setNumberOfChapters(int numberOfChapters) {
        this.numberOfChapters = numberOfChapters;
        return this;
    }

    public boolean isSolved() {
        return solved;
    }

    public LessonDto setSolved(boolean solved) {
        this.solved = solved;
        return this;
    }

    public boolean isDisplayTheory() {
        return displayTheory;
    }

    public LessonDto setDisplayTheory(boolean displayTheory) {
        this.displayTheory = displayTheory;
        return this;
    }

    public boolean isEnabledHtml() {
        return enabledHtml;
    }

    public LessonDto setEnabledHtml(boolean enabledHtml) {
        this.enabledHtml = enabledHtml;
        return this;
    }

    public boolean isEnabledCss() {
        return enabledCss;
    }

    public LessonDto setEnabledCss(boolean enabledCss) {
        this.enabledCss = enabledCss;
        return this;
    }

    public boolean isEnabledJs() {
        return enabledJs;
    }

    public LessonDto setEnabledJs(boolean enabledJs) {
        this.enabledJs = enabledJs;
        return this;
    }

    public String getBundleId() {
        return bundleId;
    }

    public LessonDto setBundleId(String bundleId) {
        this.bundleId = bundleId;
        return this;
    }

    public String getBundleUrl() {
        return bundleUrl;
    }

    public LessonDto setBundleUrl(String bundleUrl) {
        this.bundleUrl = bundleUrl;
        return this;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public LessonDto setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
        return this;
    }

    public String getCssContent() {
        return cssContent;
    }

    public LessonDto setCssContent(String cssContent) {
        this.cssContent = cssContent;
        return this;
    }

    public String getJsContent() {
        return jsContent;
    }

    public LessonDto setJsContent(String jsContent) {
        this.jsContent = jsContent;
        return this;
    }
}
