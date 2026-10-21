package com.example.edusphere.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.edusphere.data.model.User;
import com.google.gson.Gson;
import java.util.HashSet;
import java.util.Set;

public class SessionManager {
    private static final String PREF_NAME = "edusphere_session";
    private static final String KEY_USER = "key_user";
    private static final String KEY_AUTH_TOKEN = "key_auth_token";
    private static final String KEY_ENROLLED_COURSES = "key_enrolled_courses";
    private static final String KEY_BOOKMARKED_COURSES = "key_bookmarked_courses";
    private static final String KEY_COMPLETED_LESSONS = "key_completed_lessons";

    private final SharedPreferences prefs;
    private final Gson gson;
    private static SessionManager instance;

    private SessionManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }

    public void saveUserSession(User user, String token) {
        prefs.edit()
                .putString(KEY_USER, gson.toJson(user))
                .putString(KEY_AUTH_TOKEN, token)
                .apply();
    }

    public User getCurrentUser() {
        String json = prefs.getString(KEY_USER, null);
        if (json == null) return null;
        try {
            return gson.fromJson(json, User.class);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isLoggedIn() {
        return getCurrentUser() != null;
    }

    public boolean isAdmin() {
        User u = getCurrentUser();
        return u != null && u.isAdmin();
    }

    public void logout() {
        prefs.edit().clear().apply();
    }

    // Enrollments
    public void enrollCourse(String courseId) {
        Set<String> set = new HashSet<>(getEnrolledCourseIds());
        set.add(courseId);
        prefs.edit().putStringSet(KEY_ENROLLED_COURSES, set).apply();
    }

    public boolean isEnrolled(String courseId) {
        return getEnrolledCourseIds().contains(courseId);
    }

    public Set<String> getEnrolledCourseIds() {
        return prefs.getStringSet(KEY_ENROLLED_COURSES, new HashSet<>());
    }

    // Bookmarks
    public void toggleBookmark(String courseId) {
        Set<String> set = new HashSet<>(getBookmarkedCourseIds());
        if (set.contains(courseId)) {
            set.remove(courseId);
        } else {
            set.add(courseId);
        }
        prefs.edit().putStringSet(KEY_BOOKMARKED_COURSES, set).apply();
    }

    public boolean isBookmarked(String courseId) {
        return getBookmarkedCourseIds().contains(courseId);
    }

    public Set<String> getBookmarkedCourseIds() {
        return prefs.getStringSet(KEY_BOOKMARKED_COURSES, new HashSet<>());
    }

    // Lesson Completion
    public void markLessonCompleted(String lessonId) {
        Set<String> set = new HashSet<>(getCompletedLessonIds());
        set.add(lessonId);
        prefs.edit().putStringSet(KEY_COMPLETED_LESSONS, set).apply();
    }

    public boolean isLessonCompleted(String lessonId) {
        return getCompletedLessonIds().contains(lessonId);
    }

    public Set<String> getCompletedLessonIds() {
        return prefs.getStringSet(KEY_COMPLETED_LESSONS, new HashSet<>());
    }
}
