package com.example.edusphere.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuizModels {

    public static class Quiz implements Serializable {
        @SerializedName("id")
        private String id;

        @SerializedName("course_id")
        private String courseId;

        @SerializedName("lesson_id")
        private String lessonId;

        @SerializedName("chapter_id")
        private String chapterId;

        @SerializedName("title")
        private String title;

        @SerializedName("description")
        private String description;

        @SerializedName("passing_score")
        private int passingScore = 60;

        @SerializedName("time_limit_minutes")
        private int timeLimitMinutes = 15;

        @SerializedName("is_published")
        private boolean isPublished = true;

        @SerializedName("questions")
        private List<QuizQuestion> questions = new ArrayList<>();

        public Quiz() {}

        public Quiz(String id, String courseId, String title, String description, int passingScore, int timeLimitMinutes) {
            this.id = id;
            this.courseId = courseId;
            this.title = title;
            this.description = description;
            this.passingScore = passingScore;
            this.timeLimitMinutes = timeLimitMinutes;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getCourseId() { return courseId; }
        public void setCourseId(String courseId) { this.courseId = courseId; }

        public String getLessonId() { return lessonId; }
        public void setLessonId(String lessonId) { this.lessonId = lessonId; }

        public String getChapterId() { return chapterId; }
        public void setChapterId(String chapterId) { this.chapterId = chapterId; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public int getPassingScore() { return passingScore; }
        public void setPassingScore(int passingScore) { this.passingScore = passingScore; }

        public int getTimeLimitMinutes() { return timeLimitMinutes; }
        public void setTimeLimitMinutes(int timeLimitMinutes) { this.timeLimitMinutes = timeLimitMinutes; }

        public boolean isPublished() { return isPublished; }
        public void setPublished(boolean published) { isPublished = published; }

        public List<QuizQuestion> getQuestions() { return questions; }
        public void setQuestions(List<QuizQuestion> questions) { this.questions = questions; }
    }

    public static class QuizQuestion implements Serializable {
        @SerializedName("id")
        private String id;

        @SerializedName("quiz_id")
        private String quizId;

        @SerializedName("question_text")
        private String questionText;

        @SerializedName("points")
        private int points = 10;

        @SerializedName("explanation")
        private String explanation;

        @SerializedName("options")
        private List<QuizOption> options = new ArrayList<>();

        public QuizQuestion() {}

        public QuizQuestion(String id, String quizId, String questionText, int points, String explanation) {
            this.id = id;
            this.quizId = quizId;
            this.questionText = questionText;
            this.points = points;
            this.explanation = explanation;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getQuizId() { return quizId; }
        public void setQuizId(String quizId) { this.quizId = quizId; }

        public String getQuestionText() { return questionText; }
        public void setQuestionText(String questionText) { this.questionText = questionText; }

        public int getPoints() { return points; }
        public void setPoints(int points) { this.points = points; }

        public String getExplanation() { return explanation; }
        public void setExplanation(String explanation) { this.explanation = explanation; }

        public List<QuizOption> getOptions() { return options; }
        public void setOptions(List<QuizOption> options) { this.options = options; }
    }

    public static class QuizOption implements Serializable {
        @SerializedName("id")
        private String id;

        @SerializedName("question_id")
        private String questionId;

        @SerializedName("option_text")
        private String optionText;

        @SerializedName("is_correct")
        private boolean isCorrect;

        public QuizOption() {}

        public QuizOption(String id, String questionId, String optionText, boolean isCorrect) {
            this.id = id;
            this.questionId = questionId;
            this.optionText = optionText;
            this.isCorrect = isCorrect;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getQuestionId() { return questionId; }
        public void setQuestionId(String questionId) { this.questionId = questionId; }

        public String getOptionText() { return optionText; }
        public void setOptionText(String optionText) { this.optionText = optionText; }

        public boolean isCorrect() { return isCorrect; }
        public void setCorrect(boolean correct) { isCorrect = correct; }
    }

    public static class QuizAttempt implements Serializable {
        @SerializedName("id")
        private String id;

        @SerializedName("student_id")
        private String studentId;

        @SerializedName("quiz_id")
        private String quizId;

        @SerializedName("score")
        private int score;

        @SerializedName("max_score")
        private int maxScore;

        @SerializedName("passed")
        private boolean passed;

        @SerializedName("attempted_at")
        private String attemptedAt;

        public QuizAttempt() {}

        public QuizAttempt(String id, String studentId, String quizId, int score, int maxScore, boolean passed, String attemptedAt) {
            this.id = id;
            this.studentId = studentId;
            this.quizId = quizId;
            this.score = score;
            this.maxScore = maxScore;
            this.passed = passed;
            this.attemptedAt = attemptedAt;
        }

        public String getId() { return id; }
        public String getStudentId() { return studentId; }
        public String getQuizId() { return quizId; }
        public int getScore() { return score; }
        public int getMaxScore() { return maxScore; }
        public boolean isPassed() { return passed; }
        public String getAttemptedAt() { return attemptedAt; }
    }
}
