package com.bilgeadam.config;


import com.bilgeadam.entity.Employee;
import com.bilgeadam.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtUserDetail implements UserDetailsService {

    private EmployeeRepository employeeRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
    public UserDetails getAuthById(Long authId){
        Optional<Employee> employee=employeeRepository.findOptionalById(authId);
        if (employee.isEmpty()) return null;

        List<GrantedAuthority> authorizedList=new ArrayList<>();
        authorizedList.add(new SimpleGrantedAuthority("PERSONAL")); //personal
        authorizedList.add(new SimpleGrantedAuthority("USER")); //user
        authorizedList.add(new SimpleGrantedAuthority("SYSTEM_MANAGEMENT")); //sistem yöneticisi



        return org.springframework.security.core.userdetails.User.builder()
                .username(employee.get().getEmail())
                .password("")
                .accountLocked(false)
                .accountExpired(false)
                .authorities(authorizedList)
                .build();
    }
}
