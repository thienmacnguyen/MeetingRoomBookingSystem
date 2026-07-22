package com.macthien.meetingroombookingsystem.repository;

import com.macthien.meetingroombookingsystem.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByDepartmentCode(String departmentCode);
    boolean existsByDepartmentCodeAndDepartmentIdNot(String departmentCode, Long departmentId);
    Page<Department> findByDepartmentNameContainingIgnoreCase(String departmentName, Pageable pageable);
}
