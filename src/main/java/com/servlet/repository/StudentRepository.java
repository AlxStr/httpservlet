package com.servlet.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.servlet.entity.Student;
import com.servlet.provider.SQLConnectionProvider;

public class StudentRepository {
    private Connection conn = null;

    public StudentRepository(SQLConnectionProvider connectionProvider) {

        try {
            this.conn = connectionProvider.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Student> all() {
        List<Student> students = new ArrayList<Student>();

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");

            while (rs.next()) {
                students.add(entryToStudent(rs));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    public Optional<Student> get(UUID id) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM students WHERE id = ?::uuid");
            stmt.setString(1, id.toString());

            stmt.execute();
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                Student student = entryToStudent(rs);
                return Optional.of(student);
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public void save(Student student) {
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO students VALUES (?::uuid, ?, ?, ?)");
            stmt.setString(1, student.getId()
                .toString());
            stmt.setString(2, student.getFirstName());
            stmt.setString(3, student.getLastName());
            stmt.setString(4, student.getMiddleName());

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Student student) {
        try {
            PreparedStatement stmt = conn
                .prepareStatement("UPDATE students SET first_name=?, last_name=?, middle_name=? where id=?::uuid");
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getMiddleName());
            stmt.setString(4, student.getId()
                .toString());

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remove(Student student) {
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM students WHERE id = ?::uuid");
            stmt.setString(1, student.getId()
                .toString());

            stmt.execute();

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Student entryToStudent(ResultSet rs) throws SQLException {
        return new Student(UUID.fromString(rs.getString("id")), rs.getString("first_name"), rs.getString("last_name"),
                rs.getString("middle_name"));
    }
}
