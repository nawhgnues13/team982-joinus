package com.team982.joinus.domain.activity.model;

import java.sql.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Activity {
	private int activityId;
	private String title;
	private String activityDescription;
	private Date activityDate; // java.util.Date 또는 java.sql.Date
	private String isDeleted; // 'Y' or 'N'
	private int clubId;
	private Integer parentId; // NULL 허용
	private int categoryId;

	private String parentTitle;
	private String categoryName;
	private String parentDescription;
	private Date parentDate;
	private String clubName;
}
