package com.bilgeadam.enterprise.service;

import com.bilgeadam.enterprise.dto.request.*;
import com.bilgeadam.enterprise.dto.response.ChatDetailResponseDto;
import com.bilgeadam.enterprise.dto.response.GroupChatCreateResponseDto;
import com.bilgeadam.enterprise.dto.response.NewMessageResponseDto;
import com.bilgeadam.enterprise.dto.response.PrivateChatResponseDto;
import com.bilgeadam.enterprise.entity.*;
import com.bilgeadam.enterprise.exception.EnterpriseException;
import com.bilgeadam.enterprise.exception.ErrorType;
import com.bilgeadam.enterprise.repository.ChatRepository;
import com.bilgeadam.enterprise.repository.MessageRepository;
import com.bilgeadam.enterprise.repository.UserRepository;
import com.bilgeadam.enterprise.utility.JwtManager;
import com.bilgeadam.enterprise.view.ChatListView;
import com.bilgeadam.enterprise.view.UserView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
	public GroupChatCreateResponseDto createNewGroupChat(CreateGroupChatRqDto dto, String token){
		String userId = getIdFromTokenValidation(token);
		Set<User> users = userRepository.findUserByIdIn(dto.users());
		dto.users().add(userId);
		if (users.size() != dto.users().size())
			throw new EnterpriseException(ErrorType.USER_NOT_FOUND);
		Chat chat = Chat.builder().name(dto.name().isBlank() ? "New Group Chat" : dto.name())
		    .description(dto.description())
		    .eChatType(EChatType.GROUP)
		    .users(userRepository.findUserByIdIn(dto.users()))
		    .build();
		chatRepository.save(chat);
		return new GroupChatCreateResponseDto(chat.getId(), chat.getName(), chat.getDescription(), chat.getCreateDate());
	}
	
	@Transactional
	public PrivateChatResponseDto createPrivateChat(CreatePrivateChatRqDto dto, String token) {
		String userId = getIdFromTokenValidation(token);
		
		dto.users().add(userId);
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
		
		Optional<Chat> existingChat = chatRepository.findPrivateChatByUsers(users, users.size());
		if (existingChat.isPresent()) {
			Chat chat = existingChat.get();
			Pageable pageable = PageRequest.of(0, 10);
			List<Message> lastTenMessages = messageRepository.findLastMessagesByChatId(chat.getId(), pageable).getContent();
			
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
	
	public List<ChatListView> getUsersChats(String token){
		String userId = getIdFromTokenValidation(token);
		Optional<User> userById = userRepository.findUserById(userId);
		if(userById.isEmpty())
			throw new EnterpriseException(ErrorType.USER_NOT_FOUND);
		return chatRepository.findChatListViewByUserOrderByCreateDateDesc(userById.get());
	}
	
	
	public Set<User> addUsersToChat(String token, AddUserToChatDto addUserToChatDto) {
		Optional<String> optionalId = jwtManager.validateToken(token);
		if (optionalId.isEmpty()) {
			throw new EnterpriseException(ErrorType.USER_NOT_AUTHORIZED);
		}
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
			                                             .filter(userId -> !foundUserIds.contains(userId))
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
	
	public void deleteChat(String chatId, String token) {
		String userId = getIdFromTokenValidation(token);
		
		Optional<Chat> chatById = chatRepository.findChatWithUsersById(chatId);
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
	
	public void deleteMessage(String messageId, String token) {
		String userId = getIdFromTokenValidation(token);
		
		Optional<Message> messageById = messageRepository.findById(messageId);
		if (messageById.isEmpty()) {
			throw new EnterpriseException(ErrorType.MESSAGE_NOT_FOUND);
		}
		
		Message message = messageById.get();
		if (!message.getSender().getId().equals(userId)) {
			throw new EnterpriseException(ErrorType.USER_NOT_AUTHORIZED);
		}
		
		messageRepository.delete(message);
	}
	
	public Set<User> getUsersInChat(String chatId, String token) {
		String userId = getIdFromTokenValidation(token);
		
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
	
	public void updateChatDetails(UpdateChatDetailsDto dto, String token) {
		//SADECE GROUP CHAT'I ICIN GECERLI HALE GETIR
		String userId = getIdFromTokenValidation(token);
		
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
	
	public ChatDetailResponseDto getChatDetails(int page, int size, String chatId, String token) {
		String userId = getIdFromTokenValidation(token);
		
		Optional<Chat> chatById = chatRepository.findChatById(chatId);
		if (chatById.isEmpty()) {
			throw new EnterpriseException(ErrorType.CHAT_NOT_FOUND);
		}
		
		Chat chat = chatById.get();
		
		if (!chat.getUsers().stream().anyMatch(user -> user.getId().equals(userId))) {
			throw new EnterpriseException(ErrorType.USER_NOT_PARTICIPANT);
		}
		
		Pageable pageable = PageRequest.of(page, size, Sort.by("timeStamp").descending());
		List<Message> messages = messageRepository.findLastMessagesByChatId(chat.getId(), pageable).getContent();
		
		List<UserView> participants = chat.getUsers().stream()
		                                  .map(user -> new UserView(user.getId(), user.getName(), user.getSurname()))
		                                  .toList();
		
		return new ChatDetailResponseDto(
				chat.getId(),
				chat.getName(),
				chat.getDescription(),
				participants,
				messages
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