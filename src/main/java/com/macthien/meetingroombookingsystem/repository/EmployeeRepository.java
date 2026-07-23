package com.macthien.meetingroombookingsystem.repository;

import com.macthien.meetingroombookingsystem.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByDepartmentDepartmentId(Long employeeId);
}
