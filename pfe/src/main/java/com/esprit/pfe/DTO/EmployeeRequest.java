package com.esprit.pfe.DTO;

import com.esprit.pfe.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest {
    private String firstName;
    private String lastName;
    private String position;
    private Integer age;
    private Double salary;
    private Gender gender;
}
