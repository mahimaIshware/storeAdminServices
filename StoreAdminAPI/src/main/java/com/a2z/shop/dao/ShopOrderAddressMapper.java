package com.a2z.shop.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.a2z.shop.model.CartAddress;

@Component
public class ShopOrderAddressMapper implements ResultSetExtractor<ArrayList<CartAddress>>  {
	private static final Logger logger = LoggerFactory.getLogger(ShopOrderAddressMapper.class);
		
	@Autowired
	ArrayList<CartAddress> cartDeliveryAddressList;

	@Override
	public ArrayList<CartAddress> extractData(ResultSet rs) throws SQLException, DataAccessException {
		logger.info("extractData()....COUNT of RS:"+rs.getFetchSize());
		while(rs.next()) {
			int i = 1;
			//product_id, product_name, product_description, product_price, product_quantity_avlbl
			CartAddress cartAddress = new CartAddress();
			cartAddress.setAddressID(rs.getString("address_id"));
			cartAddress.setCartID(rs.getString("cart_id"));
			cartAddress.setUserID(rs.getString("user_id"));
			cartAddress.setAddressFirst(rs.getString("address_first"));
			cartAddress.setFullName(rs.getString("full_name"));
			cartAddress.setHouseNo(rs.getString("house_no"));
			cartAddress.setAddressTwo(rs.getString("address_two"));
			cartAddress.setCity(rs.getString("city"));
			cartAddress.setPhone(rs.getString("phone"));
			cartAddress.setCountry(rs.getString("country"));
			cartAddress.setLandmark(rs.getString("landmark"));
			cartAddress.setCreatedDate(rs.getDate("created_date"));
			cartAddress.setUpdateDate(rs.getDate("update_date"));
			cartAddress.setState(rs.getString("state"));
			cartAddress.setZip(rs.getString("zip"));
			cartDeliveryAddressList.add(cartAddress);
		}
		return cartDeliveryAddressList;
	}
	

}
