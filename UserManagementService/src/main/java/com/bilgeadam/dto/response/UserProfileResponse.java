package com.bilgeadam.dto.response;

import com.bilgeadam.entity.Address;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserProfileResponse {

    Long id;
    Long authId;
    String firstName;
    String lastName;
    String email;

}
