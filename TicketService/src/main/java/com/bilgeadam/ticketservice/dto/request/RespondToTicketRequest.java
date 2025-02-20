package com.bilgeadam.ticketservice.dto.request;

import com.bilgeadam.ticketservice.entity.enums.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RespondToTicketRequest(
        @NotEmpty
        String token,
        @NotEmpty
        String ticketId,
        @NotNull
        TicketStatus ticketStatus,
        @NotBlank
        String response
) {

}
