package com.test.api.paymentDetails.pojo;

import java.io.Serializable;

public class PaymentResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6740636175173304294L;
	
	private Response response;
	private String status;
	private String code;

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public static class Response {

		private int userId;
		private String itemId;
		private double discount;
		private String currency;

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}

		public String getItemId() {
			return itemId;
		}

		public void setItemId(String itemId) {
			this.itemId = itemId;
		}

		public double getDiscount() {
			return discount;
		}

		public void setDiscount(double discount) {
			this.discount = discount;
		}

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

	}

}
