package com.bilgeadam.service;

import com.bilgeadam.dto.request.EmailDto;
import com.bilgeadam.dto.request.LoginRequestDto;
import com.bilgeadam.dto.request.NewPasswordRequestDto;
import com.bilgeadam.dto.request.RegisterRequestDto;
import com.bilgeadam.entity.Auth;
import com.bilgeadam.exception.EnterpriseException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.manager.MailManager;
import com.bilgeadam.repository.AuthRepository;
import com.bilgeadam.util.enums.EAuthState;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserAuthVerifyCodeService userAuthVerifyCodeService;
    private final MailManager mailManager;

    public String doLogin(LoginRequestDto dto) {
        Optional<Auth> userOptional = userRepository.findByEmail(dto.email());
        if (userOptional.isEmpty() || !passwordEncoder.matches(dto.password(), userOptional.get().getPassword())) {
            throw new EnterpriseException(ErrorType.LOGIN_ERROR);
        }
        Auth user = userOptional.get();
        return "dummy_token"; //TODO: user Token donmeli!
    }

    public Boolean doRegister(RegisterRequestDto dto) {
        Optional<Auth> userOptional = userRepository.findByEmail(dto.email());
        if (userOptional.isPresent()) {
            throw new EnterpriseException(ErrorType.REGISTER_ERROR);
        }
        if (!dto.password().equals(dto.rePassword())) {
            throw new EnterpriseException(ErrorType.INVALID_PASSWORD);
        }
        Auth user = Auth.builder()
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .authState(EAuthState.PENDING)
                .build();
        user = userRepository.save(user);
        String authCode = userAuthVerifyCodeService.generateAuthCode(user.getId());
        mailManager.sendEmail(new EmailDto("enterprice@gmail.com", "enterprice@auth.com", user.getEmail(),
                "E-posta Adresini Onayla",
                "email adresini onaylamak icin linke tiklayiniz : http://localhost:9091/v1/dev/auth/auth-mail?auth=" + authCode));
        return true;
    }

    public Boolean authUserRegister(String authCode) {
        Optional<Auth> userOptional = checkAuthUser(authCode);
        Auth user = userOptional.get();
        user.setAuthState(EAuthState.ACTIVE);
        return true;
    }

    public Optional<Auth> checkAuthUser(String authCode) {
        Optional<Long> userIdByAuthCode = userAuthVerifyCodeService.findUserIdByAuthCode(authCode);
        if (userIdByAuthCode.isEmpty()) {
            throw new EnterpriseException(ErrorType.NOTFOUND_USER_AUTH);
        }
        Optional<Auth> userOptional = userRepository.findById(userIdByAuthCode.get());
        if (userOptional.isEmpty()) {
            throw new EnterpriseException(ErrorType.NOTFOUND_USER);
        }
        return userOptional;
    }

    public Boolean forgotPasswordMail(String email) {
        Optional<Auth> userOptional = userRepository.findOptionalByEmail(email);
        if (userOptional.isEmpty()) {
            throw new EnterpriseException(ErrorType.NOTFOUND_USER);
        }
        Auth user = userOptional.get();
        String authCode = userAuthVerifyCodeService.generateAuthCode(user.getId());
        mailManager.sendEmail(new EmailDto("enterprice@gmail.com", "enterprice@forgotpassword.com", user.getEmail(),
                "Sifre Sifirlama",
                "Sifre sifirlama linki : http://localhost:9091/v1/dev/auth/new-password?auth=" + authCode));

        return true;
    }

    public Boolean updateUserForgotPassword(NewPasswordRequestDto dto) {
        Optional<Long> userIdByAuthCode = userAuthVerifyCodeService.findUserIdByAuthCode(dto.authCode());
        if(userIdByAuthCode.isEmpty()) {
            throw new EnterpriseException(ErrorType.NOTFOUND_USER_AUTH);
        }
        Optional<Auth> userOptional = userRepository.findById(userIdByAuthCode.get());
        if (userOptional.isEmpty()) {
            throw new EnterpriseException(ErrorType.NOTFOUND_USER);
        }
        Auth user = userOptional.get();
        user.setPassword(passwordEncoder.encode(dto.password()));
        userRepository.save(user);
        return true;
    }
}
