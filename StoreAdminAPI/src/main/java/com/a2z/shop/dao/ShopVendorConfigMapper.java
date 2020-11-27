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

import com.a2z.shop.model.VendorConfig;

@Component
public class ShopVendorConfigMapper implements ResultSetExtractor<ArrayList<VendorConfig>>  {

	private static final Logger logger = LoggerFactory.getLogger(ShopVendorConfigMapper.class);
	
	@Autowired
	ArrayList<VendorConfig> vendorConfigs;
	
	
	@Override
	public ArrayList<VendorConfig> extractData(ResultSet rs) throws SQLException, DataAccessException {
		// TODO Auto-generated method stub
		
		logger.info("extractData()....COUNT of RS:"+rs.getFetchSize());
		if(rs.next()) {
			VendorConfig storeCfg = new VendorConfig();
			//String[] categories = rs.getString(i++).split(",");
			//logger.info("categories::"+categories);
			storeCfg.setVendorId(rs.getString("vendor_id"));
			storeCfg.setCategories(rs.getString("categories"));
			storeCfg.setDeliveryFlag(rs.getByte("delivery_flag"));
			storeCfg.setDeliveryStartTime(rs.getTime("delivery_start_time"));
			storeCfg.setDeliveryEndTime(rs.getTime("delivery_end_time"));
			storeCfg.setStoreStartTime(rs.getTime("store_hours_start"));
			storeCfg.setStoreEndTime(rs.getTime("store_hours_end"));
			storeCfg.setStoreOpenDays(rs.getString("store_days"));
			storeCfg.setShopName(rs.getString("shop_name"));
			storeCfg.setCategoryDescription(rs.getString("shop_cat_desc"));
			storeCfg.setStatus(rs.getString("shop_status"));
			storeCfg.setDeliveryLocationId(rs.getString("delivery_loc_id"));
			storeCfg.setPhone(rs.getString("phone"));
			storeCfg.setEmail(rs.getString("email"));
			storeCfg.setAddress(rs.getString("address"));
			storeCfg.setShopLogoLink(rs.getString("shop_logo_link"));
			storeCfg.setAboutUs(rs.getString("about_us"));
			storeCfg.setFeaturesOne(rs.getString("features_one"));
			storeCfg.setFeaturesTwo(rs.getString("features_two"));
			storeCfg.setFeaturesThree(rs.getString("features_three"));
			storeCfg.setFeaturesFour(rs.getString("features_four"));
			storeCfg.setFeaturesFive(rs.getString("features_five"));
			storeCfg.setFeaturesSix(rs.getString("features_six"));
			storeCfg.setAboutShopLogoLink(rs.getString("about_shop_logo_link"));
			storeCfg.setCountry_code(rs.getString("country_code"));
			//delivery_flag, delivery_start_time, delivery_end_time, delivery_loc_id, store_hours_start, store_hours_end, store_days
			vendorConfigs.add(storeCfg);
		}
		
		return vendorConfigs;
	}

}
