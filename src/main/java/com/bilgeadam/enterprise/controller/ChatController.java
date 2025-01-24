package com.bilgeadam.enterprise.controller;

import com.bilgeadam.enterprise.dto.request.AddUserToChatDto;
import com.bilgeadam.enterprise.dto.request.CreateChatDto;
import com.bilgeadam.enterprise.dto.request.NewMessageDto;
import com.bilgeadam.enterprise.dto.response.BaseResponse;
import com.bilgeadam.enterprise.dto.response.ChatCreateResponseDto;
import com.bilgeadam.enterprise.dto.response.ChatResponseDto;
import com.bilgeadam.enterprise.dto.response.NewMessageResponseDto;
import com.bilgeadam.enterprise.service.ChatService;
import com.bilgeadam.enterprise.view.ChatListView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/chat")
@RequiredArgsConstructor
public class ChatController {
	private final ChatService chatService;
	
	@PostMapping("/create-chat")
	public ResponseEntity<BaseResponse<ChatCreateResponseDto>> createNewChat(@RequestBody CreateChatDto chatDto, String token){
		return ResponseEntity.ok(BaseResponse.<ChatCreateResponseDto>builder().code(200).success(true).message("Chat created " +
				                                                                                         "successfully!")
		                                     .data(chatService.createNewChat(chatDto, token)).build());
	}
	
	// Kullanıcılardan gelen mesajları dinler ve belirli bir topic'e yayınlar
	@MessageMapping("/private/{chatId}/sendMessage")
	@SendTo("/topic/private/{chatId}")
	public ResponseEntity<BaseResponse<NewMessageResponseDto>> sendPrivateMessage(@DestinationVariable NewMessageDto newMessageDto,
	                                                                              @RequestParam String token) {
		return ResponseEntity.ok(BaseResponse.<NewMessageResponseDto>builder()
				                         .code(200)
				                         .message("Message sent successfully!")
				                         .success(true)
				                         .data(chatService.sendNewMessage(newMessageDto,token))
				                         .build());
	}
	
	@GetMapping("/get-chat-list")
	public ResponseEntity<BaseResponse<List<ChatListView>>> getUsersChats(@RequestParam String token){
		return ResponseEntity.ok(BaseResponse.<List<ChatListView>>builder()
		                                     .code(200)
		                                     .message("Message sent successfully!")
		                                     .success(true)
		                                     .data(chatService.getUsersChats(token))
		                                     .build());
	}
	
	@PostMapping("/add-user-chat")
	public ResponseEntity<BaseResponse<Boolean>> addUsersToChat(@RequestParam String token ,
	                                                            @RequestBody AddUserToChatDto addUserToChatDto){
		chatService.addUsersToChat(token,addUserToChatDto);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .message("Users added successfully!")
		                                     .success(true)
		                                     .data(true)
		                                     .build());
		
	}
}