package com.bilgeadam.entity.enums;

import lombok.Getter;

@Getter
public enum EmployeeRole {
    CEO(1), //şirket sahibi,siteden üyeliği alan şahıs
    DIRECTOR(2),//şirket genel müdürü
    MANAGER(3),// departman müdürü
    STAFF(4) //departman normal çalışan
    ;

    EmployeeRole(int roleRank) {
        this.roleRank = roleRank;
    }

    int roleRank;
}
