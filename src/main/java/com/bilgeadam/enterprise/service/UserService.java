package com.bilgeadam.enterprise.service;

import com.bilgeadam.enterprise.dto.request.LoginRequestDto;
import com.bilgeadam.enterprise.dto.request.NewPasswordRequestDto;
import com.bilgeadam.enterprise.dto.request.RegisterRequestDto;
import com.bilgeadam.enterprise.entity.User;
import com.bilgeadam.enterprise.exception.EnterpriseException;
import com.bilgeadam.enterprise.exception.ErrorType;
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
    private final UserAuthVerifyCodeService userAuthVerifyCodeService;

    public String doLogin(LoginRequestDto dto) {
        Optional<User> userOptional = userRepository.findByEmail(dto.email());
        if (userOptional.isEmpty() || !passwordEncoder.matches(dto.password(), userOptional.get().getPassword())) {
            throw new EnterpriseException(ErrorType.LOGIN_ERROR);
        }
        User user = userOptional.get();
        return "dummy_token"; //TODO: user Token donmeli!
    }

    public Boolean doRegister(RegisterRequestDto dto) {
        Optional<User> userOptional = userRepository.findByEmail(dto.email());
        if (userOptional.isPresent()) {
            throw new EnterpriseException(ErrorType.REGISTER_ERROR);
        }
        if (!dto.password().equals(dto.rePassword())) {
            throw new EnterpriseException(ErrorType.INVALID_PASSWORD);
        }
        User user = User.builder()
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .userState(EUserState.PENDING)
                .build();
        user = userRepository.save(user);
        String authCode = userAuthVerifyCodeService.generateAuthCode(user.getId());
        //TODO: Mail service araciliyla kullaniciya onay maili yolla!
        return true;
    }

    public Boolean authUserRegister(String authCode) {
        Optional<User> userOptional = checkAuthUser(authCode);
        User user = userOptional.get();
        user.setUserState(EUserState.ACTIVE);
        return true;
    }

    public Optional<User> checkAuthUser(String authCode) {
        Optional<Long> userIdByAuthCode = userAuthVerifyCodeService.findUserIdByAuthCode(authCode);
        if (userIdByAuthCode.isEmpty()) {
            throw new EnterpriseException(ErrorType.NOTFOUND_USER_AUTH);
        }
        Optional<User> userOptional = userRepository.findById(userIdByAuthCode.get());
        if (userOptional.isEmpty()) {
            throw new EnterpriseException(ErrorType.NOTFOUND_USER);
        }
        return userOptional;
    }

    public Boolean forgotPasswordMail(String email) {
        Optional<User> userOptional = userRepository.findOptionalByEmail(email);
        if (userOptional.isEmpty()) {
            throw new EnterpriseException(ErrorType.NOTFOUND_USER);
        }
        User user = userOptional.get();
        String authCode = userAuthVerifyCodeService.generateAuthCode(user.getId());
        //TODO: maile link gonderme islemi yapilacak

        return true;
    }

    public Boolean updateUserForgotPassword(NewPasswordRequestDto dto) {
        Optional<Long> userIdByAuthCode = userAuthVerifyCodeService.findUserIdByAuthCode(dto.authCode());
        if(userIdByAuthCode.isEmpty()) {
            throw new EnterpriseException(ErrorType.NOTFOUND_USER_AUTH);
        }
        Optional<User> userOptional = userRepository.findById(userIdByAuthCode.get());
        if (userOptional.isEmpty()) {
            throw new EnterpriseException(ErrorType.NOTFOUND_USER);
        }
        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(dto.password()));
        userRepository.save(user);
        return true;
    }
}
