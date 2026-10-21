package com.example.edusphere.ui.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edusphere.R;
import com.example.edusphere.data.model.Course;
import com.example.edusphere.data.repository.EduSphereRepository;
import com.example.edusphere.databinding.ActivityAdminDashboardBinding;
import com.example.edusphere.databinding.DialogAddCourseBinding;
import com.example.edusphere.databinding.ItemAdminCourseBinding;

import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private ActivityAdminDashboardBinding binding;
    private EduSphereRepository repository;
    private AdminCourseAdapter adapter;

    private static final String[] CATEGORIES = new String[]{
            "Web Development", "Programming", "AI & Generative AI",
            "Data Analytics", "Cloud Computing", "Cybersecurity", "Android Development"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = EduSphereRepository.getInstance(this);

        // Role verification safety check
        if (!repository.getSessionManager().isAdmin()) {
            Toast.makeText(this, "Access denied: Admin credentials required", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupUI();
        loadDashboardStats();
    }

    private void setupUI() {
        binding.btnExitAdmin.setOnClickListener(v -> finish());

        binding.btnAdminAddCourse.setOnClickListener(v -> showAddCourseDialog());

        binding.btnAdminSendAnnouncement.setOnClickListener(v -> showAnnouncementDialog());

        adapter = new AdminCourseAdapter();
        binding.rvAdminCourses.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAdminCourses.setAdapter(adapter);
    }

    private void loadDashboardStats() {
        List<Course> allCourses = repository.getCourses(false);
        binding.tvAdminTotalCourses.setText(String.valueOf(allCourses.size()));
        binding.tvAdminTotalStudents.setText(String.valueOf(repository.getAllStudents().size()));
        adapter.setCourses(allCourses);
    }

    private void showAddCourseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogAddCourseBinding dialogBinding = DialogAddCourseBinding.inflate(getLayoutInflater());
        builder.setView(dialogBinding.getRoot());
        AlertDialog dialog = builder.create();

        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, CATEGORIES);
        dialogBinding.actvCategory.setAdapter(catAdapter);

        dialogBinding.btnSubmitCourse.setOnClickListener(v -> {
            String title = dialogBinding.etCourseTitle.getText() != null ? dialogBinding.etCourseTitle.getText().toString().trim() : "";
            String category = dialogBinding.actvCategory.getText() != null ? dialogBinding.actvCategory.getText().toString().trim() : "";
            String desc = dialogBinding.etDescription.getText() != null ? dialogBinding.etDescription.getText().toString().trim() : "";
            String durationStr = dialogBinding.etDuration.getText() != null ? dialogBinding.etDuration.getText().toString().trim() : "10";

            if (title.isEmpty()) {
                dialogBinding.tilCourseTitle.setError("Title is required");
                return;
            }
            if (category.isEmpty()) {
                dialogBinding.tilCategory.setError("Please choose a category");
                return;
            }

            int hours = 10;
            try {
                hours = Integer.parseInt(durationStr);
            } catch (Exception ignored) {}

            Course newCourse = new Course(
                    null, title, category, desc, "BEGINNER", hours, "PUBLISHED"
            );
            newCourse.setFullDescription(desc);
            repository.addCourse(newCourse);

            Toast.makeText(this, "New course published successfully!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            loadDashboardStats();
        });

        dialog.show();
    }

    private void showAnnouncementDialog() {
        android.widget.EditText input = new android.widget.EditText(this);
        input.setHint("Type notification message for all students...");
        input.setPadding(32, 24, 32, 24);

        new AlertDialog.Builder(this)
                .setTitle("Broadcast Announcement")
                .setView(input)
                .setPositiveButton("Send", (dialog, which) -> {
                    String msg = input.getText().toString().trim();
                    if (!msg.isEmpty()) {
                        repository.createAnnouncement("Admin Announcement", msg);
                        Toast.makeText(this, "Announcement broadcasted to all students!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    class AdminCourseAdapter extends RecyclerView.Adapter<AdminCourseAdapter.AdminCourseVH> {
        private List<Course> courseList;

        public void setCourses(List<Course> list) {
            this.courseList = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public AdminCourseVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemAdminCourseBinding itemBinding = ItemAdminCourseBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false
            );
            return new AdminCourseVH(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull AdminCourseVH holder, int position) {
            Course c = courseList.get(position);
            holder.binding.tvAdminCourseTitle.setText(c.getTitle());
            holder.binding.tvAdminCourseCategory.setText(c.getCategoryName() + " • " + c.getDifficulty());

            boolean isPub = c.isPublished();
            holder.binding.tvAdminCourseStatus.setText(isPub ? "PUBLISHED" : "DRAFT");
            holder.binding.tvAdminCourseStatus.setTextColor(ContextCompat.getColor(AdminDashboardActivity.this,
                    isPub ? R.color.primary : R.color.text_muted));

            holder.binding.btnTogglePublish.setText(isPub ? "Unpublish" : "Publish");
            holder.binding.btnTogglePublish.setOnClickListener(v -> {
                c.setStatus(isPub ? "DRAFT" : "PUBLISHED");
                repository.updateCourse(c);
                notifyItemChanged(position);
                Toast.makeText(AdminDashboardActivity.this, "Status updated to " + c.getStatus(), Toast.LENGTH_SHORT).show();
            });

            holder.binding.btnDeleteCourse.setOnClickListener(v -> {
                new AlertDialog.Builder(AdminDashboardActivity.this)
                        .setTitle("Delete Course")
                        .setMessage("Are you sure you want to delete '" + c.getTitle() + "'?")
                        .setPositiveButton("Delete", (d, w) -> {
                            repository.deleteCourse(c.getId());
                            loadDashboardStats();
                            Toast.makeText(AdminDashboardActivity.this, "Course deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });
        }

        @Override
        public int getItemCount() {
            return courseList != null ? courseList.size() : 0;
        }

        class AdminCourseVH extends RecyclerView.ViewHolder {
            final ItemAdminCourseBinding binding;
            AdminCourseVH(ItemAdminCourseBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
