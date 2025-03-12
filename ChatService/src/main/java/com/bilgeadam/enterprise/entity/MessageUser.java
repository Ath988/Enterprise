package com.bilgeadam.enterprise.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tbl_message_user")
public class MessageUser {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	@Column(nullable = false)
	private String messageId;
	@Column(nullable = false)
	private String senderId;
	@Column(nullable = false)
	private String targetId;
	@Builder.Default
	private Boolean isDeletedFromUser = false;
	@Builder.Default
	private EMessageStatus messageStatus = EMessageStatus.SENT;
}