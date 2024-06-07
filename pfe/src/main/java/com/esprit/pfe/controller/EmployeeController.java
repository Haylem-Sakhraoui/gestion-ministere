package com.esprit.pfe.controller;

import com.esprit.pfe.DTO.EmployeeRequest;
import com.esprit.pfe.entity.Employee;
import com.esprit.pfe.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('MINISTER_ADMIN')")
    public ResponseEntity<Employee> addEmployee(@RequestBody EmployeeRequest employeeRequest) {
        Employee employee = employeeService.addEmployee(employeeRequest);
        return ResponseEntity.ok(employee);
    }
    @GetMapping("/get-all")
    @PreAuthorize("hasAnyRole('MINISTER_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<Employee>> findAll() {
        List<Employee> employees = employeeService.findAll();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/get")
    @PreAuthorize("hasAnyRole('MINISTER_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Employee> findById(Long id) {
        Optional<Employee> employee = employeeService.findById(id);
        return employee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('MINISTER_ADMIN')")
    public ResponseEntity<Void> deleteById(Long id) {
        employeeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequest employeeRequest) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employeeRequest);
        return ResponseEntity.ok(updatedEmployee);
    }
}

