package com.its.module.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaginatedList<T> {
	private List<T> entities;
	private Integer total;
}
