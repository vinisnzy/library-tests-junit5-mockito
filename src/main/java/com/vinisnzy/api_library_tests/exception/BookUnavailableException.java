package com.vinisnzy.api_library_tests.exception;

public class BookUnavailableException extends RuntimeException {
    public BookUnavailableException() {
        super("The book is already loaned.");
    }
}
