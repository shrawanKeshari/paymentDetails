package com.test.api.paymentDetails.controller;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.api.paymentDetails.beans.PaymentDetails;
import com.test.api.paymentDetails.dao.detailsDao;
import com.test.api.paymentDetails.pojo.PaymentRequest;
import com.test.api.paymentDetails.pojo.PaymentResponse;
import com.test.api.paymentDetails.pojo.PaymentResponse.Response;

@Controller
@RequestMapping(value = "/payment")
public class PaymentController {

	@Autowired
	detailsDao paymentDetailsDao;

	private static final String SUCCESS_STATUS = "SUCCESS";
	private static final String FAILURE_STATUS = "FAILURE";
	private static final String CODE_FAILURE = "101";
	private static final String CODE_SUCCESS = "100";
	
	private static final Logger LOGGER = Logger.getLogger(PaymentController.class);

	@RequestMapping(value = "/pay", method = RequestMethod.POST)
	public @ResponseBody PaymentResponse pay(@RequestBody PaymentRequest request) {
		PaymentResponse paymentResponse = new PaymentResponse();
		
		Response response = new Response();
		
		LOGGER.info("Into the pay method");
		
		int userId = request.getRequest().getUserId();
		String itemId =request.getRequest().getItemId();
		try {
			PaymentDetails paymentDetails = paymentDetailsDao.getItemIdUSerId(userId, itemId);

			paymentResponse.setStatus(SUCCESS_STATUS);
			paymentResponse.setCode(CODE_SUCCESS);
			response.setUserId(paymentDetails.getUserId());
			response.setItemId(paymentDetails.getItemId());
			response.setDiscount(paymentDetails.getDiscount());
			response.setCurrency(paymentDetails.getCurrency());
		} catch (Exception e) {
			paymentResponse.setStatus(FAILURE_STATUS);
			paymentResponse.setCode(CODE_FAILURE);
		}
		
		paymentResponse.setResponse(response);

		return paymentResponse;
	}

	@RequestMapping(value = "/addItem", method = RequestMethod.POST)
	public @ResponseBody PaymentResponse addItem(@RequestBody PaymentRequest request) {
		PaymentResponse paymentResponse = new PaymentResponse();
		
		Response response = new Response();
		
		LOGGER.info("Into the addItem method");
		
		try {

			PaymentDetails paymentDetails = new PaymentDetails();
			paymentDetails.setUserId(request.getRequest().getUserId());
			paymentDetails.setItemId(request.getRequest().getItemId());
			paymentDetails.setDiscount(request.getRequest().getDiscount());
			paymentDetails.setCurrency("INR");

			paymentDetailsDao.insertData(paymentDetails);

			paymentResponse.setStatus(SUCCESS_STATUS);
			paymentResponse.setCode(CODE_SUCCESS);
			response.setUserId(paymentDetails.getUserId());
			response.setItemId(paymentDetails.getItemId());
			response.setDiscount(paymentDetails.getDiscount());
			response.setCurrency(paymentDetails.getCurrency());
		} catch (Exception e) {
			paymentResponse.setStatus(FAILURE_STATUS);
			paymentResponse.setCode(CODE_FAILURE);
		}
		
		paymentResponse.setResponse(response);
		
		return paymentResponse;
	}

	@RequestMapping(value = "/deleteItem", method = RequestMethod.GET)
	public @ResponseBody PaymentResponse deleteItem(@RequestParam(value = "userId") int userId) {
		PaymentResponse paymentResponse = new PaymentResponse();

		Response response = new Response();
		
		LOGGER.info("Into the deleteItem method");
		
		try {
			paymentDetailsDao.delete(userId);

			paymentResponse.setStatus(SUCCESS_STATUS);
			paymentResponse.setCode(CODE_SUCCESS);
		} catch (Exception e) {
			paymentResponse.setStatus(FAILURE_STATUS);
			paymentResponse.setCode(CODE_FAILURE);
		}
		
		paymentResponse.setResponse(response);

		return paymentResponse;
	}

	@RequestMapping(value = "/updateItem", method = RequestMethod.POST)
	public @ResponseBody PaymentResponse updateItem(@RequestBody PaymentRequest request) {
		PaymentResponse paymentResponse = new PaymentResponse();
		
		Response response = new Response();
		
		LOGGER.info("Into the updateItem method");

		try {

			PaymentDetails paymentDetails = new PaymentDetails();
			paymentDetails.setUserId(request.getRequest().getUserId());
			paymentDetails.setItemId(request.getRequest().getItemId());
			paymentDetails.setDiscount(request.getRequest().getDiscount());

			paymentDetailsDao.updateData(paymentDetails);

			paymentResponse.setStatus(SUCCESS_STATUS);
			paymentResponse.setCode(CODE_SUCCESS);
			response.setUserId(paymentDetails.getUserId());
			response.setItemId(paymentDetails.getItemId());
			response.setDiscount(paymentDetails.getDiscount());
			response.setCurrency("INR");
		} catch (Exception e) {
			paymentResponse.setStatus(FAILURE_STATUS);
			paymentResponse.setCode(CODE_FAILURE);
		}
		
		paymentResponse.setResponse(response);

		return paymentResponse;
	}
}
