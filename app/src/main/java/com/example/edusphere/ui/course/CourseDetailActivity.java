package com.example.edusphere.ui.course;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.edusphere.R;
import com.example.edusphere.data.model.Course;
import com.example.edusphere.data.model.Lesson;
import com.example.edusphere.data.model.Module;
import com.example.edusphere.data.repository.EduSphereRepository;
import com.example.edusphere.databinding.ActivityCourseDetailBinding;

import java.util.List;

public class CourseDetailActivity extends AppCompatActivity {

    private ActivityCourseDetailBinding binding;
    private EduSphereRepository repository;
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = EduSphereRepository.getInstance(this);

        String courseId = getIntent().getStringExtra("course_id");
        if (courseId != null) {
            course = repository.getCourseById(courseId);
        }

        if (course == null) {
            Toast.makeText(this, "Course not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        bindCourseData();
        setupListeners();
    }

    private void bindCourseData() {
        binding.tvHeaderTitle.setText(course.getTitle());
        binding.tvCourseTitle.setText(course.getTitle());
        binding.tvCategory.setText(course.getCategoryName() != null ? course.getCategoryName() : "General");
        binding.tvDifficulty.setText(course.getDifficulty());
        binding.tvDuration.setText("⏱ " + course.getEstimatedDurationHours() + " Hours");
        binding.tvLanguage.setText("🌐 " + (course.getLanguage() != null ? course.getLanguage() : "English"));
        binding.tvDescription.setText(course.getFullDescription() != null ? course.getFullDescription() : course.getShortDescription());

        updateBookmarkUI();
        updateEnrollmentUI();
        renderCurriculum();
    }

    private void updateBookmarkUI() {
        boolean isBookmarked = repository.getSessionManager().isBookmarked(course.getId());
        binding.btnBookmark.setColorFilter(
                ContextCompat.getColor(this, isBookmarked ? R.color.primary : R.color.text_muted)
        );
    }

    private void updateEnrollmentUI() {
        boolean isEnrolled = repository.getSessionManager().isEnrolled(course.getId());
        if (isEnrolled) {
            binding.btnEnroll.setText("Resume Learning");
            binding.btnEnroll.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.success)));
        } else {
            binding.btnEnroll.setText("Enroll Now (Free)");
            binding.btnEnroll.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primary)));
        }
    }

    private void renderCurriculum() {
        binding.layoutModulesContainer.removeAllViews();
        List<Module> modules = repository.getModulesForCourse(course.getId());

        if (modules.isEmpty()) {
            TextView emptyTv = new TextView(this);
            emptyTv.setText("Curriculum modules are currently being updated by the instructor.");
            emptyTv.setTextColor(ContextCompat.getColor(this, R.color.text_muted));
            binding.layoutModulesContainer.addView(emptyTv);
            return;
        }

        for (Module m : modules) {
            // Module Header
            TextView modHeader = new TextView(this);
            modHeader.setText(m.getTitle());
            modHeader.setTextSize(15f);
            modHeader.setTypeface(null, android.graphics.Typeface.BOLD);
            modHeader.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
            modHeader.setPadding(0, 24, 0, 12);
            binding.layoutModulesContainer.addView(modHeader);

            // Lessons in this module
            for (Lesson l : m.getLessons()) {
                LinearLayout lessonRow = new LinearLayout(this);
                lessonRow.setOrientation(LinearLayout.HORIZONTAL);
                lessonRow.setPadding(24, 20, 24, 20);
                lessonRow.setBackgroundResource(R.drawable.card_bg);
                lessonRow.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.surface)));
                lessonRow.setElevation(1f);

                LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
                );
                rowParams.setMargins(0, 8, 0, 8);
                lessonRow.setLayoutParams(rowParams);

                // Play / Check icon
                ImageView icon = new ImageView(this);
                boolean isCompleted = repository.getSessionManager().isLessonCompleted(l.getId());
                icon.setImageResource(isCompleted ? R.drawable.ic_check : R.drawable.ic_play);
                icon.setColorFilter(ContextCompat.getColor(this, isCompleted ? R.color.success : R.color.primary));
                lessonRow.addView(icon);

                // Title & Duration
                LinearLayout textCol = new LinearLayout(this);
                textCol.setOrientation(LinearLayout.VERTICAL);
                textCol.setPadding(20, 0, 0, 0);

                TextView lessonTitle = new TextView(this);
                lessonTitle.setText(l.getTitle());
                lessonTitle.setTextSize(14f);
                lessonTitle.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
                textCol.addView(lessonTitle);

                TextView lessonDuration = new TextView(this);
                lessonDuration.setText(l.getDurationMinutes() + " mins • Video & Notes");
                lessonDuration.setTextSize(12f);
                lessonDuration.setTextColor(ContextCompat.getColor(this, R.color.text_muted));
                textCol.addView(lessonDuration);

                lessonRow.addView(textCol);

                lessonRow.setOnClickListener(v -> {
                    Intent intent = new Intent(CourseDetailActivity.this, LessonPlayerActivity.class);
                    intent.putExtra("lesson_id", l.getId());
                    intent.putExtra("course_id", course.getId());
                    startActivity(intent);
                });

                binding.layoutModulesContainer.addView(lessonRow);
            }
        }
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnBookmark.setOnClickListener(v -> {
            repository.getSessionManager().toggleBookmark(course.getId());
            updateBookmarkUI();
        });

        binding.btnEnroll.setOnClickListener(v -> {
            repository.getSessionManager().enrollCourse(course.getId());
            updateEnrollmentUI();
            Toast.makeText(this, "Enrolled successfully! Enjoy learning.", Toast.LENGTH_SHORT).show();

            // Open first lesson if exists
            List<Module> modules = repository.getModulesForCourse(course.getId());
            if (!modules.isEmpty() && !modules.get(0).getLessons().isEmpty()) {
                Lesson firstLesson = modules.get(0).getLessons().get(0);
                Intent intent = new Intent(CourseDetailActivity.this, LessonPlayerActivity.class);
                intent.putExtra("lesson_id", firstLesson.getId());
                intent.putExtra("course_id", course.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        renderCurriculum();
    }
}
