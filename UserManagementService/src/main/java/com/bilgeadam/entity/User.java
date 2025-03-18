package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.EGender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "tbl_user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long authId;
    Long companyId;
    String firstName;
    String lastName;
    String phoneNo;
    String tcNo;
    @Column(unique = true, nullable = false)
    String email;
    LocalDate birthDate;
    String avatarUrl;
    @Enumerated(EnumType.STRING)
    EGender gender;

    @Builder.Default
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<Address> addressList = new ArrayList<>();

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    Set<Role> roles = new HashSet<>();
    @Builder.Default
    Boolean isOnline = false;



}