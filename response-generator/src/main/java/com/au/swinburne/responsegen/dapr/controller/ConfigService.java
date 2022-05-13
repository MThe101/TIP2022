package com.au.swinburne.responsegen.dapr.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.au.swinburne.responsegen.dapr.constants.ResponseGeneratorConstants;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.State;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConfigService {

	private static final Logger log = LoggerFactory.getLogger(ConfigService.class);

	private static Map<String, Integer> configMap = new HashMap<>();
	
	@Scheduled(fixedDelay = 30000)
	public void run() throws Exception {
		List<String> keys = ResponseGeneratorConstants.KEYS;
        List<State<Integer>> resultBulk = null;
    	try (DaprClient client = (new DaprClientBuilder()).build()) {
            String STATE_STORE_NAME = "responseconfigstore";
            resultBulk = client.getBulkState(STATE_STORE_NAME, keys, Integer.class).block();
    	}
    	log.info("configs retrieved, size= {})",resultBulk.size());
    	// get delay_in_sec and instance_count
    	for(State<Integer> state : resultBulk) {
    		log.info("key={}, value={}", state.getKey(), state.getValue());

    		configMap.put(state.getKey(), state.getValue());
    	}
    	Map<String, Integer> newConfigMap = new HashMap<>();

    	Integer delay_in_sec = null != configMap.get("delay_in_sec")?configMap.get("delay_in_sec"):2 ;
    	Integer instance_count = null != configMap.get("instance_count")?configMap.get("instance_count"):2 ;
    	Integer http_200_rate = null != configMap.get("http_200_rate") ? configMap.get("http_200_rate") : 0 ;
    	Integer http_204_rate = null != configMap.get("http_204_rate") ? configMap.get("http_204_rate") : 0 ;
    	Integer http_400_rate = null != configMap.get("http_400_rate") ? configMap.get("http_400_rate") : 0 ;
    	Integer http_404_rate = null != configMap.get("http_404_rate") ? configMap.get("http_404_rate") : 0 ;
    	Integer http_500_rate = null != configMap.get("http_500_rate") ? configMap.get("http_500_rate") : 0 ;
    	if((0 == http_200_rate 
    			&& 0 == http_204_rate 
    			&& 0 == http_400_rate 
    			&& 0 == http_404_rate 
    			&& 0 == http_500_rate) || 
    			http_200_rate + http_204_rate + http_400_rate + http_404_rate + http_500_rate !=100 ) {
    		http_200_rate = 20;
    		http_204_rate = 20;
    		http_400_rate = 20;
    		http_404_rate = 20;
    		http_500_rate = 20;
    	}
    	
    	Integer http_200_range = 0 + http_200_rate;
    	Integer http_204_range = http_200_range + http_204_rate;
    	Integer http_400_range = http_204_range + http_400_rate;
    	Integer http_404_range = http_400_range + http_404_rate;
    	Integer http_500_range = http_404_range + http_500_rate;
    	
    	newConfigMap.put("delay_in_sec", delay_in_sec);
    	newConfigMap.put("instance_count", instance_count);
    	newConfigMap.put("http_200_range", http_200_range);
    	newConfigMap.put("http_204_range", http_204_range);
    	newConfigMap.put("http_400_range", http_400_range);
    	newConfigMap.put("http_404_range", http_404_range);
    	newConfigMap.put("http_500_range", http_500_range);
    	
    	synchronized (this) {
			configMap = newConfigMap;
			log.info("config Map updated, size={}", configMap.size());

		}
	}
	
	public Map<String, Integer> getConfigMap(){
		log.info("config Map returned, size={}", configMap.size());
		return configMap;
	}
	
}
