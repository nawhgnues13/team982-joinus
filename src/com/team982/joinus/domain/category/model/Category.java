package com.team982.joinus.domain.category.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Category {
	private int categoryId;
	private String categoryName;
	private Integer parentId;
}
