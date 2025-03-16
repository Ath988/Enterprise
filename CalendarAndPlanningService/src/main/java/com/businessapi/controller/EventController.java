package com.businessapi.controller;

import com.businessapi.constants.EndPoints;
import com.businessapi.dto.request.EventDeleteRequestDTO;
import com.businessapi.dto.request.EventSaveRequestDTO;
import com.businessapi.dto.request.EventUpdateRequestDTO;
import com.businessapi.dto.response.FindAllResponseDTO;
import com.businessapi.dto.response.ResponseDTO;
import com.businessapi.entity.Event;
import com.businessapi.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.businessapi.constants.EndPoints.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(EndPoints.EVENT)
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT,RequestMethod.DELETE})
public class EventController {
    private final EventService eventService;

    @PostMapping(SAVE)
    @Operation(summary = "Create event", description = "Create event")
    public ResponseEntity<ResponseDTO<Boolean>> save(@RequestBody EventSaveRequestDTO eventSaveRequestDTO) {

        return ResponseEntity.ok(ResponseDTO.<Boolean>builder()
                .data(eventService.save(eventSaveRequestDTO))
                .code(200)
                .message("Event saved successfully").build());

    }

    @PutMapping(UPDATE)
    @Operation(summary = "Update event by token",description = "Update event by token")
    public ResponseEntity<ResponseDTO<Boolean>> update(@RequestBody EventUpdateRequestDTO eventUpdateRequestDTO) {
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder()
                .data(eventService.update(eventUpdateRequestDTO))
                .code(200)
                .message("Event updated successfully").build());

    }

    @PutMapping(DELETE)
    @Operation(summary = "Delete event by token",description = "Delete event by token")
    public ResponseEntity<ResponseDTO<Boolean>> deleteById(@RequestBody EventDeleteRequestDTO eventDeleteRequestDTO) {
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder()
                .data(eventService.delete(eventDeleteRequestDTO))
                .code(200)
                .message("Event deleted successfully").build());
    }

    @GetMapping(FIND_ALL_BY_USER_ID)
    @Operation(summary = "Find all events by user id", description = "Find all events by user id")
    public ResponseEntity<ResponseDTO<List<FindAllResponseDTO>>> findAll(@RequestParam String token) {
        return ResponseEntity.ok(ResponseDTO.<List<FindAllResponseDTO>>builder()
                .data(eventService.findAllByUserId(token))
                .code(200)
                .message("Events found successfully")
                .build());
    }

    @GetMapping(FIND_BY_ID)
    @Operation(summary = "Find event by id", description = "Find event by id")
    public ResponseEntity<ResponseDTO<FindAllResponseDTO>> findbyId(@RequestParam String id) {
        return ResponseEntity.ok(ResponseDTO.<FindAllResponseDTO>builder()
                .data(eventService.findById(id))
                .code(200)
                .message("Event found successfully")
                .build());
    }
}
