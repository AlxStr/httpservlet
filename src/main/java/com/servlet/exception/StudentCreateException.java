package com.servlet.exception;

public class StudentCreateException extends Exception {
    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "Student delete error";
    }
}
