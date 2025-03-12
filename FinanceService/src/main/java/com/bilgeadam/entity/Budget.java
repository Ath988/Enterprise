package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.EBudgetCategory;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;


@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tbl_budget")
public class Budget extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Integer year;
    Integer month;
    Long departmentId; //OrganizationManagementMicroservice'inden departmanlar çekilerek departmanlara bütçe ayrılacaktır.

    BigDecimal allocatedAmount;
    @Builder.Default
    BigDecimal spentAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    EBudgetCategory budgetCategory;
    String description;



}
