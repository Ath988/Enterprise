package com.bilgeadam.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EPerformanceGrade {
    A(5),
    B(4),
    C(3),
    D(2),
    E(1),

    ;


    int grade;
}
