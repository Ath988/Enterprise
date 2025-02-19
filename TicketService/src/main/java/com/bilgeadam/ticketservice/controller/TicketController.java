package com.bilgeadam.ticketservice.controller;

import com.bilgeadam.ticketservice.dto.request.AddTicketRequest;
import com.bilgeadam.ticketservice.dto.response.BaseResponse;
import com.bilgeadam.ticketservice.entity.Ticket;
import com.bilgeadam.ticketservice.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                .message("subscription updated")
                .build());
    }

}
