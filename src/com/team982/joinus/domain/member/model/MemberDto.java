package com.team982.joinus.domain.member.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberDto {
	private String email;
	private String password;
	private String memberName;
}
