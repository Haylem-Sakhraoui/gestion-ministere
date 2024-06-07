package com.esprit.pfe.service;

import com.esprit.pfe.DTO.EmployeeRequest;
import com.esprit.pfe.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface IEmployeeService {
    List<Employee> findAll();

    Optional<Employee> findById(Long id);

    Employee addEmployee(EmployeeRequest employeeRequest);

    void deleteById(Long id);

    // New updateMinister method

    // New updateMinister method
    Employee updateEmployee(Long id, EmployeeRequest employeeRequest);
}
