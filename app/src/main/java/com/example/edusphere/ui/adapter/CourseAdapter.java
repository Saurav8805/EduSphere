package com.example.edusphere.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edusphere.R;
import com.example.edusphere.data.local.SessionManager;
import com.example.edusphere.data.model.Course;
import com.example.edusphere.databinding.ItemCourseCardBinding;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<Course> courses = new ArrayList<>();
    private final OnCourseClickListener listener;
    private SessionManager sessionManager;

    public interface OnCourseClickListener {
        void onCourseClick(Course course);
        void onBookmarkClick(Course course, boolean isBookmarked);
    }

    public CourseAdapter(OnCourseClickListener listener) {
        this.listener = listener;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses != null ? courses : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (sessionManager == null) {
            sessionManager = SessionManager.getInstance(parent.getContext());
        }
        ItemCourseCardBinding binding = ItemCourseCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new CourseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.bind(course);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {
        private final ItemCourseCardBinding binding;

        public CourseViewHolder(ItemCourseCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Course course) {
            binding.tvCourseTitle.setText(course.getTitle());
            binding.tvCourseDescription.setText(course.getShortDescription());
            binding.tvCategory.setText(course.getCategoryName() != null ? course.getCategoryName() : "General");
            binding.tvDifficulty.setText(course.getDifficulty());
            binding.tvDuration.setText("⏱ " + course.getEstimatedDurationHours() + " Hours");

            boolean isBookmarked = sessionManager != null && sessionManager.isBookmarked(course.getId());
            binding.btnBookmark.setColorFilter(
                    ContextCompat.getColor(binding.getRoot().getContext(),
                            isBookmarked ? R.color.primary : R.color.text_muted)
            );

            binding.btnBookmark.setOnClickListener(v -> {
                if (sessionManager != null) {
                    sessionManager.toggleBookmark(course.getId());
                    boolean newStatus = sessionManager.isBookmarked(course.getId());
                    binding.btnBookmark.setColorFilter(
                            ContextCompat.getColor(binding.getRoot().getContext(),
                                    newStatus ? R.color.primary : R.color.text_muted)
                    );
                    if (listener != null) listener.onBookmarkClick(course, newStatus);
                }
            });

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onCourseClick(course);
            });

            binding.btnAction.setOnClickListener(v -> {
                if (listener != null) listener.onCourseClick(course);
            });
        }
    }
}
