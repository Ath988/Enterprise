package com.bilgeadam.ticketservice.dto.request;

import com.bilgeadam.ticketservice.entity.enums.TicketType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AddTicketRequest(
        @NotEmpty
        String token,
        @NotNull
        TicketType ticketType,
        @NotEmpty
        String title,
        @NotEmpty
        String description
) {
}
