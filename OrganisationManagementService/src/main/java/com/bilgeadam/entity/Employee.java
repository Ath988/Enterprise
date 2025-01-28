package com.bilgeadam.entity;
import com.bilgeadam.entity.enums.EmployeeRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDate;


@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "tbl_employee")
public class Employee extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long companyId; //çalıştığı şirket Idsi
    Long authId;
    Long managerEmployeeId;//yöneticisinin Idsi, hiyerarşik düzen için
    String firstName;
    String lastName;
    String email;
    @CreationTimestamp
    LocalDate hireDate;
    Long departmentId; //çalışanın bağlı olduğu departman
    @Enumerated(EnumType.STRING)
    EmployeeRole employeeRole;


}
