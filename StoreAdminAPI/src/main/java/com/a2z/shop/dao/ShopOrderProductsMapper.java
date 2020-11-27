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

import com.a2z.shop.model.CartProduct;

@Component
public class ShopOrderProductsMapper implements ResultSetExtractor<ArrayList<CartProduct>>  {
	private static final Logger logger = LoggerFactory.getLogger(ShopOrderProductsMapper.class);
		
	@Autowired
	ArrayList<CartProduct> productListOrder;

	@Override
	public ArrayList<CartProduct> extractData(ResultSet rs) throws SQLException, DataAccessException {
		logger.info("extractData()....COUNT of RS:"+rs.getFetchSize());
		while(rs.next()) {
			/*
			 * int i = 1;
			 *  product_id, product_name, product_description, product_price,
			 * product_quantity_avlbl
			 */
			CartProduct product = new CartProduct();
			product.setVendor_id(rs.getString("vendor_id"));
			product.setOrder_id(rs.getString("order_id"));
			product.setProduct_id(rs.getString("product_id"));
			product.setProduct_name(rs.getString("product_name"));
			product.setProduct_description(rs.getString("product_description"));
			product.setProduct_price(rs.getDouble("product_price"));
			product.setProduct_quantity(rs.getInt("product_quantity"));
			product.setProduct_img_link(rs.getString("product_img_link"));
			//product.setProduct_category(rs.getString(i++));
			productListOrder.add(product);
		}
		return productListOrder;
	}
	

}
