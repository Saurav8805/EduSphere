package com.example.edusphere.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AcademicModels {

    public static class AcademicClass implements Serializable {
        @SerializedName("id")
        private String id;

        @SerializedName("name")
        private String name; // e.g. "Class 10", "Class 12", "College"

        @SerializedName("code")
        private String code;

        public AcademicClass() {}
        public AcademicClass(String id, String name, String code) {
            this.id = id;
            this.name = name;
            this.code = code;
        }
        public String getId() { return id; }
        public String getName() { return name; }
        public String getCode() { return code; }
    }

    public static class AcademicSubject implements Serializable {
        @SerializedName("id")
        private String id;

        @SerializedName("class_id")
        private String classId;

        @SerializedName("name")
        private String name;

        @SerializedName("icon_name")
        private String iconName;

        @SerializedName("color_code")
        private String colorCode;

        public AcademicSubject() {}
        public AcademicSubject(String id, String classId, String name, String colorCode) {
            this.id = id;
            this.classId = classId;
            this.name = name;
            this.colorCode = colorCode;
        }
        public String getId() { return id; }
        public String getClassId() { return classId; }
        public String getName() { return name; }
        public String getColorCode() { return colorCode; }
    }

    public static class AcademicChapter implements Serializable {
        @SerializedName("id")
        private String id;

        @SerializedName("subject_id")
        private String subjectId;

        @SerializedName("title")
        private String title;

        @SerializedName("chapter_number")
        private int chapterNumber;

        @SerializedName("description")
        private String description;

        @SerializedName("lessons")
        private List<AcademicLesson> lessons = new ArrayList<>();

        public AcademicChapter() {}
        public AcademicChapter(String id, String subjectId, String title, int chapterNumber, String description) {
            this.id = id;
            this.subjectId = subjectId;
            this.title = title;
            this.chapterNumber = chapterNumber;
            this.description = description;
        }
        public String getId() { return id; }
        public String getSubjectId() { return subjectId; }
        public String getTitle() { return title; }
        public int getChapterNumber() { return chapterNumber; }
        public String getDescription() { return description; }
        public List<AcademicLesson> getLessons() { return lessons; }
        public void setLessons(List<AcademicLesson> lessons) { this.lessons = lessons; }
    }

    public static class AcademicLesson implements Serializable {
        @SerializedName("id")
        private String id;

        @SerializedName("chapter_id")
        private String chapterId;

        @SerializedName("title")
        private String title;

        @SerializedName("order_index")
        private int orderIndex;

        @SerializedName("video_url")
        private String videoUrl;

        @SerializedName("notes_text")
        private String notesText;

        @SerializedName("pdf_url")
        private String pdfUrl;

        public AcademicLesson() {}
        public AcademicLesson(String id, String chapterId, String title, int orderIndex, String videoUrl, String notesText) {
            this.id = id;
            this.chapterId = chapterId;
            this.title = title;
            this.orderIndex = orderIndex;
            this.videoUrl = videoUrl;
            this.notesText = notesText;
        }
        public String getId() { return id; }
        public String getChapterId() { return chapterId; }
        public String getTitle() { return title; }
        public int getOrderIndex() { return orderIndex; }
        public String getVideoUrl() { return videoUrl; }
        public String getNotesText() { return notesText; }
        public String getPdfUrl() { return pdfUrl; }
    }
}
