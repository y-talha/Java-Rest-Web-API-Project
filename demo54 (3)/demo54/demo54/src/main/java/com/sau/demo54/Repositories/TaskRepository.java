package com.sau.demo54.Repositories;

import com.sau.demo54.Models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
