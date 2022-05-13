package com.au.swinburne.configurator.dapr.controller;

import static java.util.Collections.singletonMap;

import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.au.swinburne.configurator.dapr.dto.ConfigEvent;
import com.au.swinburne.configurator.dapr.dto.ConfigurationRequest;
import com.au.swinburne.configurator.dapr.dto.ConfigurationResponse;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.Metadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ConfigController {

	final String MESSAGE_TTL_IN_SECONDS = "1000";
	final String TOPIC_NAME = "configurations";
	final String PUBSUB_NAME = "simulator-pub-sub";

	@PostMapping(value = "/config")
	public ResponseEntity<ConfigurationResponse> processResponse(@RequestBody ConfigurationRequest request) throws Exception {

		// TODO add api to retrieve states

		log.info("request received : " + request);
		// Save state
		try (DaprClient client = (new DaprClientBuilder()).build()) {
			String STATE_STORE_NAME = "configstore";
			client.saveState(STATE_STORE_NAME, request.getKey(), request.getValue()).block();
		}

		// publish state

		TimeUnit.MILLISECONDS.sleep(5000);
		ConfigEvent event = new ConfigEvent();
		event.setKey(request.getKey());
		event.setValue(request.getValue());
		event.setService(request.getService());
		DaprClient client = new DaprClientBuilder().build();
		// Using Dapr SDK to publish a topic
		client.publishEvent(PUBSUB_NAME, TOPIC_NAME, event,
				singletonMap(Metadata.TTL_IN_SECONDS, MESSAGE_TTL_IN_SECONDS)).block();
		log.info("Published data, key={}:", request.getKey());

		// return response
		ConfigurationResponse response = new ConfigurationResponse("Success");
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

}
