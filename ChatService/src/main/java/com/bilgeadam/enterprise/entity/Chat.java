package com.bilgeadam.enterprise.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tblchat")
public class Chat {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private String name;
	private String description;
	@Enumerated(EnumType.STRING)
	private EChatType eChatType;
	@Builder.Default
	private LocalDateTime createDate=LocalDateTime.now();
	@Builder.Default
	private boolean isDeleted=false;
	private String chatImage;
	
}