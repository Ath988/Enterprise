package com.bilgeadam.utility.demoData;

import com.bilgeadam.entity.Employee;
import com.bilgeadam.entity.enums.EGender;
import com.bilgeadam.entity.enums.EmployeeRole;

import java.util.List;

public class GenerateEmployee {
    
    public static List<Employee> generateEmployee() {
        Employee companyManager = Employee.builder()
                                          .companyId(1L).authId(1L)
                                          .firstName("Vehbi").lastName("Koç")
                                          .email("vehbi@test.com")
                                          .role(EmployeeRole.COMPANY_OWNER)
                                          .gender(EGender.MALE)
                                          .positionId(1L) // CEO
                                          .avatarUrl("https://randomuser.me/api/portraits/men/1.jpg")
                                          .build();
        
        Employee cto = Employee.builder()
                               .companyId(1L).authId(2L)
                               .firstName("Anıl").lastName("Özogli")
                               .email("hasan@test.com")
                               .role(EmployeeRole.DEPARTMENT_MANAGER)
                               .gender(EGender.MALE)
                               .positionId(2L) // CTO (IT Departmanı)
                               .avatarUrl("https://randomuser.me/api/portraits/men/2.jpg")
                               .build();
        
        Employee cfo = Employee.builder()
                               .companyId(1L).authId(3L)
                               .firstName("Ayşe").lastName("Kulin")
                               .email("ayse@test.com")
                               .role(EmployeeRole.DEPARTMENT_MANAGER)
                               .gender(EGender.FEMALE)
                               .positionId(3L) // CFO (Finance Departmanı)
                               .avatarUrl("https://randomuser.me/api/portraits/women/3.jpg")
                               .build();
        
        Employee developer = Employee.builder()
                                     .companyId(1L).authId(4L)
                                     .firstName("Mehmet").lastName("Öz")
                                     .email("mehmet@test.com")
                                     .role(EmployeeRole.EMPLOYEE)
                                     .gender(EGender.MALE)
                                     .positionId(4L) // Developer (CTO'ya bağlı)
                                     .avatarUrl("https://randomuser.me/api/portraits/men/5.jpg")
                                     .build();
        
        Employee tester = Employee.builder()
                                  .companyId(1L).authId(5L)
                                  .firstName("Hülya").lastName("Pamuk")
                                  .email("hulya@test.com")
                                  .role(EmployeeRole.EMPLOYEE)
                                  .gender(EGender.FEMALE)
                                  .positionId(5L) // Tester (CTO'ya bağlı)
                                  .avatarUrl("https://randomuser.me/api/portraits/women/4.jpg")
                                  .build();
        
        Employee accountant = Employee.builder()
                                      .companyId(1L).authId(6L)
                                      .firstName("Kemal").lastName("Sunal")
                                      .email("kemal@test.com")
                                      .role(EmployeeRole.EMPLOYEE)
                                      .gender(EGender.MALE)
                                      .positionId(6L) // Accountant (CFO'ya bağlı)
                                      .avatarUrl("https://randomuser.me/api/portraits/men/6.jpg")
                                      .build();
        
        Employee hrManager = Employee.builder()
                                     .companyId(1L).authId(7L)
                                     .firstName("Selin").lastName("Yılmaz")
                                     .email("selin@test.com")
                                     .role(EmployeeRole.DEPARTMENT_MANAGER)
                                     .gender(EGender.FEMALE)
                                     .positionId(7L) // HR Manager (CFO'ya bağlı)
                                     .avatarUrl("https://randomuser.me/api/portraits/women/7.jpg")
                                     .build();
        
        Employee recruiter = Employee.builder()
                                     .companyId(1L).authId(8L)
                                     .firstName("Murat").lastName("Çelik")
                                     .email("murat@test.com")
                                     .role(EmployeeRole.EMPLOYEE)
                                     .gender(EGender.MALE)
                                     .positionId(8L) // Recruiter (HR Manager'a bağlı)
                                     .avatarUrl("https://randomuser.me/api/portraits/men/8.jpg")
                                     .build();
        
        Employee sysAdmin = Employee.builder()
                                    .companyId(1L).authId(9L)
                                    .firstName("Güven").lastName("Taş")
                                    .email("guven@test.com")
                                    .role(EmployeeRole.EMPLOYEE)
                                    .gender(EGender.MALE)
                                    .positionId(9L) // System Administrator (CTO'ya bağlı)
                                    .avatarUrl("https://randomuser.me/api/portraits/men/9.jpg")
                                    .build();
        
        Employee devOps = Employee.builder()
                                  .companyId(1L).authId(10L)
                                  .firstName("Veli").lastName("Kaya")
                                  .email("veli@test.com")
                                  .role(EmployeeRole.EMPLOYEE)
                                  .gender(EGender.MALE)
                                  .positionId(10L) // DevOps Engineer (System Administrator'a bağlı)
                                  .avatarUrl("https://randomuser.me/api/portraits/men/10.jpg")
                                  .build();
        
        Employee projectManager = Employee.builder()
                                          .companyId(1L).authId(11L)
                                          .firstName("Canan").lastName("Kaya")
                                          .email("canan@test.com")
                                          .role(EmployeeRole.DEPARTMENT_MANAGER)
                                          .gender(EGender.FEMALE)
                                          .positionId(11L) // Project Manager (CEO'ya bağlı)
                                          .avatarUrl("https://randomuser.me/api/portraits/women/11.jpg")
                                          .build();
        
        Employee businessAnalyst = Employee.builder()
                                           .companyId(1L).authId(12L)
                                           .firstName("Esra").lastName("Arslan")
                                           .email("esra@test.com")
                                           .role(EmployeeRole.EMPLOYEE)
                                           .gender(EGender.FEMALE)
                                           .positionId(12L) // Business Analyst (Project Manager'a bağlı)
                                           .avatarUrl("https://randomuser.me/api/portraits/women/12.jpg")
                                           .build();
        
        Employee hrSpecialist = Employee.builder()
                                        .companyId(1L).authId(13L)
                                        .firstName("Aylin").lastName("Yurt")
                                        .email("aylin@test.com")
                                        .role(EmployeeRole.EMPLOYEE)
                                        .gender(EGender.FEMALE)
                                        .positionId(13L) // HR Specialist (HR Manager'a bağlı)
                                        .avatarUrl("https://randomuser.me/api/portraits/women/13.jpg")
                                        .build();
        
        Employee financeAnalyst = Employee.builder()
                                          .companyId(1L).authId(14L)
                                          .firstName("Burak").lastName("Demir")
                                          .email("burak@test.com")
                                          .role(EmployeeRole.EMPLOYEE)
                                          .gender(EGender.MALE)
                                          .positionId(15L) // Finance Analyst (CFO'ya bağlı)
                                          .avatarUrl("https://randomuser.me/api/portraits/men/14.jpg")
                                          .build();
        
        Employee itSupport = Employee.builder()
                                     .companyId(1L).authId(15L)
                                     .firstName("Elif").lastName("Sarı")
                                     .email("elif@test.com")
                                     .role(EmployeeRole.EMPLOYEE)
                                     .gender(EGender.FEMALE)
                                     .positionId(14L) // IT Support (CTO'ya bağlı)
                                     .avatarUrl("https://randomuser.me/api/portraits/women/15.jpg")
                                     .build();
        
        Employee networkEngineer = Employee.builder()
                                           .companyId(1L).authId(16L)
                                           .firstName("Serkan").lastName("Balcı")
                                           .email("serkan@test.com")
                                           .role(EmployeeRole.EMPLOYEE)
                                           .gender(EGender.MALE)
                                           .positionId(16L) // Network Engineer (System Administrator'a bağlı)
                                           .avatarUrl("https://randomuser.me/api/portraits/men/16.jpg")
                                           .build();
        
        Employee securitySpecialist = Employee.builder()
                                              .companyId(1L).authId(17L)
                                              .firstName("Okan").lastName("Altun")
                                              .email("okan@test.com")
                                              .role(EmployeeRole.EMPLOYEE)
                                              .gender(EGender.MALE)
                                              .positionId(17L) // Security Specialist (System Administrator'a bağlı)
                                              .avatarUrl("https://randomuser.me/api/portraits/men/17.jpg")
                                              .build();
        
        Employee qaEngineer = Employee.builder()
                                      .companyId(1L).authId(18L)
                                      .firstName("Ebru").lastName("Çetin")
                                      .email("ebru@test.com")
                                      .role(EmployeeRole.EMPLOYEE)
                                      .gender(EGender.FEMALE)
                                      .positionId(18L) // QA Engineer (Tester'a bağlı)
                                      .avatarUrl("https://randomuser.me/api/portraits/women/18.jpg")
                                      .build();
        
        Employee seniorDeveloper = Employee.builder()
                                           .companyId(1L).authId(19L)
                                           .firstName("Tamer").lastName("Kaya")
                                           .email("tamer@test.com")
                                           .role(EmployeeRole.EMPLOYEE)
                                           .gender(EGender.MALE)
                                           .positionId(19L) // Senior Developer (Developer'a bağlı)
                                           .avatarUrl("https://randomuser.me/api/portraits/men/19.jpg")
                                           .build();
        
        Employee juniorDeveloper = Employee.builder()
                                           .companyId(1L).authId(20L)
                                           .firstName("Nisa").lastName("Gök")
                                           .email("nisa@test.com")
                                           .role(EmployeeRole.EMPLOYEE)
                                           .gender(EGender.FEMALE)
                                           .positionId(20L) // Junior Developer (Developer'a bağlı)
                                           .avatarUrl("https://randomuser.me/api/portraits/women/20.jpg")
                                           .build();
        
        return List.of(companyManager, cto, cfo, developer, tester, accountant, hrManager, recruiter,
                       sysAdmin, devOps, projectManager, businessAnalyst, hrSpecialist, financeAnalyst,
                       itSupport, networkEngineer, securitySpecialist, qaEngineer, seniorDeveloper, juniorDeveloper);
    }
    
    
}