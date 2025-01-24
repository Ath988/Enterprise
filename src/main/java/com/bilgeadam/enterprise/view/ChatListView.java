package com.bilgeadam.enterprise.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ChatListView {
	private String id;
	private String name;
	private String recipientName;
	private LocalDateTime createDate;
	private String lastMessage;
	
}