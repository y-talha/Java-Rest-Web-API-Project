package com.sau.demo54.Services;

import com.sau.demo54.DTOs.TaskerDTO;
import com.sau.demo54.Exceptions.ErrorMessages;
import com.sau.demo54.Exceptions.ResourceAlreadyExistsException;
import com.sau.demo54.Exceptions.ResourceNotFoundException;
import com.sau.demo54.Models.Employee;
import com.sau.demo54.Models.Task;
import com.sau.demo54.Models.Tasker;
import com.sau.demo54.Repositories.EmployeeRepository;
import com.sau.demo54.Repositories.TaskRepository;
import com.sau.demo54.Repositories.TaskerRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskerServiceImp implements TaskerService {

    private final TaskerRepository taskerRepository;
    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;
    private final EntityManager entityManager;

    public TaskerServiceImp(TaskerRepository taskerRepository, EmployeeRepository employeeRepository, TaskRepository taskRepository, EntityManager entityManager) {
        this.taskerRepository = taskerRepository;
        this.employeeRepository = employeeRepository;
        this.taskRepository = taskRepository;
        this.entityManager = entityManager;
    }

    @Override
    public List<TaskerDTO> getAllTaskers() {
        List<Tasker> taskers = taskerRepository.findAll();
        if (taskers.isEmpty()) {
            throw new ResourceNotFoundException("No taskers found");
        }
        return taskers.stream().map(Tasker::viewAsTaskerDTO).toList();
    }

    @Override
    public TaskerDTO getTaskerById(Long taskerId) {
        Tasker tasker = taskerRepository.findById(taskerId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_TASKER_NOT_FOUND + taskerId));
        return tasker.viewAsTaskerDTO();
    }

    @Override
    public TaskerDTO createTasker(TaskerDTO taskerDTO) {
        if (taskerDTO.getEmployeeDTO().getEmpId() == null || taskerDTO.getEmployeeDTO().getEmpId() < 1) {
            throw new IllegalArgumentException(ErrorMessages.ERROR_EMPLOYEEID_VALIDATION);
        }

        Employee employee = employeeRepository.findById(taskerDTO.getEmployeeDTO().getEmpId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_EMPLOYEE_NOT_FOUND));

        if (taskerDTO.getTaskDTO().getTaskId() == null || taskerDTO.getTaskDTO().getTaskId() < 1) {
            throw new IllegalArgumentException(ErrorMessages.ERROR_TASKID_VALIDATION);
        }

        Task task = taskRepository.findById(taskerDTO.getTaskDTO().getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_TASK_NOT_FOUND));

        if (taskerDTO.getTaskerId() != null && taskerRepository.existsById(taskerDTO.getTaskerId())) {
            throw new ResourceAlreadyExistsException(ErrorMessages.ERROR_TASKER_ALREADY_EXIST);
        }

        Optional<Tasker> existingTasker = taskerRepository.findByEmployeeAndTask(employee, task);
        if (existingTasker.isPresent()) {
            throw new ResourceAlreadyExistsException(ErrorMessages.ERROR_TASKER_ALREADY_EXIST2);
        }

        Tasker tasker = new Tasker(null, taskerDTO.getTaskDate(), taskerDTO.getTaskTime(), employee, task);
        taskerRepository.save(tasker);

        TaskerDTO taskerResponse = tasker.viewAsTaskerDTO();
        taskerResponse.setEmployeeDTO(null);
        taskerResponse.setTaskDTO(null);

        return taskerResponse;
    }

    @Override
    public TaskerDTO updateTasker(Long taskerId, TaskerDTO taskerDTO) {
        Tasker existingTasker = taskerRepository.findById(taskerId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_TASKER_NOT_FOUND + taskerId));

        if (taskerDTO.getTaskDate() != null) {
            existingTasker.setTaskDate(taskerDTO.getTaskDate());
        }
        if (taskerDTO.getTaskTime() != null) {
            existingTasker.setTaskTime(taskerDTO.getTaskTime());
        }

        return taskerRepository.save(existingTasker).viewAsTaskerDTO();
    }

    @Override
    @Transactional
    public void deleteTasker(Long taskerId) {
        if (!taskerRepository.existsById(taskerId)) {
            throw new ResourceNotFoundException(ErrorMessages.ERROR_TASKER_NOT_FOUND + taskerId);
        }
        taskerRepository.deleteById(taskerId);

        try {
            entityManager.createNativeQuery("DO $$ BEGIN " +
                    "IF EXISTS (SELECT 1 FROM tasker) THEN " +
                    "EXECUTE format('ALTER SEQUENCE tasker_tasker_id_seq RESTART WITH %s', (SELECT MAX(tasker_id) + 1 FROM tasker)); " +
                    "END IF; END $$").executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error resetting sequence", e);
        }
    }
}
