package com.bilgeadam.utility;

import com.bilgeadam.entity.Role;
import com.bilgeadam.entity.User;
import com.bilgeadam.entity.UserRolePermission;
import com.bilgeadam.entity.enums.EGender;
import com.bilgeadam.entity.enums.Permission;
import com.bilgeadam.repository.RoleRepository;
import com.bilgeadam.repository.UserRepository;
import com.bilgeadam.repository.UserRolePermissionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;

@Configuration
public class GenerateData {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserRolePermissionRepository userRolePermissionRepository;

    public GenerateData(RoleRepository roleRepository, UserRepository userRepository, UserRolePermissionRepository userRolePermissionRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userRolePermissionRepository = userRolePermissionRepository;
    }

    @PostConstruct
    public void init(){
        if(roleRepository.count() == 0 ){
            Role admin = Role.builder().name("SYSTEM_ADMIN").build(); // site admini
            Role member = Role.builder().name("MEMBER").build(); // şirket sahibi
            Role staff = Role.builder().name("STAFF").build(); //çalışan-employee
            roleRepository.saveAll(List.of(admin,member,staff));
        }

        if(userRepository.count() == 0 ){
            Role memberRole = roleRepository.findByName("MEMBER").get();
            Role staffRole = roleRepository.findByName("STAFF").get();

            User member = User.builder().roles(Set.of(memberRole)).authId(1L).companyId(1L).email("vehbi@test.com").firstName("Vehbi").lastName("Koç").gender(EGender.MALE).build();
            User user1 = User.builder().roles(Set.of(staffRole)).authId(2L).companyId(1L).email("hasan@test.com").firstName("Hasan").lastName("Kayar").gender(EGender.MALE).build();
            User user2 = User.builder().roles(Set.of(staffRole)).authId(3L).companyId(1L).email("ayse@test.com").firstName("Ayse").lastName("Kulin").gender(EGender.FEMALE).build();
            User user3 = User.builder().roles(Set.of(staffRole)).authId(4L).companyId(1L).email("mehmet@test.com").firstName("Mehmet").lastName("Oz").gender(EGender.MALE).build();
            User user4 = User.builder().roles(Set.of(staffRole)).authId(5L).companyId(1L).email("hulya@test.com").firstName("Hulya").lastName("Pamuk").gender(EGender.FEMALE).build();
            userRepository.saveAll(List.of(member,user1,user2,user3,user4));

            Set<Permission> employeePermissions = Set.of(Permission.ACCESS_PROFILE,Permission.ACCESS_CALENDAR_AND_CHAT);

            //Member
            UserRolePermission urp1 = UserRolePermission.builder().role(memberRole)
                    .permissions(Set.of(Permission.ACCESS_ALL_MODULES)).user(member).build();
            //Employee
            UserRolePermission urp2  = UserRolePermission.builder().role(staffRole)
                    .permissions(employeePermissions).user(user1).build();
            UserRolePermission urp3  = UserRolePermission.builder().role(staffRole)
                    .permissions(employeePermissions).user(user2).build();
            UserRolePermission urp4  = UserRolePermission.builder().role(staffRole)
                    .permissions(employeePermissions).user(user3).build();
            UserRolePermission urp5  = UserRolePermission.builder().role(staffRole)
                    .permissions(employeePermissions).user(user4).build();

            userRolePermissionRepository.saveAll(List.of(urp1,urp2,urp3,urp4,urp5));
        }

    }
}
