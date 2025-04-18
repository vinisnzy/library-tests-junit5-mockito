package com.vinisnzy.api_library_tests.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Book book;

    private LocalDate loanDate;
    private LocalDate returnDate;

    public boolean isLate() {
        return returnDate != null && returnDate.isAfter(loanDate.plusDays(7));
    }
}