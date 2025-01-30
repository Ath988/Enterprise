package com.bilgeadam.enterprise;

import com.bilgeadam.enterprise.dto.request.CreateGroupChatRqDto;
import com.bilgeadam.enterprise.dto.response.GroupChatCreateResponseDto;
import com.bilgeadam.enterprise.entity.Chat;
import com.bilgeadam.enterprise.entity.User;
import com.bilgeadam.enterprise.exception.EnterpriseException;
import com.bilgeadam.enterprise.exception.ErrorType;
import com.bilgeadam.enterprise.repository.ChatRepository;
import com.bilgeadam.enterprise.repository.UserRepository;
import com.bilgeadam.enterprise.service.ChatService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatServiceTest {
	
	@Mock
	private ChatRepository chatRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private ChatService chatService;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void shouldCreateNewGroupChatSuccessfully() {
		Set<String> userIds = Set.of("user1-chatId", "user2-chatId");
		User user1 = new User("user1-chatId", "test1@example.com", "test", "User1", "User1", true);
		User user2 = new User("user2-chatId", "test2@example.com", "Test", "User2", "User2", true);
		
		Mockito.when(userRepository.findUserByIdIn(userIds)).thenReturn(Set.of(user1, user2));
		
		CreateGroupChatRqDto requestDto = new CreateGroupChatRqDto("Test Group", "Description", userIds);

		Mockito.doAnswer(invocation -> {
			Chat chat = invocation.getArgument(0);
			chat.setId("chat-chatId"); // ID manuel olarak set ediliyor
			return chat;
		}).when(chatRepository).save(ArgumentMatchers.any(Chat.class));
		
		GroupChatCreateResponseDto response = chatService.createNewGroupChat(requestDto, "user1-chatId");
		
		assertNotNull(response);
		assertEquals("chat-chatId", response.id());
		assertEquals("Test Group", response.name());
		assertEquals("Description", response.description());

		verify(userRepository, times(1)).findUserByIdIn(userIds);
		verify(chatRepository, times(1)).save(ArgumentMatchers.any(Chat.class));
	}
	
	@Test
	void shouldThrowExceptionWhenUserNotFound() {
		Set<String> invalidUserIds = Set.of("invalid1", "invalid2");
		
		CreateGroupChatRqDto requestDto = new CreateGroupChatRqDto("Test Group","Test Description",invalidUserIds);

		Mockito.when(userRepository.findUserByIdIn(invalidUserIds)).thenReturn(Set.of()); // Boş set döndür
		
		EnterpriseException exception = Assertions.assertThrows(EnterpriseException.class,
		                                                        () -> chatService.createNewGroupChat(requestDto, "admin-chatId"));
		
		assertEquals(ErrorType.USER_NOT_FOUND, exception.getErrorType());
		Mockito.verify(userRepository, Mockito.times(1)).findUserByIdIn(invalidUserIds);
		Mockito.verifyNoInteractions(chatRepository);
	}
	
	@Test
	void shouldThrowExceptionWhenUsersMismatch() {
		Set<String> userIds = Set.of("user1-chatId", "user2-chatId", "user3-chatId");
		User user1 = new User("user1-chatId", "test1@example.com", "Test", "User1","User1", true);
		User user2 = new User("user2-chatId", "test2@example.com", "Test", "User2","User2", true);
		
		Mockito.when(userRepository.findUserByIdIn(userIds)).thenReturn(Set.of(user1, user2));
		
		CreateGroupChatRqDto requestDto = new CreateGroupChatRqDto("Test Group","Test Description",userIds);

		EnterpriseException exception = Assertions.assertThrows(EnterpriseException.class,
		                                                        () -> chatService.createNewGroupChat(requestDto, "admin-chatId"));
		
		assertEquals(ErrorType.USER_NOT_FOUND, exception.getErrorType());
		Mockito.verify(userRepository, Mockito.times(1)).findUserByIdIn(userIds);
		Mockito.verifyNoInteractions(chatRepository);
	}
}