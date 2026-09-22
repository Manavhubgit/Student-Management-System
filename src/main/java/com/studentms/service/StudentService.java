package com.studentms.service;

import com.studentms.model.Student;
import com.studentms.repository.StudentRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public boolean emailExists(String email) {
        return studentRepository.existsByEmail(email);
    }

    public Student register(Student student) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        return studentRepository.save(student);
    }

    /**
     * Verifies email + raw password against stored (hashed) password.
     * Returns the matching Student if credentials are valid, otherwise empty.
     */
    public Optional<Student> authenticate(String email, String rawPassword) {
        Optional<Student> studentOpt = studentRepository.findByEmail(email);
        if (studentOpt.isPresent() && passwordEncoder.matches(rawPassword, studentOpt.get().getPassword())) {
            return studentOpt;
        }
        return Optional.empty();
    }

    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    /**
     * Updates the editable profile fields (name, section, gpa, email) for a logged-in student.
     * Password is only changed if a new one is supplied.
     */
    public Student updateProfile(Long id, Student updatedData, String newRawPassword) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        existing.setName(updatedData.getName());
        existing.setSection(updatedData.getSection());
        existing.setGpa(updatedData.getGpa());
        existing.setEmail(updatedData.getEmail());

        if (newRawPassword != null && !newRawPassword.isBlank()) {
            existing.setPassword(passwordEncoder.encode(newRawPassword));
        }

        return studentRepository.save(existing);
    }
}
