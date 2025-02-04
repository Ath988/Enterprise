package com.bilgeadam.config;

import com.bilgeadam.entity.Auth;
import com.bilgeadam.repository.AuthRepository;
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

    private AuthRepository authRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
    public UserDetails getAuthById(Long authId){
        Optional<Auth> authUser=authRepository.findOptionalById(authId);
        if (authUser.isEmpty()) return null;

        List<GrantedAuthority> authorizedList=new ArrayList<>();
        authorizedList.add(new SimpleGrantedAuthority("STAFF"));
        authorizedList.add(new SimpleGrantedAuthority("MEMBER"));
        authorizedList.add(new SimpleGrantedAuthority("SYSTEM_ADMIN"));



        return org.springframework.security.core.userdetails.User.builder()
                .username(authUser.get().getEmail())
                .password("")
                .accountLocked(false)
                .accountExpired(false)
                .authorities(authorizedList)
                .build();
    }
}
