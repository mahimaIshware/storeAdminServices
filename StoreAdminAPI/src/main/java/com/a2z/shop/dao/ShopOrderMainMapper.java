package com.a2z.shop.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.a2z.shop.model.CartMain;

@Component
public class ShopOrderMainMapper implements ResultSetExtractor<ArrayList<CartMain>>   {

	@Autowired
	ArrayList<CartMain> cartMain;
	
	@Override
	public ArrayList<CartMain> extractData(ResultSet rs) throws SQLException, DataAccessException {
		// TODO Auto-generated method stub
		return cartMain;
	}


}
