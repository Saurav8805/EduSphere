package com.example.edusphere.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.edusphere.data.model.Course;
import com.example.edusphere.data.repository.EduSphereRepository;
import com.example.edusphere.databinding.FragmentMyCoursesBinding;
import com.example.edusphere.ui.adapter.CourseAdapter;
import com.example.edusphere.ui.course.CourseDetailActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class MyCoursesFragment extends Fragment {

    private FragmentMyCoursesBinding binding;
    private EduSphereRepository repository;
    private CourseAdapter adapter;
    private int selectedTabPosition = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMyCoursesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repository = EduSphereRepository.getInstance(requireContext());

        setupRecyclerView();
        setupTabs();
        loadCourses();
    }

    private void setupRecyclerView() {
        adapter = new CourseAdapter(new CourseAdapter.OnCourseClickListener() {
            @Override
            public void onCourseClick(Course course) {
                Intent intent = new Intent(requireContext(), CourseDetailActivity.class);
                intent.putExtra("course_id", course.getId());
                startActivity(intent);
            }

            @Override
            public void onBookmarkClick(Course course, boolean isBookmarked) {
                if (selectedTabPosition == 1) {
                    loadCourses(); // Refresh bookmarks tab
                }
            }
        });

        binding.rvMyCourses.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvMyCourses.setAdapter(adapter);
    }

    private void setupTabs() {
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTabPosition = tab.getPosition();
                loadCourses();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadCourses() {
        List<Course> list;
        if (selectedTabPosition == 0) {
            list = repository.getEnrolledCourses();
            binding.tvEmptyTitle.setText("No Enrolled Courses Yet");
            binding.tvEmptySubtitle.setText("Explore courses and enroll to start your learning path.");
        } else {
            list = repository.getBookmarkedCourses();
            binding.tvEmptyTitle.setText("No Bookmarked Courses");
            binding.tvEmptySubtitle.setText("Tap the bookmark icon on any course card to save it for later.");
        }

        adapter.setCourses(list);
        binding.layoutEmpty.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
        binding.rvMyCourses.setVisibility(list.isEmpty() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCourses();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
