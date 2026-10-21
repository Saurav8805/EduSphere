package com.example.edusphere.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.edusphere.MainActivity;
import com.example.edusphere.data.model.User;
import com.example.edusphere.data.repository.EduSphereRepository;
import com.example.edusphere.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private EduSphereRepository repository;

    private static final String[] CLASSES = new String[] {
            "Class 1", "Class 2", "Class 3", "Class 4", "Class 5",
            "Class 6", "Class 7", "Class 8", "Class 9", "Class 10",
            "Class 11", "Class 12", "College Student", "Other / Professional"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = EduSphereRepository.getInstance(this);

        setupClassDropdown();
        setupListeners();
    }

    private void setupClassDropdown() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                CLASSES
        );
        binding.actvClass.setAdapter(adapter);
    }

    private void setupListeners() {
        binding.tvGoToLogin.setOnClickListener(v -> finish());

        binding.btnRegister.setOnClickListener(v -> handleRegistration());
    }

    private void handleRegistration() {
        String fullName = binding.etFullName.getText() != null ? binding.etFullName.getText().toString().trim() : "";
        String mobile = binding.etMobile.getText() != null ? binding.etMobile.getText().toString().trim() : "";
        String classLevel = binding.actvClass.getText() != null ? binding.actvClass.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";

        if (fullName.isEmpty()) {
            binding.tilFullName.setError("Please enter your full name");
            return;
        } else {
            binding.tilFullName.setError(null);
        }

        if (mobile.length() < 10) {
            binding.tilMobile.setError("Please enter a valid 10-digit mobile number");
            return;
        } else {
            binding.tilMobile.setError(null);
        }

        if (classLevel.isEmpty()) {
            binding.tilClass.setError("Please select your class or learning level");
            return;
        } else {
            binding.tilClass.setError(null);
        }

        if (password.length() < 6) {
            binding.tilPassword.setError("Password must be at least 6 characters");
            return;
        } else {
            binding.tilPassword.setError(null);
        }

        binding.btnRegister.setEnabled(false);
        binding.btnRegister.setText("Creating Account...");

        repository.register(fullName, mobile, classLevel, new EduSphereRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                Toast.makeText(RegisterActivity.this, "Account created successfully! Welcome " + user.getFullName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                binding.btnRegister.setEnabled(true);
                binding.btnRegister.setText("Create Account");
                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}
