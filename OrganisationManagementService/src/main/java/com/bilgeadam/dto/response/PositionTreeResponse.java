package com.bilgeadam.dto.response;

import com.bilgeadam.view.VwPosition;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PositionTreeResponse {
	
	@JsonProperty("companyId")
	Long companyId;
	@JsonProperty("companyName")
	String companyName;
	@JsonProperty("CEO")
	String CEO;
	@JsonProperty("positions")
	List<VwPosition> positions;
	@JsonProperty("avatarUrl")
	String avatarUrl;
}