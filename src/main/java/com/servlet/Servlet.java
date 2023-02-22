package com.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.servlet.entity.Student;
import com.servlet.exception.StudentCreateException;
import com.servlet.exception.StudentDeleteException;
import com.servlet.exception.StudentNotFoundException;
import com.servlet.exception.StudentUpdateException;
import com.servlet.provider.PostgresConnectionProvider;
import com.servlet.repository.StudentRepository;
import com.servlet.service.StudentService;

@WebServlet("/students/*")
public class Servlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final StudentService studentService;
    private final Gson gson;
    private Logger logger = Logger.getLogger(Servlet.class.getName());

    public Servlet() throws ClassNotFoundException {
        super();

        // dependencies
        PostgresConnectionProvider provider = new PostgresConnectionProvider();
        provider.loadDriver();
        StudentRepository repo = new StudentRepository(provider);

        this.studentService = new StudentService(repo);
        this.gson = new Gson();
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.service(request, response);

        response.setContentType("application/json");

        logger.log(new LogRecord(Level.INFO, String.format("Accept Request path:%s", request.getPathInfo())));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path == null || path.equals("/")) {
            Collection<Student> students = this.studentService.getAll();
            String body = gson.toJson(students);

            response.getWriter()
                .append(body);
            return;
        }

        try {
            UUID id = UUID.fromString(path.replace("/", ""));
            Student student = this.studentService.get(id);;

            response.getWriter()
                .append(gson.toJson(student));
            return;
        } catch (IllegalArgumentException | StudentNotFoundException e) {
            e.printStackTrace();
            this.log(e.getMessage());
        }

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!request.getContentType()
            .equals("application/json")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            Student student = this.gson.fromJson(request.getReader(), Student.class);
            this.studentService.add(student);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter()
                .append(gson.toJson(student));
            return;
        } catch (IllegalArgumentException | StudentCreateException e) {
            e.printStackTrace();
            this.log(e.getMessage());
        }

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!request.getContentType()
            .equals("application/json")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            UUID id = UUID.fromString(request.getPathInfo()
                .replace("/", ""));
            Student student = this.gson.fromJson(request.getReader(), Student.class);

            student.setId(id);

            this.studentService.edit(student);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter()
                .append(gson.toJson(student));
            return;
        } catch (IllegalArgumentException | StudentUpdateException e) {
            e.printStackTrace();
            this.log(e.getMessage());
        }

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            UUID id = UUID.fromString(request.getPathInfo()
                .replace("/", ""));
            this.studentService.delete(id);

            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        } catch (IllegalArgumentException | StudentDeleteException | StudentNotFoundException e) {
            e.printStackTrace();
            this.log(e.getMessage());
        }

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
}
