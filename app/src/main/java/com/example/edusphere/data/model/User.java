package com.example.edusphere.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("mobile_number")
    private String mobileNumber;

    @SerializedName("class_level")
    private String classLevel;

    @SerializedName("role")
    private String role; // "STUDENT" or "ADMIN"

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("is_active")
    private boolean isActive = true;

    public User() {}

    public User(String id, String fullName, String mobileNumber, String classLevel, String role) {
        this.id = id;
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        this.classLevel = classLevel;
        this.role = role;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getClassLevel() { return classLevel; }
    public void setClassLevel(String classLevel) { this.classLevel = classLevel; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }
}
