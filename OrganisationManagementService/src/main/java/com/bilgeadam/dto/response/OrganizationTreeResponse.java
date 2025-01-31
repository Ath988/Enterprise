package com.bilgeadam.dto.response;

import com.bilgeadam.view.VwDepartment;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonPropertyOrder({"companyId","companyName","CEO","departments"})
public class OrganizationTreeResponse {

    @JsonProperty("companyId")
    Long companyId;
    @JsonProperty("companyName")
    String companyName;
    @JsonProperty("CEO")
    String CEO;
    @JsonProperty("departments")
    List<VwDepartment> departments;

}
