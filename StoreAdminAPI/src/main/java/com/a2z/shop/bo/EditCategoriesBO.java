package com.a2z.shop.bo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.a2z.shop.Category.Category;
import com.a2z.shop.eo.EditCategoriesEO;

@Component
public class EditCategoriesBO {
	public static final Logger logger = LoggerFactory.getLogger(EditCategoriesBO.class);
	@Autowired
	
	EditCategoriesEO editCategoriesEO;
    String Vendor_Id;


	public String addCategoryProducts(Category category) {
		
		logger.info("In EditCategoriesBO - EnterInDb()");
		String response = editCategoriesEO.addCategoryProducts(category);
		return response;
	}




public String getAllCategories(int vendorId) {
	String response = editCategoriesEO.getAllCategories(vendorId);

	return response;
}


}
