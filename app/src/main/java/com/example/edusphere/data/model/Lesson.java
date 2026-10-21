package com.example.edusphere.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Lesson implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("module_id")
    private String moduleId;

    @SerializedName("title")
    private String title;

    @SerializedName("order_index")
    private int orderIndex;

    @SerializedName("video_url")
    private String videoUrl;

    @SerializedName("content_text")
    private String contentText;

    @SerializedName("notes_markdown")
    private String notesMarkdown;

    @SerializedName("pdf_resource_url")
    private String pdfResourceUrl;

    @SerializedName("duration_minutes")
    private int durationMinutes;

    @SerializedName("is_completed")
    private boolean isCompleted;

    public Lesson() {}

    public Lesson(String id, String moduleId, String title, int orderIndex, String videoUrl, String contentText, int durationMinutes) {
        this.id = id;
        this.moduleId = moduleId;
        this.title = title;
        this.orderIndex = orderIndex;
        this.videoUrl = videoUrl;
        this.contentText = contentText;
        this.durationMinutes = durationMinutes;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getModuleId() { return moduleId; }
    public void setModuleId(String moduleId) { this.moduleId = moduleId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getOrderIndex() { return orderIndex; }
    public void setOrderIndex(int orderIndex) { this.orderIndex = orderIndex; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public String getContentText() { return contentText; }
    public void setContentText(String contentText) { this.contentText = contentText; }

    public String getNotesMarkdown() { return notesMarkdown; }
    public void setNotesMarkdown(String notesMarkdown) { this.notesMarkdown = notesMarkdown; }

    public String getPdfResourceUrl() { return pdfResourceUrl; }
    public void setPdfResourceUrl(String pdfResourceUrl) { this.pdfResourceUrl = pdfResourceUrl; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}
