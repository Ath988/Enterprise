package com.bilgeadam.dto.response;

import com.bilgeadam.entity.Department;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
public class DepartmentDetailResponse{

    public DepartmentDetailResponse() {

    }
    public DepartmentDetailResponse(Long departmentId,String departmentName,String description,String manager){
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.description = description;
        this.manager = manager;
    }

    //Todo: Şirket ismi de eklenicek.
    Long departmentId;
    String departmentName;
    String description;
    String manager;
    String parentDepartment; //Bağlı olduğu birim


}
