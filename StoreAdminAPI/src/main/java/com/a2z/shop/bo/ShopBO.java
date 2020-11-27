package com.a2z.shop.bo;

import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.a2z.shop.eo.ShopEO;
import com.a2z.shop.model.CartAddress;
import com.a2z.shop.model.CartProduct;
import com.a2z.shop.model.VendorConfig;
import com.a2z.shop.vo.CartRequestVO;
import com.a2z.shop.vo.ProductReqVO;
import com.a2z.shop.vo.RspnsVO;

@Component
public class ShopBO {
	
	private static final Logger logger = LoggerFactory.getLogger(ShopBO.class);
	
	@Autowired
	private ShopEO shopEO;
	@Autowired
	BuildProperties buildProperties;
	
	public RspnsVO healthCheck()
	{
		final String methodName = "healthCheck()";
		logger.info("{}: Performing Health Check", methodName);
		RspnsVO rspnsVO = new RspnsVO();
		// Logic for Health Check
		if(!shopEO.siDBUp())
		{
			rspnsVO.setRspnsCode(700);
			rspnsVO.setRspnsMsg("DB DOWN");
			logger.info("DB DOWN....");
		}else {
		
			rspnsVO.setRspnsCode(1000);
			rspnsVO.setRspnsMsg("Health Check Successfull"+",BuildVersion:"+buildProperties.getVersion()+",BuildTime:"+buildProperties.getTime()+",BuildGroup:"+buildProperties.getGroup());
			logger.info("Health Check Successfull....1");
		}
		return rspnsVO;
	}

/*	public ProductReqVO getProducts(String tranID) {
		// TODO Auto-generated method stub
		ProductReqVO products = null;
				
				// CALL EO, DAO to GET Products
				// add to products.
		
		return products;
	}*/
	
	public String getProducts(String storeID)
	{
		String response = shopEO.getProducts(storeID);

		return response;
	}
	public String getProductsCSV(String storeID)
	{
		String response = shopEO.getProductsCSV(storeID);

		return response;
	}
	public String uploadProductsCSV(MultipartFile files, String storeID)
	{
		String response = shopEO.uploadProductsCSV(files, storeID);

		return response;
	}



	public String getStoreConfig(String storeID) {
		String response = shopEO.getStoreConfig(storeID);

		return response;
	}

	public String getCategories(String storeID) {
		String response = shopEO.getCategories(storeID);

		return response;
	}

	public String getCart(CartRequestVO req) {
		String response = shopEO.getCart(req);
		return response;
	}

	public String addUpdateCart(CartRequestVO req) {
		String response = shopEO.addUpdateCart(req);
		return response;
	}

	public String addUpdateVendorConfig(VendorConfig req) {
		String response = shopEO.addUpdateVendorConfig(req);
		return response;
	}

	public String addUpdateProducts(ProductReqVO req) {
		String response = shopEO.addUpdateProducts(req);
		return response;
	}

	public String removeProduct(ProductReqVO req) {
		String response = shopEO.removeProduct(req);
		return response;
	}
	public String getProduct(String storeID, String id) {
		String response = shopEO.getProduct(storeID, id);
		return response;
	}

	public String deleteVendorConfig(String storeId) {
		String response = shopEO.deleteVendorConfig(storeId);
		return response;	}

	public String getCategoryProducts(String storeID, String product_category) {
		String response = shopEO.getCategoryProducts(storeID,product_category);
		return response;
	}
	public String initCart(CartRequestVO req, Cookie[] ck, HttpServletResponse httpResponse) {
		String response = shopEO.initCart(req, ck, httpResponse);
		return response;
	}

	public String deleteCartProduct(CartProduct req) {
		String response = shopEO.deleteCartProduct(req);
		return response;
	}
	public String addUpdateQuantity(CartProduct req) {
		String response = shopEO.addUpdateQuantity(req);
		return response;
	}
	public String refreshCart(CartRequestVO req) {
		String response = shopEO.refreshCart(req);
		return response;
	}

	public String getCartDeliveryAddress(CartAddress req) {
		String response = shopEO.getCartDeliveryAddress(req);
		return response;
	}

	public String addUpdateDeliveryAddress(CartAddress req) {
		String response = shopEO.addUpdateDeliveryAddress(req);
		return response;
	}

	public RspnsVO isValidStore(String vendorId) {
		final String methodName = "isValidStore()";
		logger.info("{}: Performing store validation", methodName);
		RspnsVO rspnsVO = new RspnsVO();
		// Logic for Health Check
		if(shopEO.isValidStore(vendorId))
		{
			rspnsVO.setRspnsCode(1000);
			rspnsVO.setRspnsMsg("Store Exists!");
			logger.info("loading store info/configuration....");
		}else {
			rspnsVO.setRspnsCode(700);
			rspnsVO.setRspnsMsg("Store Not found!");
			logger.info("Store Not found Redirecting to shop setup and vendor registration page....");
		}
		return rspnsVO;
	}

	public String changeCartStatusToBilled(CartRequestVO cartRequestVo) {
		String response = shopEO.changeCartStatusToBilled(cartRequestVo);
		return response;
	}

	public String getBilledCart(CartRequestVO req) {
		String response = shopEO.getBilledCart(req);
		return response;
	}
	
	public String getAdminDetailsByID(int userId)
	{
		logger.info("In UserBO - getUser()");
		String response = shopEO.getAdminDetailsByID(userId);
		return response;
	}

	public String getAllBilledCart(CartRequestVO req) {
		String response = shopEO.getAllBilledCart(req);
		return response;
	}

	public String getCartDetailsByID(String vendorId, String cartId) {
		String response = shopEO.getCartDetailsByID(vendorId, cartId);
		return response;
	}
	
}
