package com.a2z.shop.dao;

//import com.a2z.shop.model.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
//import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.a2z.shop.Category.Category;

@Component
public class EditCategoriesDAO {
	public static final Logger logger = org.slf4j.LoggerFactory.getLogger(EditCategoriesDAO.class);
	
	private transient static JdbcTemplate jdbcTemplate;
	@Autowired
	private transient DataSource datasource;
	
	Category category= new Category();
	int vendorId;

	public EditCategoriesDAO(DataSource datasource)throws SQLException {
		logger.info("Inside the test clean DAO");
		this.datasource = datasource;
		jdbcTemplate = new JdbcTemplate(this.datasource);
	}

	public StringBuilder addCategoryProducts(Category category) {
		StringBuilder response=new StringBuilder("Successfully adding Categories");
		logger.info("In TestCleanDAO - EnterInDb()");
		//String Vendor_Id="";
		String query="INSERT INTO vendor_category_details (categoryName,categoryDescription,vendorId,categoryStatus,create_ts,update_ts) VALUES(?,?,?,?,?,?)";
			jdbcTemplate.update((Connection connection)->{
				PreparedStatement st=connection.prepareStatement(query);
				st.setString(1,category.getCategoryName());                
				st.setString(2,category.getCategoryDescription());      
				st.setInt(3, category.getVendorId());
				st.setString(4, category.getCategoryStatus());
				st.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
				st.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
//				new Timestamp(System.currentTimeMillis());
//				 new Timestamp(System.currentTimeMillis());
				
				
				int i=st.executeUpdate();
				if(i>0)
				{
					logger.info("successfully inserted");
				}
			return st;
			});
		return response;
	}

	public ArrayList<Category> getAllCategories(int vendorId) {
		final String methodName = "getCategories()";
		logger.info("{}: Logger DB ", methodName);
		ArrayList<Category> categorysList= new ArrayList<Category>();
		String get_Category_Query="SELECT categoryName,categoryDetails,categoryStatus FROM Categories_add_details WHERE vendorId="+vendorId;
		try {
			logger.info("get_Category_Query:"+get_Category_Query+":"+vendorId);
			
			 jdbcTemplate.query(get_Category_Query,new Object[] {vendorId}, new RowMapper<Object>(){
				 public ArrayList<Category> mapRow(ResultSet rs,int rowNum) throws SQLException {
					 logger.info("extractData()....");
					while(rs.next()) 
					{
					    Category category = new Category();
					    
						category.setCategoryName(rs.getString("categoryName"));
						category.setCategoryDescription(rs.getString("categoryDescription"));
						category.setCategoryStatus(rs.getString("categoryStatus"));
						category.setVendorId(rs.getInt("vendorId"));
						categorysList.add(category);
					}
					return categorysList;
				}
			});
			logger.info("GETTING CATEGORIES..");
			
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	
		return null;
	}
}
