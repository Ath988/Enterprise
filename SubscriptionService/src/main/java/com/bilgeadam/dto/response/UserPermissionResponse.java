package com.bilgeadam.dto.response;

import java.util.Set;

public record UserPermissionResponse(

        Set<String> roles,
        Set<String> permissions,
        String subscriptionType
){
}
