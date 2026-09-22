package com.studentms.model;

import jakarta.validation.constraints.*;

public class EditProfileForm {

    @NotBlank(message = "Name is required")
    @Pattern(
            regexp = "^[A-Z][a-zA-Z]*(\\s[A-Z][a-zA-Z]*)*$",
            message = "Name must start with a capital letter (each word capitalized, e.g. 'Rahul Sharma')"
    )
    private String name;

    @NotBlank(message = "Section is required")
    @Pattern(
            regexp = "^[A-Z][0-9]$",
            message = "Section must be exactly one capital letter followed by one digit, e.g. 'A1'"
    )
    private String section;

    @NotNull(message = "GPA is required")
    @DecimalMin(value = "0.0", message = "GPA cannot be less than 0.0")
    @DecimalMax(value = "10.0", message = "GPA cannot be greater than 10.0")
    @Digits(integer = 2, fraction = 2, message = "GPA can have at most 2 decimal places, e.g. 8.75")
    private Double gpa;

    @NotBlank(message = "Email is required")
    @Email(regexp = "^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$", message = "Enter a valid email address containing '@' and a domain, e.g. name@example.com")
    private String email;

    // Optional: only filled in if the student wants to change their password
    private String newPassword;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
