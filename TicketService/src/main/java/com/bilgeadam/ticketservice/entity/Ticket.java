package com.bilgeadam.ticketservice.entity;

import com.bilgeadam.ticketservice.entity.enums.TicketStatus;
import com.bilgeadam.ticketservice.entity.enums.TicketType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Entity
@Table(name = "Ticket")
public class Ticket extends BaseEntity{
    TicketType ticketType;
    String title;
    String description;
    @Builder.Default
    TicketStatus ticketStatus = TicketStatus.PENDING;
    Long userId;
}
