package com.macthien.meetingroombookingsystem.repository;

import com.macthien.meetingroombookingsystem.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByDepartmentIdAndDeletedFalse(Long departmentId);
    boolean existsByDepartmentCodeAndDeletedFalse(String departmentCode);
    boolean existsByDepartmentCodeAndDepartmentIdNotAndDeletedFalse(String departmentCode, Long departmentId);
    Optional<Department> findFirstByDepartmentCodeStartingWithAndDeletedFalseOrderByDepartmentCodeDesc(String prefix);
    Page<Department> findAllByDeletedFalse(Pageable pageable);
    @Query("SELECT d FROM Department d WHERE d.deleted = false AND LOWER(d.departmentName) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Department> searchActiveDepartments(@Param("search") String search, Pageable pageable);
}
