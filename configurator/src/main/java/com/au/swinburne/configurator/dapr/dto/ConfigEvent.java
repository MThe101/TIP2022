package com.au.swinburne.configurator.dapr.dto;

import lombok.Data;

@Data
public class ConfigEvent {

	private String key;
	private String value;
	private String service;
}
