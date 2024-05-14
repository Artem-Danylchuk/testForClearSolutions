package com.testForCS.service;


import com.testForCS.dto.CreateUserDTO;
import com.testForCS.dto.UpdateUserDTO;
import com.testForCS.entity.User;
import com.testForCS.exception.AgeRestrictionException;
import com.testForCS.exception.EmailAlreadyExistsException;
import com.testForCS.exception.NotFoundException;
import com.testForCS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Value("${age.restriction}")
    private int ageRestriction;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User update(Long id, UpdateUserDTO updateUserDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));
        user.setName(updateUserDTO.getName());
        user.setSurname(updateUserDTO.getSurname());
        user.setBirthDate(updateUserDTO.getBirthDate());
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User addUser(CreateUserDTO createUserDTO) throws AgeRestrictionException, EmailAlreadyExistsException, DataIntegrityViolationException {
        if (checkAge(createUserDTO))
            throw new AgeRestrictionException(createUserDTO.getName());
        User user = new User(createUserDTO.getName(), createUserDTO.getSurname(), createUserDTO.getEmail(), createUserDTO.getBirthDate());
        return userRepository.save(user);
    }

    @Override
    public User update(Long id, Map<String, Object> updates) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            switch (key) {
                case "name" -> user.setName((String) value);
                case "surname" -> user.setSurname((String) value);
                case "birthDate" -> user.setBirthDate(LocalDate.parse((String) value));
                default -> {
                }
            }
        }
        return userRepository.save(user);
    }

    @Override
    public Page<User> findUsersByBirthDateRange(LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        return userRepository.findByBirthDateBetween(fromDate, toDate, pageable);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    private boolean checkAge(CreateUserDTO createUserDTO) {
        LocalDate currentDate = LocalDate.now();
        LocalDate clientBirthday = createUserDTO.getBirthDate();
        LocalDate eighteenYearsLater = clientBirthday.plusYears(ageRestriction);
        return currentDate.isBefore(eighteenYearsLater);
    }

}
