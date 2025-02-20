package com.bilgeadam.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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

}