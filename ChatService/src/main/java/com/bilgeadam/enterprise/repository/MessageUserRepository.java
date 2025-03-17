package com.bilgeadam.enterprise.repository;

import com.bilgeadam.enterprise.entity.EMessageStatus;
import com.bilgeadam.enterprise.entity.MessageUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageUserRepository extends JpaRepository<MessageUser,String> {
	
	Optional<MessageUser> findByMessageIdAndTargetIdAndMessageStatus(String messageId, Long targetId,
	                                                                  EMessageStatus status);
}