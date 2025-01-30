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
import com.bilgeadam.enterprise.view.MessageView;
import com.bilgeadam.enterprise.view.UserView;
import lombok.RequiredArgsConstructor;
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
	
	@Transactional
	public GroupChatCreateResponseDto createNewGroupChat(CreateGroupChatRqDto dto) {
		Set<User> users = userRepository.findUserByIdIn(dto.userIds());
		
		if (users.size() != dto.userIds().size()) {
			throw new EnterpriseException(ErrorType.USER_NOT_FOUND);
		}
		
		Chat chat = Chat.builder()
		                .name(dto.name().isBlank() ? "New Group Chat" : dto.name())
		                .description(dto.description())
		                .eChatType(EChatType.GROUP)
		                .build();
		
		chatRepository.save(chat);

		Set<String> allUserIds = new HashSet<>(dto.userIds());
		allUserIds.add(dto.creatorId());
		
		List<ChatUser> chatUsers = allUserIds.stream()
		                                     .map(userId -> ChatUser.builder()
		                                                            .chatId(chat.getId())
		                                                            .userId(userId)
		                                                            .build())
		                                     .toList();
		
		
		chatUserRepository.saveAll(chatUsers);
		
		return new GroupChatCreateResponseDto(chat.getId(), chat.getName(), chat.getDescription(), chat.getCreateDate());
	}
	
	
	
	
	@Transactional
	public PrivateChatResponseDto createPrivateChat(CreatePrivateChatRqDto dto, String userId) {
		
		if (dto.userIds().size() != 2)
			throw new EnterpriseException(ErrorType.INVALID_CHAT_PARTICIPANTS);
		
		if (!dto.userIds().contains(userId))
			throw new EnterpriseException(ErrorType.USER_NOT_PARTICIPANT);
		
		Set<User> users = userRepository.findUserByIdIn(dto.userIds());
		
		String recipientName = users.stream()
		                            .filter(user -> !user.getId().equals(userId))
		                            .map(user -> user.getName() + " " + user.getSurname())
		                            .findFirst()
		                            .orElse("Unknown");
		
		Optional<Chat> existingChat = chatRepository.findPrivateChatByUsers(
				dto.userIds(), dto.userIds().size(), EChatType.PRIVATE
		);
		
		if (existingChat.isPresent()) {
			Chat chat = existingChat.get();
			
			List<MessageView> lastTenMessages = messageRepository.findLastMessagesByChatId(chat.getId())
			                                                     .stream()
			                                                     .limit(10)
			                                                     .map(MessageView::fromEntity)
			                                                     .toList();
			
			return new PrivateChatResponseDto(chat.getId(), recipientName, lastTenMessages);
		}
		
		Chat newChat = Chat.builder()
		                   .eChatType(EChatType.PRIVATE)
		                   .build();
		chatRepository.save(newChat);
		
		List<ChatUser> chatUsers = dto.userIds().stream()
		                              .map(userIdVal -> ChatUser.builder()
		                                                        .chatId(newChat.getId())
		                                                        .userId(userIdVal)
		                                                        .build())
		                              .toList();
		chatUserRepository.saveAll(chatUsers);
		
		return new PrivateChatResponseDto(
				newChat.getId(),
				recipientName,
				new ArrayList<>()
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
		
		User sender = userById.get();
		UserView senderView = new UserView(
				sender.getId(),
				sender.getName(),
				sender.getSurname(),
				sender.getIsOnline()
		);
		
		return new NewMessageResponseDto(
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
		                                     .map(newUserId -> new ChatUser(UUID.randomUUID().toString(), chat.getId(), newUserId))
		                                     .toList();
		
		chatUserRepository.saveAll(chatUsers);
		Set<User> addedUsers = userRepository.findUserByIdIn(newUserIds);
		return addedUsers.stream()
		                 .map(user -> new UserView(user.getId(), user.getName(), user.getSurname(), user.getIsOnline()))
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
		            .map(user -> new UserView(user.getId(), user.getName(), user.getSurname(), user.getIsOnline()))
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
		Optional<Chat> chatById = chatRepository.findChatById(chatId);
		if (chatById.isEmpty()) {
			throw new EnterpriseException(ErrorType.CHAT_NOT_FOUND);
		}
		
		Chat chat = chatById.get();
		
		boolean isParticipant = chatUserRepository.existsByChatIdAndUserId(chat.getId(), userId);
		if (!isParticipant) {
			throw new EnterpriseException(ErrorType.USER_NOT_PARTICIPANT);
		}
		
		List<Message> messages = messageRepository.findLastMessagesByChatId(chat.getId())
		                                          .stream()
		                                          .limit(size)
		                                          .toList();
		
		List<MessageView> messageViews = messages.stream()
		                                         .map(MessageView::fromEntity)
		                                         .toList();
		
		List<String> userIds = chatUserRepository.findUserIdsByChatId(chat.getId());
		
		List<User> users = userRepository.findUsersByIds(userIds);
		
		List<UserView> participants = users.stream()
		                                   .map(user -> new UserView(user.getId(), user.getName(), user.getSurname(), user.getIsOnline()))
		                                   .toList();
		
		return new ChatDetailResponseDto(
				chat.getId(),
				chat.getName(),
				chat.getDescription(),
				participants,
				messageViews
		);
	}
	
	public String getIdFromTokenValidation(String token){
		Optional<String> optionalId = jwtManager.validateToken(token);
		if(optionalId.isEmpty())
			throw new EnterpriseException(ErrorType.USER_NOT_AUTHORIZED);
		return optionalId.get();
	}
	
	
}