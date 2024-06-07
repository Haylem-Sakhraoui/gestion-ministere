package com.esprit.pfe.service;

import com.esprit.pfe.DTO.EmployeeRequest;
import com.esprit.pfe.config.JwtService;
import com.esprit.pfe.entity.Admin;
import com.esprit.pfe.entity.Employee;
import com.esprit.pfe.entity.Minister;
import com.esprit.pfe.repository.AdminRepository;
import com.esprit.pfe.repository.EmployeeRepository;
import com.esprit.pfe.repository.MinisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;
@Service
public class EmployeeService implements IEmployeeService{
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private MinisterRepository ministerRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private JwtService jwtService;

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }
    @Override
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee addEmployee(EmployeeRequest employeeRequest) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Find the admin by email
        Admin admin = adminRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Admin not found"));

        // Get the minister associated with this admin
        Minister minister = admin.getMinister();
        if (minister == null) {
            throw new RuntimeException("Minister not found for admin");
        }

        // Create the new Employee entity
        Employee employee = new Employee();
        employee.setFirstName(employeeRequest.getFirstName());
        employee.setLastName(employeeRequest.getLastName());
        employee.setPosition(employeeRequest.getPosition());
        employee.setAge(employeeRequest.getAge());
        employee.setSalary(employeeRequest.getSalary());
        employee.setGender(employeeRequest.getGender());
        employee.setMinister(minister);

        // Save the Employee entity
        return employeeRepository.save(employee);
    }
    @Override
    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }

    // New updateMinister method
    @Override
    public Employee updateEmployee(Long id, EmployeeRequest employeeRequest) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            // Update the minister entity with new values from ministerRequest
            employee.setFirstName(employeeRequest.getFirstName());
            employee.setLastName(employeeRequest.getLastName());
            employee.setAge(employeeRequest.getAge());
            employee.setGender(employeeRequest.getGender());
            employee.setSalary(employeeRequest.getSalary());
            employee.setPosition((employee.getPosition()));
            return employeeRepository.save(employee);
        } else {
            throw new NotFoundException("Minister with id " + id + " not found.");
        }
    }
}
