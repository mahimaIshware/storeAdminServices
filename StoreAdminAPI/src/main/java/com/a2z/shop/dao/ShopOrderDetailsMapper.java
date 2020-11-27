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

import com.a2z.shop.vo.CartDetailsVO;

@Component
public class ShopOrderDetailsMapper implements ResultSetExtractor<ArrayList<CartDetailsVO>>  {
	private static final Logger logger = LoggerFactory.getLogger(ShopOrderDetailsMapper.class);
		
	@Autowired
	ArrayList<CartDetailsVO> cartDetailsVOList;

	@Override
	public ArrayList<CartDetailsVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
		logger.info("extractData()....COUNT of RS:"+rs.getFetchSize());
		while(rs.next()) {
			int i = 1;
			//product_id, product_name, product_description, product_price, product_quantity_avlbl
			CartDetailsVO cartDetailsVO = new CartDetailsVO();
			cartDetailsVO.setVendor_id(rs.getString("vendor_id"));
			cartDetailsVO.setOrder_id(rs.getString("order_id"));
			cartDetailsVO.setProduct_id(rs.getString("product_id"));
			cartDetailsVO.setProduct_name(rs.getString("product_name"));
			cartDetailsVO.setProduct_description(rs.getString("product_description"));
			cartDetailsVO.setProduct_price(rs.getDouble("product_price"));
			cartDetailsVO.setProduct_quantity(rs.getInt("product_quantity"));
			cartDetailsVO.setProduct_img_link(rs.getString("product_img_link"));
			cartDetailsVO.setTranID(rs.getString("unique_id"));
			cartDetailsVO.setPayment_status(rs.getString("payment_status"));
			cartDetailsVO.setPayment_type(rs.getString("payment_type"));
			cartDetailsVO.setOrder_status(rs.getString("order_status"));
			cartDetailsVO.setOrder_type(rs.getString("order_type"));
			cartDetailsVO.setUpdate_ts(rs.getDate("update_ts"));
			cartDetailsVO.setCreate_ts(rs.getDate("create_ts"));
			cartDetailsVO.setAddressID(rs.getString("address_id"));
			cartDetailsVO.setCartID(rs.getString("cart_id"));
			cartDetailsVO.setUserID(rs.getString("user_id"));
			cartDetailsVO.setAddressFirst(rs.getString("address_first"));
			cartDetailsVO.setFullName(rs.getString("full_name"));
			cartDetailsVO.setHouseNo(rs.getString("house_no"));
			cartDetailsVO.setAddressTwo(rs.getString("address_two"));
			cartDetailsVO.setCity(rs.getString("city"));
			cartDetailsVO.setPhone(rs.getString("phone"));
			cartDetailsVO.setCountry(rs.getString("country"));
			cartDetailsVO.setLandmark(rs.getString("landmark"));
			cartDetailsVO.setCreatedDate(rs.getDate("created_date"));
			cartDetailsVO.setUpdateDate(rs.getDate("update_date"));
			cartDetailsVO.setState(rs.getString("state"));
			cartDetailsVO.setZip(rs.getString("zip"));
			cartDetailsVOList.add(cartDetailsVO);
		}
		return cartDetailsVOList;
	}
	

}
