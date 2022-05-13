package com.au.swinburne.configurator.dapr.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
public class ConfigurationRequest {

	private String key;
	private String value;
	private String service;
	
}
