package com.test.api.paymentDetails.helpers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper<T> {
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	public String toJson(T t) throws JsonGenerationException, JsonMappingException, IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		objectMapper.writeValue(byteArrayOutputStream, t);
		String jsonString = byteArrayOutputStream.toString();
		
		return jsonString;
	}
	
}
