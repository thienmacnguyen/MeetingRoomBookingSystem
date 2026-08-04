package com.macthien.meetingroombookingsystem.service;

import com.macthien.meetingroombookingsystem.Exception.DuplicateCodeException;
import com.macthien.meetingroombookingsystem.Exception.ResourceNotFoundException;
import com.macthien.meetingroombookingsystem.dto.EmployeeRequest;
import com.macthien.meetingroombookingsystem.dto.EmployeeResponse;
import com.macthien.meetingroombookingsystem.entity.Department;
import com.macthien.meetingroombookingsystem.entity.Employee;
import com.macthien.meetingroombookingsystem.enums.EmployeeStatus;
import com.macthien.meetingroombookingsystem.repository.DepartmentRepository;
import com.macthien.meetingroombookingsystem.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) throws DuplicateCodeException, ResourceNotFoundException {
        if (employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new DuplicateCodeException("Mã nhân viên '" + request.getEmployeeCode() + "' đã tồn tại.");
        }

        if (employeeRepository.existsByEmployeeEmail(request.getEmployeeEmail())) {
            throw new DuplicateCodeException("Email '" + request.getEmployeeEmail() + "' đã được sử dụng.");
        }

        Department department = departmentRepository.findByDepartmentIdAndDeletedFalse(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng ban với ID: " + request.getDepartmentId()));

        Employee employee = new Employee();
        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setEmployeeFullName(request.getEmployeeFullName());
        employee.setEmployeeEmail(request.getEmployeeEmail());
        employee.setEmployeePhone(request.getEmployeePhone());
        employee.setDepartment(department);
        employee.setEmployeeStatus(EmployeeStatus.ACTIVE);

        Employee saved = employeeRepository.save(employee);
        return mapToResponse(saved);
    }

    @Override
    public Page<EmployeeResponse> getAllEmployees(String search, Pageable pageable) {
        Page<Employee> employees;
        employees = employeeRepository.searchEmployees(search, EmployeeStatus.ACTIVE, pageable);
        return employees.map(this::mapToResponse);
    }

    @Override
    public EmployeeResponse getEmployeeById(Long employeeId) throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + employeeId));
        return mapToResponse(employee);
    }

    @Override
    public EmployeeResponse updateEmployee(Long employeeId, EmployeeRequest request) throws ResourceNotFoundException, DuplicateCodeException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + employeeId));

        if (employeeRepository.existsByEmployeeCodeAndEmployeeIdNot(request.getEmployeeCode(), employeeId)) {
            throw new DuplicateCodeException("Mã nhân viên '" + request.getEmployeeCode() + "' đã được sử dụng bởi nhân viên khác.");
        }

        if (employeeRepository.existsByEmployeeEmailAndEmployeeIdNot(request.getEmployeeEmail(), employeeId)) {
            throw new DuplicateCodeException("Email '" + request.getEmployeeEmail() + "' đã được sử dụng bởi nhân viên khác.");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng ban với ID: " + request.getDepartmentId()));

        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setEmployeeFullName(request.getEmployeeFullName());
        employee.setEmployeeEmail(request.getEmployeeEmail());
        employee.setEmployeePhone(request.getEmployeePhone());
        employee.setDepartment(department);
        employee.setEmployeeStatus(request.getEmployeeStatus());

        Employee updated = employeeRepository.save(employee);
        return mapToResponse(updated);
    }

    @Override
    public void softDeleteEmployee(Long employeeId) throws ResourceNotFoundException {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên hoạt động với ID: " + employeeId));

        employee.setEmployeeStatus(EmployeeStatus.INACTIVE);
        employeeRepository.save(employee);
    }

    @Override
    public void hardDeleteEmployee(Long employeeId) throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + employeeId));

        employeeRepository.delete(employee);

    }

    private EmployeeResponse mapToResponse(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        response.setEmployeeId(employee.getEmployeeId());
        response.setEmployeeCode(employee.getEmployeeCode());
        response.setEmployeeFullName(employee.getEmployeeFullName());
        response.setEmployeeEmail(employee.getEmployeeEmail());
        response.setEmployeePhone(employee.getEmployeePhone());
        response.setEmployeeStatus(employee.getEmployeeStatus());

        // Trích xuất thông tin ID và Tên phòng ban từ entity Department
        if (employee.getDepartment() != null) {
            response.setDepartmentId(employee.getDepartment().getDepartmentId());
            response.setDepartmentName(employee.getDepartment().getDepartmentName());
        }

        return response;
    }
}
