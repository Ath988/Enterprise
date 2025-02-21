package com.bilgeadam.service;

import com.bilgeadam.dto.request.*;
import com.bilgeadam.dto.response.UserPermissionResponse;
import com.bilgeadam.entity.Auth;
import com.bilgeadam.exception.EnterpriseException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.manager.*;
import com.bilgeadam.repository.AuthRepository;
import com.bilgeadam.util.enums.EAuthState;
import com.bilgeadam.util.JwtManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.bilgeadam.dto.response.BaseResponse.*;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserAuthVerifyCodeService userAuthVerifyCodeService;
    private final MailManager mailManager;
    private final JwtManager jwtManager;
    private final OrganisationManagementManager organisationManagementManager;
    private final NotificationManager notificationManager;
    private final LogManager logManager;
    private final UserManager userManager;

    public String doLogin(LoginRequestDto dto) {
        Optional<Auth> userOptional = authRepository.findByEmail(dto.email());
        if (userOptional.isEmpty() || !passwordEncoder.matches(dto.password(), userOptional.get().getPassword())) {
            throw new EnterpriseException(ErrorType.LOGIN_ERROR);
        }

        Auth user = userOptional.get();
        if (user.getAuthState().equals(EAuthState.PENDING)) {
            throw new EnterpriseException(ErrorType.LOGIN_ERROR_EMAIL_VALIDATION);
        }

        UserPermissionResponse upr =
                getDataFromResponse(userManager.getUserPermission(user.getId()));



        Optional<String> token = jwtManager.createToken(user.getId(),upr.roles(),upr.permissions(),upr.subscriptionType());
        System.out.println();
        System.out.println();
        System.out.println(jwtManager.getRolesAndPermissionsFromToken(token.get()));
        System.out.println();
        System.out.println();

        if (token.isPresent()) {
//            notificationManager.notificationSender(new NotificationMessageRequestDto("Giriş bildirimi","giriş yaptınız",true));
            return token.get();
        }
        throw new EnterpriseException(ErrorType.LOGIN_ERROR);
    }

    @Transactional
    public Long doRegister(RegisterRequestDto dto) {
        checkCredentials(dto);
        Auth auth = Auth.builder()
                .email(dto.email())
                .firstname(dto.firstname())
                .lastname(dto.lastname())
                .password(passwordEncoder.encode(dto.password()))
                .authState(EAuthState.PENDING)
                .build();
        auth = authRepository.save(auth);
        String authCode = userAuthVerifyCodeService.generateAuthCode(auth.getId());

        userManager.createMember(new CreateMemberRequest(auth.getId(),dto.firstname(),dto.lastname(),dto.email()));
        organisationManagementManager.createCompanyManager(new CreateCompanyManagerRequest(auth.getId(), auth.getEmail()));
        mailManager.sendEmail(new EmailDto("enterprice@gmail.com", "enterprice@auth.com", auth.getEmail(),
                "E-posta Adresini Onayla",
                "email adresini onaylamak icin linke tiklayiniz : http://localhost:8081/v1/dev/auth/auth-mail?auth=" + authCode));
        return auth.getId();
    }



    public Long registerEmployee(String token,RegisterRequestDto dto) {
        checkCredentials(dto);
        Auth auth = Auth.builder()
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .authState(EAuthState.PENDING)
                .firstname(dto.firstname())
                .lastname(dto.lastname())
                .build();
        auth = authRepository.save(auth);
        String authCode = userAuthVerifyCodeService.generateAuthCode(auth.getId());

        userManager.createUser(token,new CreateMemberRequest(auth.getId(),dto.firstname(),dto.lastname(),dto.email()));

        mailManager.sendEmail(new EmailDto("enterprice@gmail.com", "enterprice@auth.com", auth.getEmail(),
                "E-posta Adresini Onayla",
                "email adresini onaylamak icin linke tiklayiniz : http://localhost:9091/v1/dev/auth/auth-mail?auth=" + authCode));
        return auth.getId();
    }

    public Boolean authUserRegister(String authCode) {
        Optional<Auth> userOptional = checkAuthUser(authCode);
        Auth user = userOptional.get();
        user.setAuthState(EAuthState.ACTIVE);
        authRepository.save(user);
        return true;
    }

    public Optional<Auth> checkAuthUser(String authCode) {
        Optional<Long> userIdByAuthCode = userAuthVerifyCodeService.findUserIdByAuthCode(authCode);
        if (userIdByAuthCode.isEmpty()) {
            logManager.logRequest(ErrorType.NOTFOUND_USER_AUTH.getMessage());
            throw new EnterpriseException(ErrorType.NOTFOUND_USER_AUTH);
        }
        Optional<Auth> userOptional = authRepository.findById(userIdByAuthCode.get());
        if (userOptional.isEmpty()) {
            throw new EnterpriseException(ErrorType.NOTFOUND_USER);
        }
        return userOptional;
    }

    public Boolean forgotPasswordMail(String email) {
        Optional<Auth> userOptional = authRepository.findOptionalByEmail(email);
        if (userOptional.isEmpty()) {
            throw new EnterpriseException(ErrorType.NOTFOUND_USER);
        }
        Auth user = userOptional.get();
        String authCode = userAuthVerifyCodeService.generateAuthCode(user.getId());
        mailManager.sendEmail(new EmailDto("enterprice@gmail.com", "enterprice@forgotpassword.com", user.getEmail(),
                "Sifre Sifirlama",
                "Sifre sifirlama linki : http://localhost:8081/v1/dev/auth/new-password?auth=" + authCode));

        return true;
    }

    public Boolean updateUserForgotPassword(NewPasswordRequestDto dto) {
        Optional<Long> userIdByAuthCode = userAuthVerifyCodeService.findUserIdByAuthCode(dto.authCode());
        if(userIdByAuthCode.isEmpty()) {
            throw new EnterpriseException(ErrorType.NOTFOUND_USER_AUTH);
        }
        Optional<Auth> userOptional = authRepository.findById(userIdByAuthCode.get());
        if (userOptional.isEmpty()) {
            throw new EnterpriseException(ErrorType.NOTFOUND_USER);
        }
        Auth user = userOptional.get();
        user.setPassword(passwordEncoder.encode(dto.password()));
        authRepository.save(user);
        return true;
    }

    public Boolean updateUserProfile(UpdateProfileRequestDto dto) {
        Optional<Long> optionalUserId = jwtManager.validateToken(dto.token());
        if (optionalUserId.isEmpty()) {
            throw new EnterpriseException(ErrorType.INVALID_TOKEN);
        }
        Optional<Auth> optionalAuth = authRepository.findById(optionalUserId.get());
        if (optionalAuth.isEmpty()) {
            throw new EnterpriseException(ErrorType.NOTFOUND_USER);
        }
        Auth user = optionalAuth.get();
        user.setFirstname(dto.firstname());
        user.setLastname(dto.lastname());
        user.setEmail(dto.email());
        authRepository.save(user);
        return true;
    }
    public Auth getUserProfile(String token){
        Optional<Long> optionalAuthId = jwtManager.validateToken(token);
        if (optionalAuthId.isEmpty()) {
            throw new EnterpriseException(ErrorType.INVALID_TOKEN);
        }
        Optional<Auth> optionalAuth = authRepository.findById(optionalAuthId.get());
        if (optionalAuth.isEmpty()) {
            throw new EnterpriseException(ErrorType.NOTFOUND_USER);
        }
        return optionalAuth.get();
    }

    public Boolean updateUserPassword(UpdatePasswordProfileRequestDto dto){
        Optional<Long> optionalUserId = jwtManager.validateToken(dto.token());
        if (optionalUserId.isEmpty()) {
            throw new EnterpriseException(ErrorType.INVALID_TOKEN);
        }
        Optional<Auth> optionalAuth = authRepository.findById(optionalUserId.get());
        if (optionalAuth.isEmpty()) {
            throw new EnterpriseException(ErrorType.NOTFOUND_USER);
        }
        Auth user = optionalAuth.get();
        user.setPassword(passwordEncoder.encode(dto.password()));
        authRepository.save(user);
        return true;
    }


    private void checkCredentials(RegisterRequestDto dto) {
        Optional<Auth> userOptional = authRepository.findByEmail(dto.email());
        if (userOptional.isPresent()) {
            throw new EnterpriseException(ErrorType.REGISTER_ERROR);
        }
        if (!dto.password().equals(dto.rePassword())) {
            throw new EnterpriseException(ErrorType.INVALID_PASSWORD);
        }
    }





}