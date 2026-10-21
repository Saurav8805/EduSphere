package com.example.edusphere.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.edusphere.data.model.User;
import com.example.edusphere.data.repository.EduSphereRepository;
import com.example.edusphere.databinding.FragmentProfileBinding;
import com.example.edusphere.ui.admin.AdminDashboardActivity;
import com.example.edusphere.ui.auth.LoginActivity;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private EduSphereRepository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repository = EduSphereRepository.getInstance(requireContext());

        loadUserProfile();
        setupListeners();
    }

    private void loadUserProfile() {
        User user = repository.getSessionManager().getCurrentUser();
        if (user != null) {
            binding.tvProfileName.setText(user.getFullName());
            binding.tvProfileMobile.setText("+91 " + user.getMobileNumber());
            binding.tvProfileClass.setText(user.getClassLevel());

            if (user.getFullName() != null && !user.getFullName().isEmpty()) {
                String initials = user.getFullName().substring(0, Math.min(2, user.getFullName().length())).toUpperCase();
                binding.tvAvatarInitials.setText(initials);
            }

            // Show admin entry point ONLY if role is ADMIN
            if (user.isAdmin()) {
                binding.cardAdminPortal.setVisibility(View.VISIBLE);
            } else {
                binding.cardAdminPortal.setVisibility(View.GONE);
            }
        }
    }

    private void setupListeners() {
        binding.btnOpenAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AdminDashboardActivity.class);
            startActivity(intent);
        });

        binding.btnHelpSupport.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Support: contact support@edusphere.org", Toast.LENGTH_SHORT).show();
        });

        binding.btnAboutApp.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "EduSphere v1.0 - Learn Without Limits", Toast.LENGTH_SHORT).show();
        });

        binding.btnLogout.setOnClickListener(v -> {
            repository.getSessionManager().logout();
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
