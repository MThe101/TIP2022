package com.au.swinburne.idgen.dapr.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.au.swinburne.idgen.dapr.client.OrderClient;
import com.au.swinburne.idgen.dapr.dto.IdGeneratorResponse;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class IdGeneratorController {

    private OrderClient orderClient;

    public IdGeneratorController(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    @GetMapping(value = "/id")
    public ResponseEntity<IdGeneratorResponse> getId() {
    	
    	int length = 10;
        boolean useLetters = true;
        boolean useNumbers = false;
        String uniqueId = RandomStringUtils.random(length, useLetters, useNumbers);
    	log.info("unique ID = {}", uniqueId);
        return new ResponseEntity<>(new IdGeneratorResponse(uniqueId), HttpStatus.OK);
    }

}