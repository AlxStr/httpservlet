package com.servlet.exception;

public class StudentUpdateException extends Exception {
    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "Student update error";
    }
}
