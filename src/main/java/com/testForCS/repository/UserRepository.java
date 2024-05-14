package com.testForCS.repository;

import com.testForCS.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    Optional<User> findById(Long aLong);

    boolean existsByEmail(String email);

    Page<User> findAll(Pageable pageable);

    Page<User> findByBirthDateBetween(LocalDate fromDate, LocalDate toDate, Pageable pageable);

}
