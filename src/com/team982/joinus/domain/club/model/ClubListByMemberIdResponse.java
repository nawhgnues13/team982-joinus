package com.team982.joinus.domain.club.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClubListByMemberIdResponse {
	private int clubId;
	private String clubName;
}
