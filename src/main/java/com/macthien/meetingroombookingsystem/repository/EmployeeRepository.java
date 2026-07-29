package com.macthien.meetingroombookingsystem.repository;

import com.macthien.meetingroombookingsystem.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByDepartmentDepartmentIdAndDeletedFalse(Long employeeId);
    boolean existsByEmployeeCode(String employeeCode);
    boolean existsByEmployeeCodeAndEmployeeIdNot(String employeeCode, Long employeeId);
    boolean existsByEmployeeEmail(String employeeEmail);
    boolean existsByEmployeeEmailAndEmployeeIdNot(String employeeEmail, Long employeeId);
    Optional<Employee> findByEmployeeIdAndDeletedFalse(Long employeeId);
    Page<Employee> findByEmployeeFullNameContainingIgnoreCaseOrEmployeeEmailContainingIgnoreCase(
            String fullName, String email, Pageable pageable);
}
