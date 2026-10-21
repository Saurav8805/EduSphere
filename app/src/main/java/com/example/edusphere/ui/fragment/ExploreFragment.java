package com.example.edusphere.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.edusphere.R;
import com.example.edusphere.data.model.Course;
import com.example.edusphere.data.repository.EduSphereRepository;
import com.example.edusphere.databinding.FragmentExploreBinding;
import com.example.edusphere.ui.adapter.CourseAdapter;
import com.example.edusphere.ui.course.CourseDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment {

    private FragmentExploreBinding binding;
    private EduSphereRepository repository;
    private CourseAdapter courseAdapter;
    private String selectedCategory = "All";
    private String currentSearchQuery = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repository = EduSphereRepository.getInstance(requireContext());

        setupRecyclerView();
        setupFilterChips();
        setupSearch();
        filterCourses();
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
            public void onBookmarkClick(Course course, boolean isBookmarked) {}
        });

        binding.rvCourses.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvCourses.setAdapter(courseAdapter);
    }

    private void setupFilterChips() {
        binding.chipGroupCategories.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.chipAll) {
                selectedCategory = "All";
            } else if (checkedId == R.id.chipWeb) {
                selectedCategory = "Web Development";
            } else if (checkedId == R.id.chipProgramming) {
                selectedCategory = "Programming";
            } else if (checkedId == R.id.chipAI) {
                selectedCategory = "AI & Generative AI";
            } else if (checkedId == R.id.chipData) {
                selectedCategory = "Data Analytics";
            } else if (checkedId == R.id.chipCloud) {
                selectedCategory = "Cloud Computing";
            }
            filterCourses();
        });
    }

    private void setupSearch() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearchQuery = s.toString().trim().toLowerCase();
                filterCourses();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterCourses() {
        List<Course> allCourses = repository.getCourses(true);
        List<Course> filtered = new ArrayList<>();

        for (Course c : allCourses) {
            boolean matchesCategory = "All".equalsIgnoreCase(selectedCategory)
                    || (c.getCategoryName() != null && c.getCategoryName().equalsIgnoreCase(selectedCategory));

            boolean matchesSearch = currentSearchQuery.isEmpty()
                    || c.getTitle().toLowerCase().contains(currentSearchQuery)
                    || (c.getShortDescription() != null && c.getShortDescription().toLowerCase().contains(currentSearchQuery));

            if (matchesCategory && matchesSearch) {
                filtered.add(c);
            }
        }

        courseAdapter.setCourses(filtered);
        binding.tvEmptyState.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
