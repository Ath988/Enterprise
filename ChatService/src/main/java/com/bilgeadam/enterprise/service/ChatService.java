package com.bilgeadam.enterprise.service;

import com.bilgeadam.enterprise.dto.request.*;
import com.bilgeadam.enterprise.dto.response.*;
import com.bilgeadam.enterprise.entity.*;
import com.bilgeadam.enterprise.exception.EnterpriseException;
import com.bilgeadam.enterprise.exception.ErrorType;
import com.bilgeadam.enterprise.manager.UserManagementManager;
import com.bilgeadam.enterprise.repository.*;
import com.bilgeadam.enterprise.utility.JwtManager;
import com.bilgeadam.enterprise.view.ChatUserInfo;
import com.bilgeadam.enterprise.view.ChatView;
import com.bilgeadam.enterprise.view.MessageView;
import com.bilgeadam.enterprise.view.UserView;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
	private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
	private final ChatRepository chatRepository;
	private final MessageRepository messageRepository;
	private final ChatUserRepository chatUserRepository;
	private final UserManagementManager manager;
	private final MessageUserRepository messageUserRepository;
	private final JwtManager jwtManager;
	
	
	public List<UserView> getUsers(Long userId) {
		try {
			UserDetailForChatResponse user = Optional.ofNullable(manager.getEmployeeDetailForChat(userId))
			                                         .map(ResponseEntity::getBody)
			                                         .map(BaseResponse::getData)
			                                         .orElseThrow(() -> new EnterpriseException(ErrorType.EXTERNAL_SERVICE_ERROR, "Employee details not available"));
			
			List<UserDetailForChatResponse> employees = Optional.ofNullable(manager.getEmployeesDetailByCompanyId(user.companyId(), userId))
			                                                    .map(ResponseEntity::getBody)
			                                                    .map(BaseResponse::getData)
			                                                    .orElseThrow(() -> new EnterpriseException(ErrorType.EXTERNAL_SERVICE_ERROR, "Company employee details not available"));
			
			return employees.stream()
			                .filter(u -> !u.id().equals(userId))
			                .map(u -> new UserView(
					                u.id(),
					                u.name(),
					                u.surname(),
					                u.isOnline(),
					                u.avatarUrl()))
			                .toList();
			
		} catch (EnterpriseException e) {
			logger.error("EnterpriseException: {}", e.getMessage());
			return Collections.emptyList();
		} catch (Exception e) {
			logger.error("Unexpected error fetching user details: {}", e.getMessage());
			return Collections.emptyList();
		}
	}
	
	
	
	@Transactional
	public GroupChatCreateResponseDto createNewGroupChat(CreateGroupChatRqDto dto, Long creatorId) {
		try {
			List<UserDetailForChatResponse> users = Optional.ofNullable(manager.getEmployeesDetailByIds(dto.userIds()))
			                                                .map(ResponseEntity::getBody)
			                                                .map(BaseResponse::getData)
			                                                .orElseThrow(() -> new EnterpriseException(ErrorType.EXTERNAL_SERVICE_ERROR, "User details not available"));
			
			if (users.size() != dto.userIds().size()) {
				throw new EnterpriseException(ErrorType.USER_NOT_FOUND, "Some users were not found");
			}
			
			Chat chat = Chat.builder()
			                .name(dto.name().isBlank() ? "New Group Chat" : dto.name())
			                .description(dto.description())
			                .eChatType(EChatType.GROUP)
			                .chatImage(dto.chatImageUrl())
			                .build();
			
			chatRepository.save(chat);
			
			Set<Long> allUserIds = new HashSet<>(dto.userIds());
			allUserIds.add(creatorId);
			
			List<ChatUser> chatUsers = allUserIds.stream()
			                                     .map(userId -> ChatUser.builder()
			                                                            .chatId(chat.getId())
			                                                            .userId(userId)
			                                                            .isDeletedFromUser(false)
			                                                            .build())
			                                     .toList();
			
			try {
				chatUserRepository.saveAll(chatUsers);
			} catch (Exception e) {
				logger.error("Error while saving chat users: {}", e.getMessage());
				throw new EnterpriseException(ErrorType.DATABASE_ERROR, "Failed to save chat users");
			}
			
			return new GroupChatCreateResponseDto(
					chat.getId(),
					chat.getName(),
					chat.getDescription(),
					chat.getCreateDate(),
					chat.getChatImage()
			);
			
		} catch (EnterpriseException e) {
			logger.error("EnterpriseException: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Unexpected error in createNewGroupChat: {}", e.getMessage());
			throw new EnterpriseException(ErrorType.UNEXPECTED_ERROR, "An unexpected error occurred while creating chat");
		}
	}
	
	
	
	
	
	@Transactional
	public PrivateChatResponseDto createPrivateChat(CreatePrivateChatRqDto dto, Long userId, boolean isSupportChat) {
		try {
			List<Long> userIds = List.of(dto.recipientId(), userId);
			
			List<UserDetailForChatResponse> users = Optional.ofNullable(manager.getEmployeesDetailByIds(userIds))
			                                                .map(ResponseEntity::getBody)
			                                                .map(BaseResponse::getData)
			                                                .orElseThrow(() -> new EnterpriseException(ErrorType.EXTERNAL_SERVICE_ERROR, "User details not available"));
			
			UserDetailForChatResponse recipient = users.stream()
			                                           .filter(u -> !u.id().equals(userId))
			                                           .findFirst()
			                                           .orElseThrow(() -> new EnterpriseException(ErrorType.USER_NOT_FOUND, "Recipient not found"));
			
			String recipientName = recipient.name() + " " + recipient.surname();
			UserView recipientUser = new UserView(
					recipient.id(),
					recipient.name(),
					recipient.surname(),
					recipient.isOnline(),
					recipient.avatarUrl()
			);
			
			Optional<Chat> existingChat = chatRepository.findPrivateChatByUsers(userIds, userIds.size(), EChatType.PRIVATE);
			if (existingChat.isPresent()) {
				Chat chat = existingChat.get();
				return new PrivateChatResponseDto(chat.getId(), recipientName, recipientUser, chat.isSupportChat());
			}
			
			Chat newChat = Chat.builder()
			                   .eChatType(EChatType.PRIVATE)
			                   .isSupportChat(isSupportChat)
			                   .build();

			try {
				chatRepository.save(newChat);
				List<ChatUser> chatUsers = userIds.stream()
				                                  .map(uid -> ChatUser.builder()
				                                                      .chatId(newChat.getId())
				                                                      .userId(uid)
				                                                      .isDeletedFromUser(false)
				                                                      .build())
				                                  .toList();
				
				chatUserRepository.saveAll(chatUsers);
			} catch (Exception e) {
				logger.error("Error saving private chat: {}", e.getMessage());
				throw new EnterpriseException(ErrorType.DATABASE_ERROR, "Failed to create private chat");
			}

			return new PrivateChatResponseDto(newChat.getId(), recipientName, recipientUser, isSupportChat);
			
		} catch (EnterpriseException e) {
			logger.error("EnterpriseException: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Unexpected error in createPrivateChat: {}", e.getMessage());
			throw new EnterpriseException(ErrorType.UNEXPECTED_ERROR, "An unexpected error occurred while creating chat");
		}
	}
	
	@Transactional
	public NewMessageResponseDto sendNewMessage(NewMessageDto newMessageDto, String token) {
		try {
			Long userId = getIdFromTokenValidation(token);
			
			UserDetailForChatResponse user = Optional.ofNullable(manager.getEmployeeDetailForChat(userId))
			                                         .map(ResponseEntity::getBody)
			                                         .map(BaseResponse::getData)
			                                         .orElseThrow(() -> new EnterpriseException(ErrorType.EXTERNAL_SERVICE_ERROR, "Failed to retrieve user details"));

			Chat chat = chatRepository.findChatByIdWithAtLeastOneUser(newMessageDto.chatId())
			                          .orElseThrow(() -> new EnterpriseException(ErrorType.CHAT_NOT_FOUND));
			
			if (!chatUserRepository.existsByChatIdAndUserId(newMessageDto.chatId(), userId)) {
				throw new EnterpriseException(ErrorType.USER_NOT_PARTICIPANT);
			}

			Message message = Message.builder()
			                         .content(newMessageDto.content())
			                         .senderId(userId)
			                         .chatId(newMessageDto.chatId())
			                         .messageStatus(EMessageStatus.SENT)
			                         .timeStamp(LocalDateTime.now())
			                         .build();

			try {
				messageRepository.save(message);

				List<Long> targetUserIdList = chatUserRepository.findUserIdsByChatId(newMessageDto.chatId());
				
				List<MessageUser> messageUsers = targetUserIdList.stream()
				                                                 .filter(recipientId -> !recipientId.equals(userId)) // Kendisine mesaj kaydı eklenmesin
				                                                 .map(recipientId -> MessageUser.builder()
				                                                                                .messageId(message.getId())
				                                                                                .senderId(userId)
				                                                                                .targetId(recipientId)
				                                                                                .isDeletedFromUser(false)
				                                                                                .messageStatus(EMessageStatus.SENT)
				                                                                                .build())
				                                                 .toList();
				
				messageUserRepository.saveAll(messageUsers);
				
			} catch (Exception e) {
				logger.error("Error saving message: {}", e.getMessage());
				throw new EnterpriseException(ErrorType.DATABASE_ERROR, "Failed to save message");
			}

			UserView senderView = new UserView(
					user.id(),
					user.name(),
					user.surname(),
					user.isOnline(),
					user.avatarUrl()
			);

			return new NewMessageResponseDto(
					newMessageDto.chatId(),
					message.getId(),
					message.getContent(),
					senderView,
					message.getTimeStamp(),
					message.getMessageStatus()
			);
			
		} catch (EnterpriseException e) {
			logger.error("EnterpriseException: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Unexpected error in sendNewMessage: {}", e.getMessage());
			throw new EnterpriseException(ErrorType.UNEXPECTED_ERROR, "An unexpected error occurred while sending message");
		}
	}
	
	
	public List<ChatListViewDto> getUsersChats(Long userId, int limit) {
		List<ChatListViewDto> chats = chatRepository.findTopChatsByUser(userId, limit);
		
		// Grup chat'lerinde duplicate kayıtları engellemek için chatId ile tekilleştirme yapalım
		Map<String, ChatListViewDto> uniqueChats = chats.stream()
		                                                .collect(Collectors.toMap(ChatListViewDto::chatId, Function.identity(), (chat1, chat2) -> chat1));
		chats = new ArrayList<>(uniqueChats.values());
		
		// Private chatlerde karşı tarafın kullanıcı bilgisine ihtiyaç duyuluyor.
		List<Long> missingUserIds = chats.stream()
		                                 .filter(chat -> chat.chatType() == EChatType.PRIVATE)
		                                 .map(ChatListViewDto::userId)
		                                 .filter(Objects::nonNull)
		                                 .distinct()
		                                 .collect(Collectors.toList());
		
		// Kullanıcı detaylarını çekelim (örneğin ad, soyad, online durumu gibi bilgileri içeren nesne)
		Map<Long, UserDetailForChatResponse> userDetailsMap = new HashMap<>();
		if (!missingUserIds.isEmpty()) {
			try {
				List<UserDetailForChatResponse> users = Optional.ofNullable(manager.getEmployeesDetailByIds(missingUserIds))
				                                                .map(ResponseEntity::getBody)
				                                                .map(BaseResponse::getData)
				                                                .orElseThrow(() -> new EnterpriseException(ErrorType.EXTERNAL_SERVICE_ERROR, "Failed to fetch employee details"));
				users.forEach(user -> userDetailsMap.put(user.id(), user));
			} catch (Exception e) {
				logger.error("Error fetching employee details: {}", e.getMessage());
			}
		}
		
		// Online durumu haritası oluşturma
		final Map<Long, Boolean> onlineStatusMap = new HashMap<>();
		userDetailsMap.forEach((id, userDetail) -> onlineStatusMap.put(id, userDetail.isOnline()));
		
		// Chat listesi üzerinden dönerek, private chat'lerin chatName'ini güncelleyelim
		return chats.stream()
		            .map(chat -> {
			            String chatName = chat.chatName();
			            if (chat.chatType() == EChatType.PRIVATE && (chatName == null || chatName.isEmpty())) {
				            UserDetailForChatResponse detail = userDetailsMap.get(chat.userId());
				            if (detail != null) {
					            chatName = detail.name() + " " + detail.surname();
				            }
			            }
			            return new ChatListViewDto(
					            chat.chatId(),
					            chat.chatType(),
					            chatName,
					            chat.lastMessageDate(),
					            chat.lastMessage(),
					            chat.chatImage(),
					            onlineStatusMap.getOrDefault(chat.userId(), false),
					            chat.isSupportChat(),
					            chat.userId()
			            );
		            })
		            .collect(Collectors.toList());
	}
	
	
	
	
	
	
	
	
	@Transactional
	public Set<UserView> addUsersToChat(Long userId, AddUsersToGroupChatDto addUsersToGroupChatDto) {
		Chat chat = chatRepository.findChatByIdWithAtLeastOneUser(addUsersToGroupChatDto.chatId())
		                          .orElseThrow(() -> new EnterpriseException(ErrorType.CHAT_NOT_FOUND));
		
		if (!chatUserRepository.existsByChatIdAndUserId(chat.getId(), userId)) {
			throw new EnterpriseException(ErrorType.USER_NOT_PARTICIPANT);
		}
		
		Set<Long> newUserIds = new HashSet<>(addUsersToGroupChatDto.userIds());
		Set<Long> existingUserIds = new HashSet<>(chatUserRepository.findUserIdsByChatId(chat.getId()));
		
		newUserIds.removeAll(existingUserIds);
		
		if (newUserIds.isEmpty()) {
			throw new EnterpriseException(ErrorType.USER_ALREADY_IN_CHAT, "All users are already in the chat.");
		}
		
		List<ChatUser> chatUsers = newUserIds.stream()
		                                     .map(newUserId -> ChatUser.builder().chatId(chat.getId()).userId(newUserId).build())
		                                     .toList();
		
		try {
			chatUserRepository.saveAll(chatUsers);
		} catch (Exception e) {
			logger.error("Error saving chat users: {}", e.getMessage());
			throw new EnterpriseException(ErrorType.INTERNAL_SERVER_ERROR, "Failed to add users to chat");
		}
		
		List<UserDetailForChatResponse> addedUsers;
		
		try {
			ResponseEntity<BaseResponse<List<UserDetailForChatResponse>>> response =
					manager.getEmployeesDetailByIds(new ArrayList<>(newUserIds));
			
			if (response.getBody() != null && response.getBody().getData() != null) {
				addedUsers = response.getBody().getData();
			} else {
				logger.warn("Failed to fetch employee details, returning empty list");
				addedUsers = Collections.emptyList();
			}
		} catch (Exception e) {
			logger.error("Error fetching employee details: {}", e.getMessage());
			addedUsers = Collections.emptyList();
		}
		
		return addedUsers.stream()
		                 .map(user -> new UserView(user.id(), user.name(), user.surname(), user.isOnline(), user.avatarUrl()))
		                 .collect(Collectors.toSet());
	}

	
	
	
/*	@Transactional
	public void deleteChat(DeleteChatRqDto dto, String userId) {
		Chat chat = chatRepository.findChatById(dto.chatId())
		                          .orElseThrow(() -> new EnterpriseException(ErrorType.CHAT_NOT_FOUND));

		boolean isParticipant = chatUserRepository.existsByChatIdAndUserId(chat.getId(), userId);
		if (!isParticipant) {
			throw new EnterpriseException(ErrorType.USER_NOT_PARTICIPANT);
		}
		chatUserRepository.findByUserIdAndChatId(dto.chatId(),userId);
		chat.setDeleted(true);
		chatRepository.save(chat);
	} */
	
	
	
/*	@Transactional
	public void deleteMessage(DeleteMessageRqDto dto, String userId) {
		Message message = messageRepository.findMessageById(dto.messageId())
		                                   .orElseThrow(() -> new EnterpriseException(ErrorType.MESSAGE_NOT_FOUND));

		if (!message.getSenderId().equals(userId)) {
			throw new EnterpriseException(ErrorType.USER_NOT_AUTHORIZED);
		}

		message.setDeleted(true);
		messageRepository.save(message);
	} */
	
	
	@Transactional
	public Set<UserView> getUsersInChat(String chatId, Long userId) {
		boolean chatExists = chatRepository.existsById(chatId);
		if (!chatExists) {
			throw new EnterpriseException(ErrorType.CHAT_NOT_FOUND);
		}
		
		boolean isParticipant = chatUserRepository.existsByChatIdAndUserId(chatId, userId);
		if (!isParticipant) {
			throw new EnterpriseException(ErrorType.USER_NOT_PARTICIPANT);
		}

		List<Long> userIds = chatUserRepository.findUserIdsByChatId(chatId);
		
		ResponseEntity<BaseResponse<List<UserDetailForChatResponse>>> response =
				manager.getEmployeesDetailByIds(userIds);
		if(response.getBody()!=null && response.getBody().getData()!=null)
			throw new EnterpriseException(ErrorType.EXTERNAL_SERVICE_ERROR, "Employee details not available");
		List<UserDetailForChatResponse> users = response.getBody().getData();
		
		return users.stream()
		            .map(user -> new UserView(user.id(), user.name(), user.surname(), user.isOnline(), user.avatarUrl()))
		            .collect(Collectors.toSet());
	}
	
	
	@Transactional
	public void updateChatDetails(UpdateChatDetailsDto dto, Long userId) {
		Optional<Chat> chatById = chatRepository.findChatById(dto.chatId());
		if (chatById.isEmpty()) {
			throw new EnterpriseException(ErrorType.CHAT_NOT_FOUND);
		}
		
		Chat chat = chatById.get();
		
		if (!chat.getEChatType().equals(EChatType.GROUP)) {
			throw new EnterpriseException(ErrorType.INVALID_CHAT_TYPE);
		}
		
		boolean isParticipant = chatUserRepository.existsByChatIdAndUserId(chat.getId(), userId);
		if (!isParticipant) {
			throw new EnterpriseException(ErrorType.USER_NOT_PARTICIPANT);
		}
		
		chat.setName(dto.name());
		chat.setDescription(dto.description());
		chatRepository.save(chat);
	}
	
	
	@Transactional
	public ChatDetailResponseDto getChatDetails(int size, String chatId, Long userId) {
		Chat chat = chatRepository.findChatById(chatId)
		                          .orElseThrow(() -> new EnterpriseException(ErrorType.CHAT_NOT_FOUND));
		
		if (!chatUserRepository.existsByChatIdAndUserId(chat.getId(), userId)) {
			throw new EnterpriseException(ErrorType.USER_NOT_PARTICIPANT);
		}
		
		List<Message> messages = messageRepository.findLastMessagesByChatId(chat.getId(), size);
		List<Long> userIds = chatUserRepository.findUserIdsByChatId(chat.getId());
		
		List<UserDetailForChatResponse> users;
		
		try {
			ResponseEntity<BaseResponse<List<UserDetailForChatResponse>>> response =
					manager.getEmployeesDetailByIds(userIds);
			
			if (response.getBody() == null || response.getBody().getData() == null) {
				throw new EnterpriseException(ErrorType.EXTERNAL_SERVICE_ERROR, "Failed to fetch employee details");
			}
			
			users = response.getBody().getData();
		} catch (Exception e) {
			logger.error("Error fetching employee details: {}", e.getMessage());
			users = Collections.emptyList();
		}
		
		Map<Long, UserDetailForChatResponse> userMap = users.stream()
		                                                    .collect(Collectors.toMap(UserDetailForChatResponse::id, user -> user));
		
		List<MessageView> messageViews = messages.stream()
		                                         .map(message -> {
			                                         UserDetailForChatResponse sender = userMap.getOrDefault(
					                                         message.getSenderId(),
					                                         new UserDetailForChatResponse(-1L, -1L, "Unknown", "Unknown", null,false)
			                                         );
			                                         
			                                         return new MessageView(
					                                         message.getId(),
					                                         message.getContent(),
					                                         message.getSenderId(),
					                                         sender.name(),
					                                         sender.surname(),
					                                         message.getTimeStamp(),
					                                         message.getChatId()
			                                         );
		                                         })
		                                         .toList();
		
		List<UserView> participants = userIds.stream()
		                                     .map(uid -> {
			                                     UserDetailForChatResponse user = userMap.getOrDefault(
					                                     uid,
					                                     new UserDetailForChatResponse(-1L, -1L, "Unknown", "Unknown", null, false)
			                                     );
			                                     
			                                     return new UserView(
					                                     user.id(),
					                                     user.name(),
					                                     user.surname(),
					                                     user.isOnline(),
					                                     user.avatarUrl()
			                                     );
		                                     })
		                                     .toList();
		
		return new ChatDetailResponseDto(
				chat.getId(),
				chat.getName(),
				chat.getDescription(),
				participants,
				messageViews,
				chat.getChatImage(),
				userId,
				chat.getEChatType()
		);
	}
	
	
	public ChatView getChatInfo(String chatId) {
		List<ChatUserInfo> infos = chatRepository.findChatUserInfoByChatId(chatId);
		
		if (infos.isEmpty()) {
			throw new EnterpriseException(ErrorType.CHAT_NOT_FOUND);
		}

		List<Long> userIds = infos.stream()
		                          .map(ChatUserInfo::userId)
		                          .toList();
		
		List<UserDetailForChatResponse> users;
		
		try {
			ResponseEntity<BaseResponse<List<UserDetailForChatResponse>>> response =
					manager.getEmployeesDetailByIds(userIds);
			
			if (response.getBody() == null || response.getBody().getData() == null) {
				throw new EnterpriseException(ErrorType.EXTERNAL_SERVICE_ERROR, "Failed to fetch employee details");
			}
			
			users = response.getBody().getData();
		} catch (Exception e) {
			logger.error("Error fetching employee details: {}", e.getMessage());
			users = Collections.emptyList();
		}

		Map<Long, UserDetailForChatResponse> userMap = users.stream()
		                                                    .collect(Collectors.toMap(UserDetailForChatResponse::id, user -> user));

		List<UserView> userViews = userIds.stream()
		                                  .map(uid -> {
			                                  UserDetailForChatResponse user = userMap.getOrDefault(
					                                  uid,
					                                  new UserDetailForChatResponse(-1L, -1L, "Unknown", "Unknown", "", false)
			                                  );
			                                  
			                                  return new UserView(
					                                  user.id(),
					                                  user.name(),
					                                  user.surname(),
					                                  user.isOnline(),
					                                  user.avatarUrl()
			                                  );
		                                  })
		                                  .toList();
		
		ChatUserInfo first = infos.get(0);
		
		return new ChatView(userViews, first.createdAt(), first.description(), first.name());
	}
	
	
	
	
	public List<MessageView> getChatMessagesBefore(String chatId, LocalDateTime lastTimestamp, Pageable pageable) {
		LocalDateTime effectiveTimestamp = (lastTimestamp != null) ? lastTimestamp : LocalDateTime.now().plusYears(100);

		List<Message> messages = messageRepository.findMessagesByChatIdBefore(chatId, pageable, effectiveTimestamp);

		List<Long> senderIds = messages.stream()
		                               .map(Message::getSenderId)
		                               .distinct()
		                               .toList();

		List<UserDetailForChatResponse> users;
		try {
			ResponseEntity<BaseResponse<List<UserDetailForChatResponse>>> response =
					manager.getEmployeesDetailByIds(senderIds);
			
			if (response.getBody() == null || response.getBody().getData() == null) {
				throw new EnterpriseException(ErrorType.EXTERNAL_SERVICE_ERROR, "Failed to fetch employee details");
			}
			
			users = response.getBody().getData();
		} catch (Exception e) {
			logger.error("Error fetching employee details: {}", e.getMessage());
			users = Collections.emptyList();
		}

		Map<Long, UserDetailForChatResponse> userMap = users.stream()
		                                                    .collect(Collectors.toMap(UserDetailForChatResponse::id, user -> user));

		return messages.stream()
		               .map(message -> {
			               UserDetailForChatResponse sender = userMap.getOrDefault(
					               message.getSenderId(),
					               new UserDetailForChatResponse(-1L, -1L, "Unknown", "Unknown", " ", false)
			               );
			               
			               return new MessageView(
					               message.getId(),
					               message.getContent(),
					               message.getSenderId(),
					               sender.name(),
					               sender.surname(),
					               message.getTimeStamp(),
					               message.getChatId()
			               );
		               })
		               .toList();
	}

	

/*	@Transactional
	public PrivateChatResponseDto createSupportChat(String userId) {
		List<User> allSupport = userRepository.findAllByIsSupport(true);

		if (allSupport.isEmpty()) {
			throw new EnterpriseException(ErrorType.SUPPORT_NOT_FOUND);
		}

		// get a random support from the list
		int randomNumber = (int) Math.random()*allSupport.size();
		User randomSupport = allSupport.get(randomNumber);
		CreatePrivateChatRqDto createPrivateChatRqDto = new CreatePrivateChatRqDto(randomSupport.getId());

		return createPrivateChat(createPrivateChatRqDto, userId, true);
	} */
	
	public Long getIdFromTokenValidation(String token){
		Optional<Long> optionalId = jwtManager.validateToken(token);
		if(optionalId.isEmpty())
			throw new EnterpriseException(ErrorType.USER_NOT_AUTHORIZED);
		return optionalId.get();
	}
	
	@Transactional
	public Boolean setMessageStatus(Long userId, String messageId) {
		Optional<MessageUser> byMessageIdAndTargetId =
				messageUserRepository.findByMessageIdAndTargetIdAndMessageStatus(messageId, userId,EMessageStatus.SENT);
		if(byMessageIdAndTargetId.isEmpty())
			throw new EnterpriseException(ErrorType.MESSAGE_NOT_FOUND);
		MessageUser messageUser = byMessageIdAndTargetId.get();
		messageUser.setMessageStatus(EMessageStatus.READ);
		messageUserRepository.save(messageUser);
		return true;
	}
}