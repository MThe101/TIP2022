package com.au.swinburne.dapr.repository;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.au.swinburne.dapr.dto.IdGeneratorResponse;
import com.au.swinburne.dapr.service.ConfigService;

import io.dapr.client.DaprClient;
import io.dapr.client.domain.HttpExtension;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class IdGeneratorRepository {

    private final DaprClient daprClient;
    private final ConfigService configService;


	public IdGeneratorResponse getId() {
		
        Map<String, String> configMap = configService.getConfigMap();

		String idGeneratorAppId = configMap.get("idGeneratorAppId");
        String  idGeneratorProcessMethodId = configMap.get("idGeneratorProcessMethodId");
		
		IdGeneratorResponse idResponse = daprClient.invokeMethod(idGeneratorAppId, idGeneratorProcessMethodId, 
				null, HttpExtension.GET, null, IdGeneratorResponse.class)
				.block();
		return idResponse;
	}
}
