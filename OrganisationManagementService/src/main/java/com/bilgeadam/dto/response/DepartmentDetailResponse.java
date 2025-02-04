package com.bilgeadam.dto.response;


import lombok.*;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Data
public class DepartmentDetailResponse{


    public DepartmentDetailResponse(Long departmentId,String departmentName,String description,String manager){
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.description = description;
        this.manager = manager;
    }

    public DepartmentDetailResponse(Long departmentId,String departmentName,String description,String manager,String parentDepartment){
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.description = description;
        this.manager = manager;
        this.parentDepartment = parentDepartment;
        this.positions = new ArrayList<>();
    }

    //Todo: Şirket ismi de eklenicek.
    Long departmentId;
    String departmentName;
    String description;
    String manager;
    String parentDepartment; //Bağlı olduğu birim
    List<String> positions;


}
