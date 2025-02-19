package com.bilgeadam.ticketservice.mapper;

import com.bilgeadam.ticketservice.dto.request.AddTicketRequest;
import com.bilgeadam.ticketservice.dto.request.RespondToTicketRequest;
import com.bilgeadam.ticketservice.entity.Ticket;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {
    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);

    Ticket addTicketRequestToTicket(AddTicketRequest dto, Long userId);

    // TODO mapper'ı yaz
    Ticket responseToTicket(@Valid RespondToTicketRequest dto, Ticket ticket);
}
