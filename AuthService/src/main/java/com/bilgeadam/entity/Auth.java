package com.bilgeadam.entity;

import com.bilgeadam.util.enums.EAuthState;
import com.bilgeadam.util.enums.Permission;
import com.bilgeadam.util.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tbl_auth")
public class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private EAuthState authState;
    private Role role;
}
