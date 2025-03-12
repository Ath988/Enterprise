package com.bilgeadam.service;

import com.bilgeadam.entity.EmployeeShift;
import com.bilgeadam.repository.EmployeeShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeShiftService {
    private final EmployeeShiftRepository employeeShiftRepository;

    public List<EmployeeShift> getAllEmployeeShifts() {
        return employeeShiftRepository.findAll();
    }

}