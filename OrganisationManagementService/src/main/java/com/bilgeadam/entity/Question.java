package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.ECategory;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_question")
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long employeeId;
    private String text;
    private LocalDateTime date;
    @ElementCollection
    private List<Long> replyIds = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private ECategory eCategory;

}
