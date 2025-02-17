package com.bilgeadam.util.dataGenerate;

import com.bilgeadam.entity.Auth;
import com.bilgeadam.repository.AuthRepository;
import com.bilgeadam.service.AuthService;
import com.bilgeadam.util.enums.EAuthState;
import com.bilgeadam.util.enums.ERole;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class GenerateAuth {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void generateAuth(){
        if(authRepository.count() == 0 ){
            Auth companyManager =  Auth.builder().role(ERole.MEMBER).authState(EAuthState.ACTIVE)
                    .email("vehbi@test.com").firstname("Vehbi").lastname("Koc").password(passwordEncoder.encode("Sifre123**"))
                    .build();
            Auth staff1 = Auth.builder().role(ERole.STAFF).authState(EAuthState.ACTIVE)
                    .email("hasan@test.com").firstname("Hasan").lastname("Karhan").password(passwordEncoder.encode("Sifre123**"))
                    .build();
            Auth staff2 = Auth.builder().role(ERole.STAFF).authState(EAuthState.ACTIVE)
                    .email("ayse@test.com").firstname("Ayse").lastname("Kulin").password(passwordEncoder.encode("Sifre123**"))
                    .build();
            Auth staff3 = Auth.builder().role(ERole.STAFF).authState(EAuthState.ACTIVE)
                    .email("mehmet@test.com").firstname("Mehmet").lastname("Oz").password(passwordEncoder.encode("Sifre123**"))
                    .build();
            Auth staff4 = Auth.builder().role(ERole.STAFF).authState(EAuthState.ACTIVE)
                    .email("hulya@test.com").firstname("Hulya").lastname("Pamuk").password(passwordEncoder.encode("Sifre123**"))
                    .build();
            authRepository.saveAll(List.of(companyManager,staff1,staff2,staff3,staff4));

        }
    }

}
