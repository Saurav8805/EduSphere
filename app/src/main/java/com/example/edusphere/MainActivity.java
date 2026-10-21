package com.example.edusphere;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.edusphere.data.local.SessionManager;
import com.example.edusphere.databinding.ActivityMainBinding;
import com.example.edusphere.ui.auth.LoginActivity;
import com.example.edusphere.ui.fragment.ExploreFragment;
import com.example.edusphere.ui.fragment.MyCoursesFragment;
import com.example.edusphere.ui.fragment.OverviewFragment;
import com.example.edusphere.ui.fragment.ProfileFragment;
import com.example.edusphere.ui.notification.NotificationActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = SessionManager.getInstance(this);
        if (!sessionManager.isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupNavigation();
        setupNotificationBell();

        // Default to Overview tab
        if (savedInstanceState == null) {
            loadFragment(new OverviewFragment());
        }
    }

    private void setupNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment fragment = null;

            if (itemId == R.id.nav_overview) {
                binding.tvAppTitle.setText("EduSphere");
                fragment = new OverviewFragment();
            } else if (itemId == R.id.nav_explore) {
                binding.tvAppTitle.setText("Explore Courses");
                fragment = new ExploreFragment();
            } else if (itemId == R.id.nav_my_courses) {
                binding.tvAppTitle.setText("My Learning");
                fragment = new MyCoursesFragment();
            } else if (itemId == R.id.nav_profile) {
                binding.tvAppTitle.setText("My Profile");
                fragment = new ProfileFragment();
            }

            if (fragment != null) {
                loadFragment(fragment);
                return true;
            }
            return false;
        });
    }

    private void setupNotificationBell() {
        binding.btnNotificationBell.setOnClickListener(v -> {
            // Dismiss unread dot upon opening
            binding.viewNotificationBadge.setVisibility(android.view.View.GONE);
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
