package com.bilgeadam.service;

import com.bilgeadam.dto.request.CreateShiftRequestDto;
import com.bilgeadam.dto.request.UpdateShiftRequestDto;
import com.bilgeadam.entity.Shift;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.OrganisationManagementException;
import com.bilgeadam.repository.ShiftRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final EmployeeService employeeService;

    public List<Shift> getAllShifts(String token) {
        employeeService.getEmployeeByToken(token);
        return shiftRepository.findAllByState(EState.ACTIVE);
    }


    public Boolean createShift(@Valid CreateShiftRequestDto dto, String token) {
        employeeService.getEmployeeByToken(token);
        Shift shift = Shift.builder()
                .name(dto.shiftName())
                .startTime(dto.startTime())
                .endTime(dto.endTime())
                .build();
        shiftRepository.save(shift);
        return true;

    }

    public Boolean deleteShift(Long shiftId , String token) {
        employeeService.getEmployeeByToken(token);
        Optional<Shift> optionalShift = shiftRepository.findById(shiftId);
        if (optionalShift.isEmpty()) {
            throw new OrganisationManagementException(ErrorType.SHIFT_NOT_FOUND);
        }
        Shift shift = optionalShift.get();
        shift.setState(EState.PASSIVE);
        shiftRepository.save(shift);
        return true;
    }

    public Boolean updateShift(UpdateShiftRequestDto dto,String token) {
        employeeService.getEmployeeByToken(token);
        Optional<Shift> optionalShift = shiftRepository.findById(dto.shiftId());
        if (optionalShift.isEmpty()) {
            throw new OrganisationManagementException(ErrorType.SHIFT_NOT_FOUND);
        }
        Shift shift = optionalShift.get();
        shift.setName(dto.shiftType());
        shift.setStartTime(dto.startTime());
        shift.setEndTime(dto.endTime());
        shiftRepository.save(shift);
        return true;
    }
}

