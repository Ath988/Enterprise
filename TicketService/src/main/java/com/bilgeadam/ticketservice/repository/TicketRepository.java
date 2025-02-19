package com.bilgeadam.ticketservice.repository;


import com.bilgeadam.ticketservice.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, String> {

}
