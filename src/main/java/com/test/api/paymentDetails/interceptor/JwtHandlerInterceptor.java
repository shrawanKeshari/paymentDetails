package com.test.api.paymentDetails.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.test.api.paymentDetails.helpers.JsonHelper;
import com.test.api.paymentDetails.pojo.PaymentResponse;

@Component
public class JwtHandlerInterceptor implements HandlerInterceptor {

//	private static final String jwtTimestamp = "ts";
//	private static final String jwtClientId = "clientId";
//	private static final String jwtSceretKey = "1234567898876543234569877";
//	private final long jwtWindow = 1000 * 60 * 5;
	private static final String jwtHeader = "jwtToken";
	
	private static final Logger LOGGER = Logger.getLogger(JwtHandlerInterceptor.class);
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String jwtToken = request.getHeader(jwtHeader);
		int errorType = -1;
		String responseMessage = null;

		LOGGER.debug("Into the pre handler method");
		System.out.println("Into the pre handler method");

		try {
			if (!jwtToken.equals("1")) {
				errorType = HttpServletResponse.SC_FORBIDDEN;
				responseMessage = "error";
			}
		} catch (Exception e) {
			errorType = HttpServletResponse.SC_FORBIDDEN;
			responseMessage = e.getMessage();
		}

		if (errorType != -1) {
			errorResponse(String.valueOf(errorType), response, responseMessage);
			return false;
		}

		return true;
	}

	private void errorResponse(String errorType, HttpServletResponse response, String responseMessage)
			throws IOException {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		JsonHelper<PaymentResponse> jsonHelper = new JsonHelper<PaymentResponse>();
		PaymentResponse paymentResponse = new PaymentResponse();
		paymentResponse.setCode(errorType + "");
		paymentResponse.setStatus(responseMessage);
		String paymentResponseJson = jsonHelper.toJson(paymentResponse);
		System.out.println(paymentResponseJson);
		response.getWriter().write(paymentResponseJson);
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		LOGGER.debug("Into the post handler method");
		System.out.println("into post handler");

	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		LOGGER.debug("Into the completion method");
		System.out.println("into completion");

	}

}
