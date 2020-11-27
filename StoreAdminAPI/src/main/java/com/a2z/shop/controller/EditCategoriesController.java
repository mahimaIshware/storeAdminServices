package com.a2z.shop.controller;



import static com.a2z.shop.constant.ShopConstants.GET_PRODUCTS_MERGE_URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.a2z.shop.Category.Category;
import com.a2z.shop.bo.EditCategoriesBO;
import com.a2z.shop.vo.RspnsVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;




@RestController
@RequestMapping(value = "/ShopAdmin")
@Api(value = "CategoriesAdd", tags = "This MicroService will add Category to the vendor shop as per vendor id")
public class EditCategoriesController {
	public static final Logger logger = LoggerFactory.getLogger(EditCategoriesController.class);
	@Autowired
	private EditCategoriesBO editCategoriesBO;
	int Vendor_Id;


	
	@ApiOperation(value = "Enter Data", notes = "This is categories insertion operation.")
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value ="/addCategory", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RspnsVO> addCategoryProducts(HttpServletRequest request, HttpServletResponse response,@RequestBody Category category) throws Exception
	{
	
				final String methodName = "EnterInDb()";
				logger.info("Vendor_id : {}", category.getVendorId());
				logger.info("categoryStatus: {}", category.getCategoryStatus());
				logger.info("{}: Entering all entries for categories", methodName);
				RspnsVO rspnsVO = new RspnsVO();
				try {
					rspnsVO.setRspnsCode(1000);
					rspnsVO.setRspnsMsg("GOOD");
					rspnsVO.setResponse(editCategoriesBO.addCategoryProducts(category));
				}catch(Exception e)
				{
					rspnsVO.setResponse("ERROR RESPONSE");
					rspnsVO.setRspnsCode(1010);
					rspnsVO.setRspnsMsg(null);
					return new ResponseEntity<RspnsVO>(rspnsVO,HttpStatus.INTERNAL_SERVER_ERROR);
				}
				logger.info("Response : {}", rspnsVO.toString());
				return new ResponseEntity<RspnsVO>(rspnsVO, HttpStatus.OK);
	}

	
	@ApiOperation(value = "Get Categories", notes = "This is get product service")
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getAllCategories", method = RequestMethod.POST)
	public ResponseEntity<RspnsVO> getAllCategories(HttpServletRequest request, HttpServletResponse response,@RequestBody Category category) throws Exception
	{
		final String methodName = "getAllCategories()";
		logger.info("{}: Getting Categories",methodName);
		logger.info("{}: Vendor  Id",category.getVendorId());
		RspnsVO rspnsVO = new RspnsVO();
		try {
			rspnsVO.setRspnsCode(1000);
			rspnsVO.setRspnsMsg("GOOD");
			rspnsVO.setResponse(editCategoriesBO.getAllCategories(category.getVendorId()));
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
		
}
