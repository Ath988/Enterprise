package com.bilgeadam.enterprise.controller;

import com.bilgeadam.enterprise.dto.request.*;
import com.bilgeadam.enterprise.dto.response.*;
import com.bilgeadam.enterprise.entity.User;
import com.bilgeadam.enterprise.exception.EnterpriseException;
import com.bilgeadam.enterprise.exception.ErrorType;
import com.bilgeadam.enterprise.service.ChatService;
import com.bilgeadam.enterprise.view.ChatListView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController("/chat")
@RequiredArgsConstructor
public class ChatController {
	private final ChatService chatService;
	private final SimpMessagingTemplate messagingTemplate;
	
	@PostMapping("/login")
	public ResponseEntity<BaseResponse<String>> login(@RequestBody LoginRqDto dto){
		return ResponseEntity.ok(BaseResponse.<String>builder()
		                                     .code(200)
		                                     .message("Message sent successfully!")
		                                     .success(true)
		                                     .data(chatService.login(dto))
		                                     .build());
	}
	
	@PostMapping("/create-group-chat")
	public ResponseEntity<BaseResponse<GroupChatCreateResponseDto>> createNewGroupChat( @RequestBody @Valid CreateGroupChatRqDto chatDto,
	                                                                                    @RequestHeader("Authorization") String token){
		return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.<GroupChatCreateResponseDto>builder().code(200).success(true).message("Group chat created " +
				                                                                                         "successfully!")
		                                                             .data(chatService.createNewGroupChat(chatDto, getTokenFromHeader(token))).build());
	}
	
	@PostMapping("/create-private-chat")
	public ResponseEntity<BaseResponse<PrivateChatResponseDto>> createOrGetPrivateChat(@RequestBody @Valid CreatePrivateChatRqDto chatDto,
	                                                                                   @RequestHeader("Authorization") String token){
		return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.<PrivateChatResponseDto>builder().code(200).success(true)
		                                                                  .message("Private chat created or retrieved successfully!")
		                                                                  .data(chatService.createPrivateChat(chatDto, getTokenFromHeader(token))).build());
	}
	
	@MessageMapping("/private/{chatId}/sendMessage")
	public void sendPrivateMessage(@DestinationVariable String chatId,
	                               @Payload NewMessageDto newMessageDto,
	                               @Header("Authorization") String token) {
		System.out.println("Received message -> chatId: " + chatId + ", content: " + newMessageDto.content());
		String actualToken = getTokenFromHeader(token);
		System.out.println("actual token: "+actualToken);
		chatService.sendNewMessage(newMessageDto,actualToken);
		messagingTemplate.convertAndSend("/topic/private/" + chatId, newMessageDto);
	}
	
	@MessageMapping("/group/{chatId}/sendMessage")
	public void sendGroupMessage(@DestinationVariable String chatId,
	                               @Payload NewMessageDto newMessageDto,
	                               @Header("Authorization") String token) {
		System.out.println("Received message -> chatId: " + chatId + ", content: " + newMessageDto.content());
		String actualToken = getTokenFromHeader(token);
		System.out.println("actual token: "+actualToken);
		chatService.sendNewMessage(newMessageDto,actualToken);
		messagingTemplate.convertAndSend("/topic/group/" + chatId, newMessageDto);
	}
	
	
	
	@GetMapping("/get-chat-list")
	public ResponseEntity<BaseResponse<List<ChatListView>>> getUsersChats(@RequestParam int page,
	                                                                      @RequestParam int size,
	                                                                      @RequestHeader("Authorization") String token){
		return ResponseEntity.ok(BaseResponse.<List<ChatListView>>builder()
		                                     .code(200)
		                                     .message("Message sent successfully!")
		                                     .success(true)
		                                     .data(chatService.getUsersChats(getTokenFromHeader(token)))
		                                     .build());
	}
	
	@PostMapping("/add-user-chat")
	public ResponseEntity<BaseResponse<Set<User>>> addUsersToChat(@RequestHeader("Authorization") String token ,
	                                                            @RequestBody @Valid AddUserToChatDto addUserToChatDto){
		return ResponseEntity.ok(BaseResponse.<Set<User>>builder()
		                                     .code(200)
		                                     .message("Users successfully added to chat!")
		                                     .success(true)
		                                     .data(chatService.addUsersToChat(getTokenFromHeader(token),addUserToChatDto))
		                                     .build());
		
	}
	
	@PutMapping("/delete-group-chat")
	public ResponseEntity<BaseResponse<Boolean>> deleteGroupChat(@RequestBody @NotBlank String chatId, @RequestHeader("Authorization") String token){
		chatService.deleteChat(chatId,getTokenFromHeader(token));
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .message("Group chat deleted successfully!")
		                                     .success(true)
		                                     .data(true)
		                                     .build());
	}
	
	@PutMapping("/delete-message")
	public ResponseEntity<BaseResponse<Boolean>> deleteMessage(@RequestBody @NotBlank String messageId, @RequestHeader("Authorization") String token ){
		chatService.deleteMessage(messageId,getTokenFromHeader(token));
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .message("Message deleted successfully!")
		                                     .success(true)
		                                     .data(true)
		                                     .build());
	}
	
	@GetMapping("/get-users-in-chat")
	public ResponseEntity<BaseResponse<Set<User>>> getUsersInChat(@RequestParam @NotBlank String chatId, @RequestHeader(
			"Authorization") String token){
		return ResponseEntity.ok(BaseResponse.<Set<User>>builder()
		                                     .code(200)
		                                     .message("Users in chat retrieved successfully!")
		                                     .success(true)
		                                     .data(chatService.getUsersInChat(chatId,getTokenFromHeader(token)))
		                                     .build());
	}
	
	@PutMapping("/update-chat-details")
	public ResponseEntity<BaseResponse<Boolean>> updateChatDetails(@RequestBody @Valid UpdateChatDetailsDto detailsDto, @RequestHeader("Authorization") String token){
		chatService.updateChatDetails(detailsDto,getTokenFromHeader(token));
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .message("Chat details updated successfully!")
		                                     .success(true)
		                                     .data(true)
		                                     .build());
	}
	
	@GetMapping("/get-chat-details")
	public ResponseEntity<BaseResponse<ChatDetailResponseDto>> getChatDetails(@RequestParam @NotBlank String chatId,
	                                                                          @RequestParam int page,
	                                                                          @RequestParam int size,
	                                                                          @RequestHeader("Authorization") String token){
		return ResponseEntity.ok(BaseResponse.<ChatDetailResponseDto>builder()
		                                     .code(200)
		                                     .message("Chat details updated successfully!")
		                                     .success(true)
		                                     .data(chatService.getChatDetails(page,size,chatId,
		                                                                      getTokenFromHeader(token)))
		                                     .build());
	}
	
	public String getTokenFromHeader(String headerToken) {
		if (headerToken == null || !headerToken.startsWith("Bearer ")) {
			throw new EnterpriseException(ErrorType.USER_NOT_AUTHORIZED, "Invalid Authorization header");
		}
		return headerToken.replace("Bearer ", "");
	}
	
	
	
}