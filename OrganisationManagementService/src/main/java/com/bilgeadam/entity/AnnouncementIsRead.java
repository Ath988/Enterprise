package com.bilgeadam.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@EqualsAndHashCode(callSuper = true )
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "tbl_announcementisread")
public class AnnouncementIsRead extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long employeeId;
    Long announcementId;
    Long receiverId;
    Boolean isRead;


}
