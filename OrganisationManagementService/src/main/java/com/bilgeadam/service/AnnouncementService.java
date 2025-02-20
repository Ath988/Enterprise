package com.bilgeadam.service;

import com.bilgeadam.dto.request.AnnouncementRequestDto;
import com.bilgeadam.dto.response.AllEmployeeResponse;
import com.bilgeadam.entity.Announcement;
import com.bilgeadam.entity.Employee;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.OrganisationManagementException;
import com.bilgeadam.repository.AnnouncementRepository;
import com.bilgeadam.repository.EmployeeRepository;
import com.bilgeadam.utility.JwtManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnnouncementService {
	
	private final EmployeeService employeeService;
	private final AnnouncementRepository announcementRepository;
	private final JwtManager jwtManager;

	
	
	public Boolean createAnnouncement(String token, AnnouncementRequestDto dto) {
		Employee employee = employeeService.getEmployeeByToken(token);
		
	
		Announcement announcement =
				Announcement.builder().title(dto.title()).content(dto.content()).creationDate(LocalDate.now())
				            .companyId(employee.getCompanyId()).build();
		
		announcementRepository.save(announcement);
		
		
		return true;
	}
}