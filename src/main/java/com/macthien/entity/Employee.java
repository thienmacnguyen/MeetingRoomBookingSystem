package entity;

import enums.EmployeeStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "employee_code", unique = true, nullable = false, length = 50)
    private String employeeCode;

    @Column(name = "employee_full_name", nullable = false, length = 100)
    private String employeeFullName;

    @Column(name = "employee_email", unique = true, nullable = false, length = 100)
    private String employeeEmail;

    @Column(name = "employee_phone", length = 20)
    private String employeePhone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Enumerated(EnumType.STRING)
    @Column(name = "employee_status", nullable = false, length = 20)
    private EmployeeStatus employeeStatus;
}
