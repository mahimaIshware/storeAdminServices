package com.a2z.shop.config;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

import com.a2z.shop.model.CartAddress;
import com.a2z.shop.model.CartMain;
import com.a2z.shop.model.CartProduct;
import com.a2z.shop.model.Product;
import com.a2z.shop.model.VendorConfig;
import com.a2z.shop.vo.CartDetailsVO;

@Configuration
public class ShopConfig {
	
	protected Logger logger = LoggerFactory.getLogger(ShopConfig.class);
			
	@Bean
	@RequestScope
	public ArrayList<Product> productList(){
		logger.info("---Generating Products Array List ---");
		return new ArrayList<Product>();
	}
	
	@Bean
	@RequestScope
	public ArrayList<CartProduct> productListOrder(){
		logger.info("---Generating Products Order Array List ---");
		return new ArrayList<CartProduct>();
	}
	
	@Bean
	@RequestScope
	public ArrayList<CartMain> cartMain(){
		logger.info("---Generating Products Order Array List ---");
		return new ArrayList<CartMain>();
	}
	
	@Bean
	@RequestScope
	public ArrayList<VendorConfig> vendorConfigs(){
		logger.info("---Generating storeCfg ---");
		return new ArrayList<VendorConfig>();
	}
	
	@Bean
	@RequestScope
	public ArrayList<CartAddress> cartDeliveryAddressList(){
		logger.info("---Generating storeCfg ---");
		return new ArrayList<CartAddress>();
	}
	@Bean
	@RequestScope
	public ArrayList<CartDetailsVO> cartDetailsVOList(){
		logger.info("---Generating storeCfg ---");
		return new ArrayList<CartDetailsVO>();
	}

}
