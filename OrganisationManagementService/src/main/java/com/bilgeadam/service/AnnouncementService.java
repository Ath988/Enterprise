package com.bilgeadam.service;

import com.bilgeadam.dto.request.AnnouncementRequestDto;
import com.bilgeadam.dto.request.EmailDto;
import com.bilgeadam.dto.request.NotificationMessageRequestDto;
import com.bilgeadam.dto.response.AllEmployeeResponse;
import com.bilgeadam.dto.response.AnnouncementReadResponseDto;
import com.bilgeadam.entity.Announcement;
import com.bilgeadam.entity.AnnouncementIsRead;
import com.bilgeadam.entity.Employee;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.OrganisationManagementException;
import com.bilgeadam.manager.MailManager;
import com.bilgeadam.manager.NotificationManager;
import com.bilgeadam.repository.AnnouncementIsReadRepository;
import com.bilgeadam.repository.AnnouncementRepository;
import com.bilgeadam.repository.EmployeeRepository;
import com.bilgeadam.utility.JwtManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final EmployeeService employeeService;
    private final AnnouncementRepository announcementRepository;
    private final NotificationManager notificationManager;
    private final MailManager mailManager;
    private final AnnouncementIsReadService announcementIsReadService;


    public Boolean createAnnouncement(String token, AnnouncementRequestDto dto) {
        Employee employee = employeeService.getEmployeeByToken(token);

        Announcement announcement = Announcement.builder()
                .title(dto.title())
                .content(dto.content())
                .creationDate(LocalDate.now())
                .employeeId(employee.getId())
                .companyId(employee.getCompanyId())
                .build();
        announcementRepository.save(announcement);

        // Şirket çalışanlarını getir
        List<Employee> employees = employeeService.getEmployeesByCompanyId(employee.getCompanyId());

        // Bildirim gönder
        notificationManager.notificationSender(new NotificationMessageRequestDto(dto.title(), dto.content(), false));

        // Her çalışana e-posta gönder
        for (Employee e : employees) {
            if (employee.getId().equals(e.getId())) {
                continue;
            }
            mailManager.sendEmail(new EmailDto(
                    "enterprice@gmail.com",
                    employee.getEmail(),
                    e.getEmail(),
                    dto.title(),
                    dto.content()));

            announcementIsReadService.markAnnouncementIsRead(token, announcement.getId(), e.getId());


        }
        return true;
    }

    public List<AnnouncementReadResponseDto> getAnnouncements(String token) {
        // Kullanıcıyı bul
        Employee employee = employeeService.getEmployeeByToken(token);
        Long companyId = employee.getCompanyId(); // Şirket ID'sini al

        // Şirketin tüm duyurularını al
        List<Announcement> announcementList = announcementRepository.findByCompanyId(companyId);
        List<AnnouncementReadResponseDto> dtoList = new ArrayList<>();

        for (Announcement announcement : announcementList) {
            // Duyuruya ait okuma durumlarını al
            List<AnnouncementIsRead> announcementIsReadList = announcementIsReadService.findById(announcement.getId());

            // Okuma durumlarının birleşimini belirleyin (hepsinin okundu/okunmadığını hesaplayabilirsiniz)
            boolean isRead = false;
            for (AnnouncementIsRead announcementIsRead : announcementIsReadList) {
                if (announcementIsRead.getIsRead()) {
                    isRead = true; // Eğer okuma durumu varsa, 'isRead' true olarak ayarlanır.
                    break;
                }
            }

            // Duyuru için yalnızca bir DTO oluşturulacak
            AnnouncementReadResponseDto dto = AnnouncementReadResponseDto.builder()
                    .id(announcement.getId())
                    .isRead(isRead)
                    .content(announcement.getContent())
                    .title(announcement.getTitle())
                    .creationDate(announcement.getCreationDate())
                    .build();
            dtoList.add(dto);
        }

        return dtoList;
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

    public Announcement getAnnouncementById(Long id) {
        return announcementRepository.findById(id)
                .orElseThrow(() -> new OrganisationManagementException(ErrorType.NOT_FOUND_ANNOUNCEMENT));
    }

    public List<AnnouncementReadResponseDto> getReadAnnouncements(String token) {
        // Çalışan bilgisini token üzerinden al
        Employee employee = employeeService.getEmployeeByToken(token);

        // Çalışan bilgisiyle okunan duyuruları repository'den al
        return announcementRepository.findReadAnnouncementsByEmployeeId(employee.getId());
    }

    public List<Announcement> getUnreadAnnouncements(String token) {
        // Çalışan bilgisini token üzerinden al
        Employee employee = employeeService.getEmployeeByToken(token);
        // Çalışan bilgisiyle okunmamış duyuruları repository'den al
        List<Announcement> unreadAnnouncementsByEmployeeId = announcementRepository.findUnreadAnnouncementsByEmployeeId(employee.getId());

        if (unreadAnnouncementsByEmployeeId.isEmpty()) {
            throw new OrganisationManagementException(ErrorType.NOT_FOUND_UNREAD_ANNOUNCEMENT);
        }

        return unreadAnnouncementsByEmployeeId;
    }


}