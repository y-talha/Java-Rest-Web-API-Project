package com.sau.demo54.Controllers;

import com.sau.demo54.DTOs.EmployeeDTO;

import com.sau.demo54.Services.EmployeeService;
import com.sau.demo54.Services.TaskService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.validation.BindingResult;
import javax.validation.Valid;



import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/employee")
public class EmployeeController {


    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService  employeeService, TaskService taskService) {
        this.employeeService  = employeeService;
    }


    @GetMapping("/all")  // this method responds to GET /employee/all
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
        //return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/get/{empId}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable("empId") Long empId) {

        EmployeeDTO employeeDTO = employeeService.getEmployeeById(empId);
        return new ResponseEntity<>(employeeDTO, HttpStatus.OK);

    }


    @PostMapping("/add")
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody @Valid EmployeeDTO employeeDTO, BindingResult bindingResult) {

        EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }


    @PutMapping("/update/{empId}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable("empId") Long empId, @RequestBody @Valid EmployeeDTO employeeDTO, BindingResult bindingResult) {
        EmployeeDTO updatedEmployee = employeeService.updateEmployee(empId, employeeDTO);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{empId}")
    @Transactional
    public ResponseEntity<Void> deleteEmployee(@PathVariable("empId") Long empId) {
        employeeService.deleteEmployee(empId);
        return new ResponseEntity<>(HttpStatus.OK);

    }


}
