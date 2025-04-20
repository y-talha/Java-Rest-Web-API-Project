package com.sau.demo54.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sau.demo54.DTOs.TaskDTO;
import com.sau.demo54.DTOs.TaskerDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Entity

public class Tasker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskerId;

    @NotNull(message = "Task date cannot be null")
    private LocalDate taskDate;

    @NotNull(message = "Task time cannot be null")
    private LocalTime taskTime;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee; // it is "employee" to match with mappedBy property

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    public TaskerDTO viewAsTaskerDTO() {
        return new TaskerDTO(
                this.getTaskerId(),
                this.getTaskDate(),
                this.getTaskTime(),
                this.getEmployee().viewAsEmployeeDTO(), // Convert Employee to EmployeeDTO
                this.getTask().viewAsTaskDTO() // Convert Task to TaskDTO
        );
    }
    public TaskerDTO viewAsTaskerDTO2() {
        return new TaskerDTO(
                this.getTaskerId(),
                this.getTaskDate(),
                this.getTaskTime()
        );
    }


    public Tasker() {
    }

    public Tasker(Long taskerId, LocalDate taskDate, LocalTime taskTime,Employee employee, Task task) {
        this.taskerId = taskerId;
        this.taskDate = taskDate;
        this.taskTime = taskTime;
        this.employee = employee;
        this.task = task;
    }

    public Long getTaskerId() {return taskerId;}
    public void setTaskerId(Long taskerId) {this.taskerId = taskerId;}

    public LocalDate getTaskDate() {return taskDate;}
    public void setTaskDate(LocalDate taskDate) {this.taskDate = taskDate;}

    public LocalTime getTaskTime() {return taskTime;}
    public void setTaskTime(LocalTime taskTime) {this.taskTime = taskTime;}

    public Employee getEmployee() {return employee;}
    public void setEmployee(Employee employee) {this.employee = employee;}

    public Task getTask() {return task;}
    public void setTask(Task task) {this.task = task;}

    @Override
    public String toString() {
        return "Tasker{" +
                "id=" + taskerId +
                ", taskDate=" + taskDate +
                ", taskTime=" + taskTime +
                ", employee=" + employee +
                ", task=" + task +
                '}';
    }
}


