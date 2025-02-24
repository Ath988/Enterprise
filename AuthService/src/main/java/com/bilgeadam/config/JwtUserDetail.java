package com.bilgeadam.config;

import com.bilgeadam.dto.response.UserPermissionResponse;
import com.bilgeadam.util.JwtManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class JwtUserDetail implements UserDetailsService {

    private final JwtManager jwtManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
    public UserDetails getAuthFromToken(String token){
        UserPermissionResponse urp = jwtManager.getRolesAndPermissionsFromToken(token);
        Set<String> roles = urp.roles();
        Set<String> permissions = urp.permissions();
        String subscriptionType = urp.subscriptionType();

        Set<GrantedAuthority> authorizedList=new HashSet<>();

        roles.forEach(role -> {authorizedList.add(new SimpleGrantedAuthority("ROLE_"+role));}); //hasRole ile kontrol edilecek. ör: hasRole("MEMBER")
        permissions.forEach(permission -> {authorizedList.add(new SimpleGrantedAuthority(permission));});//bu ve subscription hasAuthority ile ör: hasAuthority("ENTERPRISE")
        authorizedList.add(new SimpleGrantedAuthority(subscriptionType));


        return org.springframework.security.core.userdetails.User.builder()
                .username("user@ornek.com") //Todo: Belki bu da tokendan claim ile çekilebilir.
                .password("")
                .accountLocked(false)
                .accountExpired(false)
                .authorities(authorizedList)
                .build();
    }
}
