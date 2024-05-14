package com.testForCS.service;

import com.testForCS.dto.CreateUserDTO;
import com.testForCS.dto.UpdateUserDTO;
import com.testForCS.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Map;

public interface UserService {

    User update(Long id, UpdateUserDTO updateUserDTO);

    User update(Long id, Map<String, Object> updates);

    User addUser(CreateUserDTO createUserDTO);

    Page<User> findUsersByBirthDateRange(LocalDate fromDate, LocalDate toDate, Pageable pageable);

    void deleteUser(Long id);

    User findById(Long id);

    boolean existsById(Long id);

}
