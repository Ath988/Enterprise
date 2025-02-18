package com.bilgeadam.utility;

import com.bilgeadam.entity.Role;
import com.bilgeadam.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;

@Configuration
public class GenerateRole {

    private final RoleRepository roleRepository;

    public GenerateRole(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void init(){
        if(roleRepository.count() == 0 ){
            Role admin = Role.builder().name("SYSTEM_ADMIN").build(); // site admini
            Role member = Role.builder().name("MEMBER").build(); // şirket sahibi
            Role staff = Role.builder().name("STAFF").build(); //çalışan-employee
            roleRepository.saveAll(List.of(admin,member,staff));
        }
    }
}
