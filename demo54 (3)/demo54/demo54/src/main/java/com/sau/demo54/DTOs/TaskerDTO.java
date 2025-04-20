package com.sau.demo54.DTOs;

import java.time.LocalDate;
import java.time.LocalTime;

public class TaskerDTO {

    private Long taskerId;
    private LocalDate taskDate;
    private LocalTime taskTime;
    private EmployeeDTO employeeDTO;
    private TaskDTO taskDTO;

    public TaskerDTO() {
    }
    // Constructors
    public TaskerDTO(Long taskerId, LocalDate taskDate, LocalTime taskTime) {
        this.taskerId = taskerId;
        this.taskDate = taskDate;
        this.taskTime = taskTime;
    }

    public TaskerDTO(Long taskerId, LocalDate taskDate, LocalTime taskTime, EmployeeDTO employeeDTO, TaskDTO taskDTO) {
        this.taskerId = taskerId;
        this.taskDate = taskDate;
        this.taskTime = taskTime;
        this.employeeDTO = employeeDTO;
        this.taskDTO = taskDTO;
    }

    // Getters and Setters
    public Long getTaskerId() {
        return taskerId;
    }

    public void setTaskerId(Long taskerId) {
        this.taskerId = taskerId;
    }

    public LocalDate getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(LocalDate taskDate) {
        this.taskDate = taskDate;
    }

    public LocalTime getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(LocalTime taskTime) {
        this.taskTime = taskTime;
    }

    public EmployeeDTO getEmployeeDTO() {
        return employeeDTO;
    }

    public void setEmployeeDTO(EmployeeDTO employeeDTO) {
        this.employeeDTO = employeeDTO;
    }

    public TaskDTO getTaskDTO() {
        return taskDTO;
    }

    public void setTaskDTO(TaskDTO taskDTO) {
        this.taskDTO = taskDTO;
    }
}
