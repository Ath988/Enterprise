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

    //Bu alanlar OrganizationManagementService'den gelecek.
    @Column(nullable = false,unique = true)
    Long employeeId;
    @Column(nullable = false)
    Long companyId; // memberId de olabilir. duruma göre güncellenicek.
    //

    LocalDate startDate;
    LocalDate endDate;

    String perfonelFileName;
    String perfonelFileUrl; //Gerek var mı ?

}
