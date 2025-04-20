package com.sau.demo54.Repositories;

import com.sau.demo54.Models.Employee;
import com.sau.demo54.Models.Task;
import com.sau.demo54.Models.Tasker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskerRepository extends JpaRepository<Tasker, Long>{

    // Custom method to find Tasker by Employee and Task
    Optional<Tasker> findByEmployeeAndTask(Employee employee, Task task);
}
