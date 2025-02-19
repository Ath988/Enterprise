package com.bilgeadam.ticketservice.controller;

import com.bilgeadam.ticketservice.dto.request.AddTicketRequest;
import com.bilgeadam.ticketservice.dto.request.RespondToTicketRequest;
import com.bilgeadam.ticketservice.dto.response.BaseResponse;
import com.bilgeadam.ticketservice.entity.Ticket;
import com.bilgeadam.ticketservice.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.ticketservice.constant.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(TICKET)
@CrossOrigin("*")
public class TicketController {
    private final TicketService ticketService;


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
                .message("successsfully added a new ticket")
                .build());
    }
}
