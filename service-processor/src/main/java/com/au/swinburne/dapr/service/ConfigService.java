package com.au.swinburne.dapr.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.au.swinburne.dapr.config.ServiceConstants;
import com.au.swinburne.responsegen.dapr.constants.ResponseGeneratorConstants;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.State;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConfigService {

	private static final Logger log = LoggerFactory.getLogger(ConfigService.class);

	private static Map<String, String> configMap = new HashMap<>();
	
	@Scheduled(fixedDelay = 60000)
	public void run() throws Exception {
		List<String> keys = ServiceConstants.KEYS;
        List<State<String>> resultBulk = null;
    	try (DaprClient client = (new DaprClientBuilder()).build()) {
            String STATE_STORE_NAME = "serviceconfigstore";
            resultBulk = client.getBulkState(STATE_STORE_NAME, keys, String.class).block();
    	}
    	log.info("configs retrieved, size= {})",resultBulk.size());
    	// get delay_in_sec and instance_count
    	for(State<String> state : resultBulk) {
    		log.info("key={}, value={}", state.getKey(), state.getValue());

    		configMap.put(state.getKey(), state.getValue());
    	}
    	Map<String, String> newConfigMap = new HashMap<>();

    	String responseGeneratorAppId = null != configMap.get("responseGeneratorAppId")? configMap.get("responseGeneratorAppId"):"responsegenapp";
    	String responseGeneratorProcessMethodId = null != configMap.get("processMethodId")? configMap.get("processMethodId"):"process";
    	
    	String idGeneratorAppId = null != configMap.get("idGeneratorAppId")? configMap.get("idGeneratorAppId"):"idgenapp";
    	String idGeneratorProcessMethodId = null != configMap.get("generateIDMethodId")? configMap.get("generateIDMethodId"):"id";
    	
    	newConfigMap.put("responseGeneratorAppId", responseGeneratorAppId);
    	newConfigMap.put("processMethodId", responseGeneratorProcessMethodId);
    	newConfigMap.put("idGeneratorAppId", idGeneratorAppId);
    	newConfigMap.put("idGeneratorProcessMethodId", idGeneratorProcessMethodId);
    	
    	synchronized (this) {
			configMap = newConfigMap;
			log.info("Service config Map updated, size={}", configMap.size());
		}
	}
	
	public Map<String, String> getConfigMap(){
		log.info("Service config Map returned, size={}", configMap.size());
		return configMap;
	}
	
}
