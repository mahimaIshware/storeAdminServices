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

import com.a2z.shop.model.Product;

@Component
public class ShopProductsMapper implements ResultSetExtractor<ArrayList<Product>> {
	private static final Logger logger = LoggerFactory.getLogger(ShopProductsMapper.class);
	
	@Autowired
	ArrayList<Product> productsList;
	
	@Override
	public ArrayList<Product> extractData(ResultSet rs) throws SQLException, DataAccessException {
	
		logger.info("extractData()....COUNT of RS:"+rs.getFetchSize());
		while(rs.next()) {
			int i = 1;
			Product product = new Product();
			product.setProduct_id(rs.getString(i++));
			product.setProduct_name(rs.getString(i++));
			product.setProduct_description(rs.getString(i++));
			product.setProduct_price(rs.getDouble(i++));
			product.setProduct_quantity_avlbl(rs.getInt(i++));
			product.setProduct_category(rs.getString(i++));
			product.setProduct_img_link(rs.getString(i++));
			product.setProduct_img_sqr_link(rs.getString(i++));
			productsList.add(product);
		}
		return productsList;
	}

}
