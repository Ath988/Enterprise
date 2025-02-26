package com.bilgeadam.dto.request.otherServices;

import java.util.List;

public record ManageEmployeePermissionsRequest(
        String organization, //Pozisyon mu yoksa departman üzerinden mi izinleri atayacak
        List<Long> idList, //Pozisyon ya da departmanların listesi.
        List<String> grantedPermissions //Yeni tanımlanan izinlerin listesi. Permissions enumı ile tutarlı olmalı.
) {
}
