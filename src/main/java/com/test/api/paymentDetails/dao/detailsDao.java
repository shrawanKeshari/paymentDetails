package com.test.api.paymentDetails.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.test.api.paymentDetails.beans.PaymentDetails;

public class detailsDao {
	
	JdbcTemplate template;

	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}

	public int insertData(PaymentDetails paymentDetails) {
		String sql = "insert into paymentDetails (userId,itemId,discount,currency) values ("
				+ paymentDetails.getUserId() + ",'" + paymentDetails.getItemId() + "'," + paymentDetails.getDiscount()
				+ ",'" + paymentDetails.getCurrency() + "')";

		return template.update(sql);
	}

	public int updateData(PaymentDetails paymentDetails) {
		String sql = "update paymentDetails set discount=" + paymentDetails.getDiscount() + " where userId="
				+ paymentDetails.getUserId() + " and itemId='" + paymentDetails.getItemId() + "'";
		return template.update(sql);
	}

	public int delete(int userId) {
		String sql = "delete from paymentDetails where userId=" + userId + "";
		return template.update(sql);
	}

	public PaymentDetails getItemIdUSerId(int userId, String itemId) {
		String sql = "select * from paymentDetails where userId=? and itemId=?";
		return template.queryForObject(sql, new Object[] { userId, itemId },
				new BeanPropertyRowMapper<PaymentDetails>(PaymentDetails.class));
	}

	public List<PaymentDetails> getAllItem() {
		String sql = "select * from paymentDetails";
		return template.query(sql, new RowMapper<PaymentDetails>() {

			public PaymentDetails mapRow(ResultSet rs, int row) throws SQLException {
				PaymentDetails paymentDetails = new PaymentDetails();
				paymentDetails.setId(rs.getInt(1));
				paymentDetails.setUserId(rs.getInt(2));
				paymentDetails.setItemId(rs.getString(3));
				paymentDetails.setDiscount(rs.getDouble(4));
				paymentDetails.setCurrency(rs.getString(5));
				return paymentDetails;
			}

		});
	}
	
}
