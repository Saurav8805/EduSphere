package com.example.edusphere.ui.course;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.edusphere.R;
import com.example.edusphere.data.model.Course;
import com.example.edusphere.data.model.Lesson;
import com.example.edusphere.data.model.Module;
import com.example.edusphere.data.repository.EduSphereRepository;
import com.example.edusphere.databinding.ActivityLessonPlayerBinding;

import java.util.List;

public class LessonPlayerActivity extends AppCompatActivity {

    private ActivityLessonPlayerBinding binding;
    private EduSphereRepository repository;
    private Lesson lesson;
    private String courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLessonPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = EduSphereRepository.getInstance(this);

        String lessonId = getIntent().getStringExtra("lesson_id");
        courseId = getIntent().getStringExtra("course_id");

        if (courseId != null && lessonId != null) {
            findLesson(courseId, lessonId);
        }

        if (lesson == null) {
            Toast.makeText(this, "Lesson not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        bindLessonData();
        setupListeners();
    }

    private void findLesson(String cId, String lId) {
        List<Module> modules = repository.getModulesForCourse(cId);
        for (Module m : modules) {
            for (Lesson l : m.getLessons()) {
                if (l.getId().equals(lId)) {
                    lesson = l;
                    return;
                }
            }
        }
    }

    private void bindLessonData() {
        binding.tvLessonHeaderTitle.setText(lesson.getTitle());
        binding.tvLessonTitle.setText(lesson.getTitle());
        binding.tvNotesContent.setText(lesson.getContentText() != null ? lesson.getContentText() : "No written notes attached for this lesson.");

        updateCompletedUI();
    }

    private void updateCompletedUI() {
        boolean isCompleted = repository.getSessionManager().isLessonCompleted(lesson.getId());
        if (isCompleted) {
            binding.btnCompleteLesson.setText("Completed ✓");
            binding.btnCompleteLesson.setTextColor(ContextCompat.getColor(this, R.color.success));
            binding.btnCompleteLesson.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_light));
        } else {
            binding.btnCompleteLesson.setText("Mark as Completed ✓");
            binding.btnCompleteLesson.setTextColor(ContextCompat.getColor(this, R.color.success));
            binding.btnCompleteLesson.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        }
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v -> finish());

        binding.layoutVideoPlayer.setOnClickListener(v -> {
            if (lesson.getVideoUrl() != null && !lesson.getVideoUrl().isEmpty()) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(lesson.getVideoUrl()));
                    startActivity(browserIntent);
                } catch (Exception e) {
                    Toast.makeText(this, "Unable to launch video link", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Video link not provided for this lesson", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnCompleteLesson.setOnClickListener(v -> {
            repository.getSessionManager().markLessonCompleted(lesson.getId());
            updateCompletedUI();
            Toast.makeText(this, "Lesson progress saved!", Toast.LENGTH_SHORT).show();
        });
    }
}
