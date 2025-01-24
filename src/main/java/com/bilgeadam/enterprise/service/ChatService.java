package com.bilgeadam.enterprise.service;

import com.bilgeadam.enterprise.dto.request.AddUserToChatDto;
import com.bilgeadam.enterprise.dto.request.CreateChatDto;
import com.bilgeadam.enterprise.dto.request.NewMessageDto;
import com.bilgeadam.enterprise.dto.response.ChatCreateResponseDto;
import com.bilgeadam.enterprise.dto.response.ChatResponseDto;
import com.bilgeadam.enterprise.dto.response.NewMessageResponseDto;
import com.bilgeadam.enterprise.entity.*;
import com.bilgeadam.enterprise.exception.EnterpriseException;
import com.bilgeadam.enterprise.exception.ErrorType;
import com.bilgeadam.enterprise.repository.ChatRepository;
import com.bilgeadam.enterprise.repository.MessageRepository;
import com.bilgeadam.enterprise.repository.UserRepository;
import com.bilgeadam.enterprise.utility.JwtManager;
import com.bilgeadam.enterprise.view.ChatListView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatService {
	private final ChatRepository chatRepository;
	private final JwtManager jwtManager;
	private final UserRepository userRepository;
	private final MessageRepository messageRepository;
	
	public ChatCreateResponseDto createNewChat(CreateChatDto dto, String token){
		Optional<String> optionalId = jwtManager.validateToken(token);
		if(optionalId.isEmpty())
			throw new EnterpriseException(ErrorType.USER_NOT_AUTHORIZED);
		Chat chat = Chat.builder().name(dto.name().isEmpty() ? )
		    .description(dto.description())
		    .eChatType(dto.chatUserIds().size()>1 ? EChatType.GROUP : EChatType.PRIVATE)
		    .users(userRepository.findUserByIdIn(dto.chatUserIds()))
		    .build();
		chatRepository.save(chat);
		return new ChatCreateResponseDto(chat.getId(),chat.getName(),chat.getDescription(),chat.getCreateDate());
	}
	
	public NewMessageResponseDto sendNewMessage(NewMessageDto newMessageDto, String token){
		Optional<String> optionalId = jwtManager.validateToken(token);
		if(optionalId.isEmpty())
			throw new EnterpriseException(ErrorType.USER_NOT_AUTHORIZED);
		Optional<User> userById = userRepository.findUserById(optionalId.get());
		if(userById.isEmpty())
			throw new EnterpriseException(ErrorType.USER_NOT_FOUND);
		Optional<Chat> chatById = chatRepository.findChatById(newMessageDto.chatId());
		if(chatById.isEmpty())
			throw new EnterpriseException(ErrorType.CHAT_NOT_FOUND);
		Message message = Message.builder().content(newMessageDto.content())
				.sender(userById.get())
				.chat(chatById.get())
				.messageStatus(EMessageStatus.SENT)
				.build();
		messageRepository.save(message);
		return new NewMessageResponseDto(message.getId(), message.getTimeStamp(), message.getMessageStatus());
	}
	
	public List<ChatListView> getUsersChats(String token){
		Optional<String> optionalId = jwtManager.validateToken(token);
		if(optionalId.isEmpty())
			throw new EnterpriseException(ErrorType.USER_NOT_AUTHORIZED);
		Optional<User> userById = userRepository.findUserById(optionalId.get());
		if(userById.isEmpty())
			throw new EnterpriseException(ErrorType.USER_NOT_FOUND);
		return chatRepository.findChatListViewByUserOrderByCreateDateDesc(userById.get());
	}
	
	public void addUsersToChat(String token, AddUserToChatDto addUserToChatDto){
		Optional<String> optionalId = jwtManager.validateToken(token);
		if(optionalId.isEmpty())
			throw new EnterpriseException(ErrorType.USER_NOT_AUTHORIZED);
		Optional<Chat> chatById = chatRepository.findChatById(addUserToChatDto.id());
		if(chatById.isEmpty())
			throw new EnterpriseException(ErrorType.CHAT_NOT_FOUND);
		Chat chat = chatById.get();
		Set<User> users = chat.getUsers();
		addUserToChatDto.users().forEach(userId -> {
			Optional<User> user = userRepository.findById(userId);
			if (user.isPresent()) {
				users.add(user.get());
			} else {
				throw new EnterpriseException(ErrorType.USER_NOT_FOUND);
			}
		});
		if(users.size()>1)
			chat.setEChatType(EChatType.GROUP);
		
		chatRepository.save(chat);
		
	}
}