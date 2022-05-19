package com.au.swinburne.dapr.config;

import java.util.Arrays;
import java.util.List;

public class ServiceConstants {

	public static final List<String> KEYS = Arrays.asList("responseGeneratorAppId", "processMethodId","idGeneratorAppId","generateIDMethodId");
	public static final String SERVER_ERROR_MESSAGE = "Downstream services unavailable. Please try after some time";
}
