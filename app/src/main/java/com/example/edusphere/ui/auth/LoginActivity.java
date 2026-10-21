package com.example.edusphere.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.edusphere.MainActivity;
import com.example.edusphere.data.model.User;
import com.example.edusphere.data.repository.EduSphereRepository;
import com.example.edusphere.databinding.ActivityLoginBinding;
import com.example.edusphere.ui.admin.AdminDashboardActivity;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private EduSphereRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = EduSphereRepository.getInstance(this);

        // Check if already logged in
        if (repository.getSessionManager().isLoggedIn()) {
            routeUser(repository.getSessionManager().getCurrentUser());
            return;
        }

        setupListeners();
    }

    private void setupListeners() {
        binding.btnLogin.setOnClickListener(v -> handleLogin());

        binding.tvGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Quick demo fills for convenience
        binding.tvFillStudent.setOnClickListener(v -> {
            binding.etMobile.setText("9876543210");
            binding.etPassword.setText("student123");
        });

        binding.tvFillAdmin.setOnClickListener(v -> {
            binding.etMobile.setText("9999999999");
            binding.etPassword.setText("admin123");
        });
    }

    private void handleLogin() {
        String mobile = binding.etMobile.getText() != null ? binding.etMobile.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";

        if (mobile.length() < 10) {
            binding.tilMobile.setError("Please enter a valid 10-digit mobile number");
            return;
        } else {
            binding.tilMobile.setError(null);
        }

        if (password.isEmpty()) {
            binding.tilPassword.setError("Please enter your password");
            return;
        } else {
            binding.tilPassword.setError(null);
        }

        binding.btnLogin.setEnabled(false);
        binding.btnLogin.setText("Signing In...");

        repository.login(mobile, password, new EduSphereRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                Toast.makeText(LoginActivity.this, "Welcome " + user.getFullName(), Toast.LENGTH_SHORT).show();
                routeUser(user);
            }

            @Override
            public void onError(String errorMessage) {
                binding.btnLogin.setEnabled(true);
                binding.btnLogin.setText("Sign In");
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void routeUser(User user) {
        if (user.isAdmin()) {
            Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
