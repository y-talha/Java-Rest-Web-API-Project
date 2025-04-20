package com.sau.demo54.Controllers;

import com.sau.demo54.DTOs.TaskDTO;
import com.sau.demo54.Services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import org.springframework.validation.BindingResult;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return new ResponseEntity<>(taskService.getAllTasks(), HttpStatus.OK);

    }

    @GetMapping("/get/{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable("taskId") Long taskId) {

        TaskDTO taskDTO = taskService.getTaskByTaskId(taskId);
        return new ResponseEntity<>(taskDTO, HttpStatus.OK);

    }

    @PostMapping("/add")
    public ResponseEntity<TaskDTO> createTask(@RequestBody @Valid TaskDTO taskDTO, BindingResult bindingResult) {

        TaskDTO createdTask = taskService.createTask(taskDTO);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/update/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable("taskId") Long taskId, @RequestBody @Valid TaskDTO taskDTO, BindingResult bindingResult) {


        TaskDTO updatedTask = taskService.updateTask(taskId, taskDTO);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);

    }

    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable("taskId") Long taskId) {
            taskService.deleteTask(taskId);
            return new ResponseEntity<>(HttpStatus.OK);
    }

}
