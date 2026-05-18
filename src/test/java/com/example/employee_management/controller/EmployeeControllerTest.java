package com.example.employee_management.controller;

import com.example.employee_management.dto.EmployeeResponse;
import com.example.employee_management.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import com.example.employee_management.security.JwtService;
import com.example.employee_management.security.CustomUserDetailsService;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.example.employee_management.exception.ResourceNotFoundException;
import com.example.employee_management.dto.EmployeeRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private EmployeeService employeeService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void getEmployeeById_存在する社員なら社員情報を返す() throws Exception {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(1L);
        response.setEmployeeCode("EMP001");
        response.setName("山田太郎");
        response.setEmail("yamada@example.com");
        response.setPosition("エンジニア");

        when(employeeService.findById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.employeeCode").value("EMP001"))
                .andExpect(jsonPath("$.name").value("山田太郎"))
                .andExpect(jsonPath("$.email").value("yamada@example.com"))
                .andExpect(jsonPath("$.position").value("エンジニア"));
    }
    @Test
    void getEmployeeById_存在しない社員なら404を返す() throws Exception {
        when(employeeService.findById(999L))
                .thenThrow(new ResourceNotFoundException("指定された社員が存在しません"));

        mockMvc.perform(get("/api/employees/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("指定された社員が存在しません"));
    }
    @Test
    void createEmployee_正常な入力なら社員を登録できる() throws Exception {
        EmployeeRequest request = new EmployeeRequest();
        request.setEmployeeCode("EMP001");
        request.setName("山田太郎");
        request.setEmail("yamada@example.com");
        request.setPosition("エンジニア");

        EmployeeResponse response = new EmployeeResponse();
        response.setId(1L);
        response.setEmployeeCode("EMP001");
        response.setName("山田太郎");
        response.setEmail("yamada@example.com");
        response.setPosition("エンジニア");

        when(employeeService.create(any(EmployeeRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.employeeCode").value("EMP001"))
                .andExpect(jsonPath("$.name").value("山田太郎"))
                .andExpect(jsonPath("$.email").value("yamada@example.com"))
                .andExpect(jsonPath("$.position").value("エンジニア"));
    }

    @Test
    void updateEmployee_正常な入力なら社員情報を更新できる() throws Exception {

        EmployeeRequest request = new EmployeeRequest();
        request.setEmployeeCode("EMP001");
        request.setName("山田次郎");
        request.setEmail("jiro@example.com");
        request.setPosition("マネージャー");

        EmployeeResponse response = new EmployeeResponse();
        response.setId(1L);
        response.setEmployeeCode("EMP001");
        response.setName("山田次郎");
        response.setEmail("jiro@example.com");
        response.setPosition("マネージャー");

        when(employeeService.update(eq(1L), any(EmployeeRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("山田次郎"))
                .andExpect(jsonPath("$.email").value("jiro@example.com"))
                .andExpect(jsonPath("$.position").value("マネージャー"));
    }
    @Test
    void updateEmployee_存在しない社員なら404を返す() throws Exception {

        EmployeeRequest request = new EmployeeRequest();
        request.setEmployeeCode("EMP001");
        request.setName("山田次郎");
        request.setEmail("jiro@example.com");
        request.setPosition("マネージャー");

        when(employeeService.update(eq(999L), any(EmployeeRequest.class)))
                .thenThrow(new ResourceNotFoundException("指定された社員が存在しません"));

        mockMvc.perform(put("/api/employees/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("指定された社員が存在しません"));
    }
    @Test
    void deleteEmployee_存在する社員なら削除できる() throws Exception {

        doNothing().when(employeeService).delete(1L);

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isOk());

        verify(employeeService, times(1)).delete(1L);
    }
    @Test
    void deleteEmployee_存在しない社員なら404を返す() throws Exception {

        doThrow(new ResourceNotFoundException("指定された社員が存在しません"))
                .when(employeeService).delete(999L);

        mockMvc.perform(delete("/api/employees/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("指定された社員が存在しません"));

        verify(employeeService, times(1)).delete(999L);
    }
}