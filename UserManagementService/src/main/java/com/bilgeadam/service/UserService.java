package com.bilgeadam.service;

import com.bilgeadam.dto.request.CreateMemberRequest;
import com.bilgeadam.dto.response.UserPermissionResponse;
import com.bilgeadam.dto.response.UserProfileResponse;
import com.bilgeadam.entity.Role;
import com.bilgeadam.entity.User;
import com.bilgeadam.entity.UserRolePermission;
import com.bilgeadam.entity.enums.Permission;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.UserManagementException;
import com.bilgeadam.repository.UserRepository;
import com.bilgeadam.utility.JwtManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserRolePermissionService userRolePermissionService;
    private final JwtManager jwtManager;

    /*
       Siteye ilk defa üye olan kişi için.
     */
    @Transactional
    public Boolean createMember(CreateMemberRequest dto){
        User user = User.builder()
                .authId(dto.authId())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .build();

        Role role = roleService.findByName("MEMBER");
        user.getRoles().add(role);

        userRepository.save(user);

        UserRolePermission userRolePermission = UserRolePermission.builder()
                .user(user)
                .role(role)
                .permissions(Set.of(Permission.ACCESS_ALL_MODULES))
                .build();

        userRolePermissionService.save(userRolePermission);

        return true;
    }

    //Todo: Geliştirilecek.
    public UserProfileResponse getUserProfile(String token){
        User user = getUserByToken(token);
        UserProfileResponse response = UserProfileResponse.builder()
                .id(user.getId())
                .authId(user.getAuthId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();

        return response;
    }


    public User getUserByToken(String token){

        Long authId = jwtManager.validateToken(token.substring(7))
                .orElseThrow(()->new UserManagementException(ErrorType.INVALID_TOKEN));
        return userRepository.findByAuthId(authId)
                .orElseThrow(()->new UserManagementException(ErrorType.USER_NOT_FOUND));
    }

    public UserPermissionResponse findUserPermissionResponse(Long authId){
        User user = userRepository.findByAuthId(authId)
                .orElseThrow(()->new UserManagementException(ErrorType.USER_NOT_FOUND));
        Set<String> userRolePermission = userRolePermissionService.findAllPermissionsByUserId(user.getId());
        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        return new UserPermissionResponse(roles,userRolePermission );
    }



}
