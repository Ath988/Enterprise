package com.bilgeadam.ticketservice.controller;

import com.bilgeadam.ticketservice.dto.request.AddTicketRequest;
import com.bilgeadam.ticketservice.dto.request.CancelMyTicketRequest;
import com.bilgeadam.ticketservice.dto.request.RespondToTicketRequest;
import com.bilgeadam.ticketservice.dto.response.BaseResponse;
import com.bilgeadam.ticketservice.entity.Ticket;
import com.bilgeadam.ticketservice.exception.EnterpriseException;
import com.bilgeadam.ticketservice.exception.ErrorType;
import com.bilgeadam.ticketservice.service.TicketService;
import com.bilgeadam.ticketservice.utility.JwtManager;
import com.bilgeadam.ticketservice.utility.enums.ERole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.bilgeadam.ticketservice.constant.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(TICKET)
@CrossOrigin("*")
public class TicketController {
    private final TicketService ticketService;
    private final JwtManager jwtManager = new JwtManager();

    @PostMapping(ADD)
    public ResponseEntity<BaseResponse<Ticket>> addTicket(@RequestBody @Valid AddTicketRequest dto) {
        return ResponseEntity.ok(BaseResponse.<Ticket>builder()
                .data(ticketService.addTicket(dto))
                .success(true)
                .code(200)
                .message("successsfully added a new ticket")
                .build());
    }

    @GetMapping(TICKETS_BY_TOKEN)
    public ResponseEntity<BaseResponse<List<Ticket>>> getTicketsForMember(String token){
        return ResponseEntity.ok(BaseResponse.<List<Ticket>>builder()
                .data(ticketService.getTicketsByToken(token))
                .success(true)
                .code(200)
                .message("successfully fetched all tickets of the user")
                .build());
    }

    @GetMapping(ALL_TICKETS)
    public ResponseEntity<BaseResponse<List<Ticket>>> getAllTickets(String token){
        return ResponseEntity.ok(BaseResponse.<List<Ticket>>builder()
                .data(ticketService.getAllTickets(token))
                .success(true)
                .code(200)
                .message("successfully fetched all pending tickets")
                .build());
    }

    @PostMapping(RESPOND)
    public ResponseEntity<BaseResponse<Ticket>> respondToTicket(@RequestBody @Valid RespondToTicketRequest dto) {
        return ResponseEntity.ok(BaseResponse.<Ticket>builder()
                .data(ticketService.respondToTicket(dto))
                .success(true)
                .code(200)
                .message("successsfully responded to the ticket")
                .build());
    }

    @PostMapping(CANCEL_MY_TICKET)
    public ResponseEntity<BaseResponse<Ticket>> cancelMyTicket(@RequestHeader String Authorization, @RequestBody @Valid CancelMyTicketRequest dto) {
        String token = Authorization.replace("Bearer ", "");
        Optional<Long> optUserId = jwtManager.getIdFromToken(token);
        if (optUserId.isEmpty()) throw new EnterpriseException(ErrorType.INVALID_TOKEN);
        else {
            Long userId = optUserId.get();
            return ResponseEntity.ok(BaseResponse.<Ticket>builder()
                    .data(ticketService.cancelMyTicket(dto, userId))
                    .success(true)
                    .code(200)
                    .message("successsfully responded to the ticket")
                    .build());
        }

    }
}
