package com.example.edusphere.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.edusphere.data.model.AcademicModels;
import com.example.edusphere.data.model.Course;
import com.example.edusphere.data.model.User;
import com.example.edusphere.data.repository.EduSphereRepository;
import com.example.edusphere.databinding.FragmentOverviewBinding;
import com.example.edusphere.ui.adapter.CourseAdapter;
import com.example.edusphere.ui.course.CourseDetailActivity;

import java.util.List;

public class OverviewFragment extends Fragment {

    private FragmentOverviewBinding binding;
    private EduSphereRepository repository;
    private CourseAdapter courseAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOverviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repository = EduSphereRepository.getInstance(requireContext());

        setupRecyclerView();
        setupSwipeRefresh();
        loadDashboardData();
    }

    private void setupRecyclerView() {
        courseAdapter = new CourseAdapter(new CourseAdapter.OnCourseClickListener() {
            @Override
            public void onCourseClick(Course course) {
                Intent intent = new Intent(requireContext(), CourseDetailActivity.class);
                intent.putExtra("course_id", course.getId());
                startActivity(intent);
            }

            @Override
            public void onBookmarkClick(Course course, boolean isBookmarked) {
                String msg = isBookmarked ? "Course bookmarked!" : "Bookmark removed";
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        binding.rvFeaturedCourses.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvFeaturedCourses.setAdapter(courseAdapter);
    }

    private void setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener(() -> {
            loadDashboardData();
            binding.swipeRefresh.setRefreshing(false);
        });
    }

    private void loadDashboardData() {
        User user = repository.getSessionManager().getCurrentUser();
        if (user != null) {
            binding.tvGreeting.setText("Hi, " + user.getFullName() + "! 👋");
            binding.tvClassBadge.setText("Enrolled in " + user.getClassLevel());
        }

        // Stats
        int enrolledCount = repository.getSessionManager().getEnrolledCourseIds().size();
        int completedLessons = repository.getSessionManager().getCompletedLessonIds().size();
        binding.tvEnrolledCount.setText(String.valueOf(enrolledCount));
        binding.tvCompletedLessonsCount.setText(String.valueOf(completedLessons));

        // Academic Subjects horizontal chips
        loadSubjects();

        // Technical Featured Courses
        List<Course> featuredCourses = repository.getCourses(true);
        courseAdapter.setCourses(featuredCourses);
    }

    private void loadSubjects() {
        binding.layoutSubjectsHorizontal.removeAllViews();
        List<AcademicModels.AcademicClass> classes = repository.getAcademicClasses();
        if (!classes.isEmpty()) {
            List<AcademicModels.AcademicSubject> subjects = repository.getSubjectsForClass(classes.get(0).getId());
            for (AcademicModels.AcademicSubject subject : subjects) {
                TextView chip = new TextView(requireContext());
                chip.setText(subject.getName());
                chip.setTextSize(14f);
                chip.setPadding(36, 20, 36, 20);
                chip.setTextColor(android.graphics.Color.WHITE);
                chip.setBackgroundResource(com.example.edusphere.R.drawable.card_bg);
                try {
                    chip.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                            android.graphics.Color.parseColor(subject.getColorCode())
                    ));
                } catch (Exception ignored) {
                    chip.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                            android.graphics.Color.parseColor("#2563EB")
                    ));
                }

                android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.setMarginEnd(24);
                chip.setLayoutParams(params);

                chip.setOnClickListener(v -> {
                    Toast.makeText(requireContext(), "Opening " + subject.getName() + " Academic Chapters", Toast.LENGTH_SHORT).show();
                });

                binding.layoutSubjectsHorizontal.addView(chip);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDashboardData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
