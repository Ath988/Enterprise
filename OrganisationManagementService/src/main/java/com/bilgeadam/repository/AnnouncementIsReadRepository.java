package com.bilgeadam.repository;

import com.bilgeadam.dto.response.EmployeeReadAnnouncementResponseDto;
import com.bilgeadam.entity.Announcement;
import com.bilgeadam.entity.AnnouncementIsRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AnnouncementIsReadRepository extends JpaRepository<AnnouncementIsRead, Long> {

    List<AnnouncementIsRead> findAllByAnnouncementIdAndReceiverId(Long announcementId, Long receiverId);

    @Query("SELECT a FROM Announcement a WHERE a.id IN " +
            "(SELECT ar.announcementId FROM AnnouncementIsRead ar WHERE ar.receiverId = :employeeId AND ar.isRead = true)")
    List<Announcement> findReadAnnouncementsByEmployeeId(@Param("employeeId") Long employeeId);

    @Query("SELECT NEW com.bilgeadam.dto.response.EmployeeReadAnnouncementResponseDto(ar.announcementId, e.firstName, e.lastName) " +
            "FROM AnnouncementIsRead ar " +
            "JOIN Employee e ON ar.employeeId = e.id " +
            " WHERE ar.isRead = true")
    List<EmployeeReadAnnouncementResponseDto> findAllReaders();


   List<AnnouncementIsRead> findByAnnouncementId(Long announcementId);
}
