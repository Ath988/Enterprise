package com.bilgeadam.service;

import com.bilgeadam.dto.response.EmployeeReadAnnouncementResponseDto;
import com.bilgeadam.entity.Announcement;
import com.bilgeadam.entity.AnnouncementIsRead;
import com.bilgeadam.entity.Employee;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.OrganisationManagementException;
import com.bilgeadam.repository.AnnouncementIsReadRepository;
import com.bilgeadam.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnnouncementIsReadService {

    private final AnnouncementIsReadRepository announcementIsReadRepository;
    private final EmployeeService employeeService;

    public Boolean markAnnouncementIsRead(String token,Long announcementId,Long receiverId) {
        Employee employee = employeeService.getEmployeeByToken(token);

        AnnouncementIsRead announcementStatus = AnnouncementIsRead.builder()
                .announcementId(announcementId)
                .employeeId(employee.getId())
                .isRead(false)
                .receiverId(receiverId)
                .build();

        announcementIsReadRepository.save(announcementStatus);

        return true;
    }

    public Boolean announcementIsReadTrue(String token, Long announcementId) {
        Employee employee = employeeService.getEmployeeByToken(token);

        // Belirtilen duyuru ID'sine ve çalışan ID'sine göre okuma kaydını getir
        List<AnnouncementIsRead> announcementReadList = announcementIsReadRepository
                .findAllByAnnouncementIdAndReceiverId(announcementId, employee.getId());


        System.out.println("announcementId: " + announcementId);
        System.out.println("employeeId: " + employee.getId());

        if (announcementReadList.isEmpty()) {
            System.out.println("Bu duyuru için okunma kaydı bulunamadı. ID = " + announcementId);
            throw new OrganisationManagementException(ErrorType.CANNOT_ISREADANNOUNCEMENT_LIST);
        }

        // Çalışanın kendi kaydını güncellemesi için
        for (AnnouncementIsRead announcementIsRead : announcementReadList) {
            if (!announcementIsRead.getReceiverId().equals(employee.getId())) {
                throw new OrganisationManagementException(ErrorType.CANNOT_ISREAD_ANNOUNCEMENT);
            }
            announcementIsRead.setIsRead(true);
        }

        // Güncellenmiş tüm kayıtları kaydet
        announcementIsReadRepository.saveAll(announcementReadList);
        return true;
    }

    //okunmuş duyuruları listelemek için yazılan metod
    public List<Announcement> getReadAnnouncements(String token) {
        Employee employee = employeeService.getEmployeeByToken(token);
        return announcementIsReadRepository.findReadAnnouncementsByEmployeeId(employee.getId());
    }

    public List<AnnouncementIsRead> findById(Long announcementId) {
       return announcementIsReadRepository.findByAnnouncementId(announcementId);

    }


    //public List<EmployeeReadAnnouncementResponseDto> getAllReaders(Long announcementId) {}
}