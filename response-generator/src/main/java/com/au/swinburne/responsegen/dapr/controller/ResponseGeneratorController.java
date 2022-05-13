package com.au.swinburne.responsegen.dapr.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.au.swinburne.responsegen.dapr.constants.ResponseGeneratorConstants;
import com.au.swinburne.responsegen.dapr.dto.ResponseGeneratorRequest;
import com.au.swinburne.responsegen.dapr.dto.ResponseGeneratorResponse;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ResponseGeneratorController {
    
    private final ConfigService configService;

    // TODO add a scheduler to update state every minute
    @PostMapping(value = "/process")
    public ResponseEntity<ResponseGeneratorResponse> processResponse(@RequestBody ResponseGeneratorRequest request) throws Exception {
    	
    	String id = request.getId();
    	log.info("id : " + id);
    	// from state store, fetch all keys
        Map<String, Integer> configMap = configService.getConfigMap();
        Integer http_200_range = configMap.get("http_200_range");
        Integer http_204_range = configMap.get("http_204_range");
        Integer http_400_range = configMap.get("http_400_range");
        Integer http_404_range = configMap.get("http_404_range");
        Integer http_500_range = configMap.get("http_500_range");
        
    	// generate random number between 1 to 100
    	
    	Random r = new Random();
    	int lowRate = 1;
    	int highRate = 100;
    	int httpCodeProbabilityValue = (r.nextInt(highRate-lowRate) + lowRate);
    	configMap.forEach((key,value) -> {
    		log.info("conf key={}, conf value={}",key, value);
    	});
    	// ADD HTTP RESPONSE CODE
    	Integer httpCode = null;
    	
    	if(httpCodeProbabilityValue > 0 && httpCodeProbabilityValue <= http_200_range ) {
    		httpCode = 200;
    	} 
    	else if(httpCodeProbabilityValue > http_200_range && httpCodeProbabilityValue <= http_204_range) {
    		httpCode = 204;

    	}
    	else if(httpCodeProbabilityValue > http_204_range && httpCodeProbabilityValue <= http_400_range) {
    		httpCode = 400;

    	}
    	else if(httpCodeProbabilityValue > http_400_range && httpCodeProbabilityValue <= http_404_range) {
    		httpCode = 404;

    	}
    	else if(httpCodeProbabilityValue > http_404_range && httpCodeProbabilityValue <= http_500_range) {
    		httpCode = 500;

    	}
    	// ADD DELAY
    	Integer delay_in_sec = configMap.get("delay_in_sec");
        Integer instance_count = configMap.get("instance_count");
        
    	double httpCodeProbabilityRate = ((double)httpCodeProbabilityValue/100) * ((double)delay_in_sec/instance_count);
    	int latencyInMillis = 	(int)(httpCodeProbabilityRate * 1000);

    	log.info("latencyInMillis={}",latencyInMillis);
    	log.info("httpCodeProbabilityValue={}",httpCodeProbabilityValue);
    	try {
    		Thread.sleep(latencyInMillis);
    	} catch(InterruptedException ex) {
    		ex.printStackTrace();
    	}
    	ResponseGeneratorResponse response = new ResponseGeneratorResponse(id, httpCode);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}