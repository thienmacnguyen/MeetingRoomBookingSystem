package com.macthien.meetingroombookingsystem.repository;

import com.macthien.meetingroombookingsystem.entity.Employee;
import com.macthien.meetingroombookingsystem.enums.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByDepartmentDepartmentIdAndEmployeeStatus(Long departmentId, EmployeeStatus status);
    boolean existsByEmployeeCode(String employeeCode);
    boolean existsByEmployeeCodeAndEmployeeIdNot(String employeeCode, Long employeeId);
    boolean existsByEmployeeEmail(String employeeEmail);
    boolean existsByEmployeeEmailAndEmployeeIdNot(String employeeEmail, Long employeeId);
    @Query("SELECT e FROM Employee e WHERE e.employeeStatus = :status AND " +
            "(:search IS NULL OR :search = '') OR " +
            "(LOWER(e.employeeFullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.employeeEmail) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Employee> searchEmployees(
            @Param("search") String search,
            @Param("status") EmployeeStatus status,
            Pageable pageable
    );

}
