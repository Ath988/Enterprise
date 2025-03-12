package com.bilgeadam.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserProfileResponse {

    public UserProfileResponse(Long id,Long authId,String firstName,String lastName,String phoneNo,
                               String tcNo,String email,LocalDate birthDate,String avatarUrl){
        this.id = id;
        this.authId = authId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
        this.tcNo = tcNo;
        this.email = email;
        this.birthDate = birthDate;
        this.avatarUrl = avatarUrl;
    }

    Long id;
    Long authId;
    String firstName;
    String lastName;
    String phoneNo;
    String tcNo;
    String email;
    LocalDate birthDate;
    String avatarUrl;

    String positionName;
    String departmentName;

}
