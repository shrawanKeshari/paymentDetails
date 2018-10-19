package com.test.api.paymentDetails.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.test.api.paymentDetails.pojo.PaymentRequest;
import com.test.api.paymentDetails.pojo.PaymentResponse;

@Controller
@RequestMapping("/payment")
public class PaymentController {

	@RequestMapping(value = "/pay", method = RequestMethod.POST)
	public @ResponseBody PaymentResponse pay(@RequestBody PaymentRequest request) {
		PaymentResponse response = new PaymentResponse();
		int userId = request.getUserId();
		String itemId = request.getItemId();
		double discount = request.getDiscount();
		
		response.setStatus("SUCCESS");
		response.setCode("200");
		response.setUserId(userId);
		response.setItemId(itemId);
		response.setDiscount(discount);
		response.setCurrency("INR");

		return response;
	}
}
