package com.sau.demo54.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor

public class EmployeeDTO {
    private Long empId;
    private String empName;
    private String department;

    private List<TaskerDTO> taskers;

    public EmployeeDTO(Long empId, String empName, String department) {
        this.empId = empId;
        this.empName = empName;
        this.department = department;
    }
    public EmployeeDTO(Long empId, String empName, String department, List<TaskerDTO> taskers) {
        this.empId = empId;
        this.empName = empName;
        this.department = department;
        this.taskers = taskers;
    }
}
