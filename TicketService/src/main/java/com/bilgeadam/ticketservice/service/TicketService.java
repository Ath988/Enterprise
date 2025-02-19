package com.bilgeadam.ticketservice.service;

import com.bilgeadam.ticketservice.dto.request.AddTicketRequest;
import com.bilgeadam.ticketservice.entity.Ticket;
import com.bilgeadam.ticketservice.exception.EnterpriseException;
import com.bilgeadam.ticketservice.exception.ErrorType;
import com.bilgeadam.ticketservice.repository.TicketRepository;
import com.bilgeadam.ticketservice.utility.JwtManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final JwtManager jwtManager = new JwtManager();

    private Long tokenToUserId(String token) {
        Optional<Long> optUserId = jwtManager.getIdFromToken(token);
        if (optUserId.isPresent()) {
            return optUserId.get();
        }
        else throw new EnterpriseException(ErrorType.INVALID_TOKEN);
    }

    public Ticket addTicket(@Valid AddTicketRequest dto) {
        Long userId = tokenToUserId(dto.token());

    }
}
