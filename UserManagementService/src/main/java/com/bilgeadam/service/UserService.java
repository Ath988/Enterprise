package com.bilgeadam.service;

import com.bilgeadam.dto.request.CreateMemberRequest;
import com.bilgeadam.dto.request.otherServices.AddSubscriptionRequest;
import com.bilgeadam.dto.request.otherServices.ManageEmployeePermissionsRequest;
import com.bilgeadam.dto.response.AdminDetailsForChatResponse;
import com.bilgeadam.dto.response.UserDetailsForChatResponse;
import com.bilgeadam.dto.response.UserPermissionResponse;
import com.bilgeadam.dto.response.UserProfileResponse;
import com.bilgeadam.dto.response.otherServices.VwDepartmendAndPosition;
import com.bilgeadam.entity.Address;
import com.bilgeadam.entity.Role;
import com.bilgeadam.entity.User;
import com.bilgeadam.entity.UserRolePermission;
import com.bilgeadam.entity.enums.Permission;
import com.bilgeadam.entity.enums.SubscriptionPlan;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.UserManagementException;
import com.bilgeadam.manager.DepartmentManager;
import com.bilgeadam.manager.SubscriptionManager;
import com.bilgeadam.repository.AddressRepository;
import com.bilgeadam.repository.UserRepository;
import com.bilgeadam.utility.JwtManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.bilgeadam.dto.response.BaseResponse.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserRolePermissionService userRolePermissionService;
    private final JwtManager jwtManager;
    private final SubscriptionManager subscriptionManager;
    private final AddressRepository addressRepository;
    private final DepartmentManager departmentManager;

    /*
       Siteye ilk defa üye olan kişi için.
     */
    @Transactional
    public Boolean createMember(CreateMemberRequest dto) {
        Role role = roleService.findByName("MEMBER");

        User user = User.builder()
                .authId(dto.authId())
                .companyId(dto.authId()) // ilk üyenin companyId(memberId) authId si neyse o olsun.
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .roles(Set.of(role))
                .build();


        userRepository.save(user);
        String subscriptionToken = jwtManager.createTokenForSubscription(user.getAuthId(), Set.of("MEMBER"),Set.of("MANAGE_USERS_AND_SUBSCRIPTIONS"))
                        .orElseThrow(()->new UserManagementException(ErrorType.INVALID_TOKEN));
        subscriptionManager //Todo: SubscriptionPlan dtodan gelmeli
                .addSubscription(new AddSubscriptionRequest(subscriptionToken,user.getId(), SubscriptionPlan.ENTERPRISE));

        UserRolePermission userRolePermission = UserRolePermission.builder()
                .user(user)
                .role(role)
                .permissions(Set.of(Permission.ACCESS_ALL_MODULES)) //bütün izinlere sahip
                .build();

        userRolePermissionService.save(userRolePermission);

        return true;
    }

    /**
     * İlk member kaydı açan User dışındaki çalışanların kaydı buradan açılacak.
     */
    @Transactional
    public Boolean createUser(String token,CreateMemberRequest dto) {

        User manager = getUserByToken(token);
        Role role = roleService.findByName("STAFF"); //Çalışan rolü için
        Address address = Address.builder().build();
        addressRepository.save(address);

        User user = User.builder()
                .authId(dto.authId())
                .companyId(manager.getCompanyId())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .roles(Set.of(role))
                .build();
        user.getAddressList().add(address);
        userRepository.save(user);

        Set<Permission> permissions = userRolePermissionService.getPermissionsForEmployee();

        UserRolePermission userRolePermission = UserRolePermission.builder()
                .role(role)
                .user(user)
                .permissions(permissions)
                .build();
        userRolePermissionService.save(userRolePermission);

        return true;
    }


    //Todo: Address kısmı sonra eklenicek.
    public UserProfileResponse getUserProfile(String token) {
        User user = getUserByToken(token);
        UserProfileResponse response = userRepository.findUserProfileById(user.getId());

        VwDepartmendAndPosition vwDepartmendAndPosition =
                getDataFromResponse(departmentManager.getPositionAndDepartmentName(token));
        response.setDepartmentName(vwDepartmendAndPosition.departmentName());
        response.setPositionName(vwDepartmendAndPosition.positionName());

        return response;
    }


    public User getUserByToken(String token) {

        Long authId = jwtManager.validateToken(token.substring(7))
                .orElseThrow(() -> new UserManagementException(ErrorType.INVALID_TOKEN));
        return userRepository.findByAuthId(authId)
                .orElseThrow(() -> new UserManagementException(ErrorType.USER_NOT_FOUND));
    }

    public UserPermissionResponse findUserPermissionResponse(Long authId) {
        User user = userRepository.findByAuthId(authId)
                .orElseThrow(() -> new UserManagementException(ErrorType.USER_NOT_FOUND));
        Set<String> userRolePermission = userRolePermissionService.findAllPermissionsByUserId(user.getId());
        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());

        String subscriptionType = getSubscriptionType(user.getId());

        return new UserPermissionResponse(roles, userRolePermission, subscriptionType);
    }



    private String getSubscriptionType(Long userId) {
        Long memberId = getMemberIdFromUserId(userId);
        SubscriptionPlan subscriptionPlan = getDataFromResponse(subscriptionManager.getUserSubscriptionPlan(memberId));
        return subscriptionPlan.name();
    }

    private Long getMemberIdFromUserId(Long userId) {
       return userRepository.findMemberIdByUserId(userId,"MEMBER")
               .orElseThrow(()->new UserManagementException(ErrorType.MEMBER_NOT_FOUND));
    }

    @Transactional
    public Boolean updateUserPermissionsByAuthIdList(ManageEmployeePermissionsRequest dto){
        List<UserRolePermission> permissionsList = userRolePermissionService.findByAuthIdList(dto.idList());
        List<String> grandtedPermissions = dto.grantedPermissions(); //bu string değerler front endden enumlara uygun olacak şekilde gelecek.
        Set<Permission> grantedPermissionEnums = grandtedPermissions.stream().map(Permission::valueOf).collect(Collectors.toSet());
        grantedPermissionEnums.addAll(Set.of(Permission.ACCESS_PROFILE,Permission.ACCESS_CALENDAR_AND_CHAT)); // Her halükarda bunlar eklensin.
        for (UserRolePermission userRolePermission : permissionsList) {
            userRolePermission.setPermissions(grantedPermissionEnums);
        }
        userRolePermissionService.saveAll(permissionsList);
        return true;

    }
    
    //CHATSERVICE-1
    public UserDetailsForChatResponse getUserDetailForChat(Long userId) {
        User user = userRepository.findById(userId)
                                              .orElseThrow(() -> new UserManagementException(ErrorType.USER_NOT_FOUND));
        
        return new UserDetailsForChatResponse(
                userId, user.getCompanyId(), user.getFirstName(), user.getLastName(),
                user.getAvatarUrl(), user.getIsOnline());
    }
    
    //CHATSERVICE-2
    public List<UserDetailsForChatResponse> getUsersDetailByCompanyId(Long userId, Long companyId){
        return userRepository.findAllActiveUsersByCompanyId(companyId,userId);
    }
    
    //CHATSERVICE-3
    public List<UserDetailsForChatResponse> getUsersDetailByIds(List<Long> ids) {
        return userRepository.findUsersByIds(ids);
    }
    
    //CHATSERVICE-4
    public boolean setUserOnlineStatus(Long userId, boolean status) {
        return userRepository.findById(userId).map(user -> {
            user.setIsOnline(status);
            userRepository.save(user);
            return true;
        }).orElse(false);
    }


    public List<AdminDetailsForChatResponse> getAdminsForChat() {
        Role adminRole = roleService.findByName("SYSTEM_ADMIN");
        return userRepository.findTop10ByRolesIn(List.of(adminRole));
    }
}