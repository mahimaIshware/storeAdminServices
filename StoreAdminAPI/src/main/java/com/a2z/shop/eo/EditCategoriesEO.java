package com.a2z.shop.eo;

import java.util.ArrayList;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a2z.shop.Category.Category;
import com.a2z.shop.dao.EditCategoriesDAO;
import com.a2z.shop.model.Product;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EditCategoriesEO {
	public static Logger logger = LoggerFactory.getLogger(EditCategoriesEO.class);
	@Autowired
	EditCategoriesDAO editCategoriesDAO;
	String vendorId;
	@Autowired
	private transient DataSource datasource;

	public String addCategoryProducts(Category category) {
   
		logger.info("In EditCategoriesEO - EnterInDb()");
		String Response="Error Entering Categories Details";

		StringBuilder responseBuilder=new StringBuilder(" ADDING UPDATING USER");
		try{
			responseBuilder = new EditCategoriesDAO(datasource).addCategoryProducts(category);
		}catch(Exception e)
		{
			logger.info("EXCEPTION IN EO - Add Categories Info: {}"+ e.getMessage(), "MESAGE");
		}
		return responseBuilder.toString();
	}

	public String getAllCategories(int vendorId) {
		ArrayList<Category> productList;
		String response = null;
		try{
			productList = editCategoriesDAO.getAllCategories(vendorId);
			ObjectMapper objmapper = new ObjectMapper();
			response = objmapper.writeValueAsString(productList);
		}catch(Exception e)
		{
			logger.info("EXCEPTION IN EO - GET Categories: {}"+ e.getMessage(), e);
		}
		return response;
	}

}
