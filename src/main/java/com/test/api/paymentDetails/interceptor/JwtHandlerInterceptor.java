package com.test.api.paymentDetails.interceptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.test.api.paymentDetails.helpers.JsonHelper;
import com.test.api.paymentDetails.pojo.PaymentResponse;

@Component
public class JwtHandlerInterceptor implements HandlerInterceptor {

	private static final String jwtTimestamp = "ts";
	private static final String jwtClientId = "clientId";
	private static final String jwtSceretKey = "1234567898876543234569877";
	private final long jwtWindow = 1000 * 60 * 5;
	private static final String jwtHeader = "jwtToken";

	private static final Logger LOGGER = Logger.getLogger(JwtHandlerInterceptor.class);

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String jwtToken = request.getHeader(jwtHeader);
		int errorType = -1;
		String responseMessage = null;

		LOGGER.info("Into the pre handler method");
		System.out.println("Into the pre handler method");

		try {
			String clientId = getClientIdAndVerifyToken(jwtToken);
			validateClientId(clientId);
		} catch (IllegalArgumentException e) {
			errorType = HttpServletResponse.SC_FORBIDDEN;
			responseMessage = e.getMessage();
		} catch (UnsupportedEncodingException e) {
			errorType = HttpServletResponse.SC_FORBIDDEN;
			responseMessage = e.getMessage();
		} catch (Exception e) {
			errorType = HttpServletResponse.SC_FORBIDDEN;
			responseMessage = e.getMessage();
		}

		if (errorType != -1) {
			errorResponse(String.valueOf(errorType), response, responseMessage);
			LOGGER.info("Error in validating request " + request.getRequestURI() + " ErrorType= " + errorType
					+ " Message " + responseMessage);
			return false;
		}

		return true;
	}

	private void validateClientId(String clientId) {
		LOGGER.info("going to verify client id");
		if (clientId == null || clientId.equals("")) {
			throw new IllegalArgumentException("client authentication failed");
		} else if (!clientId.equals("a5516f104428408fb6051f833c9bb9e0")) {
			throw new IllegalArgumentException("client authentication failed");
		}
		LOGGER.info("client authenticated successfully");
	}

	private String getClientIdAndVerifyToken(String jwtToken)
			throws IllegalArgumentException, UnsupportedEncodingException {

		LOGGER.info("going to verify jwt token");

		String clientId = null;

		Algorithm algorithm = Algorithm.HMAC256(jwtSceretKey);
		JWTVerifier jwtVerifier = JWT.require(algorithm).build();
		DecodedJWT decodedJWT = jwtVerifier.verify(jwtToken);
		Map<String, Claim> claimMap = decodedJWT.getClaims();
		validateTimestamp(claimMap.get(jwtTimestamp).asString());
		clientId = claimMap.get(jwtClientId).asString();

		return clientId;
	}

	private void validateTimestamp(String timeStamp) throws IllegalArgumentException {
		LOGGER.info("Timestamp value is : " + timeStamp);
		if (timeStamp == null || timeStamp.equals("")) {
			throw new IllegalArgumentException("invalid timestamp");
		} else if (Math.abs(System.currentTimeMillis() - Long.parseLong(timeStamp)) > jwtWindow) {
			throw new IllegalArgumentException("invalid timestamp");
		}
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
		LOGGER.info("Into the post handler method");
		System.out.println("into post handler");

	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		LOGGER.info("Into the completion method");
		System.out.println("into completion");

	}

}
