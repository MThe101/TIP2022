package com.au.swinburne.dapr.repository;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.au.swinburne.dapr.dto.IdGeneratorResponse;
import com.au.swinburne.dapr.dto.ResponseGeneratorResponse;
import com.au.swinburne.dapr.service.ConfigService;

import io.dapr.client.DaprClient;
import io.dapr.client.domain.HttpExtension;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ResponseGeneratorRepository {

	private final DaprClient daprClient;
    private final ConfigService configService;


	public Integer getResponse(IdGeneratorResponse id) {
		
        Map<String, String> configMap = configService.getConfigMap();

        String responseGeneratorAppId = configMap.get("responseGeneratorAppId");
        String  processMethodId = configMap.get("processMethodId");
		ResponseGeneratorResponse response = daprClient.invokeMethod(responseGeneratorAppId, processMethodId, 
				id, HttpExtension.POST, null, ResponseGeneratorResponse.class)
				.block();
		return response.getResponseType();
	}
}
