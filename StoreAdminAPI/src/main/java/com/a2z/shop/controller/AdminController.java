package com.a2z.shop.controller;

import static com.a2z.shop.constant.ShopConstants.ADMIN_URL;
import static com.a2z.shop.constant.ShopConstants.DELETE_VENDOR_CONFIG_URL;
import static com.a2z.shop.constant.ShopConstants.GET_ALL_BIILED_CART_URL;
import static com.a2z.shop.constant.ShopConstants.GET_CART_DELIVERY_ADDRESS_URL;
import static com.a2z.shop.constant.ShopConstants.GET_CART_URL;
import static com.a2z.shop.constant.ShopConstants.GET_CSV_URL;
import static com.a2z.shop.constant.ShopConstants.GET_PRODUCTS_CATEGORY_URL;
import static com.a2z.shop.constant.ShopConstants.GET_PRODUCTS_MERGE_URL;
import static com.a2z.shop.constant.ShopConstants.GET_PRODUCTS_URL;
import static com.a2z.shop.constant.ShopConstants.GET_STORE_CONFIG_URL;
import static com.a2z.shop.constant.ShopConstants.GET_USER_BY_ID_URL;
import static com.a2z.shop.constant.ShopConstants.HEALTH_CHECK_URL;
import static com.a2z.shop.constant.ShopConstants.REMOVE_PRODUCTS_URL;
import static com.a2z.shop.constant.ShopConstants.STORE_ADMIN_VALIDITY_URL;
import static com.a2z.shop.constant.ShopConstants.UPDATE_PRODUCTS_URL;
import static com.a2z.shop.constant.ShopConstants.UPDATE_VENDOR_CONFIG_URL;
import static com.a2z.shop.constant.ShopConstants.UPLOAD_CSV_URL;
import static com.a2z.shop.constant.ShopConstants.GET_CART_DETAILS_BY_ID_URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.valves.CrawlerSessionManagerValve;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.a2z.shop.bo.ShopBO;
import com.a2z.shop.model.CartAddress;
import com.a2z.shop.model.VendorConfig;
import com.a2z.shop.vo.CartRequestVO;
import com.a2z.shop.vo.ProductReqVO;
import com.a2z.shop.vo.RspnsVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = ADMIN_URL, tags = {"This is the controller for the Store Administration Services"})
@RestController
@RequestMapping(ADMIN_URL)
public class AdminController {
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	Environment env;
	
	@Autowired
	private ShopBO shopBO;

	@ApiOperation(value = "Health Check", notes = "This is health check.")
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping(value = HEALTH_CHECK_URL, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RspnsVO> healthCheck() {
		final String methodName = "healthCheck()";
		logger.info("{}: Checking Health of Shop Service", methodName);
		RspnsVO healthCheckRspnsVO = new RspnsVO();
		try {
			healthCheckRspnsVO = shopBO.healthCheck();
		}catch(Exception e)
		{
			healthCheckRspnsVO.setResponse("ERROR RESPONSE");
			healthCheckRspnsVO.setRspnsCode(1010);
			healthCheckRspnsVO.setRspnsMsg(null);
			return new ResponseEntity<RspnsVO>(healthCheckRspnsVO,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<RspnsVO>(healthCheckRspnsVO,HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get Product", notes = "This is get product service")
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = GET_PRODUCTS_MERGE_URL, method = RequestMethod.POST)
	public ResponseEntity<RspnsVO> getProduct(@RequestParam String storeID, @RequestParam String id) throws Exception
	{
		final String methodName = "getProduct()";
		logger.info("{}: Getting Product",methodName);
		RspnsVO rspnsVO = new RspnsVO();
		try {
			rspnsVO.setRspnsCode(1000);
			rspnsVO.setRspnsMsg("GOOD");
			rspnsVO.setResponse(shopBO.getProduct(storeID, id));
		}catch(Exception e)
		{
			rspnsVO.setResponse("ERROR RESPONSE");
			rspnsVO.setRspnsCode(1010);
			rspnsVO.setRspnsMsg(null);
			return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		logger.info("Response : {}", rspnsVO.toString());
		return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.OK);
		
	}
	@ApiOperation(value = "Get Product as CSV", notes = "Getting Product List as CSV")
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = GET_CSV_URL)
	public ResponseEntity<RspnsVO> getProductsCSV(HttpServletRequest request, HttpServletResponse httpResponse, @RequestBody ProductReqVO req) throws Exception
	{
		final String methodName = "getProductsCSV()";
		logger.info("{}: Getting Products",methodName);
		logger.info("Store ID : {}", req.getStoreID());
		RspnsVO rspnsVO = new RspnsVO();
		try {
			rspnsVO.setRspnsCode(1000);
			rspnsVO.setRspnsMsg("GOOD");
			rspnsVO.setResponse(shopBO.getProductsCSV(req.getStoreID()));
		}catch(Exception e)
		{
			rspnsVO.setResponse("ERROR RESPONSE");
			rspnsVO.setRspnsCode(1010);
			rspnsVO.setRspnsMsg(null);
			return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		logger.info("Response : {}", rspnsVO.toString());
		return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.OK);
		
	}
	@ApiOperation(value = "Batch Upload Products as CSV", notes = "Batch Upload Products as CSV")
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = UPLOAD_CSV_URL)
	public ResponseEntity<RspnsVO> uploadProductsCSV(@RequestParam("file") MultipartFile files, @RequestParam("storeID") String storeID)
	{
		final String methodName = "uploadProductsCSV()";
		logger.info("{}: Uploading Products",methodName);
		RspnsVO rspnsVO = new RspnsVO();
		try {
			rspnsVO.setRspnsCode(1000);
			rspnsVO.setRspnsMsg("GOOD");
			rspnsVO.setResponse(shopBO.uploadProductsCSV(files,storeID));
		}catch(Exception e)
		{
			rspnsVO.setResponse("ERROR RESPONSE");
			rspnsVO.setRspnsCode(1010);
			rspnsVO.setRspnsMsg(null);
			return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		logger.info("Response : {}", rspnsVO.toString());
		return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.OK);
		
	}
	@ApiOperation(value = "Get Product", notes = "Getting list of product for given vendorID")
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = GET_PRODUCTS_URL,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE,
			method = RequestMethod.POST)
	public ResponseEntity<RspnsVO> getProducts(HttpServletRequest request, HttpServletResponse httpResponse, @RequestBody ProductReqVO req) throws Exception
	{
		final String methodName = "getProducts()";
		logger.info("{}: Getting Products",methodName);
		logger.info("Store ID : {}", req.getStoreID());

		RspnsVO rspnsVO = new RspnsVO();
		try {
			rspnsVO.setRspnsCode(1000);
			rspnsVO.setRspnsMsg("GOOD");
			rspnsVO.setResponse(shopBO.getProducts(req.getStoreID()));
		}catch(Exception e)
		{
			rspnsVO.setResponse("ERROR RESPONSE");
			rspnsVO.setRspnsCode(1010);
			rspnsVO.setRspnsMsg(null);
			return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		logger.info("Response : {}", rspnsVO.toString());
		return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.OK);
		
	}

	@ApiOperation(value = "Add Update Product", notes = "This is add update product.")
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = UPDATE_PRODUCTS_URL,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE,
			method = RequestMethod.POST)
	public ResponseEntity<RspnsVO> addUpdateProducts(HttpServletRequest request, HttpServletResponse httpResponse, @RequestBody ProductReqVO req) throws Exception
	{
		final String methodName = "addUpdateProducts()";
		logger.info("{}: Admin-Add Update Products",methodName);
		logger.info("Store ID : {}", req.getStoreID());

		RspnsVO rspnsVO = new RspnsVO();
		try {
			rspnsVO.setRspnsCode(1000);
			rspnsVO.setRspnsMsg("GOOD");
			rspnsVO.setResponse(shopBO.addUpdateProducts(req));
		}catch(Exception e)
		{
			rspnsVO.setResponse("ERROR RESPONSE");
			rspnsVO.setRspnsCode(1010);
			rspnsVO.setRspnsMsg(null);
			return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		logger.info("Response : {}", rspnsVO.toString());
		return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.OK);
		
	}
	

	@ApiOperation(value = "Remove Product", notes = "This is removing the products.")
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = REMOVE_PRODUCTS_URL,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE,
			method = RequestMethod.POST)
	public ResponseEntity<RspnsVO> removeProduct(HttpServletRequest request, HttpServletResponse httpResponse, @RequestBody ProductReqVO req) throws Exception
	{
		final String methodName = "removeProduct()";
		logger.info("Store ID : {}", req.getStoreID());

		RspnsVO rspnsVO = new RspnsVO();
		try {
			rspnsVO.setRspnsCode(1000);
			rspnsVO.setRspnsMsg("GOOD");
			rspnsVO.setResponse(shopBO.removeProduct(req));
		}catch(Exception e)
		{
			rspnsVO.setResponse("ERROR RESPONSE");
			rspnsVO.setRspnsCode(1010);
			rspnsVO.setRspnsMsg(null);
			return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		logger.info("Response : {}", rspnsVO.toString());
		return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.OK);
		
	}
	
	@ApiOperation(value = "Vendor Add Update Config", notes = "This is vendor configuration update.")
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping(value = UPDATE_VENDOR_CONFIG_URL,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RspnsVO> addUpdateVendorConfig(HttpServletRequest request, HttpServletResponse httpResponse, @RequestBody VendorConfig req) throws Exception
	{
		final String methodName = "getVendorConfig()";
		logger.info("{}: Admin - Add Update Config",methodName);
		logger.info("Store ID : {}", req.getVendorId());
		
		RspnsVO rspnsVO = new RspnsVO();
		try {
			rspnsVO.setRspnsCode(1000);
			rspnsVO.setRspnsMsg("GOOD");
			rspnsVO.setResponse(shopBO.addUpdateVendorConfig(req));
		}catch(Exception e)
		{
			rspnsVO.setResponse("ERROR RESPONSE");
			rspnsVO.setRspnsCode(1010);
			rspnsVO.setRspnsMsg(null);
			return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		logger.info("Response : {}", rspnsVO.toString());
		return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get Vendor Config", notes = "Fetching Vendor config from DB")
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping(value = GET_STORE_CONFIG_URL,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RspnsVO> getVendorConfig(HttpServletRequest request, HttpServletResponse httpResponse, @RequestBody ProductReqVO req) throws Exception
	{
		final String methodName = "getVendorConfig()";
		logger.info("{}: Getting StoreConfig",methodName);
		logger.info("Store ID : {}", req.getStoreID());
		
		RspnsVO rspnsVO = new RspnsVO();
		try {
			
			rspnsVO.setRspnsCode(1000);
			rspnsVO.setRspnsMsg("GOOD");
			rspnsVO.setResponse(shopBO.getStoreConfig(req.getStoreID()));
		}catch(Exception e)
		{
			rspnsVO.setResponse("ERROR RESPONSE");
			rspnsVO.setRspnsCode(1010);
			rspnsVO.setRspnsMsg(null);
			return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		logger.info("Response : {}", rspnsVO.toString());
		return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.OK);
	}
	
	@ApiOperation(value = "Delete Vendor Config", notes = "Soft Deleting the vendor configuration")
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = DELETE_VENDOR_CONFIG_URL,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
	public ResponseEntity<RspnsVO> deleteVendorConfig(@RequestBody ProductReqVO req) {
		final String methodName = "deleteVendorConfig()";
		logger.info("{}: Deleting StoreConfig",methodName);
		logger.info("Store ID : {}",req.getStoreID());
		
		RspnsVO rspnsVO = new RspnsVO();
		try {
			
			rspnsVO.setRspnsCode(1000);
			rspnsVO.setRspnsMsg("GOOD");
			rspnsVO.setResponse(shopBO.deleteVendorConfig(req.getStoreID()));
		}catch(Exception e)
		{
			rspnsVO.setResponse("ERROR RESPONSE");
			rspnsVO.setRspnsCode(1010);
			rspnsVO.setRspnsMsg(null);
			return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		logger.info("Response : {}", rspnsVO.toString());
		return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.OK);
		
	}
	
	@ApiOperation(value = "Get Category Product", notes = "Getting list of product for given vendorID and selected category!")
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = GET_PRODUCTS_CATEGORY_URL,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE,
			method = RequestMethod.POST)
	public ResponseEntity<RspnsVO> getCategoryProducts(HttpServletRequest request, HttpServletResponse httpResponse, @RequestBody ProductReqVO req) throws Exception
	{
		final String methodName = "getCategoryProducts()";
		logger.info("{}: Getting Products",methodName);
		logger.info("Store ID : {}", req.getStoreID());

		RspnsVO rspnsVO = new RspnsVO();
		try {
			rspnsVO.setRspnsCode(1000);
			rspnsVO.setRspnsMsg("GOOD");
			rspnsVO.setResponse(shopBO.getCategoryProducts(req.getStoreID(), req.getProduct_category()));
		}catch(Exception e)
		{
			rspnsVO.setResponse("ERROR RESPONSE");
			rspnsVO.setRspnsCode(1010);
			rspnsVO.setRspnsMsg(null);
			return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		logger.info("Response : {}", rspnsVO.toString());
		return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.OK);
		
	}
	
	@ApiOperation(value = "store admin validation", notes ="Check if vendor is registered and have active state.if condition not fullfil returns false.")
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = STORE_ADMIN_VALIDITY_URL, 
			produces = MediaType.APPLICATION_JSON_VALUE,
			method = RequestMethod.GET)
	public ResponseEntity<RspnsVO> isValidStore(HttpServletRequest request, HttpServletResponse httpResponse, @RequestParam(value = "vendorId", required=true) String vendorId) throws Exception
	{
		final String methodName = "isValidStore()";
		logger.info("{}: Getting store validation",methodName);
		logger.info("Store ID : {}", vendorId);

		RspnsVO rspnsVO = new RspnsVO();
		try {
			rspnsVO = shopBO.isValidStore(vendorId);
		}catch(Exception e)
		{
			rspnsVO.setResponse("ERROR RESPONSE");
			rspnsVO.setRspnsCode(1010);
			rspnsVO.setRspnsMsg(null);
			return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		logger.info("Response : {}", rspnsVO.toString());
		return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.OK);
		
	}
	
	@ApiOperation(value = "Get Admin Details By ID", notes = "This is for get admin details  from db using specific ID.")
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = GET_USER_BY_ID_URL,
			produces = MediaType.APPLICATION_JSON_VALUE,
			method = RequestMethod.GET)
	public ResponseEntity<RspnsVO> getAdminDetailsByID(@RequestParam(value="vendorId") String vendorId) throws Exception
	{
		final String methodName = "getAdminDetailsByID()";
		logger.info("{}: Fetching User Details",methodName);
		logger.info(" ID : {}", vendorId);

		RspnsVO rspnsVO = new RspnsVO();
		try {
			rspnsVO.setRspnsCode(1000);
			rspnsVO.setRspnsMsg("GOOD");
			if(vendorId != null && !vendorId.isEmpty()) {
				rspnsVO.setResponse(shopBO.getAdminDetailsByID(Integer.parseInt(vendorId)));
			}
		}catch(Exception e)
		{
			rspnsVO.setResponse("ERROR RESPONSE");
			rspnsVO.setRspnsCode(1010);
			rspnsVO.setRspnsMsg(null);
			return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		logger.info("Response : {}", rspnsVO.toString());
		return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.OK);
		
	}
	
	@ApiOperation(value = "Get All Billed Cart", notes = "This service use to get all Billed cart for admin or user.")
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = GET_ALL_BIILED_CART_URL,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE,
			method = RequestMethod.POST)
	public ResponseEntity<RspnsVO> getAllBilledCart(HttpServletRequest request, HttpServletResponse httpResponse, @RequestBody CartRequestVO req) throws Exception
	{
		final String methodName = "getAllBilledCart()";
		logger.info("{}: Method",methodName);
		logger.info("Store ID : {}", req.getStoreID());
		
		RspnsVO rspnsVO = new RspnsVO();
		try {
			rspnsVO.setRspnsCode(1000);
			rspnsVO.setRspnsMsg("GOOD");
			rspnsVO.setResponse(shopBO.getAllBilledCart(req));
		}catch(Exception e)
		{
			rspnsVO.setResponse("ERROR RESPONSE");
			rspnsVO.setRspnsCode(1010);
			rspnsVO.setRspnsMsg(null);
			return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		logger.info("Response : {}", rspnsVO.toString());
		return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get Cart", notes = "This service use to get all items added in cart.")
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping(value = GET_CART_URL,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RspnsVO> getCart(HttpServletRequest request, HttpServletResponse httpResponse, @RequestBody CartRequestVO req) throws Exception
	{
		final String methodName = "getCart()";
		logger.info("{}: Method",methodName);
		logger.info("Store ID : {}", req.getStoreID());
		
		RspnsVO rspnsVO = new RspnsVO();
		try {
			rspnsVO.setRspnsCode(1000);
			rspnsVO.setRspnsMsg("GOOD");
			rspnsVO.setResponse(shopBO.getCart(req));
		}catch(Exception e)
		{
			rspnsVO.setResponse("ERROR RESPONSE");
			rspnsVO.setRspnsCode(1010);
			rspnsVO.setRspnsMsg(null);
			return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		logger.info("Response : {}", rspnsVO.toString());
		return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get Cart Delivery Address", notes = "This service is to get delivery address..")
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping(value = GET_CART_DELIVERY_ADDRESS_URL,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RspnsVO> getCartDeliveryAddress(HttpServletRequest request, HttpServletResponse httpResponse, @RequestBody CartAddress req) throws Exception
	{
		final String methodName = "getCartDeliveryAddress()";
		logger.info("{}: Method",methodName);
		logger.info("Store ID : {}", req.getCartID());
		
		RspnsVO rspnsVO = new RspnsVO();
		try {
			rspnsVO.setRspnsCode(1000);
			rspnsVO.setRspnsMsg("GOOD");
			rspnsVO.setResponse(shopBO.getCartDeliveryAddress(req));
		}catch(Exception e)
		{
			rspnsVO.setResponse("ERROR RESPONSE");
			rspnsVO.setRspnsCode(1010);
			rspnsVO.setRspnsMsg(null);
			return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		logger.info("Response : {}", rspnsVO.toString());
		return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.OK);
	}
	
	@ApiOperation(value="Get CART DETAILS BY ID", notes="This api get single cart details for vendor")
	@CrossOrigin(allowedHeaders = "*", origins = "*")
	@GetMapping(value = GET_CART_DETAILS_BY_ID_URL)
	public ResponseEntity<RspnsVO> getCartDetailsByID(HttpServletRequest  request, HttpServletResponse response,@RequestParam("vendorId")String vendorId,@RequestParam("cartId")String cartId){
		final String methodName = "getCartDetailsByID()";
		logger.info("{}: Method",methodName);
		logger.info("Store ID : {}", vendorId);

		RspnsVO rspnsVO = new RspnsVO();
		try {

			if (cartId != null && vendorId != null && !cartId.equals("") && !vendorId.equals("")) {
				rspnsVO.setRspnsCode(200);
				rspnsVO.setRspnsMsg("GOOD");
				rspnsVO.setResponse(shopBO.getCartDetailsByID(vendorId, cartId));
			}

		} catch (Exception e) {
			rspnsVO.setResponse("ERROR RESPONSE");
			rspnsVO.setRspnsCode(1010);
			rspnsVO.setRspnsMsg(null);
			return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<RspnsVO>(rspnsVO, HttpStatus.OK);
	}
	
	
//	@PostMapping(value = GET_CATEGORIES_URL,
//			consumes = MediaType.APPLICATION_JSON_VALUE,
//			produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<RspnsVO> getCategories(HttpServletRequest request, HttpServletResponse httpResponse, @RequestBody ProductReqVO req) throws Exception
//	{
//		final String methodName = "getCategories()";
//		logger.info("{}: Getting Categories",methodName);
//		logger.info("Store ID : {}", req.getStoreID());
//		
//		RspnsVO rspnsVO = new RspnsVO();
//		try {
//			rspnsVO.setRspnsCode(1000);
//			rspnsVO.setRspnsMsg("GOOD");
//			rspnsVO.setResponse(shopBO.getCategories(req.getStoreID()));
//		}catch(Exception e)
//		{
//			rspnsVO.setResponse("ERROR RESPONSE");
//			rspnsVO.setRspnsCode(1010);
//			rspnsVO.setRspnsMsg(null);
//			return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//		
//		logger.info("Response : {}", rspnsVO.toString());
//		return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.OK);
//	}
	
	
//	@PostMapping(value = GET_CART_URL,
//			consumes = MediaType.APPLICATION_JSON_VALUE,
//			produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<RspnsVO> getCart(HttpServletRequest request, HttpServletResponse httpResponse, @RequestBody CartRequestVO req) throws Exception
//	{
//		final String methodName = "getCart()";
//		logger.info("{}: Method",methodName);
//		logger.info("Store ID : {}", req.getCartID());
//		
//		RspnsVO rspnsVO = new RspnsVO();
//		try {
//			rspnsVO.setRspnsCode(1000);
//			rspnsVO.setRspnsMsg("GOOD");
//			rspnsVO.setResponse(shopBO.getCart(req.getCartID()));
//		}catch(Exception e)
//		{
//			rspnsVO.setResponse("ERROR RESPONSE");
//			rspnsVO.setRspnsCode(1010);
//			rspnsVO.setRspnsMsg(null);
//			return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//		
//		logger.info("Response : {}", rspnsVO.toString());
//		return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.OK);
//	}
	
//	@PostMapping(value = ADD_UPDATE_CART_URL,
//			consumes = MediaType.APPLICATION_JSON_VALUE,
//			produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<RspnsVO> addUpdateCart(HttpServletRequest request, HttpServletResponse httpResponse, @RequestBody CartRequestVO req) throws Exception
//	{
//		final String methodName = "getCart()";
//		logger.info("{}: Method",methodName);
//		logger.info("Store ID : {}", req.getCartID());
//		
//		RspnsVO rspnsVO = new RspnsVO();
//		try {
//			rspnsVO.setRspnsCode(1000);
//			rspnsVO.setRspnsMsg("GOOD");
//			rspnsVO.setResponse(shopBO.addUpdateCart(req));
//		}catch(Exception e)
//		{
//			rspnsVO.setResponse("ERROR RESPONSE");
//			rspnsVO.setRspnsCode(1010);
//			rspnsVO.setRspnsMsg(null);
//			return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//		
//		logger.info("Response : {}", rspnsVO.toString());
//		return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.OK);
//	}
	
	
	

}
