package com.macthien.meetingroombookingsystem.service;

import com.macthien.meetingroombookingsystem.Exception.DeleteConstraintException;
import com.macthien.meetingroombookingsystem.Exception.DuplicateCodeException;
import com.macthien.meetingroombookingsystem.Exception.ResourceNotFoundException;
import com.macthien.meetingroombookingsystem.dto.DepartmentRequest;
import com.macthien.meetingroombookingsystem.dto.DepartmentResponse;
import com.macthien.meetingroombookingsystem.entity.Department;
import com.macthien.meetingroombookingsystem.repository.DepartmentRepository;
import com.macthien.meetingroombookingsystem.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    @Transactional(rollbackFor = DuplicateCodeException.class)
    public DepartmentResponse createDepartment(DepartmentRequest request) throws DuplicateCodeException {
        String code = request.getDepartmentCode();
        if (code == null || code.trim().isEmpty()) {
            code = generateNextDepartmentCode();
        } else {
            if (departmentRepository.existsByDepartmentCodeAndDeletedFalse(code)) {
                throw new DuplicateCodeException("Mã phòng ban '" + code + "' đã tồn tại.");
            }
        }
        Department department = new Department();
        department.setDepartmentCode(code);
        department.setDepartmentName(request.getDepartmentName());
        department.setDepartmentDescription(request.getDepartmentDescription());
        department.setDeleted(false);

        Department saved = departmentRepository.save(department);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentResponse> getAllDepartments(String search, Pageable pageable) {
        Page<Department> departments;
        if (search == null || search.trim().isEmpty()) {
            departments = departmentRepository.findAllByDeletedFalse(pageable);
        } else {
            departments = departmentRepository.searchActiveDepartments(search, pageable);
        }
        return departments.map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(Long departmentId) throws ResourceNotFoundException {
        Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng ban với ID: " + departmentId));
            return mapToResponse(department);
    }

    @Override
    @Transactional(rollbackFor = {ResourceNotFoundException.class, DuplicateCodeException.class})
    public DepartmentResponse updateDepartment(Long departmentId, DepartmentRequest request)
            throws ResourceNotFoundException, DuplicateCodeException {

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng ban với ID: " + departmentId));

        if (departmentRepository.existsByDepartmentCodeAndDepartmentIdNotAndDeletedFalse(request.getDepartmentCode(), departmentId)) {
            throw new DuplicateCodeException("Mã phòng ban '" + request.getDepartmentCode() + "' đã được sử dụng bởi phòng ban khác.");
        }

        department.setDepartmentCode(request.getDepartmentCode());
        department.setDepartmentName(request.getDepartmentName());
        department.setDepartmentDescription(request.getDepartmentDescription());

        Department updated = departmentRepository.save(department);
        return mapToResponse(updated);
    }

    @Override
    public void softDeleteDepartment(Long id) throws ResourceNotFoundException, DeleteConstraintException {
        Department department = departmentRepository.findByDepartmentIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng ban hoạt động với ID: " + id));

        if (employeeRepository.existsByDepartmentDepartmentIdAndDeletedFalse(id)) {
            throw new DeleteConstraintException("Không thể xóa phòng ban này vì vẫn còn nhân viên hoạt động trực thuộc.");
        }

        department.setDeleted(true);
        departmentRepository.save(department);

        System.err.println("Đã xóa phòng ban" + department.getDepartmentName());
    }

    @Override
    public void hardDeleteDepartment(Long id) throws ResourceNotFoundException, DeleteConstraintException {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng ban với ID: " + id));

        if (employeeRepository.existsByDepartmentDepartmentIdAndDeletedFalse(id)) {
            throw new DeleteConstraintException("Không thể xóa phòng ban này vì vẫn còn nhân viên hoạt động trực thuộc.");
        }

        departmentRepository.delete(department);

        System.err.println("Đã xóa phòng ban" + department.getDepartmentName());
    }

    private String generateNextDepartmentCode() {
        String prefix = "PB";
        int defaultStartNumber = 1;

        Optional<Department> lastDeptOpt = departmentRepository
                .findFirstByDepartmentCodeStartingWithAndDeletedFalseOrderByDepartmentCodeDesc(prefix);

        if (lastDeptOpt.isEmpty()) {
            return prefix + String.format("%03d", defaultStartNumber);
        }

        String lastCode = lastDeptOpt.get().getDepartmentCode();
        try {
            String numericPart = lastCode.substring(prefix.length());
            int lastNumber = Integer.parseInt(numericPart);
            int nextNumber = lastNumber + 1;
            return prefix + String.format("%03d", nextNumber);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return prefix + String.format("%03d", defaultStartNumber);
        }
    }

    private DepartmentResponse mapToResponse(Department department) {
        DepartmentResponse response = new DepartmentResponse();
        response.setDepartmentId(department.getDepartmentId());
        response.setDepartmentCode(department.getDepartmentCode());
        response.setDepartmentName(department.getDepartmentName());
        response.setDepartmentDescription(department.getDepartmentDescription());
        return response;
    }
}
