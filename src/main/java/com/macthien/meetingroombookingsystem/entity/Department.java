package entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "department_code", unique = true, nullable = false, length = 50)
    private String departmentCode;

    @Column(name = "department_name", nullable = false, length = 100)
    private String departmentName;

    @Column(name = "department_description", columnDefinition = "TEXT")
    private String departmentDescription;

    @OneToMany(mappedBy = "department", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Employee> employees;
}
