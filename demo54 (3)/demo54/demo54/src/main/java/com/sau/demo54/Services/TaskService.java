package com.sau.demo54.Services;

import com.sau.demo54.DTOs.TaskDTO;
import java.util.List;

public interface TaskService {
    public List<TaskDTO> getAllTasks();
    public TaskDTO getTaskByTaskId(Long taskId);
    public TaskDTO createTask(TaskDTO taskDTO);
    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO);
    public void deleteTask(Long taskId);
}
