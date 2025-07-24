package com.team982.joinus.domain.club.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClubDetailResponse {
	private int clubId;
	private String clubName;
	private String clubDescription;
	private String memberName;
	private int currentMemberCount;
	private int maxMemberCount;
}
