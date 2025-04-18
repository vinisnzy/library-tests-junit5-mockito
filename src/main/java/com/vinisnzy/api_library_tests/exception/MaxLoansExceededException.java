package com.vinisnzy.api_library_tests.exception;

public class MaxLoansExceededException extends RuntimeException{
    public MaxLoansExceededException() {
        super("User has reached the maximum of 3 active loans.");
    }
}
