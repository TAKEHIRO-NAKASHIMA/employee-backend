package com.example.employee_management.service;

import com.example.employee_management.dto.EmployeeRequest;
import com.example.employee_management.dto.EmployeeResponse;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.exception.ResourceNotFoundException;
import com.example.employee_management.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void findById_存在する社員なら社員情報を返す() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setEmployeeCode("EMP001");
        employee.setName("山田太郎");
        employee.setEmail("yamada@example.com");
        employee.setPosition("エンジニア");

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        EmployeeResponse response = employeeService.findById(1L);

        assertEquals(1L, response.getId());
        assertEquals("EMP001", response.getEmployeeCode());
        assertEquals("山田太郎", response.getName());
        assertEquals("yamada@example.com", response.getEmail());
        assertEquals("エンジニア", response.getPosition());

        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void findById_存在しない社員なら例外を投げる() {
        when(employeeRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.findById(999L)
        );

        assertEquals("指定された社員が存在しません", exception.getMessage());

        verify(employeeRepository, times(1)).findById(999L);
    }

    @Test
    void create_社員を登録できる() {
        EmployeeRequest request = new EmployeeRequest();
        request.setEmployeeCode("EMP001");
        request.setName("山田太郎");
        request.setEmail("yamada@example.com");
        request.setPosition("エンジニア");

        Employee savedEmployee = new Employee();
        savedEmployee.setId(1L);
        savedEmployee.setEmployeeCode("EMP001");
        savedEmployee.setName("山田太郎");
        savedEmployee.setEmail("yamada@example.com");
        savedEmployee.setPosition("エンジニア");

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(savedEmployee);

        EmployeeResponse response = employeeService.create(request);

        assertEquals(1L, response.getId());
        assertEquals("EMP001", response.getEmployeeCode());
        assertEquals("山田太郎", response.getName());
        assertEquals("yamada@example.com", response.getEmail());
        assertEquals("エンジニア", response.getPosition());

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void update_存在する社員なら更新できる() {

        EmployeeRequest request = new EmployeeRequest();
        request.setEmployeeCode("EMP001");
        request.setName("山田次郎");
        request.setEmail("jiro@example.com");
        request.setPosition("マネージャー");

        Employee existingEmployee = new Employee();
        existingEmployee.setId(1L);
        existingEmployee.setEmployeeCode("EMP001");
        existingEmployee.setName("山田太郎");
        existingEmployee.setEmail("yamada@example.com");
        existingEmployee.setPosition("エンジニア");

        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(1L);
        updatedEmployee.setEmployeeCode("EMP001");
        updatedEmployee.setName("山田次郎");
        updatedEmployee.setEmail("jiro@example.com");
        updatedEmployee.setPosition("マネージャー");

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(existingEmployee));

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(updatedEmployee);

        EmployeeResponse response = employeeService.update(1L, request);

        assertEquals("山田次郎", response.getName());
        assertEquals("jiro@example.com", response.getEmail());
        assertEquals("マネージャー", response.getPosition());

        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }
    @Test
    void update_存在しない社員なら例外を投げる() {

        EmployeeRequest request = new EmployeeRequest();
        request.setEmployeeCode("EMP001");
        request.setName("山田次郎");
        request.setEmail("jiro@example.com");
        request.setPosition("マネージャー");

        when(employeeRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.update(999L, request)
        );

        assertEquals("指定された社員が存在しません", exception.getMessage());

        verify(employeeRepository, times(1)).findById(999L);

        verify(employeeRepository, never()).save(any(Employee.class));
    }
    @Test
    void delete_存在する社員なら削除できる() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setEmployeeCode("EMP001");
        employee.setName("山田太郎");
        employee.setEmail("yamada@example.com");
        employee.setPosition("エンジニア");

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        employeeService.delete(1L);

        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).delete(employee);
        verify(employeeRepository, never()).deleteById(1L);
    }

    @Test
    void delete_存在しない社員なら例外を投げる() {

        when(employeeRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.delete(999L)
        );

        assertEquals("指定された社員が存在しません", exception.getMessage());

        verify(employeeRepository, times(1)).findById(999L);

        verify(employeeRepository, never()).delete(any(Employee.class));
    }
}
