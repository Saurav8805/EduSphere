package com.example.edusphere.ui.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edusphere.data.model.NotificationItem;
import com.example.edusphere.data.repository.EduSphereRepository;
import com.example.edusphere.databinding.ActivityNotificationBinding;
import com.example.edusphere.databinding.ItemNotificationBinding;

import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private ActivityNotificationBinding binding;
    private EduSphereRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = EduSphereRepository.getInstance(this);

        binding.btnBack.setOnClickListener(v -> finish());
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        List<NotificationItem> notifications = repository.getNotifications();
        binding.rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        binding.rvNotifications.setAdapter(new RecyclerView.Adapter<NotificationViewHolder>() {
            @NonNull
            @Override
            public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                ItemNotificationBinding itemBinding = ItemNotificationBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false
                );
                return new NotificationViewHolder(itemBinding);
            }

            @Override
            public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
                NotificationItem item = notifications.get(position);
                holder.binding.tvNotificationTitle.setText(item.getTitle());
                holder.binding.tvNotificationMessage.setText(item.getMessage());
                holder.binding.tvNotificationTime.setText(item.getCreatedAt());
            }

            @Override
            public int getItemCount() {
                return notifications.size();
            }
        });
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        final ItemNotificationBinding binding;
        NotificationViewHolder(ItemNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
