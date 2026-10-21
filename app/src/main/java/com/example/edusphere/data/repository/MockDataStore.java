package com.example.edusphere.data.repository;

import com.example.edusphere.data.model.AcademicModels;
import com.example.edusphere.data.model.Course;
import com.example.edusphere.data.model.Lesson;
import com.example.edusphere.data.model.Module;
import com.example.edusphere.data.model.NotificationItem;
import com.example.edusphere.data.model.QuizModels;
import com.example.edusphere.data.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MockDataStore {
    private static MockDataStore instance;

    private final List<Course> courses = new ArrayList<>();
    private final Map<String, List<Module>> courseModulesMap = new HashMap<>();
    private final List<AcademicModels.AcademicClass> academicClasses = new ArrayList<>();
    private final Map<String, List<AcademicModels.AcademicSubject>> classSubjectsMap = new HashMap<>();
    private final Map<String, List<AcademicModels.AcademicChapter>> subjectChaptersMap = new HashMap<>();
    private final List<NotificationItem> notifications = new ArrayList<>();
    private final List<User> registeredUsers = new ArrayList<>();
    private final List<QuizModels.Quiz> quizzes = new ArrayList<>();
    private final List<QuizModels.QuizAttempt> quizAttempts = new ArrayList<>();

    private MockDataStore() {
        seedInitialData();
    }

    public static synchronized MockDataStore getInstance() {
        if (instance == null) {
            instance = new MockDataStore();
        }
        return instance;
    }

    private void seedInitialData() {
        // Seed default Admin & Student accounts
        registeredUsers.add(new User("admin-1", "Master Admin", "9999999999", "College", "ADMIN"));
        registeredUsers.add(new User("student-1", "Aarav Sharma", "9876543210", "Class 10", "STUDENT"));

        // Seed Technical Courses
        Course c1 = new Course("course-web-1", "Complete MERN Stack Web Development", "Web Development",
                "Learn HTML, CSS, JavaScript, React, Node.js, Express & MongoDB from scratch with real-world projects.",
                "BEGINNER", 45, "PUBLISHED");
        c1.setFeatured(true);
        c1.setThumbnailUrl("https://images.unsplash.com/photo-1593720213428-28a5b9e94613?w=800");

        Course c2 = new Course("course-prog-1", "Java & Data Structures Mastery", "Programming",
                "Master Object-Oriented Programming, Core Java, Collections framework, recursion, trees, and graphs.",
                "INTERMEDIATE", 60, "PUBLISHED");
        c2.setFeatured(true);
        c2.setThumbnailUrl("https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=800");

        Course c3 = new Course("course-ai-1", "AI & Generative AI: From Basics to Agents", "AI & Generative AI",
                "Understand LLMs, Prompt Engineering, RAG architectures, and build modern generative AI applications.",
                "ADVANCED", 35, "PUBLISHED");
        c3.setFeatured(true);
        c3.setThumbnailUrl("https://images.unsplash.com/photo-1677442136019-21780ecad995?w=800");

        Course c4 = new Course("course-data-1", "Data Analytics with SQL & Power BI", "Data Analytics",
                "Hands-on data querying with SQL, data cleaning in Excel, and building interactive enterprise dashboards.",
                "BEGINNER", 25, "PUBLISHED");
        c4.setThumbnailUrl("https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=800");

        Course c5 = new Course("course-cloud-1", "Cloud Computing & DevOps: AWS + Docker", "Cloud Computing",
                "Understand Linux basics, AWS services (EC2, S3, IAM), Docker containerization, and automated CI/CD pipelines.",
                "INTERMEDIATE", 40, "PUBLISHED");
        c5.setThumbnailUrl("https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=800");

        courses.add(c1);
        courses.add(c2);
        courses.add(c3);
        courses.add(c4);
        courses.add(c5);

        // Modules & Lessons for c1 (MERN Stack)
        List<Module> mernModules = new ArrayList<>();
        Module m1 = new Module("mod-1", c1.getId(), "Module 1: Modern Web Foundations", 1);
        m1.getLessons().add(new Lesson("les-1", m1.getId(), "Introduction to HTML5 & Semantic Web", 1,
                "https://www.youtube.com/watch?v=kUMe1FH4CHE", "Learn how HTML structure forms the skeleton of modern websites.", 15));
        m1.getLessons().add(new Lesson("les-2", m1.getId(), "CSS3 Flexbox and Responsive Layouts", 2,
                "https://www.youtube.com/watch?v=JJSoEo8JSnc", "Master modern layout techniques using Flexbox and Grid without float hacks.", 20));

        Module m2 = new Module("mod-2", c1.getId(), "Module 2: JavaScript ES6+ & Async Programming", 2);
        m2.getLessons().add(new Lesson("les-3", m2.getId(), "Arrow functions, Promises & Async/Await", 1,
                "https://www.youtube.com/watch?v=V_Kr9OSfDeU", "Handle asynchronous operations smoothly in modern JavaScript.", 25));
        m2.getLessons().add(new Lesson("les-4", m2.getId(), "React Functional Components & Hooks", 2,
                "https://www.youtube.com/watch?v=SqcY0GlETPk", "Build dynamic reactive UIs using useState, useEffect, and custom hooks.", 30));

        mernModules.add(m1);
        mernModules.add(m2);
        courseModulesMap.put(c1.getId(), mernModules);

        // Modules & Lessons for c2 (Java & DSA)
        List<Module> javaModules = new ArrayList<>();
        Module jm1 = new Module("jmod-1", c2.getId(), "Module 1: Core Java & OOP Principles", 1);
        jm1.getLessons().add(new Lesson("jles-1", jm1.getId(), "Classes, Objects, and Encapsulation", 1,
                "https://www.youtube.com/watch?v=grEKMHGYyns", "Understanding the foundational pillars of Object Oriented Programming in Java.", 20));
        jm1.getLessons().add(new Lesson("jles-2", jm1.getId(), "Polymorphism, Inheritance & Interfaces", 2,
                "https://www.youtube.com/watch?v=xk4_1vDrzzo", "Designing modular, extensible, and clean object hierarchies.", 25));
        javaModules.add(jm1);
        courseModulesMap.put(c2.getId(), javaModules);

        // Seed Sample Quiz for MERN Stack Course
        QuizModels.Quiz quiz1 = new QuizModels.Quiz("quiz-1", c1.getId(), "MERN Web Development Assessment", "Test your knowledge of HTML, CSS, JavaScript ES6 and React hooks.", 60, 10);
        QuizModels.QuizQuestion q1 = new QuizModels.QuizQuestion("q-1", quiz1.getId(), "Which HTML5 element is best suited for wrapping independent, self-contained content like a blog post?", 10, "<article> is designed for standalone composition.");
        q1.getOptions().add(new QuizModels.QuizOption("opt-1", q1.getId(), "<section>", false));
        q1.getOptions().add(new QuizModels.QuizOption("opt-2", q1.getId(), "<article>", true));
        q1.getOptions().add(new QuizModels.QuizOption("opt-3", q1.getId(), "<div>", false));
        q1.getOptions().add(new QuizModels.QuizOption("opt-4", q1.getId(), "<aside>", false));

        QuizModels.QuizQuestion q2 = new QuizModels.QuizQuestion("q-2", quiz1.getId(), "In React, which hook is used to manage component state?", 10, "useState returns a stateful value and a function to update it.");
        q2.getOptions().add(new QuizModels.QuizOption("opt-5", q2.getId(), "useEffect", false));
        q2.getOptions().add(new QuizModels.QuizOption("opt-6", q2.getId(), "useContext", false));
        q2.getOptions().add(new QuizModels.QuizOption("opt-7", q2.getId(), "useState", true));
        q2.getOptions().add(new QuizModels.QuizOption("opt-8", q2.getId(), "useRef", false));

        quiz1.getQuestions().add(q1);
        quiz1.getQuestions().add(q2);
        quizzes.add(quiz1);

        // Academic Classes (Classes 1 - 12 and College)
        AcademicModels.AcademicClass ac10 = new AcademicModels.AcademicClass("ac-10", "Class 10", "C10");
        AcademicModels.AcademicClass ac12 = new AcademicModels.AcademicClass("ac-12", "Class 12", "C12");
        AcademicModels.AcademicClass acCollege = new AcademicModels.AcademicClass("ac-coll", "College", "COL");

        academicClasses.add(ac10);
        academicClasses.add(ac12);
        academicClasses.add(acCollege);

        // Subjects for Class 10
        List<AcademicModels.AcademicSubject> c10Subjects = new ArrayList<>();
        AcademicModels.AcademicSubject subMath = new AcademicModels.AcademicSubject("sub-1", ac10.getId(), "Mathematics", "#2563EB");
        AcademicModels.AcademicSubject subSci = new AcademicModels.AcademicSubject("sub-2", ac10.getId(), "Science", "#059669");
        AcademicModels.AcademicSubject subEng = new AcademicModels.AcademicSubject("sub-3", ac10.getId(), "English", "#D97706");
        c10Subjects.add(subMath);
        c10Subjects.add(subSci);
        c10Subjects.add(subEng);
        classSubjectsMap.put(ac10.getId(), c10Subjects);

        // Chapters for Math Class 10
        List<AcademicModels.AcademicChapter> mathChapters = new ArrayList<>();
        AcademicModels.AcademicChapter ch1 = new AcademicModels.AcademicChapter("ch-1", subMath.getId(), "Real Numbers", 1, "Euclid's division lemma, fundamental theorem of arithmetic.");
        ch1.getLessons().add(new AcademicModels.AcademicLesson("al-1", ch1.getId(), "Introduction to Real Numbers & Euclid Lemma", 1, "https://www.youtube.com/watch?v=dQw4w9WgXcQ", "Summary notes on Real Numbers."));
        AcademicModels.AcademicChapter ch2 = new AcademicModels.AcademicChapter("ch-2", subMath.getId(), "Polynomials", 2, "Geometrical meaning of zeroes, relationship between zeroes and coefficients.");
        mathChapters.add(ch1);
        mathChapters.add(ch2);
        subjectChaptersMap.put(subMath.getId(), mathChapters);

        // Notifications
        notifications.add(new NotificationItem("notif-1", "Welcome to EduSphere!", "Start exploring high quality technical and academic courses today.", false, "Just now"));
        notifications.add(new NotificationItem("notif-2", "New Course Published", "Complete MERN Stack Web Development is now open for enrollment.", false, "2 hours ago"));
    }

    public List<Course> getCourses() {
        return courses;
    }

    public Course getCourseById(String id) {
        for (Course c : courses) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }

    public void addCourse(Course course) {
        if (course.getId() == null || course.getId().isEmpty()) {
            course.setId("course-" + UUID.randomUUID().toString().substring(0, 8));
        }
        courses.add(0, course);
    }

    public void updateCourse(Course updatedCourse) {
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getId().equals(updatedCourse.getId())) {
                courses.set(i, updatedCourse);
                return;
            }
        }
    }

    public void deleteCourse(String courseId) {
        courses.removeIf(c -> c.getId().equals(courseId));
        courseModulesMap.remove(courseId);
    }

    public List<Module> getModulesForCourse(String courseId) {
        List<Module> list = courseModulesMap.get(courseId);
        if (list == null) {
            list = new ArrayList<>();
            courseModulesMap.put(courseId, list);
        }
        return list;
    }

    public void addModule(String courseId, Module module) {
        List<Module> modules = getModulesForCourse(courseId);
        if (module.getId() == null || module.getId().isEmpty()) {
            module.setId("mod-" + UUID.randomUUID().toString().substring(0, 8));
        }
        modules.add(module);
    }

    public void addLessonToModule(String moduleId, Lesson lesson) {
        for (List<Module> modules : courseModulesMap.values()) {
            for (Module m : modules) {
                if (m.getId().equals(moduleId)) {
                    if (lesson.getId() == null || lesson.getId().isEmpty()) {
                        lesson.setId("les-" + UUID.randomUUID().toString().substring(0, 8));
                    }
                    m.getLessons().add(lesson);
                    return;
                }
            }
        }
    }

    public List<QuizModels.Quiz> getQuizzesForCourse(String courseId) {
        List<QuizModels.Quiz> result = new ArrayList<>();
        for (QuizModels.Quiz q : quizzes) {
            if (courseId.equals(q.getCourseId())) {
                result.add(q);
            }
        }
        return result;
    }

    public QuizModels.Quiz getQuizById(String quizId) {
        for (QuizModels.Quiz q : quizzes) {
            if (q.getId().equals(quizId)) return q;
        }
        return null;
    }

    public void addQuiz(QuizModels.Quiz quiz) {
        if (quiz.getId() == null || quiz.getId().isEmpty()) {
            quiz.setId("quiz-" + UUID.randomUUID().toString().substring(0, 8));
        }
        quizzes.add(quiz);
    }

    public void saveQuizAttempt(QuizModels.QuizAttempt attempt) {
        quizAttempts.add(attempt);
    }

    public List<QuizModels.QuizAttempt> getQuizAttemptsForStudent(String studentId) {
        List<QuizModels.QuizAttempt> list = new ArrayList<>();
        for (QuizModels.QuizAttempt a : quizAttempts) {
            if (a.getStudentId().equals(studentId)) {
                list.add(a);
            }
        }
        return list;
    }

    public List<AcademicModels.AcademicClass> getAcademicClasses() {
        return academicClasses;
    }

    public List<AcademicModels.AcademicSubject> getSubjectsForClass(String classId) {
        List<AcademicModels.AcademicSubject> subjects = classSubjectsMap.get(classId);
        return subjects != null ? subjects : new ArrayList<>();
    }

    public List<AcademicModels.AcademicChapter> getChaptersForSubject(String subjectId) {
        List<AcademicModels.AcademicChapter> chapters = subjectChaptersMap.get(subjectId);
        return chapters != null ? chapters : new ArrayList<>();
    }

    public List<NotificationItem> getNotifications() {
        return notifications;
    }

    public void addNotification(NotificationItem item) {
        notifications.add(0, item);
    }

    public User findUserByMobile(String mobile) {
        for (User u : registeredUsers) {
            if (u.getMobileNumber().equals(mobile)) return u;
        }
        return null;
    }

    public void registerUser(User user) {
        registeredUsers.add(user);
    }

    public List<User> getRegisteredUsers() {
        return registeredUsers;
    }
}
