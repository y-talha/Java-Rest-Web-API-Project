package com.sau.demo54.Services;

import com.sau.demo54.DTOs.TaskDTO;
import com.sau.demo54.Exceptions.ErrorMessages;
import com.sau.demo54.Exceptions.ResourceAlreadyExistsException;
import com.sau.demo54.Exceptions.ResourceNotFoundException;
import com.sau.demo54.Models.Task;
import com.sau.demo54.Repositories.TaskRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImp implements TaskService {

    private final TaskRepository taskRepository;
    private final EntityManager entityManager;

    public TaskServiceImp(TaskRepository taskRepository, EntityManager entityManager) {
        this.taskRepository = taskRepository;
        this.entityManager = entityManager;
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        if (tasks.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessages.ERROR_TASK_NOT_FOUND);
        }
        return tasks.stream().map(Task::viewAsTaskDTO2).toList();
    }

    @Override
    public TaskDTO getTaskByTaskId(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_TASK_NOT_FOUND + taskId));
        return task.viewAsTaskDTO2();
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        if (taskDTO.getTaskName() == null || taskDTO.getTaskName().length() < 2 ||
                taskDTO.getDescription() == null || taskDTO.getDescription().length() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.ERROR_FIELDS_NOT_VALID);
        }

        if (taskDTO.getTaskId() != null && taskRepository.existsById(taskDTO.getTaskId())) {
            throw new ResourceAlreadyExistsException(ErrorMessages.ERROR_TASK_ALREADY_EXIST);
        }

        Task task = new Task(taskDTO.getTaskId(), taskDTO.getTaskName(), taskDTO.getDescription());
        return taskRepository.save(task).viewAsTaskDTO();
    }

    @Override
    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_TASK_NOT_FOUND + taskId));

        if (taskDTO.getTaskName() == null || taskDTO.getTaskName().length() < 2 ||
                taskDTO.getDescription() == null || taskDTO.getDescription().length() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.ERROR_FIELDS_NOT_VALID);
        }

        existingTask.setTaskName(taskDTO.getTaskName());
        existingTask.setDescription(taskDTO.getDescription());
        return taskRepository.save(existingTask).viewAsTaskDTO2();
    }

    @Override
    @Transactional
    public void deleteTask(Long taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessages.ERROR_TASK_NOT_FOUND + taskId);
        }

        taskRepository.deleteById(taskId);

        try { // reset id
            entityManager.createNativeQuery("DO $$ BEGIN " +
                    "IF EXISTS (SELECT 1 FROM task) THEN " +
                    "EXECUTE format('ALTER SEQUENCE task_task_id_seq RESTART WITH %s', (SELECT MAX(task_id) + 1 FROM task)); " +
                    "END IF; END $$").executeUpdate();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error resetting sequence", e);
        }
    }
}
