package com.team982.joinus.domain.club.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClubUpdateRequest {
	private String clubName;
	private String clubDescription;
	private int memberId;
	private int maxMemberCount;
}
