package com.sau.demo54.Services;

import com.sau.demo54.DTOs.EmployeeDTO;
import com.sau.demo54.Exceptions.ErrorMessages;
import com.sau.demo54.Exceptions.ResourceAlreadyExistsException;
import com.sau.demo54.Exceptions.ResourceNotFoundException;
import com.sau.demo54.Models.Employee;
import com.sau.demo54.Repositories.EmployeeRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImp implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EntityManager entityManager;

    public EmployeeServiceImp(EmployeeRepository employeeRepository, EntityManager entityManager) {
        this.employeeRepository = employeeRepository;
        this.entityManager = entityManager;
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessages.ERROR_EMPLOYEE_NOT_FOUND);
        }
        return employees.stream().map(Employee::viewAsEmployeeDTO2).toList();
    }

    @Override
    public EmployeeDTO getEmployeeById(Long empId) {
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_EMPLOYEE_NOT_FOUND + empId));
        return employee.viewAsEmployeeDTO2();
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        if (employeeDTO.getEmpName() == null || employeeDTO.getEmpName().length() < 2 ||
                employeeDTO.getDepartment() == null || employeeDTO.getDepartment().length() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.ERROR_FIELDS_NOT_VALID);
        }

        if (employeeDTO.getEmpId() != null && employeeRepository.existsById(employeeDTO.getEmpId())) {
            throw new ResourceAlreadyExistsException(ErrorMessages.ERROR_EMPLOYEE_ALREADY_EXIST);
        }

        Employee employee = new Employee(employeeDTO.getEmpId(), employeeDTO.getEmpName(), employeeDTO.getDepartment());
        return employeeRepository.save(employee).viewAsEmployeeDTO();
    }

    @Override
    public EmployeeDTO updateEmployee(Long empId, EmployeeDTO employeeDTO) {
        Employee existingEmployee = employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_EMPLOYEE_NOT_FOUND + empId));

        if (employeeDTO.getEmpName() == null || employeeDTO.getEmpName().length() < 2 ||
                employeeDTO.getDepartment() == null || employeeDTO.getDepartment().length() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.ERROR_FIELDS_NOT_VALID);
        }

        existingEmployee.setEmpName(employeeDTO.getEmpName());
        existingEmployee.setDepartment(employeeDTO.getDepartment());
        return employeeRepository.save(existingEmployee).viewAsEmployeeDTO2();
    }

    @Override
    @Transactional
    public void deleteEmployee(Long empId) {
        Optional<Employee> employee = employeeRepository.findById(empId);
        if (employee.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessages.ERROR_EMPLOYEE_NOT_FOUND + empId);
        }

        employeeRepository.deleteById(empId);

        try { // reset id
            entityManager.createNativeQuery("DO $$ BEGIN " +
                    "IF EXISTS (SELECT 1 FROM employee) THEN " +
                    "EXECUTE format('ALTER SEQUENCE employee_emp_id_seq RESTART WITH %s', (SELECT MAX(emp_id) + 1 FROM employee)); " +
                    "END IF; END $$").executeUpdate();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error resetting sequence", e);
        }
    }
}
