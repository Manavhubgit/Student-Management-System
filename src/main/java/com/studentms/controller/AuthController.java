package com.studentms.controller;

import com.studentms.model.LoginForm;
import com.studentms.model.Student;
import com.studentms.service.StudentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {

    private final StudentService studentService;

    public AuthController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    // ---------- Registration ----------

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        if (!model.containsAttribute("student")) {
            model.addAttribute("student", new Student());
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerStudent(@Valid @ModelAttribute("student") Student student,
                                   BindingResult bindingResult,
                                   Model model) {

        if (student.getEmail() != null && studentService.emailExists(student.getEmail())) {
            bindingResult.addError(new FieldError("student", "email", "An account with this email already exists"));
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        studentService.register(student);
        model.addAttribute("registered", true);
        return "login";
    }

    // ---------- Login ----------

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginForm());
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginStudent(@Valid @ModelAttribute("loginForm") LoginForm loginForm,
                                BindingResult bindingResult,
                                HttpSession session,
                                Model model) {

        if (bindingResult.hasErrors()) {
            return "login";
        }

        Optional<Student> authenticated = studentService.authenticate(loginForm.getEmail(), loginForm.getPassword());

        if (authenticated.isEmpty()) {
            model.addAttribute("loginError", "Invalid email or password");
            return "login";
        }

        session.setAttribute("studentId", authenticated.get().getId());
        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
