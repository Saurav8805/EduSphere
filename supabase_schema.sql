-- ============================================================================
-- EDUSPHERE POSTGRESQL SCHEMA & ROW LEVEL SECURITY (RLS) POLICIES
-- Target Platform: Supabase / PostgreSQL 15+
-- ============================================================================

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ----------------------------------------------------------------------------
-- 1. ENUMS & CONSTANTS
-- ----------------------------------------------------------------------------
CREATE TYPE user_role AS ENUM ('student', 'admin');
CREATE TYPE course_difficulty AS ENUM ('Beginner', 'Intermediate', 'Advanced', 'All Levels');
CREATE TYPE content_status AS ENUM ('draft', 'published', 'archived');
CREATE TYPE question_type AS ENUM ('multiple_choice', 'true_false');

-- ----------------------------------------------------------------------------
-- 2. TABLES DEFINITION
-- ----------------------------------------------------------------------------

-- A. PROFILES TABLE
CREATE TABLE IF NOT EXISTS public.profiles (
    id UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
    full_name TEXT NOT NULL,
    mobile_number TEXT UNIQUE NOT NULL,
    student_class TEXT NOT NULL DEFAULT 'Other',
    role user_role NOT NULL DEFAULT 'student',
    avatar_url TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- B. COURSE CATEGORIES
CREATE TABLE IF NOT EXISTS public.course_categories (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name TEXT NOT NULL UNIQUE,
    slug TEXT NOT NULL UNIQUE,
    description TEXT,
    icon_name TEXT DEFAULT 'ic_category_default',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- C. ACADEMIC CLASSES (Class 1 to Class 12, College, Other)
CREATE TABLE IF NOT EXISTS public.academic_classes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    class_name TEXT NOT NULL UNIQUE,
    order_index INT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- D. SUBJECTS
CREATE TABLE IF NOT EXISTS public.subjects (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    class_id UUID REFERENCES public.academic_classes(id) ON DELETE CASCADE,
    name TEXT NOT NULL,
    code TEXT,
    description TEXT,
    icon_url TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(class_id, name)
);

-- E. COURSES
CREATE TABLE IF NOT EXISTS public.courses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title TEXT NOT NULL,
    slug TEXT UNIQUE,
    description TEXT NOT NULL,
    category_id UUID REFERENCES public.course_categories(id) ON DELETE SET NULL,
    subject_id UUID REFERENCES public.subjects(id) ON DELETE SET NULL,
    class_id UUID REFERENCES public.academic_classes(id) ON DELETE SET NULL,
    difficulty course_difficulty NOT NULL DEFAULT 'Beginner',
    language TEXT NOT NULL DEFAULT 'English',
    duration_hours NUMERIC(5,2) DEFAULT 0.0,
    thumbnail_url TEXT,
    is_published BOOLEAN NOT NULL DEFAULT FALSE,
    is_featured BOOLEAN NOT NULL DEFAULT FALSE,
    learning_outcomes TEXT,
    prerequisites TEXT,
    created_by UUID REFERENCES public.profiles(id) ON DELETE SET NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- F. CHAPTERS
CREATE TABLE IF NOT EXISTS public.chapters (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    subject_id UUID REFERENCES public.subjects(id) ON DELETE CASCADE,
    title TEXT NOT NULL,
    description TEXT,
    order_index INT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- G. TOPICS
CREATE TABLE IF NOT EXISTS public.topics (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    chapter_id UUID REFERENCES public.chapters(id) ON DELETE CASCADE,
    title TEXT NOT NULL,
    description TEXT,
    order_index INT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- H. COURSE MODULES
CREATE TABLE IF NOT EXISTS public.course_modules (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    course_id UUID REFERENCES public.courses(id) ON DELETE CASCADE,
    title TEXT NOT NULL,
    description TEXT,
    order_index INT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- I. LESSONS
CREATE TABLE IF NOT EXISTS public.lessons (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    module_id UUID REFERENCES public.course_modules(id) ON DELETE CASCADE,
    chapter_id UUID REFERENCES public.chapters(id) ON DELETE CASCADE,
    title TEXT NOT NULL,
    description TEXT,
    content_text TEXT,
    video_url TEXT,
    duration_minutes INT DEFAULT 0,
    is_free BOOLEAN NOT NULL DEFAULT FALSE,
    order_index INT NOT NULL DEFAULT 0,
    is_published BOOLEAN NOT NULL DEFAULT TRUE,
    pdf_url TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- J. ENROLLMENTS
CREATE TABLE IF NOT EXISTS public.enrollments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    student_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    course_id UUID NOT NULL REFERENCES public.courses(id) ON DELETE CASCADE,
    enrolled_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    completion_percentage INT NOT NULL DEFAULT 0,
    status TEXT NOT NULL DEFAULT 'in_progress',
    last_accessed_at TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(student_id, course_id)
);

-- K. LESSON PROGRESS
CREATE TABLE IF NOT EXISTS public.lesson_progress (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    student_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    lesson_id UUID NOT NULL REFERENCES public.lessons(id) ON DELETE CASCADE,
    is_completed BOOLEAN NOT NULL DEFAULT FALSE,
    completed_at TIMESTAMPTZ,
    last_accessed_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(student_id, lesson_id)
);

-- L. BOOKMARKS
CREATE TABLE IF NOT EXISTS public.bookmarks (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    student_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    course_id UUID NOT NULL REFERENCES public.courses(id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(student_id, course_id)
);

-- M. QUIZZES
CREATE TABLE IF NOT EXISTS public.quizzes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    course_id UUID REFERENCES public.courses(id) ON DELETE CASCADE,
    lesson_id UUID REFERENCES public.lessons(id) ON DELETE CASCADE,
    chapter_id UUID REFERENCES public.chapters(id) ON DELETE CASCADE,
    title TEXT NOT NULL,
    description TEXT,
    passing_score INT NOT NULL DEFAULT 60,
    time_limit_minutes INT DEFAULT 15,
    is_published BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- N. QUIZ QUESTIONS
CREATE TABLE IF NOT EXISTS public.quiz_questions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    quiz_id UUID NOT NULL REFERENCES public.quizzes(id) ON DELETE CASCADE,
    question_text TEXT NOT NULL,
    type question_type NOT NULL DEFAULT 'multiple_choice',
    points INT NOT NULL DEFAULT 10,
    explanation TEXT,
    order_index INT NOT NULL DEFAULT 0
);

-- O. QUIZ OPTIONS
CREATE TABLE IF NOT EXISTS public.quiz_options (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    question_id UUID NOT NULL REFERENCES public.quiz_questions(id) ON DELETE CASCADE,
    option_text TEXT NOT NULL,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,
    order_index INT NOT NULL DEFAULT 0
);

-- P. QUIZ ATTEMPTS
CREATE TABLE IF NOT EXISTS public.quiz_attempts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    student_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    quiz_id UUID NOT NULL REFERENCES public.quizzes(id) ON DELETE CASCADE,
    score INT NOT NULL DEFAULT 0,
    max_score INT NOT NULL DEFAULT 100,
    passed BOOLEAN NOT NULL DEFAULT FALSE,
    attempted_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Q. QUIZ ANSWERS
CREATE TABLE IF NOT EXISTS public.quiz_answers (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    attempt_id UUID NOT NULL REFERENCES public.quiz_attempts(id) ON DELETE CASCADE,
    question_id UUID NOT NULL REFERENCES public.quiz_questions(id) ON DELETE CASCADE,
    selected_option_id UUID REFERENCES public.quiz_options(id) ON DELETE CASCADE,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE
);

-- R. NOTIFICATIONS
CREATE TABLE IF NOT EXISTS public.notifications (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title TEXT NOT NULL,
    message TEXT NOT NULL,
    target_role user_role DEFAULT 'student',
    user_id UUID REFERENCES public.profiles(id) ON DELETE CASCADE,
    link_type TEXT,
    link_id TEXT,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- S. ANNOUNCEMENTS
CREATE TABLE IF NOT EXISTS public.announcements (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title TEXT NOT NULL,
    content TEXT NOT NULL,
    target_class TEXT DEFAULT 'All',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- T. ADMIN AUDIT LOGS
CREATE TABLE IF NOT EXISTS public.admin_audit_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    admin_id UUID REFERENCES public.profiles(id) ON DELETE SET NULL,
    action TEXT NOT NULL,
    target_entity TEXT NOT NULL,
    details TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ----------------------------------------------------------------------------
-- 3. TRIGGERS & FUNCTIONS
-- ----------------------------------------------------------------------------

-- Auto-update updated_at timestamp
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_profiles_timestamp
BEFORE UPDATE ON public.profiles
FOR EACH ROW EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER trg_update_courses_timestamp
BEFORE UPDATE ON public.courses
FOR EACH ROW EXECUTE FUNCTION update_timestamp();

-- Sync Auth User to Profiles on Supabase Signup
CREATE OR REPLACE FUNCTION public.handle_new_user()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO public.profiles (id, full_name, mobile_number, student_class, role)
    VALUES (
        NEW.id,
        COALESCE(NEW.raw_user_meta_data->>'full_name', 'Student User'),
        COALESCE(NEW.raw_user_meta_data->>'mobile_number', NEW.email),
        COALESCE(NEW.raw_user_meta_data->>'student_class', 'Other'),
        COALESCE((NEW.raw_user_meta_data->>'role')::user_role, 'student'::user_role)
    )
    ON CONFLICT (id) DO UPDATE SET
        full_name = EXCLUDED.full_name,
        mobile_number = EXCLUDED.mobile_number,
        student_class = EXCLUDED.student_class;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- ----------------------------------------------------------------------------
-- 4. ROW LEVEL SECURITY (RLS) POLICIES
-- ----------------------------------------------------------------------------

ALTER TABLE public.profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.course_categories ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.academic_classes ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.subjects ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.courses ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.chapters ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.topics ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.course_modules ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.lessons ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.enrollments ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.lesson_progress ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.bookmarks ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.quizzes ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.quiz_questions ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.quiz_options ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.quiz_attempts ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.quiz_answers ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.notifications ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.announcements ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.admin_audit_logs ENABLE ROW LEVEL SECURITY;

-- Helper function: Check if current user is admin
CREATE OR REPLACE FUNCTION public.is_admin()
RETURNS BOOLEAN AS $$
BEGIN
    RETURN EXISTS (
        SELECT 1 FROM public.profiles
        WHERE id = auth.uid() AND role = 'admin' AND is_active = TRUE
    );
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- Profiles: Users can read their profile; Admin can read/manage all
CREATE POLICY "Read own profile" ON public.profiles FOR SELECT USING (auth.uid() = id OR public.is_admin());
CREATE POLICY "Update own profile" ON public.profiles FOR UPDATE USING (auth.uid() = id OR public.is_admin());

-- Courses: Everyone can read published courses; Admin manages all
CREATE POLICY "Read published courses" ON public.courses FOR SELECT USING (is_published = TRUE OR public.is_admin());
CREATE POLICY "Admin manage courses" ON public.courses FOR ALL USING (public.is_admin());

-- Lessons: Read published lessons for published courses; Admin manages all
CREATE POLICY "Read published lessons" ON public.lessons FOR SELECT USING (is_published = TRUE OR public.is_admin());
CREATE POLICY "Admin manage lessons" ON public.lessons FOR ALL USING (public.is_admin());

-- Enrollments: Students access their own enrollments; Admin views all
CREATE POLICY "Student manage enrollments" ON public.enrollments FOR ALL USING (auth.uid() = student_id OR public.is_admin());

-- Lesson Progress: Students manage their own progress
CREATE POLICY "Student manage progress" ON public.lesson_progress FOR ALL USING (auth.uid() = student_id OR public.is_admin());

-- Bookmarks: Students manage their own bookmarks
CREATE POLICY "Student manage bookmarks" ON public.bookmarks FOR ALL USING (auth.uid() = student_id OR public.is_admin());

-- Quizzes & Questions: Read published quizzes; Admin manages all
CREATE POLICY "Read published quizzes" ON public.quizzes FOR SELECT USING (is_published = TRUE OR public.is_admin());
CREATE POLICY "Admin manage quizzes" ON public.quizzes FOR ALL USING (public.is_admin());
CREATE POLICY "Read quiz questions" ON public.quiz_questions FOR SELECT USING (EXISTS (SELECT 1 FROM public.quizzes WHERE id = quiz_id AND is_published = TRUE) OR public.is_admin());
CREATE POLICY "Admin manage quiz questions" ON public.quiz_questions FOR ALL USING (public.is_admin());

-- Quiz Attempts: Students manage their own attempts
CREATE POLICY "Student manage attempts" ON public.quiz_attempts FOR ALL USING (auth.uid() = student_id OR public.is_admin());

-- Categories, Classes, Subjects: Publicly readable, Admin editable
CREATE POLICY "Public read categories" ON public.course_categories FOR SELECT USING (TRUE);
CREATE POLICY "Admin manage categories" ON public.course_categories FOR ALL USING (public.is_admin());

CREATE POLICY "Public read classes" ON public.academic_classes FOR SELECT USING (TRUE);
CREATE POLICY "Admin manage classes" ON public.academic_classes FOR ALL USING (public.is_admin());

CREATE POLICY "Public read subjects" ON public.subjects FOR SELECT USING (TRUE);
CREATE POLICY "Admin manage subjects" ON public.subjects FOR ALL USING (public.is_admin());

CREATE POLICY "Public read announcements" ON public.announcements FOR SELECT USING (is_active = TRUE OR public.is_admin());
CREATE POLICY "Admin manage announcements" ON public.announcements FOR ALL USING (public.is_admin());

-- Notifications: User specific
CREATE POLICY "Read own notifications" ON public.notifications FOR SELECT USING (auth.uid() = user_id OR user_id IS NULL OR public.is_admin());
CREATE POLICY "User update notifications" ON public.notifications FOR UPDATE USING (auth.uid() = user_id OR public.is_admin());

-- ----------------------------------------------------------------------------
-- 5. INITIAL SEED DATA
-- ----------------------------------------------------------------------------

-- Insert Categories
INSERT INTO public.course_categories (id, name, slug, description, icon_name) VALUES
('c0000000-0000-0000-0000-000000000001', 'Web Development', 'web-dev', 'MERN, React, Node.js, Frontend & Backend', 'ic_web_dev'),
('c0000000-0000-0000-0000-000000000002', 'Programming', 'programming', 'Java, Python, C++, Data Structures & Algorithms', 'ic_code'),
('c0000000-0000-0000-0000-000000000003', 'Data Science & Analytics', 'data-science', 'Python, SQL, Machine Learning, Power BI', 'ic_analytics'),
('c0000000-0000-0000-0000-000000000004', 'Artificial Intelligence', 'ai-ml', 'AI Fundamentals, LLMs, GenAI & RAG Applications', 'ic_ai'),
('c0000000-0000-0000-0000-000000000005', 'Cloud Computing & DevOps', 'cloud-devops', 'AWS, Azure, Docker, Linux & Kubernetes', 'ic_cloud'),
('c0000000-0000-0000-0000-000000000006', 'Cybersecurity', 'cybersecurity', 'Ethical Hacking, Network Security & Defense', 'ic_security'),
('c0000000-0000-0000-0000-000000000007', 'Academic Education', 'academic', 'Class 1 to Class 12 School Curriculum', 'ic_school')
ON CONFLICT (name) DO NOTHING;

-- Insert Academic Classes
INSERT INTO public.academic_classes (id, class_name, order_index) VALUES
('a0000000-0000-0000-0000-000000000001', 'Class 1', 1),
('a0000000-0000-0000-0000-000000000002', 'Class 2', 2),
('a0000000-0000-0000-0000-000000000003', 'Class 3', 3),
('a0000000-0000-0000-0000-000000000004', 'Class 4', 4),
('a0000000-0000-0000-0000-000000000005', 'Class 5', 5),
('a0000000-0000-0000-0000-000000000006', 'Class 6', 6),
('a0000000-0000-0000-0000-000000000007', 'Class 7', 7),
('a0000000-0000-0000-0000-000000000008', 'Class 8', 8),
('a0000000-0000-0000-0000-000000000009', 'Class 9', 9),
('a0000000-0000-0000-0000-000000000010', 'Class 10', 10),
('a0000000-0000-0000-0000-000000000011', 'Class 11', 11),
('a0000000-0000-0000-0000-000000000012', 'Class 12', 12),
('a0000000-0000-0000-0000-000000000013', 'College Student', 13),
('a0000000-0000-0000-0000-000000000014', 'Other', 14)
ON CONFLICT (class_name) DO NOTHING;

-- Insert Initial Featured Courses
INSERT INTO public.courses (id, title, slug, description, category_id, difficulty, language, duration_hours, thumbnail_url, is_published, is_featured, learning_outcomes, prerequisites) VALUES
('b0000000-0000-0000-0000-000000000001', 'Complete MERN Stack Developer Bootcamp', 'mern-stack-bootcamp', 'Master MongoDB, Express.js, React.js, Node.js, REST APIs, Authentication, and Cloud Deployment.', 'c0000000-0000-0000-0000-000000000001', 'Intermediate', 'English', 45.0, 'https://images.unsplash.com/photo-1633356122544-f134324a6cee?w=600', TRUE, TRUE, 'Build Fullstack Apps, Deploy to Production, React State Management', 'Basic HTML/CSS'),
('b0000000-0000-0000-0000-000000000002', 'Python Programming & Data Science Masterclass', 'python-data-science', 'Learn Python 3 from basics to advanced, Data Analysis with Pandas, NumPy, and Machine Learning.', 'c0000000-0000-0000-0000-000000000003', 'Beginner', 'English', 38.0, 'https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=600', TRUE, TRUE, 'Analyze data, build predictive models, automate workflows', 'None'),
('b0000000-0000-0000-0000-000000000003', 'Java & Android Development Mastery', 'java-android-mastery', 'Build enterprise Java applications and native Android apps with XML, Material Design & Clean Architecture.', 'c0000000-0000-0000-0000-000000000002', 'Intermediate', 'English', 50.0, 'https://images.unsplash.com/photo-1607252650355-f7fd0460ccdb?w=600', TRUE, TRUE, 'Java OOP, Android Views & ViewBinding, REST API integration', 'Basic programming logic'),
('b0000000-0000-0000-0000-000000000004', 'Generative AI & LLM Engineering', 'genai-llm-engineering', 'Explore Prompt Engineering, OpenAI APIs, LangChain, RAG Applications, and AI Agents.', 'c0000000-0000-0000-0000-000000000004', 'Advanced', 'English', 28.0, 'https://images.unsplash.com/photo-1677442136019-21780efad99a?w=600', TRUE, TRUE, 'Build RAG pipelines, fine-tune models, create intelligent agents', 'Python knowledge')
ON CONFLICT DO NOTHING;

-- Insert Initial Announcements
INSERT INTO public.announcements (id, title, content, target_class, is_active) VALUES
('e0000000-0000-0000-0000-000000000001', 'Welcome to EduSphere!', 'Start exploring technical courses and Class 1 to 12 academic subjects today.', 'All', TRUE),
('e0000000-0000-0000-0000-000000000002', 'New Course Released: GenAI & LLM Engineering', 'Learn how to build RAG applications and AI agents in our latest expert course.', 'All', TRUE)
ON CONFLICT DO NOTHING;
