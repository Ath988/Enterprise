package com.bilgeadam.entity;
import com.bilgeadam.entity.enums.EGender;
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
    String firstName;
    String lastName;
    String email;
    Long positionId;
    @Enumerated(EnumType.STRING)
    EmployeeRole role;
    @Enumerated(EnumType.STRING)
    EGender gender;
    String avatarUrl;
    @Builder.Default
    Boolean isOnline = false;


}