package com.bilgeadam.dto.response.otherServices;

import java.util.Set;

public record UserPermissionResponse(

        Set<String> roles,
        Set<String> permissions,
        String subscriptionType
){
}
