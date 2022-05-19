package com.au.swinburne.dapr.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.au.swinburne.dapr.config.ServiceConstants;
import com.au.swinburne.dapr.dto.SimulatorError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler({Exception.class})
	public ResponseEntity<SimulatorError> handleException( Exception exception, HttpServletRequest request){
		
		log.error("Exception received, exception={}",exception);
		SimulatorError error = new SimulatorError("API-500", ServiceConstants.SERVER_ERROR_MESSAGE);
		
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
