package com.sau.demo54.Models;

//import com.sau.cr.dtos.CarDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sau.demo54.DTOs.EmployeeDTO;
import com.sau.demo54.DTOs.TaskDTO;
import com.sau.demo54.DTOs.TaskerDTO;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


import java.util.List;
import java.util.stream.Collectors;


@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long empId;

    @Column(nullable = false)
    @NotNull(message = "Employee name cannot be null")
    @Size(min = 3, max = 16, message = "Employee name must be between 3 and 16 characters")
    private String empName;

    @Column(nullable = false)
    @NotNull(message = "Department cannot be null")
    @Size(min = 3, max = 16, message = "Department name must be between 3 and 16 characters")
    private String department;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL) // it determines what name it will be called
    @JsonIgnore
    private List<Tasker> taskers;

    public EmployeeDTO viewAsEmployeeDTO() {
        return new EmployeeDTO(this.getEmpId(), this.getEmpName(), this.getDepartment());
    }

    public EmployeeDTO viewAsEmployeeDTO2() {
        List<TaskerDTO> taskerDTOs = this.getTaskers().stream()
                .map(Tasker::viewAsTaskerDTO2)
                .collect(Collectors.toList());

        return new EmployeeDTO(this.getEmpId(), this.getEmpName(), this.getDepartment(), taskerDTOs);
    }




    public Employee() {
    }

    public Employee(Long empId,String empName, String department) {
        this.empId = empId;
        this.empName = empName;
        this.department = department;
    }

    public Long getEmpId() {return empId;}
    public void setEmpId(Long empId) {this.empId = empId;}

    public String getEmpName() {return empName;}
    public void setEmpName(String empName) {this.empName = empName;}

    public String getDepartment() {return department;}
    public void setDepartment(String department) {this.department = department;}

    public List<Tasker> getTaskers() {return taskers;}
    public void setTaskers(List<Tasker> taskers) {this.taskers = taskers;}

}
