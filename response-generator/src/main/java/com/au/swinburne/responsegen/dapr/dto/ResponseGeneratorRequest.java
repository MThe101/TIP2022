package com.au.swinburne.responsegen.dapr.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ResponseGeneratorRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	
}
