package com.sau.demo54.Services;

import com.sau.demo54.DTOs.TaskerDTO;
import java.util.List;


public interface TaskerService {
    public List<TaskerDTO> getAllTaskers();
    public TaskerDTO getTaskerById(Long taskerId);
    public TaskerDTO createTasker(TaskerDTO taskerDTO);
    public TaskerDTO updateTasker(Long taskerId, TaskerDTO taskerDTO);
    public void deleteTasker(Long taskerId);

}
