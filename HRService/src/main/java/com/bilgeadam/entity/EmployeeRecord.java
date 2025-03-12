package com.bilgeadam.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "tbl_employee_record")
public class EmployeeRecord extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @Column(nullable = false,unique = true)
    Long employeeId;
    @Column(nullable = false)
    Long companyId;


    LocalDate startDate;
    LocalDate endDate;

    @Column(length = 512)
    String perfonelFileName;
    @Column(length = 1024)
    String perfonelFileUrl;

}
