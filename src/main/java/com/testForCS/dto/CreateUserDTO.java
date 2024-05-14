package com.testForCS.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CreateUserDTO extends UpdateUserDTO {

    private String email;

    public CreateUserDTO(String email, String name, String surname, LocalDate birthDate) {
        super(name, surname, birthDate);
        this.email = email;
    }

}
