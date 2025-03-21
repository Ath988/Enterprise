package com.bilgeadam.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "tbl_survey")
public class Survey extends BaseEntity{
	String title;
	String description;
	LocalDateTime expirationDate;
	Long createdBy;
	@ElementCollection
	List<String> questionIds;
	Long companyId;
	@ElementCollection
	@CollectionTable(name = "survey_assigned_employee_ids"
			,joinColumns = @JoinColumn(name = "survey_id"))
	@Column(name = "employee_id")
	List<Long> assignedEmployeeIds;
}