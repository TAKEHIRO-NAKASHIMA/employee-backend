package com.example.employee_management.service;

import com.example.employee_management.dto.EmployeeRequest;
import com.example.employee_management.dto.EmployeeResponse;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.repository.EmployeeRepository;
import com.example.employee_management.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Page<EmployeeResponse> findAll(Pageable pageable) {
        return employeeRepository.findAll(pageable)
                .map(this::toResponse);
    }

    public EmployeeResponse findById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("指定された社員が存在しません"));

        return toResponse(employee);
    }

    public EmployeeResponse create(EmployeeRequest request) {
        Employee employee = new Employee();
        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setPosition(request.getPosition());

        Employee savedEmployee = employeeRepository.save(employee);
        return toResponse(savedEmployee);
    }

    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("指定された社員が存在しません"));

        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setPosition(request.getPosition());

        Employee updatedEmployee = employeeRepository.save(employee);
        return toResponse(updatedEmployee);
    }

    public void delete(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("指定された社員が存在しません"));

        employeeRepository.delete(employee);
    }

    private EmployeeResponse toResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getEmployeeCode(),
                employee.getName(),
                employee.getEmail(),
                employee.getPosition()
        );
    }

    public List<EmployeeResponse> searchByName(String name) {
        return employeeRepository.findByNameContaining(name)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Page<EmployeeResponse> search(String name, String email, String position, Pageable pageable) {
        String safeName = name == null ? "" : name;
        String safeEmail = email == null ? "" : email;
        String safePosition = position == null ? "" : position;

        return employeeRepository
                .findByNameContainingAndEmailContainingAndPositionContaining(
                        safeName, safeEmail, safePosition, pageable
                )
                .map(this::toResponse);
    }
}
