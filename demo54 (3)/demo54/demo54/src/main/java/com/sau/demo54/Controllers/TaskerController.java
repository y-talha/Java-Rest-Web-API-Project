package com.sau.demo54.Controllers;

import com.sau.demo54.DTOs.TaskDTO;
import com.sau.demo54.DTOs.TaskerDTO;
import com.sau.demo54.Models.Employee;
import com.sau.demo54.Models.Task;
import com.sau.demo54.Models.Tasker;
import com.sau.demo54.Repositories.EmployeeRepository;
import com.sau.demo54.Repositories.TaskRepository;
import com.sau.demo54.Repositories.TaskerRepository;
import com.sau.demo54.Services.TaskerService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.cache.support.NullValue;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import javax.validation.Valid;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasker")
public class TaskerController {

    private final TaskerService  taskerService;


    public TaskerController(TaskerService  taskerService) {
       this.taskerService = taskerService;
    }

    @GetMapping("/all")  // this method responds to GET /taskers/all
    public ResponseEntity<List<TaskerDTO>> getAllTaskers() {

        return new ResponseEntity<>(taskerService.getAllTaskers(), HttpStatus.OK);
    }

    @GetMapping("/get/{taskerId}")
    public ResponseEntity<TaskerDTO> getTaskerById(@PathVariable("taskerId") Long taskerId) {

        TaskerDTO taskerDTO = taskerService.getTaskerById(taskerId);
        return new ResponseEntity<>(taskerDTO, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<TaskerDTO> createTasker(@RequestBody @Valid TaskerDTO taskerDTO, BindingResult bindingResult) {
        /*if (bindingResult.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            bindingResult.getAllErrors().forEach(error ->
                    errorMessages.append(error.getDefaultMessage()).append(" ")
            );
            return new ResponseEntity<>(errorMessages.toString(), HttpStatus.BAD_REQUEST);
        }*/
        TaskerDTO createdTasker = taskerService.createTasker(taskerDTO);
        return new ResponseEntity<>(createdTasker, HttpStatus.CREATED);


    }

    @PutMapping("/update/{taskerId}")
    public ResponseEntity<TaskerDTO> updateTasker(@PathVariable("taskerId") Long taskerId, @RequestBody @Valid TaskerDTO taskerDTO, BindingResult bindingResult) {

        TaskerDTO updatedTasker = taskerService.updateTasker(taskerId, taskerDTO);
        return new ResponseEntity<>(updatedTasker, HttpStatus.OK);

    }

    @DeleteMapping("/delete/{taskerId}")
    @Transactional
    public ResponseEntity<Void> deleteTasker(@PathVariable("taskerId") Long taskerId) {
        taskerService.deleteTasker(taskerId);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}