package com.example.edusphere.data.api;

public class SupabaseConfig {
    // Replace with your real Supabase Project credentials when deploying
    public static final String SUPABASE_URL = "https://your-project.supabase.co/";
    public static final String SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";

    // Toggle for offline/demo mock repository support
    public static boolean USE_MOCK_FALLBACK = true;
}
