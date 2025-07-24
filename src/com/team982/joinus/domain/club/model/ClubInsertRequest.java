package com.team982.joinus.domain.club.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor	
public class ClubInsertRequest {
	private int clubId;
	private String clubName;
	private int memberId;
	private int maxMemberCount;
}
