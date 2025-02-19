package com.bilgeadam.ticketservice.repository;


import com.bilgeadam.ticketservice.entity.Ticket;
import com.bilgeadam.ticketservice.entity.enums.EntityStatus;
import com.bilgeadam.ticketservice.entity.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, String> {

    List<Ticket> findAllByUserIdAndEntityStatus(Long userId, EntityStatus entityStatus);

    List<Ticket> findAllByTicketStatusAndEntityStatus(TicketStatus ticketStatus, EntityStatus entityStatus);
}
