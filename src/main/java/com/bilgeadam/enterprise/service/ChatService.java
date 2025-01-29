package com.bilgeadam.enterprise.service;

import com.bilgeadam.enterprise.dto.request.*;
import com.bilgeadam.enterprise.dto.response.*;
import com.bilgeadam.enterprise.entity.*;
import com.bilgeadam.enterprise.exception.EnterpriseException;
import com.bilgeadam.enterprise.exception.ErrorType;
import com.bilgeadam.enterprise.repository.ChatRepository;
import com.bilgeadam.enterprise.repository.MessageRepository;
import com.bilgeadam.enterprise.repository.UserRepository;
import com.bilgeadam.enterprise.utility.JwtManager;
import com.bilgeadam.enterprise.view.MessageView;
import com.bilgeadam.enterprise.view.UserView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
	private final ChatRepository chatRepository;
	private final JwtManager jwtManager;
	private final UserRepository userRepository;
	private final MessageRepository messageRepository;
	
	
	public String login(LoginRqDto dto){
		Optional<String> optId =
				userRepository.findIdByUsernameAndPassword(dto.mail(), dto.password());
		if(optId.isEmpty())
			throw new EnterpriseException(ErrorType.USER_NOT_FOUND);
		return jwtManager.createToken(optId.get());
	}
	
	@Transactional
	public GroupChatCreateResponseDto createNewGroupChat(CreateGroupChatRqDto dto, String userId) {
		Set<User> users = userRepository.findUserByIdIn(dto.users());
		
		Set<String> userIds = new HashSet<>(dto.users());
		userIds.add(userId);
		
		if (users.size() != userIds.size())
			throw new EnterpriseException(ErrorType.USER_NOT_FOUND);
		
		Chat chat = Chat.builder()
		                .name(dto.name().isBlank() ? "New Group Chat" : dto.name())
		                .description(dto.description())
		                .eChatType(EChatType.GROUP)
		                .users(users)
		                .build();
		
		chatRepository.save(chat);
		return new GroupChatCreateResponseDto(chat.getId(), chat.getName(), chat.getDescription(), chat.getCreateDate());
	}
	
	
	@Transactional
	public PrivateChatResponseDto createPrivateChat(CreatePrivateChatRqDto dto, String userId) {
		
		if (dto.users().size() != 2)
			throw new EnterpriseException(ErrorType.INVALID_CHAT_PARTICIPANTS);
		
		if (!dto.users().contains(userId))
			throw new EnterpriseException(ErrorType.USER_NOT_PARTICIPANT);
		
		Set<User> users = userRepository.findUserByIdIn(dto.users());
		
		String recipientName = users.stream()
		                            .filter(user -> !user.getId().equals(userId))
		                            .map(user -> user.getName() + " " + user.getSurname())
		                            .findFirst()
		                            .orElse("Unknown");
		
		Optional<Chat> existingChat = chatRepository.findPrivateChatByUsers(users, users.size(), EChatType.PRIVATE);
		
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
		                   .users(users)
		                   .build();
		
		chatRepository.save(newChat);
		
		return new PrivateChatResponseDto(
				newChat.getId(),
				recipientName,
				new ArrayList<>()
		);
	}
	
	
	
	@Transactional
	public NewMessageResponseDto sendNewMessage(NewMessageDto newMessageDto, String token){
		String userId = getIdFromTokenValidation(token);
		Optional<User> userById = userRepository.findUserById(userId);
		if(userById.isEmpty())
			throw new EnterpriseException(ErrorType.USER_NOT_FOUND);
		Optional<Chat> chatById = chatRepository.findChatWithUsersById(newMessageDto.chatId());
		if(chatById.isEmpty())
			throw new EnterpriseException(ErrorType.CHAT_NOT_FOUND);
		if (!chatById.get().getUsers().contains(userById.get()))
			throw new EnterpriseException(ErrorType.USER_NOT_PARTICIPANT);
		Message message = Message.builder().content(newMessageDto.content())
				.sender(userById.get())
				.chat(chatById.get())
				.messageStatus(EMessageStatus.SENT)
				.build();
		messageRepository.save(message);
		return new NewMessageResponseDto(message.getId(), message.getContent(), message.getSender(), message.getTimeStamp(), message.getMessageStatus());
	}
	
	public List<ChatListViewDto> getUsersChats(String userId, int limit) {
		Optional<User> userById = userRepository.findUserById(userId);
		if (userById.isEmpty())
			throw new EnterpriseException(ErrorType.USER_NOT_FOUND);
		
		return chatRepository.findTopChatsByUser(userId, limit);
	}
	
	
	
	
	
	public Set<User> addUsersToChat(String userId, AddUserToChatDto addUserToChatDto) {
		Optional<Chat> chatById = chatRepository.findChatWithUsersById(addUserToChatDto.chatId());
		if (chatById.isEmpty()) {
			throw new EnterpriseException(ErrorType.CHAT_NOT_FOUND);
		}
		Chat chat = chatById.get();
		
		Set<User> existingUsers = chat.getUsers();
		
		Set<User> newUsers = userRepository.findUserByIdIn(addUserToChatDto.users());
		
		if (newUsers.size() != addUserToChatDto.users().size()) {
			Set<String> foundUserIds = newUsers.stream()
			                                   .map(User::getId)
			                                   .collect(Collectors.toSet());
			
			Set<String> missingUserIds = addUserToChatDto.users().stream()
			                                             .filter(userIdx -> !foundUserIds.contains(userId))
			                                             .collect(Collectors.toSet());
			
			throw new EnterpriseException(ErrorType.USER_NOT_FOUND, "Missing users: " + missingUserIds);
		}
		
		Set<User> alreadyInChat = newUsers.stream()
		                                  .filter(existingUsers::contains)
		                                  .collect(Collectors.toSet());
		
		if (!alreadyInChat.isEmpty()) {
			Set<String> alreadyInChatIds = alreadyInChat.stream()
			                                            .map(User::getId)
			                                            .collect(Collectors.toSet());
			throw new EnterpriseException(ErrorType.USER_ALREADY_IN_CHAT, "Users already in chat: " + alreadyInChatIds);
		}
		
		existingUsers.addAll(newUsers);
		chatRepository.save(chat);
		return newUsers;
	}
	
	public void deleteChat(DeleteChatRqDto dto, String userId) {
		Optional<Chat> chatById = chatRepository.findChatWithUsersById(dto.chatId());
		if (chatById.isEmpty()) {
			throw new EnterpriseException(ErrorType.CHAT_NOT_FOUND);
		}
		
		Chat chat = chatById.get();
		if (!chat.getUsers().stream().anyMatch(user -> user.getId().equals(userId))) {
			throw new EnterpriseException(ErrorType.USER_NOT_PARTICIPANT);
		}
		
		chat.setDeleted(true);
		chatRepository.save(chat);
	}
	
	
	public void deleteMessage(DeleteMessageRqDto dto, String userId) {
		Optional<Message> messageById = messageRepository.findMessageById(dto.messageId());
		if (messageById.isEmpty()) {
			throw new EnterpriseException(ErrorType.MESSAGE_NOT_FOUND);
		}
		
		Message message = messageById.get();
		if (!message.getSender().getId().equals(userId)) {
			throw new EnterpriseException(ErrorType.USER_NOT_AUTHORIZED);
		}
		
		messageRepository.delete(message);
	}
	
	public Set<User> getUsersInChat(String chatId, String userId) {
		Optional<Chat> chatById = chatRepository.findChatWithUsersById(chatId);
		if (chatById.isEmpty()) {
			throw new EnterpriseException(ErrorType.CHAT_NOT_FOUND);
		}
		
		Chat chat = chatById.get();
		if (!chat.getUsers().stream().anyMatch(user -> user.getId().equals(userId))) {
			throw new EnterpriseException(ErrorType.USER_NOT_PARTICIPANT);
		}
		
		return chat.getUsers();
	}
	
	public void updateChatDetails(UpdateChatDetailsDto dto, String userId) {
		//SADECE GROUP CHAT'I ICIN GECERLI HALE GETIR
		
		Optional<Chat> chatById = chatRepository.findChatWithUsersById(dto.chatId());
		if (chatById.isEmpty()) {
			throw new EnterpriseException(ErrorType.CHAT_NOT_FOUND);
		}
		
		Chat chat = chatById.get();
		if (!chat.getUsers().stream().anyMatch(user -> user.getId().equals(userId))) {
			throw new EnterpriseException(ErrorType.USER_NOT_PARTICIPANT);
		}
		
		chat.setName(dto.name());
		chat.setDescription(dto.description());
		chatRepository.save(chat);
	}
	
	public ChatDetailResponseDto getChatDetails(int size, String chatId, String userId) {
		Optional<Chat> chatById = chatRepository.findChatWithUsersById(chatId);
		if (chatById.isEmpty()) {
			throw new EnterpriseException(ErrorType.CHAT_NOT_FOUND);
		}
		
		Chat chat = chatById.get();
		
		if (chat.getUsers().stream().noneMatch(user -> user.getId().equals(userId))) {
			throw new EnterpriseException(ErrorType.USER_NOT_PARTICIPANT);
		}
		
		List<Message> messages = messageRepository.findLastMessagesByChatId(chat.getId())
		                                          .stream()
		                                          .limit(size)
		                                          .toList();
		
		List<MessageView> messageViews = messages.stream()
		                                         .map(MessageView::fromEntity)
		                                         .toList();
		
		List<UserView> participants = chat.getUsers().stream()
		                                  .map(user -> new UserView(user.getId(), user.getName(), user.getSurname()))
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
	
	private Set<User> validateAndFetchUsers(Set<String> userIds) {
		Set<User> users = userRepository.findUserByIdIn(userIds);
		if (users.size() != userIds.size()) {
			throw new EnterpriseException(ErrorType.USER_NOT_FOUND, "Invalid users: " + userIds);
		}
		return users;
	}
	
	
	public boolean isUserInChat(String chatId,String userId){
		return chatRepository.isUserInChat(chatId,userId);
	}
	
}