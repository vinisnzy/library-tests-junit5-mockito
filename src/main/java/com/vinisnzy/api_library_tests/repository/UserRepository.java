package com.vinisnzy.api_library_tests.repository;

import com.vinisnzy.api_library_tests.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
