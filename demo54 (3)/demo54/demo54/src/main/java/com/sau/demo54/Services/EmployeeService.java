package com.sau.demo54.Services;

import com.sau.demo54.DTOs.EmployeeDTO;
import java.util.List;

public interface EmployeeService {
    public List<EmployeeDTO> getAllEmployees();
    public EmployeeDTO getEmployeeById(Long empId);
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
    public EmployeeDTO updateEmployee(Long employeeId, EmployeeDTO employeeDTO);
    public void deleteEmployee(Long empId);
}
