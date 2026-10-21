package com.example.edusphere.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Course implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("category_id")
    private String categoryId;

    @SerializedName("category_name")
    private String categoryName;

    @SerializedName("title")
    private String title;

    @SerializedName("slug")
    private String slug;

    @SerializedName("short_description")
    private String shortDescription;

    @SerializedName("full_description")
    private String fullDescription;

    @SerializedName("thumbnail_url")
    private String thumbnailUrl;

    @SerializedName("difficulty")
    private String difficulty; // "BEGINNER", "INTERMEDIATE", "ADVANCED"

    @SerializedName("language")
    private String language;

    @SerializedName("estimated_duration_hours")
    private int estimatedDurationHours;

    @SerializedName("status")
    private String status; // "DRAFT", "PUBLISHED", "ARCHIVED"

    @SerializedName("is_featured")
    private boolean isFeatured;

    @SerializedName("lesson_count")
    private int lessonCount;

    @SerializedName("enrolled_count")
    private int enrolledCount;

    public Course() {}

    public Course(String id, String title, String categoryName, String shortDescription, String difficulty, int hours, String status) {
        this.id = id;
        this.title = title;
        this.categoryName = categoryName;
        this.shortDescription = shortDescription;
        this.difficulty = difficulty;
        this.estimatedDurationHours = hours;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getFullDescription() { return fullDescription; }
    public void setFullDescription(String fullDescription) { this.fullDescription = fullDescription; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public int getEstimatedDurationHours() { return estimatedDurationHours; }
    public void setEstimatedDurationHours(int hours) { this.estimatedDurationHours = hours; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isFeatured() { return isFeatured; }
    public void setFeatured(boolean featured) { isFeatured = featured; }

    public int getLessonCount() { return lessonCount; }
    public void setLessonCount(int count) { this.lessonCount = count; }

    public int getEnrolledCount() { return enrolledCount; }
    public void setEnrolledCount(int count) { this.enrolledCount = count; }

    public boolean isPublished() {
        return "PUBLISHED".equalsIgnoreCase(status);
    }
}
