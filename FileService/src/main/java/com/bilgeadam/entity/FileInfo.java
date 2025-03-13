package com.bilgeadam.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "tbl_file_info")
public class FileInfo extends BaseEntity {
	@Column(length = 2048)
	String fileName;
	@Column(length = 2048)
	String url;
	Long size;
	Boolean isInTheRoot;
}