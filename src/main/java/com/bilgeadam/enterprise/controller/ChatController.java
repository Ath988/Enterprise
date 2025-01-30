package com.bilgeadam.enterprise.controller;

import com.bilgeadam.enterprise.dto.request.*;
import com.bilgeadam.enterprise.dto.response.*;
import com.bilgeadam.enterprise.entity.User;
import com.bilgeadam.enterprise.exception.EnterpriseException;
import com.bilgeadam.enterprise.exception.ErrorType;
import com.bilgeadam.enterprise.service.ChatService;
import com.bilgeadam.enterprise.utility.MessageProducer;
import com.bilgeadam.enterprise.view.UserView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static com.bilgeadam.enterprise.constant.RestApis.*;

@RestController
@RequestMapping(CHAT)
@RequiredArgsConstructor
public class ChatController {
	private final ChatService chatService;
	private final SimpMessagingTemplate messagingTemplate;
	private final MessageProducer messageProducer;
	
	@PostMapping("/login")
	public ResponseEntity<BaseResponse<String>> login(@RequestBody LoginRqDto dto){
		return ResponseEntity.ok(BaseResponse.<String>builder()
		                                     .code(200)
		                                     .message("Message sent successfully!")
		                                     .success(true)
		                                     .data(chatService.login(dto))
		                                     .build());
	}
	
	@PostMapping(CREATE_GROUP_CHAT)
	public ResponseEntity<BaseResponse<GroupChatCreateResponseDto>> createNewGroupChat( @RequestBody @Valid CreateGroupChatRqDto chatDto,
	                                                                                    HttpServletRequest request){
		return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.<GroupChatCreateResponseDto>builder().code(200).success(true).message("Group chat created " +
				                                                                                         "successfully!")
		                                                             .data(chatService.createNewGroupChat(chatDto)).build());
	}
	
	@PostMapping(CREATE_PRIVATE_CHAT)
	public ResponseEntity<BaseResponse<PrivateChatResponseDto>> createOrGetPrivateChat(@RequestBody @Valid CreatePrivateChatRqDto chatDto,
	                                                                                   HttpServletRequest request){
		String userId = (String) request.getAttribute("userId");
		System.out.println("Private chat request userId: " + userId);
		return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.<PrivateChatResponseDto>builder().code(200).success(true)
		                                                                  .message("Private chat created or retrieved successfully!")
		                                                                  .data(chatService.createPrivateChat(chatDto
				                                                                  , userId)).build());
	}
	
	@MessageMapping("/private/{chatId}/sendMessage")
	public void sendPrivateMessage(@DestinationVariable String chatId,
	                               @Payload NewMessageDto newMessageDto,
	                               @Header("Authorization") String token) {
		messageProducer.sendMessage(newMessageDto.content());
		String actualToken = getTokenFromHeader(token);
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
	
	
	@GetMapping(GET_USERS_CHAT)
	public ResponseEntity<BaseResponse<List<ChatListViewDto>>> getUsersChats(
			@RequestParam int limit,
			HttpServletRequest request) {
		
		String userId = (String) request.getAttribute("userId");
		List<ChatListViewDto> chatList = chatService.getUsersChats(userId, limit);
		
		return ResponseEntity.ok(BaseResponse.<List<ChatListViewDto>>builder()
		                                     .code(200)
		                                     .message("Chat list retrieved successfully!")
		                                     .success(true)
		                                     .data(chatList)
		                                     .build());
	}
	
	
	@PostMapping(ADD_USER_CHAT)
	public ResponseEntity<BaseResponse<Set<UserView>>> addUsersToChat(HttpServletRequest request ,
	                                                            @RequestBody @Valid AddUsersToGroupChatDto addUsersToGroupChatDto){
		String userId = (String) request.getAttribute("userId");
		return ResponseEntity.ok(BaseResponse.<Set<UserView>>builder()
		                                     .code(200)
		                                     .message("Users successfully added to chat!")
		                                     .success(true)
		                                     .data(chatService.addUsersToChat(userId, addUsersToGroupChatDto))
		                                     .build());
		
	}
	
	@PostMapping(DELETE_GROUP_CHAT)
	public ResponseEntity<BaseResponse<Boolean>> deleteGroupChat(@RequestBody @Valid DeleteChatRqDto dto,
	                                                             HttpServletRequest request){
		String userId = (String) request.getAttribute("userId");
		chatService.deleteChat(dto,userId);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .message("Group chat deleted successfully!")
		                                     .success(true)
		                                     .data(true)
		                                     .build());
	}
	
	
	
	@PostMapping(DELETE_MESSAGE)
	public ResponseEntity<BaseResponse<Boolean>> deleteMessage(@RequestBody @Valid DeleteMessageRqDto dto,
	                                                           HttpServletRequest request ){
		String userId = (String) request.getAttribute("userId");
		chatService.deleteMessage(dto,userId);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .message("Message deleted successfully!")
		                                     .success(true)
		                                     .data(true)
		                                     .build());
	}
	
	@GetMapping(GET_USERS_IN_CHAT)
	public ResponseEntity<BaseResponse<Set<UserView>>> getUsersInChat(@RequestParam @NotBlank String chatId, HttpServletRequest request){
		String userId = (String) request.getAttribute("userId");
		return ResponseEntity.ok(BaseResponse.<Set<UserView>>builder()
		                                     .code(200)
		                                     .message("Users in chat retrieved successfully!")
		                                     .success(true)
		                                     .data(chatService.getUsersInChat(chatId,userId))
		                                     .build());
	}
	
	@PutMapping(UPDATE_CHAT_DETAILS)
	public ResponseEntity<BaseResponse<Boolean>> updateChatDetails(@RequestBody @Valid UpdateChatDetailsDto detailsDto, HttpServletRequest request){
		String userId = (String) request.getAttribute("userId");
		chatService.updateChatDetails(detailsDto,userId);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .message("Chat details updated successfully!")
		                                     .success(true)
		                                     .data(true)
		                                     .build());
	}
	
	@GetMapping(GET_CHAT_DETAILS)
	public ResponseEntity<BaseResponse<ChatDetailResponseDto>> getChatDetails(
			@RequestParam @NotBlank String chatId,
			@RequestParam int size,
			HttpServletRequest request) {
		//TODO: LIMIT YERINE PAGEABLE KULLAN!!!!
		String userId = (String) request.getAttribute("userId");
		return ResponseEntity.ok(BaseResponse.<ChatDetailResponseDto>builder()
		                                     .code(200)
		                                     .message("Chat details retrieved successfully!")
		                                     .success(true)
		                                     .data(chatService.getChatDetails(size, chatId, userId))
		                                     .build());
	}

	
	public String getTokenFromHeader(String headerToken) {
		if (headerToken == null || !headerToken.startsWith("Bearer ")) {
			throw new EnterpriseException(ErrorType.USER_NOT_AUTHORIZED, "Invalid Authorization header");
		}
		return headerToken.replace("Bearer ", "");
	}
	
	
	
}