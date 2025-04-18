package com.vinisnzy.api_library_tests.service;

import com.vinisnzy.api_library_tests.entity.Book;
import com.vinisnzy.api_library_tests.entity.Loan;
import com.vinisnzy.api_library_tests.entity.User;
import com.vinisnzy.api_library_tests.exception.BookUnavailableException;
import com.vinisnzy.api_library_tests.exception.MaxLoansExceededException;
import com.vinisnzy.api_library_tests.repository.BookRepository;
import com.vinisnzy.api_library_tests.repository.LoanRepository;
import com.vinisnzy.api_library_tests.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    public void loanBook(Long userId, Long bookId) {
        if (loanRepository.countByUserIdAndReturnDateIsNull(userId) >= 3) throw new MaxLoansExceededException();

        Optional<Loan> existingLoan = loanRepository.findByBookIdAndReturnDateIsNull(bookId);
        if (existingLoan.isPresent()) throw new BookUnavailableException();

        User user = userRepository.findById(userId).orElseThrow();
        Book book = bookRepository.findById(bookId).orElseThrow();

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());
        book.setAvailable(false);

        bookRepository.save(book);
        loanRepository.save(loan);
    }


    public void returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow();
        loan.setReturnDate(LocalDate.now());
        loan.getBook().setAvailable(true);
        bookRepository.save(loan.getBook());
        loanRepository.save(loan);
    }
}
