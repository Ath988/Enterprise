package com.bilgeadam.service;

import com.bilgeadam.entity.Role;
import com.bilgeadam.entity.User;
import com.bilgeadam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;


    public Boolean createCompanyOwner(String kullanici){
        User user = User.builder().firstName(kullanici)
                .email(kullanici+"@email.com")
                .build();

        Role role = roleService.findByName("MEMBER");
        user.getRoles().add(role);
        userRepository.save(user);
        return true;
    }


    public List<User> findAllTest() {

        return userRepository.findAll();
    }
}
