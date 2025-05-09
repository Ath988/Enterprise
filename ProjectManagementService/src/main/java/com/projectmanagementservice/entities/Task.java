package com.projectmanagementservice.entities;
import com.projectmanagementservice.utility.ETaskPriorityStatus;
import com.projectmanagementservice.utility.ETaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tbltask")
public class Task extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long authId; //Daha sonra giriş yapan kullanıcıdan bilgiler alınarak kaydedilecek.
    Long projectId;
    String name;
    String description;
    @Enumerated(EnumType.STRING)
    ETaskStatus taskStatus;
    @Enumerated(EnumType.STRING)
    ETaskPriorityStatus taskPriorityStatus;
}