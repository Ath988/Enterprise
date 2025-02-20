package com.bilgeadam.ticketservice.service;

import com.bilgeadam.ticketservice.dto.request.AddTicketRequest;
import com.bilgeadam.ticketservice.dto.request.RespondToTicketRequest;
import com.bilgeadam.ticketservice.entity.Ticket;
import com.bilgeadam.ticketservice.entity.enums.EntityStatus;
import com.bilgeadam.ticketservice.entity.enums.TicketStatus;
import com.bilgeadam.ticketservice.exception.EnterpriseException;
import com.bilgeadam.ticketservice.exception.ErrorType;
import com.bilgeadam.ticketservice.mapper.TicketMapper;
import com.bilgeadam.ticketservice.repository.TicketRepository;
import com.bilgeadam.ticketservice.utility.JwtManager;
import com.bilgeadam.ticketservice.utility.enums.ERole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

        Ticket ticket = TicketMapper.INSTANCE.addTicketRequestToTicket(dto, userId);

        return ticketRepository.save(ticket);
    }

    public List<Ticket> getTicketsByToken(String token) {
        Long userId = tokenToUserId(token);
        return ticketRepository.findAllByUserIdAndEntityStatus(userId, EntityStatus.ACTIVE);
    }

    public List<Ticket> getAllTickets(String token) {
        if (jwtManager.validateSystemAdmin(token)) {
            return ticketRepository.findAllByTicketStatusAndEntityStatus(TicketStatus.PENDING, EntityStatus.ACTIVE);
        }
        else throw new EnterpriseException(ErrorType.UNAUTHORIZED);
    }

    public Ticket respondToTicket(@Valid RespondToTicketRequest dto) {
        Long systemAdminId = jwtManager.getIdFromTokenIfSystemAdmin(dto.token());
        Ticket ticket = getTicketById(dto.ticketId());
        ticket = TicketMapper.INSTANCE.responseToTicket(dto, ticket, systemAdminId);
        return ticketRepository.save(ticket);
    }

    private Ticket getTicketById(String id){
        Optional<Ticket> optTicket = ticketRepository.findById(id);
        if (optTicket.isPresent()) return optTicket.get();
        else throw new EnterpriseException(ErrorType.TICKET_NOT_FOUND);
    }

    public String createToken(Long userId, ERole role) {
        Optional<String> optToken = jwtManager.createToken(userId, role);
        if (optToken.isPresent()) {
            return optToken.get();
        }
        else throw  new EnterpriseException(ErrorType.INTERNAL_SERVER_ERROR);
    }
}
