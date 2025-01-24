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
	@OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Message> messages = new ArrayList<>();
	@ManyToMany
	@JoinTable(
			name = "tblchat_users",
			joinColumns = @JoinColumn(name = "chat_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	private Set<User> users = new HashSet<>();
	@Enumerated(EnumType.STRING)
	private EChatType eChatType;
	@Builder.Default
	private LocalDateTime createDate=LocalDateTime.now();
	@Builder.Default
	private boolean isDeleted=false;
	
	
}