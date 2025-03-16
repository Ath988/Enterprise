package com.businessapi.service;

import com.businessapi.dto.request.EventDeleteRequestDTO;
import com.businessapi.dto.request.EventSaveRequestDTO;
import com.businessapi.dto.request.EventUpdateRequestDTO;
import com.businessapi.dto.response.FindAllResponseDTO;
import com.businessapi.entity.Event;
import com.businessapi.entity.enums.EStatus;
import com.businessapi.exception.CalendarAndPlannigServiceException;
import com.businessapi.exception.ErrorType;
import com.businessapi.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EventService {
    private final EventRepository eventRepository;
    private final RabbitTemplate rabbitTemplate;

    public Boolean save(EventSaveRequestDTO eventSaveRequestDTO) {


        Long userId =(Long) rabbitTemplate.convertSendAndReceive("businessDirectExchange", "keyGetUserIdByToken", eventSaveRequestDTO.token());

        Event event = Event.builder()
                .userId(userId)
                .title(eventSaveRequestDTO.title())
                .startTime(eventSaveRequestDTO.startTime())
                .endTime(eventSaveRequestDTO.endTime())
                .allDay(eventSaveRequestDTO.allDay())
                .build();
        eventRepository.save(event);
        return true;
    }

    public Boolean update(EventUpdateRequestDTO eventUpdateRequestDTO) {
        Event event = eventRepository.findById(eventUpdateRequestDTO.id()).orElseThrow(() -> new CalendarAndPlannigServiceException(ErrorType.EVENT_NOT_FOUND));

        Long userId =(Long) rabbitTemplate.convertSendAndReceive("businessDirectExchange", "keyGetUserIdByToken", eventUpdateRequestDTO.token());

        if (!event.getUserId().equals(userId)) {
            throw new CalendarAndPlannigServiceException(ErrorType.BAD_REQUEST_ERROR);
        }

        if (event.getStatus().equals(EStatus.ACTIVE)) {
            event.setTitle(eventUpdateRequestDTO.title() != null ? eventUpdateRequestDTO.title() : event.getTitle());
            event.setStartTime(eventUpdateRequestDTO.startTime() != null ? eventUpdateRequestDTO.startTime() : event.getStartTime());
            event.setEndTime(eventUpdateRequestDTO.endTime() != null ? eventUpdateRequestDTO.endTime() : event.getEndTime());
            eventRepository.save(event);
        }else
            throw new CalendarAndPlannigServiceException(ErrorType.EVENT_IS_NOT_ACTIVE);
        return true;

    }

    public Boolean delete(EventDeleteRequestDTO eventDeleteRequestDTO) {
        Event event = eventRepository.findById(eventDeleteRequestDTO.id()).orElseThrow(() -> new CalendarAndPlannigServiceException(ErrorType.EVENT_NOT_FOUND));

        Long userId =(Long) rabbitTemplate.convertSendAndReceive("businessDirectExchange", "keyGetUserIdByToken", eventDeleteRequestDTO.token());

        if (!event.getUserId().equals(userId)) {
            throw new CalendarAndPlannigServiceException(ErrorType.BAD_REQUEST_ERROR);
        }

        if (event.getStatus().equals(EStatus.DELETED)) {
            throw new CalendarAndPlannigServiceException(ErrorType.BAD_REQUEST_ERROR);

        }
        event.setStatus(EStatus.DELETED);
        eventRepository.save(event);
        return true;
    }

    public List<FindAllResponseDTO> findAllByUserId(String token) {
        Long userId = (Long) rabbitTemplate.convertSendAndReceive("businessDirectExchange", "keyGetUserIdByToken", token);
        List<Event> events = eventRepository.findAllByUserIdAndStatus(userId, EStatus.ACTIVE)
                .orElseThrow(() -> new CalendarAndPlannigServiceException(ErrorType.EVENT_NOT_FOUND));


        return events.stream()
                .map(event -> new FindAllResponseDTO(
                        event.getId(),
                        event.getTitle(),
                        event.getStartTime(),
                        event.getEndTime()

                ))
                .collect(Collectors.toList());
    }

    public FindAllResponseDTO findById(String id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new CalendarAndPlannigServiceException(ErrorType.EVENT_NOT_FOUND));
        return new FindAllResponseDTO(
                event.getId(),
                event.getTitle(),
                event.getStartTime(),
                event.getEndTime()
        );

    }
}
