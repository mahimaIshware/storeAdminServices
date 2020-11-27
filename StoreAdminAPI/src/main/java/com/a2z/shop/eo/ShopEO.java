package com.a2z.shop.eo;

import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.a2z.shop.bo.ShopBO;
import com.a2z.shop.dao.ShopDAO;
import com.a2z.shop.model.CartAddress;
import com.a2z.shop.model.CartProduct;
import com.a2z.shop.model.Product;
import com.a2z.shop.model.User;
import com.a2z.shop.model.VendorConfig;
import com.a2z.shop.vo.CartDetailsVO;
import com.a2z.shop.vo.CartRequestVO;
import com.a2z.shop.vo.ProductReqVO;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class ShopEO {
	private static final Logger logger = LoggerFactory.getLogger(ShopBO.class);
	
	@Autowired
	private ShopDAO shopDAO;
	
	public boolean siDBUp()
	{
		logger.info("siDBUp....1");
		return shopDAO.isDBUp();
	}

	public String getProducts(String storeID) {
		ArrayList<Product> productList;
		String response = null;
		try{
			productList = shopDAO.getProducts(storeID);
			ObjectMapper objmapper = new ObjectMapper();
			response = objmapper.writeValueAsString(productList);
		}catch(Exception e)
		{
			logger.info("EXCEPTION IN EO - GET PRODUCTS: {}"+ e.getMessage(), e);
		}
		return response;
	}
	public String uploadProductsCSV(MultipartFile files, String storeID) {
		String response=null;
		try{
			response = shopDAO.uploadProductsCSV(files, storeID);
		}catch(Exception e)
		{
			logger.info("EXCEPTION IN EO - GET PRODUCTS CSV: {}"+ e.getMessage(), e);
		}
		return response;
	}
	public String getProductsCSV(String storeID) {
		String response=null;
		try{
			response = shopDAO.getProductsCSV(storeID);
		}catch(Exception e)
		{
			logger.info("EXCEPTION IN EO - GET PRODUCTS CSV: {}"+ e.getMessage(), e);
		}
		return response;
	}

	public String getStoreConfig(String storeID) {
		ArrayList<VendorConfig> configs;
		String response="";
		try{
			configs = shopDAO.getStoreConfig(storeID);
			ObjectMapper objmapper = new ObjectMapper();
			response = objmapper.writeValueAsString(configs);
		}catch(Exception e)
		{
			logger.info("EXCEPTION IN EO - GET STORE CONFIG: {}"+ e.getMessage(), "MESAGE");
		}
		return response;
	}

	public String getCategories(String storeID) {
		VendorConfig config;
		String response = null;
//		try{
//			config = shopDAO.getStoreConfig(storeID);
//			//ObjectMapper objmapper = new ObjectMapper();
//			response = config.getCategories();
//		}catch(Exception e)
//		{
//			logger.info("EXCEPTION IN EO - GET CATEGORIES: {}"+ e.getMessage(), "MESAGE");
//		}
		return response;
	}

	public String getCart(CartRequestVO req) {
		ArrayList<CartProduct> cartResponse;
		String response="";
		try{
			cartResponse = shopDAO.getCart(req);
			ObjectMapper objmapper = new ObjectMapper();
			response = objmapper.writeValueAsString(cartResponse);
		}catch(Exception e)
		{
			logger.info("EXCEPTION IN EO - GET STORE CONFIG: {}"+ e.getMessage(), "MESAGE");
		}
		return response;
	}

	public String addUpdateCart(CartRequestVO req) {
		StringBuilder responseBuilder=new StringBuilder("ERROR ADDING UPDATING TO CART");
		try{
			responseBuilder = shopDAO.addUpdateCart(req);
		}catch(Exception e)
		{
			logger.info("EXCEPTION IN EO - STORE - UPDATE CART: {}"+ e.getMessage(), "MESAGE");
		}
		return responseBuilder.toString();
	}

	public String addUpdateVendorConfig(VendorConfig req) {
		StringBuilder responseBuilder=new StringBuilder("ERROR ADDING UPDATING TO CONFIG");
		try{
			responseBuilder = shopDAO.addUpdateVendorConfig(req);
		}catch(Exception e)
		{
			logger.info("EXCEPTION IN EO - ADMIN - UPDATE CONFIG: {}"+ e.getMessage(), "MESAGE");
		}
		return responseBuilder.toString();
	}

	public String addUpdateProducts(ProductReqVO req) {
		StringBuilder responseBuilder=new StringBuilder("ERROR ADDING UPDATING TO PRODUCTS");
		try{
			responseBuilder = shopDAO.addUpdateProducts(req);
		}catch(Exception e)
		{
			logger.info("EXCEPTION IN EO - ADMIN - UPDATE PRODUCTS: {}"+ e.getMessage(), "MESAGE");
		}
		return responseBuilder.toString();
	}
	public String removeProduct(ProductReqVO req) {
		StringBuilder responseBuilder=new StringBuilder("ERROR REMOVING PRODUCT");
		try{
			responseBuilder = shopDAO.removeProduct(req);
		}catch(Exception e)
		{
			logger.info("EXCEPTION IN EO - ADMIN - REMOVE PRODUCT: {}"+ e.getMessage(), "MESAGE");
		}
		return responseBuilder.toString();
	}

	public String getProduct(String storeID, String id) {
		String responseBuilder="ERROR GETTING PRODUCT";
		try{
			Product product = (Product)shopDAO.getProduct(storeID,id);
			ObjectMapper objmapper = new ObjectMapper();
			responseBuilder= objmapper.writeValueAsString(product);
		}catch(Exception e)
		{
			logger.info("EXCEPTION IN EO - ADMIN - GETTING PRODUCT: {}"+ e.getMessage(), "MESAGE");
		}
		return responseBuilder;
	}

	public String deleteVendorConfig(String storeId) {
		StringBuilder responseBuilder=new StringBuilder("ERROR DELETING VENDOR CONFIG");
		try{
			responseBuilder = shopDAO.deleteVendorConfig(storeId);
		}catch(Exception e)
		{
			logger.info("EXCEPTION IN EO - STORE -DELETE VENDOR CONFIG: {}"+ e.getMessage(), "MESAGE");
		}
		return responseBuilder.toString();
	}

	public String getCategoryProducts(String storeID, String product_category) {
		ArrayList<Product> productList;
		String response = null;
		try{
			productList = shopDAO.getCategoryProducts(storeID,product_category);
			ObjectMapper objmapper = new ObjectMapper();
			response = objmapper.writeValueAsString(productList);
		}catch(Exception e)
		{
			logger.info("EXCEPTION IN EO - GET PRODUCTS: {}"+ e.getMessage(), e);
		}
		return response;
	}
	public String initCart(CartRequestVO req, Cookie[] ck, HttpServletResponse httpResponse) {
		ArrayList<CartRequestVO> cartResponse;
		String response="";
		try{
			cartResponse = shopDAO.initCart(req, ck, httpResponse);
			ObjectMapper objmapper = new ObjectMapper();
			response = objmapper.writeValueAsString(cartResponse);
		}catch(Exception e)
		{
			logger.info("EXCEPTION IN EO - GET STORE CONFIG: {}"+ e.getMessage(), "MESAGE");
		}
		return response;
	}

	public String deleteCartProduct(CartProduct req) {
		StringBuilder responseBuilder=new StringBuilder("ERROR DELETING CART TO PRODUCTS");
		try{
			responseBuilder = shopDAO.deleteCartProduct(req);
		}catch(Exception e)
		{
			logger.info("EXCEPTION IN EO - STORE - DELETING PRODUCTS: {}"+ e.getMessage(), "MESAGE");
		}
		return responseBuilder.toString();
	}
	public String addUpdateQuantity(CartProduct req) {
		StringBuilder responseBuilder=new StringBuilder("ERROR UPDATING CART PRODUCT QUANTITY");
		try{
			responseBuilder = shopDAO.addUpdateQuantity(req);
		}catch(Exception e)
		{
			logger.info("EXCEPTION IN EO - STORE - UPDATING PRODUCTS: {}"+ e.getMessage(), "MESAGE");
		}
		return responseBuilder.toString();
	}
	public String refreshCart(CartRequestVO req) {
		ArrayList<CartRequestVO> cartResponse;
		String response="";
		try{
			cartResponse = shopDAO.refreshCart(req);
			ObjectMapper objmapper = new ObjectMapper();
			response = objmapper.writeValueAsString(cartResponse);
		}catch(Exception e)
		{
			logger.info("EXCEPTION IN EO - REFRESHING CART ITEM: {}"+ e.getMessage(), "MESAGE");
		}
		return response;
	}

	public String getCartDeliveryAddress(CartAddress req) {
		ArrayList<CartAddress> cartAddress;
		String response="";
		try{
			cartAddress = shopDAO.getCartDeliveryAddress(req);
			ObjectMapper objmapper = new ObjectMapper();
			response = objmapper.writeValueAsString(cartAddress);
		}catch(Exception e)
		{
			logger.info("EXCEPTION IN EO - GET STORE CART DELIVERY ADDRESS: {}"+ e.getMessage(), "MESAGE");
		}
		return response;
	}

	public String addUpdateDeliveryAddress(CartAddress req) {
		StringBuilder responseBuilder=new StringBuilder("ERROR UPDATING CART DELIVERY ADDRESS");
		try{
			responseBuilder = shopDAO.addUpdateDeliveryAddress(req);
		}catch(Exception e)
		{
			logger.info("EXCEPTION IN EO - STORE - UPDATING CART DELIVERY ADDRESS: {}"+ e.getMessage(), "MESAGE");
		}
		return responseBuilder.toString();
	}

	public boolean isValidStore( String vendorId) {
		logger.info("store valodation......");
		return shopDAO.isValidStore(vendorId);
	}

	public String changeCartStatusToBilled(CartRequestVO cartRequestVo) {
		StringBuilder responseBuilder=new StringBuilder("ERROR UPDATING CART STATUS IN EO");
		try{
			responseBuilder = shopDAO.changeCartStatusToBilled(cartRequestVo);
		}catch(Exception e)
		{
			logger.info("EXCEPTION IN EO -CHANGE STORES CART SATATUS CONFIG: {}"+ e.getMessage(), "MESAGE");
		}
		return responseBuilder.toString();
	}

	public String getBilledCart(CartRequestVO req) {
		ArrayList<CartRequestVO> cartResponse;
		String response="";
		try{
			cartResponse = shopDAO.getBilledCart(req);
			ObjectMapper objmapper = new ObjectMapper();
			response = objmapper.writeValueAsString(cartResponse);
		}catch(Exception e)
		{
			logger.info("EXCEPTION IN EO - GET BILLED  CART: {}"+ e.getMessage(), "MESAGE");
		}
		return response;
	}

	public String getAdminDetailsByID(int userId) {
		User user = new User();
		String response="";
		try{
			user =  shopDAO.getAdminDetailsByID(userId);
			ObjectMapper objmapper = new ObjectMapper();
			response = objmapper.writeValueAsString(user);
		}catch(Exception e)
		{
			logger.info("EXCEPTION IN EO - GET ADMIN USER DETAILS: {}"+ e.getMessage(), "MESAGE");
		}
		return response;
	}

	public  String getAllBilledCart(CartRequestVO req) {
		ArrayList<CartRequestVO> cartResponse;
		String response="";
		try{
			cartResponse = shopDAO.getAllBilledCart(req);
			ObjectMapper objmapper = new ObjectMapper();
			response = objmapper.writeValueAsString(cartResponse);
		}catch(Exception e)
		{
			logger.info("EXCEPTION IN EO - GET STORE CONFIG: {}"+ e.getMessage(), "MESAGE");
		}
		return response;
	}

	public String getCartDetailsByID(String vendorId, String cartId) {
		ArrayList<CartDetailsVO> cartDetailsVoList = new ArrayList<CartDetailsVO>();
		String response = "ERROR IN CART DETAILS PRODUCTS";
		try {

			cartDetailsVoList = shopDAO.getCartDetailsByID(vendorId, cartId);
			ObjectMapper objectMapper = new ObjectMapper();
			response = objectMapper.writeValueAsString(cartDetailsVoList);

		} catch (Exception e) {
			logger.info("EXCEPTION IN EO - GET CART DETAILS: {}" + e.getMessage());
		}

		return response;
	}

}
