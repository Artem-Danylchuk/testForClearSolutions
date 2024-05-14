package com.testForCS.controller;

import com.testForCS.dto.CreateUserDTO;
import com.testForCS.dto.UpdateUserDTO;
import com.testForCS.dto.UserDTO;
import com.testForCS.entity.User;
import com.testForCS.exception.BadRequestException;
import com.testForCS.exception.EmailInvalidException;
import com.testForCS.service.UserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Value("${age.restriction}")
    private int ageRestriction;
    private final UserService userService;
    private final EmailValidator validator = EmailValidator.getInstance();

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private UserDTO toDto(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getSurname(), user.getEmail(), user.getBirthDate());
    }

    private boolean validate(CreateUserDTO createUserDTO) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(createUserDTO.getEmail());
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserDTO createUserDTO) {
        if (!validate(createUserDTO))
            throw new EmailInvalidException(createUserDTO.getEmail());
        User user = userService.addUser(createUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Long id) {
        if (userService.existsById(id)) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UpdateUserDTO updateUserDTO) {
        userService.update(id, updateUserDTO);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> partialUpdateUser(@PathVariable Long id,
                                                     @RequestBody Map<String, Object> updates) {
        userService.update(id, updates);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public Page<User> searchUsers(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @Nullable Pageable pageable) {
        if (fromDate.isAfter(toDate)) {
            throw new BadRequestException(String.valueOf(fromDate));
        }
        return userService.findUsersByBirthDateRange(fromDate, toDate, pageable);
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return toDto(user);
    }

}



