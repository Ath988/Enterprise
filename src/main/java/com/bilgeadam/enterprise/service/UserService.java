package com.bilgeadam.enterprise.service;

import com.bilgeadam.enterprise.dto.request.LoginRequestDto;
import com.bilgeadam.enterprise.dto.request.RegisterRequestDto;
import com.bilgeadam.enterprise.entity.User;
import com.bilgeadam.enterprise.repository.UserRepository;
import com.bilgeadam.enterprise.util.enums.EUserState;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String doLogin(LoginRequestDto dto) {
        Optional<User> userOptional = userRepository.findByEmail(dto.email());
        if (userOptional.isEmpty() || !passwordEncoder.matches(dto.password(), userOptional.get().getPassword())) {
            //TODO: error exception handling eklenmeli!
            return "Invalid email or password";
        }
        User user = userOptional.get();
        return "dummy_token"; //TODO: user Token donmeli!
    }

    public Boolean doRegister(RegisterRequestDto dto) {
        Optional<User> userOptional = userRepository.findByEmail(dto.email());
        if (userOptional.isPresent()) {
            //!Kullanici zaten sisteme kayitli
            //TODO: error exception handling eklenmeli!
            return false;
        }
        if (!dto.password().equals(dto.rePassword())) {
            //!Sifreler uyusmuyor
            //TODO: error exception handling eklenmeli!
            return false;
        }
        User user = User.builder()
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .userState(EUserState.PENDING)
                .build();
        userRepository.save(user);
        //TODO: Mail service araciliyla kullaniciya onay maili yolla!
        return true;
    }
}
