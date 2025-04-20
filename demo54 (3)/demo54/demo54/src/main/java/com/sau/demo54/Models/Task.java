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

public class Task {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long taskId;

    @NotNull(message = "Task name cannot be null")
    @Size(min = 3, max = 16, message = "Task name must be between 3 and 16 characters")
    private String taskName;

    @NotNull(message = "Description cannot be null")
    @Size(min = 3, max = 64, message = "Description must be between 3 and 64 characters")
    private String description;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Tasker> taskers;

    public TaskDTO viewAsTaskDTO() {
        return new TaskDTO(this.getTaskId(), this.getTaskName(), this.getDescription());
    }

    public TaskDTO viewAsTaskDTO2() {
        List<TaskerDTO> taskerDTOs = this.getTaskers().stream()
                .map(Tasker::viewAsTaskerDTO2)
                .collect(Collectors.toList());

        return new TaskDTO(this.getTaskId(), this.getTaskName(), this.getDescription(), taskerDTOs);
    }


    public Task() {
    }

    public Task(Long taskId,String taskName, String description) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.description = description;
    }

    public Long getTaskId() {return taskId;}
    public void setTaskId(Long taskId) {this.taskId = taskId;}

    public String getTaskName() {return taskName;}
    public void setTaskName(String taskName) {this.taskName = taskName;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public List<Tasker> getTaskers() {return taskers;}
    public void setTaskers(List<Tasker> taskers) {this.taskers = taskers;}

}
