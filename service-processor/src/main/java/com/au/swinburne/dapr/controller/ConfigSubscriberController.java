package com.au.swinburne.responsegen.dapr.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.au.swinburne.dapr.dto.ConfigEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.dapr.Topic;
import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.CloudEvent;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ConfigSubscriberController {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	  @Topic(name = "configurations", pubsubName = "simulator-pub-sub")
	  @PostMapping(path = "/configure")
	  public Mono<Void> handleMessage(@RequestBody(required = false) CloudEvent<?> cloudEvent) {
	    return Mono.fromRunnable(() -> {
	      try {
	        log.debug("service api received event, data={}",cloudEvent.getData());
	        log.debug("event received, event={}", OBJECT_MAPPER.writeValueAsString(cloudEvent));
	        String id = cloudEvent.getId();
	        ConfigEvent configEvent = OBJECT_MAPPER.convertValue(cloudEvent.getData(), ConfigEvent.class);
	        try (DaprClient client = (new DaprClientBuilder()).build()) {
	            String STATE_STORE_NAME = "serviceconfigstore";
	            client.saveState(STATE_STORE_NAME, configEvent.getKey(), configEvent.getValue()).block();
	    	}
	      } catch (Exception e) {
	        throw new RuntimeException(e);
	      }
	    });
	  }
}
