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
				Announcement.builder().title(dto.title()).content(dto.content()).creationDate(LocalDate.now()).employeeId(employee.getId())
				            .companyId(employee.getCompanyId()).build();
		announcementRepository.save(announcement);
		
		return true;
	}
	
	public List<Announcement> getAnnouncements(String token) {
		Employee employee = employeeService.getEmployeeByToken(token); // Kullanıcıyı bul
		Long companyId = employee.getCompanyId(); // Şirket ID'sini al
		return announcementRepository.findByCompanyId(companyId); // Aynı şirkete ait duyuruları getir
	}
	
	public Boolean deleteAnnouncement(String token, Long announcementId) {
		Employee employee = employeeService.getEmployeeByToken(token); // Kullanıcıyı bul
		Optional<Announcement> announcement = announcementRepository.findById(announcementId);
		
		if (announcement.isEmpty()) {
			throw new OrganisationManagementException(ErrorType.NOT_FOUND_ANNOUNCEMENT);
		}
		if (!announcement.get().getEmployeeId().equals(employee.getId())) {
			throw new OrganisationManagementException(ErrorType.CANNOT_DELETE_ANNOUNCEMENT);
		}
		announcementRepository.delete((announcement.get()));
		return true;
	}
}