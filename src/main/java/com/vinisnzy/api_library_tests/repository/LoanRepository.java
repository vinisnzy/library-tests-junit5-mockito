package com.vinisnzy.api_library_tests.repository;

import com.vinisnzy.api_library_tests.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    int countByUserIdAndReturnDateIsNull(Long userId);
    Optional<Loan> findByBookIdAndReturnDateIsNull(Long bookId);
    List<Loan> findByUserId(Long userId);
}
