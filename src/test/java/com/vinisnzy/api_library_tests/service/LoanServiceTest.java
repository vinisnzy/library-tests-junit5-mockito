package com.vinisnzy.api_library_tests.service;

import com.vinisnzy.api_library_tests.entity.Book;
import com.vinisnzy.api_library_tests.entity.Loan;
import com.vinisnzy.api_library_tests.entity.User;
import com.vinisnzy.api_library_tests.exception.BookUnavailableException;
import com.vinisnzy.api_library_tests.exception.MaxLoansExceededException;
import com.vinisnzy.api_library_tests.repository.BookRepository;
import com.vinisnzy.api_library_tests.repository.LoanRepository;
import com.vinisnzy.api_library_tests.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

class LoanServiceTest {

    @InjectMocks
    private LoanService loanService;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldThrowMaxLoansExceptionWhenUserHasMoreThanThreeLoans() {
        Long userId = 1L;
        when(loanRepository.countByUserIdAndReturnDateIsNull(userId)).thenReturn(3);
        MaxLoansExceededException exception = assertThrows(MaxLoansExceededException.class,
                () -> loanService.loanBook(userId, 1L));
        assertEquals("User has reached the maximum of 3 active loans.", exception.getMessage());
    }

    @Test
    public void shouldThrowBookUnavailableExceptionIfBookIsAlreadyLoaned() {
        Long bookId = 1L;
        Long userId = 1L;
        when(loanRepository.findByBookIdAndReturnDateIsNull(bookId)).thenReturn(Optional.of(new Loan()));
        BookUnavailableException exception = assertThrows(BookUnavailableException.class,
                () -> loanService.loanBook(userId, bookId));
        assertEquals("The book is already loaned.", exception.getMessage());
    }

    @Test
    public void shouldLoanBookSuccessfully() {
        Long userId = 1L;
        Long bookId = 2L;

        User user = new User(userId, "Test User");
        Book book = new Book(bookId, "Test Book", true);
        when(loanRepository.countByUserIdAndReturnDateIsNull(userId)).thenReturn(0);
        when(loanRepository.findByBookIdAndReturnDateIsNull(bookId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        loanService.loanBook(userId, bookId);

        assertFalse(book.isAvailable());
        verify(bookRepository).save(book);
        verify(loanRepository).save(any(Loan.class));
    }
}