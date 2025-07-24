package com.team982.joinus.domain.participation.model;

import java.sql.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Participation {
	private int participationId;
    private int memberId;
    private int activityId;
    private Date participationDate;
}
