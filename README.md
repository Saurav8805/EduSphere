# 📚 EduSphere — Android Learning Management System

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/Language-Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Backend-Supabase-3ECF8E?style=for-the-badge&logo=supabase&logoColor=white"/>
  <img src="https://img.shields.io/badge/Min_SDK-24-blue?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Target_SDK-35-blue?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Version-1.0-orange?style=for-the-badge"/>
</p>

<p align="center">
  <b>EduSphere</b> is a full-featured Android e-learning platform that connects students with structured course content, video lessons, quizzes, and academic resources — all managed through a dedicated Admin portal.
</p>

---

## ✨ Features

### 🎓 Student Portal

| Feature | Description |
|---|---|
| **Splash & Onboarding** | Branded splash screen with smooth transition to auth flow |
| **Authentication** | Register, Login, Forgot Password with secure session management |
| **Course Discovery** | Browse and enroll in available courses |
| **Course Details** | Rich course overview with lesson list and progress tracking |
| **Lesson Player** | Video lesson playback with notes and PDF resources |
| **Quizzes** | Interactive quizzes with instant result feedback |
| **Notifications** | In-app announcements and push notifications |
| **Academic Content** | Class-wise subjects → chapters → lessons hierarchy |

### 🛠️ Admin Portal

| Feature | Description |
|---|---|
| **Admin Dashboard** | Overview of platform stats and quick actions |
| **Course Management** | Create, edit, and delete courses |
| **Lesson Management** | Add/edit lessons with video URLs, notes, and PDFs |
| **Academic Management** | Manage classes, subjects, and chapter content |
| **Quiz Management** | Create and manage quizzes per course |
| **Student Management** | View and manage registered students |
| **Announcements** | Push announcements to all students |

---

## 🏗️ Architecture & Tech Stack

```
EduSphere/
├── app/src/main/java/com/example/edusphere/
│   ├── MainActivity.java
│   ├── SplashActivity.java
│   ├── data/
│   │   ├── api/          # Retrofit API interfaces (Supabase REST)
│   │   ├── local/        # Local storage (EncryptedSharedPreferences)
│   │   ├── model/        # POJO data models (Course, Lesson, Quiz, etc.)
│   │   └── repository/   # Repository pattern — single source of truth
│   └── ui/
│       ├── auth/         # Login, Register, ForgotPassword, AdminLogin
│       ├── admin/        # All admin management screens
│       ├── course/       # CourseDetails, LessonPlayer, Quiz, QuizResult
│       ├── fragment/     # Home, Profile, Explore, Academic fragments
│       ├── adapter/      # RecyclerView adapters
│       └── notification/ # Notifications screen
└── supabase_schema.sql   # Full database schema
```

### 🔧 Libraries & Dependencies

| Category | Library |
|---|---|
| **UI Components** | Material Design 3, ConstraintLayout, RecyclerView, CardView, SwipeRefreshLayout |
| **View Binding** | Android ViewBinding |
| **Networking** | Retrofit 2 + OkHttp3 + Gson |
| **Image Loading** | Glide |
| **Security** | AndroidX Security Crypto (EncryptedSharedPreferences) |
| **Backend** | Supabase (PostgreSQL + REST API) |
| **Testing** | JUnit, AndroidX Test, Espresso |

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio** Flamingo or later
- **JDK 11**
- **Android SDK** API 24–35
- A **Supabase** project (free tier works)

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/YOUR_USERNAME/EduSphere.git
   cd EduSphere
   ```

2. **Set up Supabase**
   - Create a new project at [supabase.com](https://supabase.com)
   - Run the provided `supabase_schema.sql` in the Supabase SQL Editor to initialize your database schema
   - Copy your **Project URL** and **anon key** from *Project Settings → API*

3. **Configure the API**
   - Open `app/src/main/java/com/example/edusphere/data/api/`
   - Update the Supabase base URL and API key constants with your project credentials

4. **Firebase (Optional — for Notifications)**
   - Add your `google-services.json` to the `app/` directory
   - Or remove the Firebase dependency from `build.gradle.kts` if not needed

5. **Build & Run**
   ```bash
   ./gradlew assembleDebug
   ```
   Or simply open the project in Android Studio and click **Run ▶**

---

## 🗄️ Database Schema

The full PostgreSQL schema for Supabase is provided in [`supabase_schema.sql`](supabase_schema.sql).

Key tables include:

| Table | Purpose |
|---|---|
| `users` | Student accounts and profiles |
| `courses` | Course catalog |
| `lessons` | Individual lesson content (video, notes, PDF) |
| `quizzes` / `quiz_questions` | Quiz definitions and questions |
| `enrollments` | Student–course relationships |
| `academic_classes` | Class levels (e.g. Class 10, Class 12) |
| `academic_subjects` | Subjects per class |
| `academic_chapters` | Chapters per subject |
| `academic_lessons` | Lessons per chapter |
| `announcements` | Admin-pushed notifications |

---

## 📱 Supported Android Versions

| Version | API Level | Support |
|---|---|---|
| Android 7.0 Nougat | API 24 | ✅ Minimum |
| Android 10 Q | API 29 | ✅ |
| Android 12 | API 31 | ✅ |
| Android 14 | API 34 | ✅ |
| Android 15 | API 35 | ✅ Target |

---

## 🔐 Security

- Session tokens stored in **EncryptedSharedPreferences** (AES-256)
- Admin routes protected by separate login flow with role-based access
- API keys should be stored in `local.properties` — never commit them to source control

> ⚠️ **Note:** `local.properties` and `google-services.json` are already in `.gitignore`. Make sure your API keys are not hardcoded before pushing.

---

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a **Pull Request**

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

<p align="center">Made with ❤️ using Java & Supabase</p>




Application Visualization:



<img width="575" height="1280" alt="WhatsApp Image 2026-10-21 at 5 12 12 PM" src="https://github.com/user-attachments/assets/215d5ef5-03a4-42c4-b63a-e5f72da3a35d" />

<img width="575" height="1280" alt="WhatsApp Image 2026-10-21 at 5 12 13 PM (1)" src="https://github.com/user-attachments/assets/7feb5e12-1c87-401e-9521-ec9c08c49b98" />

<img width="575" height="1280" alt="WhatsApp Image 2026-10-21 at 5 12 13 PM" src="https://github.com/user-attachments/assets/5d6320d1-cba0-44e0-8eea-7106917f62ad" />

<img width="575" height="1280" alt="WhatsApp Image 2026-10-21 at 5 12 14 PM (1)" src="https://github.com/user-attachments/assets/591bcfe4-563d-44af-8aa0-7313ddc48b8b" />

<img width="575" height="1280" alt="WhatsApp Image 2026-10-21 at 5 12 14 PM (2)" src="https://github.com/user-attachments/assets/105ac94e-9e72-452f-8dc4-d066d1f7f516" />

