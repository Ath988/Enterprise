package com.bilgeadam.repository;

import com.bilgeadam.entity.AnnouncementIsRead;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnnouncementIsReadRepository extends JpaRepository<AnnouncementIsRead,Long> {

    List<AnnouncementIsRead> findAllByAnnouncementIdAndReceiverId(Long announcementId, Long receiverId);

}
