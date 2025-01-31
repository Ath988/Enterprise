package com.bilgeadam.view;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VwDepartment {

    public VwDepartment(Long departmentId, String departmentName) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }
    public VwDepartment() {

    }

        Long departmentId;
        String departmentName;
        VwEmployee manager;
        List<VwEmployee> employees;



}
