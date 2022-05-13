package com.au.swinburne.dapr.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ResponseGeneratorResponse {

	private String id;
	private Integer responseType;
}
