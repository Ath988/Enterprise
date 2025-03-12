package com.bilgeadam.enterprise.service;

import com.bilgeadam.enterprise.dto.request.*;
import com.bilgeadam.enterprise.dto.response.*;
import com.bilgeadam.enterprise.entity.*;
import com.bilgeadam.enterprise.exception.EnterpriseException;
import com.bilgeadam.enterprise.exception.ErrorType;
import com.bilgeadam.enterprise.repository.ChatRepository;
import com.bilgeadam.enterprise.repository.ChatUserRepository;
import com.bilgeadam.enterprise.repository.MessageRepository;
import com.bilgeadam.enterprise.repository.UserRepository;
import com.bilgeadam.enterprise.utility.JwtManager;
import com.bilgeadam.enterprise.view.ChatUserInfo;
import com.bilgeadam.enterprise.view.ChatView;
import com.bilgeadam.enterprise.view.MessageView;
import com.bilgeadam.enterprise.view.UserView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
	private final ChatRepository chatRepository;
	private final JwtManager jwtManager;
	private final UserRepository userRepository;
	private final MessageRepository messageRepository;
	private final ChatUserRepository chatUserRepository;
	
	
	public String login(LoginRqDto dto){
		Optional<String> optId =
				userRepository.findIdByUsernameAndPassword(dto.mail(), dto.password());
		if(optId.isEmpty())
			throw new EnterpriseException(ErrorType.USER_NOT_FOUND);
		return jwtManager.createToken(optId.get());
	}
	
	public List<UserView> getUsers(String userId) {
		return userRepository.findAll().stream()
		                     .filter(user -> !user.getId().equals(userId))
		                     .map(user -> new UserView(
				                     user.getId(),
				                     user.getName(),
				                     user.getSurname(),
				                     user.getIsOnline(),
				                     user.getProfilePicture()))
		                     .toList();
	}

	
	@Transactional
	public GroupChatCreateResponseDto createNewGroupChat(CreateGroupChatRqDto dto, String creatorId) {
		Set<User> users = userRepository.findUserByIdIn(dto.userIds());
		if (users.size() != dto.userIds().size()) {
			throw new EnterpriseException(ErrorType.USER_NOT_FOUND);
		}
		
		Chat chat = Chat.builder()
		                .name(dto.name().isBlank() ? "New Group Chat" : dto.name())
		                .description(dto.description())
		                .eChatType(EChatType.GROUP)
						.chatImage(dto.chatImageUrl())
		                .build();
		
		chatRepository.save(chat);

		Set<String> allUserIds = new HashSet<>(dto.userIds());
		allUserIds.add(creatorId);
		
		List<ChatUser> chatUsers = allUserIds.stream()
		                                     .map(userId -> ChatUser.builder()
		                                                            .chatId(chat.getId())
		                                                            .userId(userId)
				                                                    .isDeletedFromUser(false)
		                                                            .build())
		                                     .toList();
		
		
		chatUserRepository.saveAll(chatUsers);
		
		return new GroupChatCreateResponseDto(chat.getId(), chat.getName(), chat.getDescription(), chat.getCreateDate(), chat.getChatImage());
	}
	
	
	
	
	@Transactional
	public PrivateChatResponseDto createPrivateChat(CreatePrivateChatRqDto dto, String userId) {
		
		Set<String> userIds = new HashSet<>();
		userIds.add(dto.recipientId());
		userIds.add(userId);

		List<Object[]> userResults = userRepository.findUserNamesByIds(userIds);
		Map<String, String[]> userMap = userResults.stream()
		                                           .collect(Collectors.toMap(
				                                           row -> (String) row[0],
				                                           row -> new String[]{(String) row[1], (String) row[2]}
		                                           ));
		
		String recipientName = userMap.entrySet().stream()
		                              .filter(entry -> !entry.getKey().equals(userId))
		                              .map(entry -> entry.getValue()[0] + " " + entry.getValue()[1])
		                              .findFirst()
		                              .orElse("Unknown");
		
		
		Optional<String> recipientIdOptional = userMap.keySet().stream()
		                                              .filter(id -> !id.equals(userId))
		                                              .findFirst();
		
		String recipientId = recipientIdOptional.orElseThrow(() ->
				                                                     new EnterpriseException(ErrorType.USER_NOT_PARTICIPANT)
		);
		
		User userById = userRepository.findUserById(recipientId).orElseThrow(()->new EnterpriseException(ErrorType.USER_NOT_FOUND));
		
		UserView recipientUser = new UserView(userById.getId(), userById.getName(), userById.getSurname(), userById.getIsOnline(), userById.getProfilePicture());
		
		Optional<Chat> existingChat = chatRepository.findPrivateChatByUsers(userIds, userIds.size(), EChatType.PRIVATE);
		
		if (existingChat.isPresent()) {
			Chat chat = existingChat.get();
			
			
			return new PrivateChatResponseDto(chat.getId(), recipientName, recipientUser);
		}
		
		Chat newChat = Chat.builder()
		                   .eChatType(EChatType.PRIVATE)
		                   .build();
		chatRepository.save(newChat);
		
		List<ChatUser> chatUsers = userIds.stream()
		                                  .map(userIdVal -> ChatUser.builder()
		                                                            .chatId(newChat.getId())
		                                                            .userId(userIdVal)
				                                                    .isDeletedFromUser(false)
		                                                            .build())
		                                  .toList();
		chatUserRepository.saveAll(chatUsers);
		
		return new PrivateChatResponseDto(
				newChat.getId(),
				recipientName,
				recipientUser
		);
	}
	
	
	
	
	
	
	@Transactional
	public NewMessageResponseDto sendNewMessage(NewMessageDto newMessageDto, String token) {
		String userId = getIdFromTokenValidation(token);
		
		Optional<User> userById = userRepository.findUserById(userId);
		if (userById.isEmpty()) {
			throw new EnterpriseException(ErrorType.USER_NOT_FOUND);
		}
		
		Optional<Chat> chatById = chatRepository.findChatByIdWithAtLeastOneUser(newMessageDto.chatId());
		if (chatById.isEmpty()) {
			throw new EnterpriseException(ErrorType.CHAT_NOT_FOUND);
		}
		
		boolean isParticipant = chatUserRepository.existsByChatIdAndUserId(newMessageDto.chatId(), userId);
		if (!isParticipant) {
			throw new EnterpriseException(ErrorType.USER_NOT_PARTICIPANT);
		}
		
		Message message = Message.builder()
		                         .content(newMessageDto.content())
		                         .senderId(userId)
		                         .chatId(newMessageDto.chatId())
		                         .messageStatus(EMessageStatus.SENT)
		                         .timeStamp(LocalDateTime.now())
		                         .build();
		
		messageRepository.save(message);
		
		// Grup sohbetinde, mesajı gönderen dışındaki her kullanıcı için MessageUser kaydı oluşturuluyor.
		List<String> targetUserIdList = chatUserRepository.findUserIdsByChatId(newMessageDto.chatId());
		for (String recipientId : targetUserIdList) {
			if (!recipientId.equals(userId)) {  // Doğru string karşılaştırması
				MessageUser messageUser = MessageUser.builder()
				                                     .messageId(message.getId())
				                                     .senderId(userId)      // Mesajı gönderenin ID'si
				                                     .targetId(recipientId) // Alıcının ID'si
				                                     .isDeletedFromUser(false)
				                                     .messageStatus(EMessageStatus.SENT)
				                                     .build();
				messageUserRepository.save(messageUser);  // Kaydetmeyi unutmayın
			}
		}
		
		
		User sender = userById.get();
		UserView senderView = new UserView(
				sender.getId(),
				sender.getName(),
				sender.getSurname(),
				sender.getIsOnline(),
				sender.getProfilePicture()
		);
		
		return new NewMessageResponseDto(newMessageDto.chatId(),
				message.getId(),
				message.getContent(),
				senderView,
				message.getTimeStamp(),
				message.getMessageStatus()
		);
	}
	
	public List<ChatListViewDto> getUsersChats(String userId, int limit) {
		Optional<User> userById = userRepository.findUserById(userId);
		if (userById.isEmpty())
			throw new EnterpriseException(ErrorType.USER_NOT_FOUND);
		System.out.println(chatRepository.findTopChatsByUser(userId, limit));
		return chatRepository.findTopChatsByUser(userId, limit);
	}
	
	
	@Transactional
	public Set<UserView> addUsersToChat(String userId, AddUsersToGroupChatDto addUsersToGroupChatDto) {
		Chat chat = chatRepository.findChatByIdWithAtLeastOneUser(addUsersToGroupChatDto.chatId())
		                          .orElseThrow(() -> new EnterpriseException(ErrorType.CHAT_NOT_FOUND));

		if (!chatUserRepository.existsByChatIdAndUserId(chat.getId(), userId)) {
			throw new EnterpriseException(ErrorType.USER_NOT_PARTICIPANT);
		}

		Set<String> newUserIds = new HashSet<>(addUsersToGroupChatDto.userIds());

		Set<String> existingUserIds = new HashSet<>(chatUserRepository.findUserIdsByChatId(chat.getId()));

		newUserIds.removeAll(existingUserIds);

		if (newUserIds.isEmpty()) {
			throw new EnterpriseException(ErrorType.USER_ALREADY_IN_CHAT, "All users are already in the chat.");
		}

		boolean areUsersValid = userRepository.existsAllUsers(newUserIds, newUserIds.size());
		if (!areUsersValid) {
			throw new EnterpriseException(ErrorType.USER_NOT_FOUND, "Some users do not exist in the system.");
		}

		List<ChatUser> chatUsers = newUserIds.stream()
		                                     .map(newUserId -> ChatUser.builder().chatId(chat.getId()).userId(newUserId).build())
		                                     .toList();
		
		try {
			chatUserRepository.saveAll(chatUsers);
		} catch (Exception e) {
			System.out.println("Hata: " + e.getMessage());
			throw e;
		}
		Set<User> addedUsers = userRepository.findUserByIdIn(newUserIds);
		return addedUsers.stream()
		                 .map(user -> new UserView(user.getId(), user.getName(), user.getSurname(), user.getIsOnline(), user.getProfilePicture()))
		                 .collect(Collectors.toSet());
	}
	
	
	
	@Transactional
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
	}
	
	
	
	@Transactional
	public void deleteMessage(DeleteMessageRqDto dto, String userId) {
		Message message = messageRepository.findMessageById(dto.messageId())
		                                   .orElseThrow(() -> new EnterpriseException(ErrorType.MESSAGE_NOT_FOUND));

		if (!message.getSenderId().equals(userId)) {
			throw new EnterpriseException(ErrorType.USER_NOT_AUTHORIZED);
		}

		message.setDeleted(true);
		messageRepository.save(message);
	}
	
	
	@Transactional
	public Set<UserView> getUsersInChat(String chatId, String userId) {
		boolean chatExists = chatRepository.existsById(chatId);
		if (!chatExists) {
			throw new EnterpriseException(ErrorType.CHAT_NOT_FOUND);
		}
		
		boolean isParticipant = chatUserRepository.existsByChatIdAndUserId(chatId, userId);
		if (!isParticipant) {
			throw new EnterpriseException(ErrorType.USER_NOT_PARTICIPANT);
		}

		List<String> userIds = chatUserRepository.findUserIdsByChatId(chatId);

		List<User> users = userRepository.findUsersByIds(userIds);

		return users.stream()
		            .map(user -> new UserView(user.getId(), user.getName(), user.getSurname(), user.getIsOnline(), user.getProfilePicture()))
		            .collect(Collectors.toSet());
	}
	
	
	@Transactional
	public void updateChatDetails(UpdateChatDetailsDto dto, String userId) {
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
	public ChatDetailResponseDto getChatDetails(int size, String chatId, String userId) {
		Chat chat = chatRepository.findChatById(chatId)
		                          .orElseThrow(() -> new EnterpriseException(ErrorType.CHAT_NOT_FOUND));
		
		if (!chatUserRepository.existsByChatIdAndUserId(chat.getId(), userId)) {
			throw new EnterpriseException(ErrorType.USER_NOT_PARTICIPANT);
		}
		
		List<Message> messages = messageRepository.findLastMessagesByChatId(chat.getId(), size);
		
		List<String> userIds = chatUserRepository.findUserIdsByChatId(chat.getId());
		
		Map<String, Object[]> userMap = userRepository.findUserNamesByIds(new HashSet<>(userIds))
		                                              .stream()
		                                              .collect(Collectors.toMap(
				                                              row -> (String) row[0],
				                                              row -> new Object[]{
						                                              (String) row[1],       // firstName
						                                              (String) row[2],       // lastName
						                                              (Boolean) row[3],      // isOnline
						                                              (String) row[4]        // profilePicture
				                                              }
		                                              ));
		
		List<MessageView> messageViews = messages.stream()
		                                         .map(message -> new MessageView(
				                                         message.getId(),
				                                         message.getContent(),
				                                         message.getSenderId(),
				                                         (String) userMap.getOrDefault(
						                                         message.getSenderId(),
						                                         new Object[]{"Unknown", "Unknown", false, ""}
				                                         )[0],
				                                         (String) userMap.getOrDefault(
						                                         message.getSenderId(),
						                                         new Object[]{"Unknown", "Unknown", false, ""}
				                                         )[1],
				                                         message.getTimeStamp(),
				                                         message.getChatId()
		                                         ))
		                                         .toList();
		
		
		List<UserView> participants = userIds.stream()
		                                     .map(uid -> new UserView(
				                                     uid,
				                                     (String) userMap.getOrDefault(
						                                     uid,
						                                     new Object[]{"Unknown", "Unknown", false, ""}
				                                     )[0],
				                                     (String) userMap.getOrDefault(
						                                     uid,
						                                     new Object[]{"Unknown", "Unknown", false, ""}
				                                     )[1],
				                                     (Boolean) userMap.getOrDefault(
						                                     uid,
						                                     new Object[]{"Unknown", "Unknown", false, ""}
				                                     )[2],
				                                     (String) userMap.getOrDefault(
						                                     uid,
						                                     new Object[]{"Unknown", "Unknown", false, ""}
				                                     )[3]
		                                     ))
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
		
		ChatUserInfo first = infos.get(0);
		List<UserView> userViews = infos.stream()
		                                .map(info -> new UserView(
				                                info.userId(),
				                                info.userName(),
				                                info.userSurname(),
				                                info.isOnline(),
				                                info.profilePicture()))
		                                .collect(Collectors.toList());
		
		return new ChatView(userViews, first.createdAt(), first.description(), first.name());
	}


	
	public String getIdFromTokenValidation(String token){
		Optional<String> optionalId = jwtManager.validateToken(token);
		if(optionalId.isEmpty())
			throw new EnterpriseException(ErrorType.USER_NOT_AUTHORIZED);
		return optionalId.get();
	}
	
	
	public List<MessageView> getChatMessagesBefore(String chatId, LocalDateTime lastTimestamp, Pageable pageable) {
		LocalDateTime effectiveTimestamp = lastTimestamp != null ? lastTimestamp : LocalDateTime.now().plusYears(100);
		return messageRepository.findMessagesByChatIdBefore(chatId, pageable, effectiveTimestamp);
	}
	
	
	public void setUserOnlineStatus(String userId, boolean status) {
		System.out.println("metoda giriyor mu?"+userId);
		Optional<User> userById = userRepository.findUserById(userId);
		if(userById.isEmpty())
			throw new EnterpriseException(ErrorType.USER_NOT_FOUND);
		User user = userById.get();
		user.setIsOnline(status);
		userRepository.save(user);
	}
}