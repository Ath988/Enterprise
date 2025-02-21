package com.bilgeadam.ticketservice.mapper;

import com.bilgeadam.ticketservice.dto.request.AddTicketRequest;
import com.bilgeadam.ticketservice.dto.request.RespondToTicketRequest;
import com.bilgeadam.ticketservice.entity.Ticket;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {
    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);

    Ticket addTicketRequestToTicket(AddTicketRequest dto, Long userId);

    @Mapping(target = "ticketStatus", source = "dto.ticketStatus")
    @Mapping(target = "response", source = "dto.response")
    @Mapping(target = "respondingUserId", source = "respondingUserId")
    Ticket responseToTicket(@Valid RespondToTicketRequest dto, Ticket ticket, Long respondingUserId);
}
