package com.servlet.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.servlet.entity.Student;
import com.servlet.exception.StudentCreateException;
import com.servlet.exception.StudentDeleteException;
import com.servlet.exception.StudentNotFoundException;
import com.servlet.exception.StudentUpdateException;
import com.servlet.repository.StudentRepository;

final public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepo) {
        this.studentRepository = studentRepo;
    }

    public List<Student> getAll() {
        return this.studentRepository.all();
    }

    public Student get(UUID id) throws StudentNotFoundException {
        Optional<Student> student = this.studentRepository.get(id);

        if (student.isEmpty()) {
            throw new StudentNotFoundException();
        }

        return student.get();
    }

    public void add(Student student) throws StudentCreateException {
        try {
            this.studentRepository.save(student);
        } catch (Exception e) {
            throw new StudentCreateException();
        }
    }

    public void edit(Student student) throws StudentUpdateException {
        try {
            this.studentRepository.update(student);
        } catch (Exception e) {
            throw new StudentUpdateException();
        }
    }

    public void delete(UUID id) throws StudentNotFoundException, StudentDeleteException {
        Student student = this.get(id);

        try {
            this.studentRepository.remove(student);
        } catch (Exception e) {
            throw new StudentDeleteException();
        }
    }
}
