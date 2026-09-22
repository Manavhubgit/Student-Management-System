package com.studentms.controller;

import com.studentms.model.EditProfileForm;
import com.studentms.model.Student;
import com.studentms.service.StudentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DashboardController {

    private final StudentService studentService;

    public DashboardController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Long studentId = (Long) session.getAttribute("studentId");
        Student student = studentService.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Logged-in student not found"));
        model.addAttribute("student", student);
        return "dashboard";
    }

    @GetMapping("/edit")
    public String showEditForm(HttpSession session, Model model) {
        Long studentId = (Long) session.getAttribute("studentId");
        Student student = studentService.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Logged-in student not found"));

        if (!model.containsAttribute("editProfileForm")) {
            EditProfileForm form = new EditProfileForm();
            form.setName(student.getName());
            form.setSection(student.getSection());
            form.setGpa(student.getGpa());
            form.setEmail(student.getEmail());
            model.addAttribute("editProfileForm", form);
        }
        return "edit";
    }

    @PostMapping("/edit")
    public String updateProfile(@Valid @ModelAttribute("editProfileForm") EditProfileForm form,
                                 BindingResult bindingResult,
                                 HttpSession session,
                                 Model model) {

        Long studentId = (Long) session.getAttribute("studentId");

        // If the email changed, make sure it isn't already taken by someone else
        Student current = studentService.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Logged-in student not found"));

        boolean emailChanged = form.getEmail() != null && !form.getEmail().equalsIgnoreCase(current.getEmail());
        if (emailChanged && studentService.emailExists(form.getEmail())) {
            bindingResult.addError(new FieldError("editProfileForm", "email", "This email is already used by another account"));
        }

        if (form.getNewPassword() != null && !form.getNewPassword().isBlank() && form.getNewPassword().length() < 6) {
            bindingResult.addError(new FieldError("editProfileForm", "newPassword", "New password must be at least 6 characters long"));
        }

        if (bindingResult.hasErrors()) {
            return "edit";
        }

        Student updatedData = new Student();
        updatedData.setName(form.getName());
        updatedData.setSection(form.getSection());
        updatedData.setGpa(form.getGpa());
        updatedData.setEmail(form.getEmail());

        studentService.updateProfile(studentId, updatedData, form.getNewPassword());

        model.addAttribute("updated", true);
        model.addAttribute("student", studentService.findById(studentId).orElseThrow());
        return "dashboard";
    }
}
