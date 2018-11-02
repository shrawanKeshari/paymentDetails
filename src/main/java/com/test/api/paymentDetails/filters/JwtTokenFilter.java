package com.test.api.paymentDetails.filters;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.test.api.paymentDetails.pojo.PaymentResponse;

public class JwtTokenFilter implements Filter {

	private static final String jwtTimestamp = "ts";
	private static final String jwtClientId = "clientId";
	private static final String jwtSceretKey = "1234567898876543234569877";
	private final long jwtWindow = 1000 * 60 * 5;
	private static final String jwtHeader = "jwtToken";

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String jwtToken = httpRequest.getHeader(jwtHeader);
		int errorType = -1;
		String responseMessage = null;
		
		if(!jwtToken.equals("1")) {
			errorType = HttpServletResponse.SC_FORBIDDEN;
			responseMessage = "error";
		}
//		try {
//			verifyTokenGetClientId(jwtToken);
//			chain.doFilter(request, response);
//		} catch (IllegalArgumentException e) {
//			errorType = HttpServletResponse.SC_FORBIDDEN;
//			responseMessage = e.getMessage();
//		} catch (UnsupportedEncodingException e) {
//			errorType = HttpServletResponse.SC_FORBIDDEN;
//			responseMessage = e.getMessage();
//		}

		if (errorType != -1) {
			errorResponse(String.valueOf(errorType), response, responseMessage);
		}
	}

	public void destroy() {

	}

	public String verifyTokenGetClientId(String jwtToken)
			throws IllegalArgumentException, UnsupportedEncodingException {

		String clientId = null;

		Algorithm algorithm = Algorithm.HMAC256(jwtToken);
		JWTVerifier jwtVerifier = JWT.require(algorithm).build();
		DecodedJWT decodedJWT = jwtVerifier.verify(jwtToken);
		Map<String, Claim> claimMap = decodedJWT.getClaims();
		clientId = claimMap.get(jwtClientId).asString();
		if (!validateTimeStamp(claimMap.get(jwtTimestamp).asString())) {
			throw new IllegalArgumentException("Invalid TimeStamp");
		}

		return clientId;

	}

	private boolean validateTimeStamp(String timeStamp) {
		if (timeStamp == null || timeStamp.equals("")) {
			return false;
		} else if (Math.abs(System.currentTimeMillis() - Long.parseLong(timeStamp)) > jwtWindow) {
			return false;
		}

		return true;
	}

	private void errorResponse(String errorType, ServletResponse response, String responseMessage) throws IOException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
		PaymentResponse paymentResponse = new PaymentResponse();
		if (responseMessage != null) {
			paymentResponse.setCode(errorType + "");
			paymentResponse.setStatus(responseMessage);
			httpResponse.getWriter().write(paymentResponse.toString());
		}
	}

	public String generateJwtToken() {

		try {
			JWTCreator.Builder jwtBuilder = JWT.create();
			jwtBuilder.withClaim(jwtTimestamp, String.valueOf(System.currentTimeMillis()));
			jwtBuilder.withClaim(jwtClientId, "a5516f104428408fb6051f833c9bb9e0");
			Algorithm algorithm = Algorithm.HMAC256(jwtSceretKey);
			return jwtBuilder.sign(algorithm);
		} catch (Exception e) {
			System.out.println("Exception while generating JWT token  : { " + e + " }");
		}

		return null;
	}

	public static void main(String[] args) {
		JwtTokenFilter jwtTokenFilter = new JwtTokenFilter();
		System.out.println(jwtTokenFilter.generateJwtToken());
	}
}
