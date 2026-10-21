package com.example.edusphere.data.repository;

import android.content.Context;
import com.example.edusphere.data.api.SupabaseConfig;
import com.example.edusphere.data.local.SessionManager;
import com.example.edusphere.data.model.AcademicModels;
import com.example.edusphere.data.model.Course;
import com.example.edusphere.data.model.Lesson;
import com.example.edusphere.data.model.Module;
import com.example.edusphere.data.model.NotificationItem;
import com.example.edusphere.data.model.QuizModels;
import com.example.edusphere.data.model.User;

import java.util.ArrayList;
import java.util.List;

public class EduSphereRepository {
    private static EduSphereRepository instance;
    private final MockDataStore mockStore;
    private final SessionManager sessionManager;

    private EduSphereRepository(Context context) {
        this.mockStore = MockDataStore.getInstance();
        this.sessionManager = SessionManager.getInstance(context);
    }

    public static synchronized EduSphereRepository getInstance(Context context) {
        if (instance == null) {
            instance = new EduSphereRepository(context);
        }
        return instance;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    // Authentication
    public boolean login(String mobileNumber, String password, AuthCallback callback) {
        User user = mockStore.findUserByMobile(mobileNumber);
        if (user != null) {
            sessionManager.saveUserSession(user, "dummy_token_" + user.getId());
            if (callback != null) callback.onSuccess(user);
            return true;
        } else {
            if (callback != null) callback.onError("No account found with mobile number: " + mobileNumber);
            return false;
        }
    }

    public boolean register(String fullName, String mobileNumber, String classLevel, AuthCallback callback) {
        if (mockStore.findUserByMobile(mobileNumber) != null) {
            if (callback != null) callback.onError("An account with this mobile number already exists.");
            return false;
        }

        User newUser = new User("user-" + System.currentTimeMillis(), fullName, mobileNumber, classLevel, "STUDENT");
        mockStore.registerUser(newUser);
        sessionManager.saveUserSession(newUser, "dummy_token_" + newUser.getId());
        if (callback != null) callback.onSuccess(newUser);
        return true;
    }

    // Courses
    public List<Course> getCourses(boolean onlyPublished) {
        List<Course> all = mockStore.getCourses();
        if (!onlyPublished) return all;

        List<Course> published = new ArrayList<>();
        for (Course c : all) {
            if (c.isPublished()) published.add(c);
        }
        return published;
    }

    public List<Course> getEnrolledCourses() {
        List<Course> result = new ArrayList<>();
        for (Course c : mockStore.getCourses()) {
            if (sessionManager.isEnrolled(c.getId())) {
                result.add(c);
            }
        }
        return result;
    }

    public List<Course> getBookmarkedCourses() {
        List<Course> result = new ArrayList<>();
        for (Course c : mockStore.getCourses()) {
            if (sessionManager.isBookmarked(c.getId())) {
                result.add(c);
            }
        }
        return result;
    }

    public Course getCourseById(String courseId) {
        return mockStore.getCourseById(courseId);
    }

    public void addCourse(Course course) {
        mockStore.addCourse(course);
    }

    public void updateCourse(Course course) {
        mockStore.updateCourse(course);
    }

    public void deleteCourse(String courseId) {
        mockStore.deleteCourse(courseId);
    }

    public List<Module> getModulesForCourse(String courseId) {
        return mockStore.getModulesForCourse(courseId);
    }

    public void addModule(String courseId, Module module) {
        mockStore.addModule(courseId, module);
    }

    public void addLesson(String moduleId, Lesson lesson) {
        mockStore.addLessonToModule(moduleId, lesson);
    }

    // Quizzes
    public List<QuizModels.Quiz> getQuizzesForCourse(String courseId) {
        return mockStore.getQuizzesForCourse(courseId);
    }

    public QuizModels.Quiz getQuizById(String quizId) {
        return mockStore.getQuizById(quizId);
    }

    public void addQuiz(QuizModels.Quiz quiz) {
        mockStore.addQuiz(quiz);
    }

    public void saveQuizAttempt(QuizModels.QuizAttempt attempt) {
        mockStore.saveQuizAttempt(attempt);
    }

    public List<QuizModels.QuizAttempt> getQuizAttemptsForStudent(String studentId) {
        return mockStore.getQuizAttemptsForStudent(studentId);
    }

    // Academic
    public List<AcademicModels.AcademicClass> getAcademicClasses() {
        return mockStore.getAcademicClasses();
    }

    public List<AcademicModels.AcademicSubject> getSubjectsForClass(String classId) {
        return mockStore.getSubjectsForClass(classId);
    }

    public List<AcademicModels.AcademicChapter> getChaptersForSubject(String subjectId) {
        return mockStore.getChaptersForSubject(subjectId);
    }

    // Notifications
    public List<NotificationItem> getNotifications() {
        return mockStore.getNotifications();
    }

    public void createAnnouncement(String title, String message) {
        mockStore.addNotification(new NotificationItem("notif-" + System.currentTimeMillis(), title, message, false, "Just now"));
    }

    public List<User> getAllStudents() {
        return mockStore.getRegisteredUsers();
    }

    public interface AuthCallback {
        void onSuccess(User user);
        void onError(String errorMessage);
    }
}
