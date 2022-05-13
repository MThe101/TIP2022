package com.au.swinburne.dapr.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.au.swinburne.dapr.client.OrderClient;
import com.au.swinburne.dapr.dto.IdGeneratorResponse;
import com.au.swinburne.dapr.dto.ServiceResponse;
import com.au.swinburne.dapr.repository.IdGeneratorRepository;
import com.au.swinburne.dapr.repository.ResponseGeneratorRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/simulator")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ServiceController {
    
    private final IdGeneratorRepository idGenRepo;
    private final ResponseGeneratorRepository responseGeneratorRepo;
    

    @GetMapping(value = "/service")
    public ResponseEntity<ServiceResponse> getOrders() {
    	
    	// Call ID generator 
    	IdGeneratorResponse id = idGenRepo.getId();
    	log.info("id : " + id);
    	// Call Response service
    	Integer responseType = responseGeneratorRepo.getResponse(id);
    	HttpStatus httpStatus = HttpStatus.valueOf(responseType);
    	ServiceResponse response = new ServiceResponse(id.getId(), responseType);
    	
    	// Return ResponseEntity
    	return new ResponseEntity<>(response, httpStatus);
    }

}