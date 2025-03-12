package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.EAssetCategoryType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_maintenance")
public class Maintenance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long companyId;
    Long employeeId;
    String description;
    @Enumerated(EnumType.STRING)
    EAssetCategoryType type;
}
