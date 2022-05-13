package com.au.swinburne.dapr.dto;

import lombok.Data;

@Data
public class ConfigEvent {

	private String key;
	private String value;
	private String service;
}
