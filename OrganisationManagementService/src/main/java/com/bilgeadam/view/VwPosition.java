package com.bilgeadam.view;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VwPosition {
	
	public VwPosition(Long positionId, String positionName) {
		this.positionId = positionId;
		this.positionName = positionName;
	}
	public VwPosition() {
	
	}
	
	Long positionId;
	String positionName;
	List<VwEmployee> employees;
	List<VwPosition> subPositions;
}