package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.EAssetCategoryType;
import com.bilgeadam.entity.enums.EAssetStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_asset")
public class Asset extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long companyId;
    Long employeeId;
    String description;
    LocalDate givenDate;

    @Enumerated(EnumType.STRING)
    EAssetCategoryType type;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    EAssetStatus status = EAssetStatus.IN_USE;

}
