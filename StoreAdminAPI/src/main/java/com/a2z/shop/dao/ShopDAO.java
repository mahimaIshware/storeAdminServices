package com.a2z.shop.dao;

import static com.a2z.shop.constant.ShopConstants.DELETE_PRODUCT_SQL;
import static com.a2z.shop.constant.ShopConstants.GET_CART_DELIVERY_ADDRESS_SQL;
import static com.a2z.shop.constant.ShopConstants.GET_CART_DELIVERY_ADDRESS_USER_ID_SQL;
import static com.a2z.shop.constant.ShopConstants.GET_CART_PRODUCTS_SQL;
import static com.a2z.shop.constant.ShopConstants.GET_CATEGORY_PRODUCTS_SQL;
import static com.a2z.shop.constant.ShopConstants.GET_PRODUCTS_SQL;
import static com.a2z.shop.constant.ShopConstants.GET_PRODUCT_EXISTS_SQL;
import static com.a2z.shop.constant.ShopConstants.GET_PRODUCT_MERGE_SQL;
import static com.a2z.shop.constant.ShopConstants.GET_STORE_CONFIG_SQL;
import static com.a2z.shop.constant.ShopConstants.GET_USER_BY_ID;
import static com.a2z.shop.constant.ShopConstants.HEALTH_CHECK_SQL;
import static com.a2z.shop.constant.ShopConstants.INSERT_PRODUCTS_SQL;
import static com.a2z.shop.constant.ShopConstants.INSERT_STORE_CONFIG_SQL;
import static com.a2z.shop.constant.ShopConstants.UPDATE_PRODUCTS_SQL;
import static com.a2z.shop.constant.ShopConstants.UPDATE_STORE_CONFIG_SQL;
import static com.a2z.shop.constant.ShopConstants.GET_CART_DETAILS_BY_ID;
import static com.a2z.shop.constant.ShopConstants.uniqueID;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.a2z.shop.constant.ShopConstants;
import com.a2z.shop.model.CartAddress;
import com.a2z.shop.model.CartMain;
import com.a2z.shop.model.CartProduct;
import com.a2z.shop.model.Product;
import com.a2z.shop.model.User;
import com.a2z.shop.model.UserRoles;
import com.a2z.shop.model.VendorConfig;
import com.a2z.shop.services.messaging.EmailSenderService;
import com.a2z.shop.vo.CartDetailsVO;
import com.a2z.shop.vo.CartRequestVO;
import com.a2z.shop.vo.ProductReqVO;


@Component
public class ShopDAO {
	private static final Logger logger = LoggerFactory.getLogger(ShopDAO.class);
	
	//protected static final String HEALTH_CHECK_SQL = "Select CURRENT_DATE()";
	
	private transient JdbcTemplate jdbcTemplate;
	
	@Autowired
	private transient DataSource datasource;
	
	//private static Connection connexion;
	
	@Autowired
	private transient ShopProductsMapper shopProductsMapper;
	
	@Autowired
	ArrayList<Product> productsList;
	
	@Autowired
	ArrayList<CartProduct> productsListOrder;
	
	@Autowired
	ArrayList<VendorConfig> vendorConfigs;
	
	@Autowired
	ArrayList<CartMain> cartMain;
	
	@Autowired
	ArrayList<CartAddress> cartDeliveryAddressList;
		
	@Autowired
	private transient ShopVendorConfigMapper shopVendorConfigMapper;
	
//	@Autowired
//	private transient ShopOrderMainMapper shopOrderMainMapper;
	
	@Autowired
	private transient ShopOrderProductsMapper shopOrderProductsMapper;
	
	@Autowired
	private transient ShopOrderAddressMapper shopOrderAddressMapper;
	
	@Autowired
	private transient ShopOrderDetailsMapper shopOrderDetailsMapper;
	
	@Autowired
	ArrayList<CartDetailsVO> cartDetailsVOList;
	
	@Autowired
	private EmailSenderService emailSenderService;
	
	@Autowired
	public ShopDAO(DataSource dataSource) throws SQLException {
		logger.debug("Inside ShopDAO Constructor");
		this.datasource = dataSource;
		jdbcTemplate = new JdbcTemplate(this.datasource);
		
//		try {
//			if(connexion == null || connexion.isClosed())
//			{
//				if(dataSource != null)
//				{
//					connexion = dataSource.getConnection();
//					logger.info("connexion created");
//				}else
//				{
//					logger.error("datasource is null");
//				}
//			}
//				
//				
//			}catch(Exception e)
//			{
//				logger.error("EXCEPTION CREAING CONNECTION");
//				e.printStackTrace();
//				throw new SQLException("EXCEPTION CREAING CONNECTION");
//			}
		
	}
	
	
	public boolean isDBUp()
	{
		final String methodName = "isDBUp()";
		logger.info("{}: Logger DB HEALTH CHECK ", methodName);
		boolean isDBStatusUp = false;
		
		try {
			//long startTime = System.currentTimeMillis();
			jdbcTemplate.queryForObject(HEALTH_CHECK_SQL,  new Object[] {}, Date.class);
			logger.info("The Database is Up & Running");
			isDBStatusUp = true;
		} catch(Exception e)
		{
			logger.error("Exception Occurred while Connecting to database :{} "+e.getMessage(), e);
		}
		return isDBStatusUp;
	}
	
	
	// QUERIES
	
	private String testQuery() {
		
		return "WILL RETURN QUERY RESPONSE FROM HERE";
		
	}
	
	
	
	public ArrayList<Product> getProducts(String storeID)
	{
		final String methodName = "getProducts()";
		logger.info("{}: Logger DB ", methodName);
		
		try {
			
			logger.info("GET_PRODUCTS_SQL:"+GET_PRODUCTS_SQL+":"+storeID);
			jdbcTemplate.query(GET_PRODUCTS_SQL,  ShopDAOHelper.createKeySQLParams(storeID), shopProductsMapper);
			logger.info("GETTING PRODUCTS..");
		} catch(Exception e)
		{
			logger.error("Exception Occurred while Connecting to database :{} "+e.getMessage(), e);;
		}
		return productsList;
	}
	public String uploadProductsCSV(MultipartFile file, String storeID)
	{
		final String methodName = "uploadProductsCSV()";
		logger.info("{}: Logger DB ", methodName);
		String response = null;
		try {
			if(file.isEmpty())
			{
				response = "Empty File!";
			}
			else
			{
				String content = new String(file.getBytes());
				String[] rows = content.split("\n");
				if(rows[0].equals("Name,Description,Rate,Stock"))
				{
					logger.info(rows[0]);
					ProductReqVO prd = new ProductReqVO();
					prd.setStoreID(storeID);
				   for(int i=1; i < rows.length; i++)
				   {
					ArrayList<Product> prdlist = new ArrayList<Product>();
					 logger.info(rows[i]);
					 String[] elements = rows[i].split(",");
					 if(elements.length==4)
					 {
						Product p = new Product();
						p.setProduct_name(elements[0]);
						p.setProduct_description(elements[1]);
						p.setProduct_price(Double.parseDouble(elements[2]));
						p.setProduct_quantity_avlbl(Integer.parseInt(elements[3]));
						prdlist.add(p);
					 }
					 prd.setProduct(prdlist);
					addUpdateProducts(prd);
				   }

				}
				response = "All rows uploaded succesfully!";
			}
		} catch(Exception e)
		{
			response = "Error while Uploading!";
			logger.error("Exception Occurred while Connecting to database :{} "+e.getMessage(), e);;
		}
		return response;
	}
	public String getProductsCSV(String storeID)
	{
		final String methodName = "getProductsCSV()";
		logger.info("{}: Logger DB ", methodName);
		ArrayList<Product>products=getProducts(storeID);
		String response = "Product-ID,Stock,ProductName,ProductDescription,Rate\n";
		try {
			for(Product i:products)
			{
				response = response + i.getProduct_id() + "," + Integer.toString(i.getProduct_quantity_avlbl()) + "," + i.getProduct_name() + ","+ i.getProduct_description() + "," + String.valueOf(i.getProduct_price()) + "\n"; 
			}
			logger.info(response);
		} catch(Exception e)
		{
			logger.error("Exception Occurred while Connecting to database :{} "+e.getMessage(), e);
		}
		return response;
	}


	
	public Product getProduct(String storeID, String id)
	{
		try {
			return (Product) jdbcTemplate.queryForObject(GET_PRODUCT_MERGE_SQL, new Object[] { storeID, id }, new RowMapper<Object>() {
				public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
						Product p = new Product();
						//p.setProduct_id(rs.getString("product_id"));
						p.setProduct_name(rs.getString("product_name"));
						p.setProduct_description(rs.getString("product_description"));
						p.setProduct_price(rs.getDouble("product_price"));
						p.setProduct_quantity_avlbl(rs.getInt("product_quantity_avlbl"));
						p.setProduct_category(rs.getString("product_category"))	;
						return p;
				}
			});
		} catch (Exception e) {
			logger.error("Exception Occurred while Connecting to database :{} " + e.getMessage(), e);
		}
		return null;
	}
	
	public ArrayList<VendorConfig> getStoreConfig(String storeID) {
		final String methodName = "getStoreConfig()";
		logger.info("{}: Logger DB ", methodName);
		
		try {
			
			logger.info("GET_STORE_CONFIG_SQL:"+GET_STORE_CONFIG_SQL+":"+storeID);
			jdbcTemplate.query(GET_STORE_CONFIG_SQL,  ShopDAOHelper.createKeySQLParams(storeID), shopVendorConfigMapper);
			logger.info("GETTING CONFIG..");
		} catch(Exception e)
		{
			logger.error("Exception Occurred while Connecting to database :{} "+e.getMessage(), e);;
		}
		return vendorConfigs;
	}


	public ArrayList<CartProduct> getCart(CartRequestVO req) {
		final String methodName = "getCart()";
		logger.info("{}:", methodName);
		
		try {
			
			logger.info("GET_CART_PRODUCTS_SQL:"+GET_CART_PRODUCTS_SQL+":"+req.getCartID());
			logger.info("CART ID ::"+req.getCartID());
			if(req.getCartID() == null)
		    {
				logger.info("CART ID WAS NULL");
				return productsListOrder;
			}
			jdbcTemplate.query(GET_CART_PRODUCTS_SQL,  ShopDAOHelper.createKeySQLParams(req.getCartID()), shopOrderProductsMapper);
			logger.info("GETTING CONFIG..");
		} catch(Exception e)
		{
			logger.error("Exception Occurred while Connecting to database :{} "+e.getMessage(), e);
		}
		return productsListOrder;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public StringBuilder addUpdateCart(CartRequestVO req) {
		final String methodName = "addUpdateCart()";
		StringBuilder response = new StringBuilder("ERROR ADDING UPDATING");
		logger.info("{}:", methodName);
		String storeId = req.getStoreID();
		String userId = req.getUserID();
		String tranId = req.getTranID();
		String cartId = req.getCartID();
		ArrayList<Product> productsList = new ArrayList<Product>();
		boolean isOrderIdExists = false;
		try {
			logger.info("adding updating cart products..");
			if (req.getProduct_id() != null && !StringUtils.isEmpty(req.getProduct_id())
					&& !req.getProduct_id().equalsIgnoreCase("null")) {
				productsList = getProductByProductId(storeId, req.getProduct_id());
				isOrderIdExists = jdbcTemplate.queryForObject(
						"SELECT EXISTS(SELECT 1 FROM shopdb.order_products WHERE  order_id=? AND product_id =CAST(? AS UNSIGNED) AND vendor_id=CAST(? AS UNSIGNED))", boolean.class,
						new Object[] { cartId,req.getProduct_id(),storeId});
				if (productsList != null && !productsList.isEmpty() ) {
					if (!isOrderIdExists) {
						for (Product pr : productsList) {
							jdbcTemplate.update(
									"INSERT INTO shopdb.order_products(unique_prod_id,vendor_id,order_id,product_id,product_name,product_description,product_img_link,product_price,product_quantity,"
											+ "product_discount,product_mrp,product_tax,product_category,product_sub_category,product_tags,create_ts,update_ts) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
									ShopConstants.uniqueID(""), storeId, cartId, pr.getProduct_id(),
									pr.getProduct_name(), pr.getProduct_description(), pr.getProduct_img_link(),
									pr.getProduct_price(), 1, pr.getProduct_discount(),
									pr.getProduct_mrp(), 00.00, pr.getProduct_category(), pr.getProduct_sub_category(),
									pr.getProduct_tags(), new Timestamp(System.currentTimeMillis()),
									new Timestamp(System.currentTimeMillis()));
						}
						return response = new StringBuilder("product added to products order table with cartId:"
								+ storeId + "cartId" + cartId);
					} else {
						for (Product pr : productsList) {
							jdbcTemplate.update(
									"UPDATE shopdb.order_products SET product_id=?,product_name=?,product_description=?,product_img_link=?,product_price=?,product_quantity=?,"
											+ "product_discount=?,product_mrp=?,product_tax=?,product_category=?,product_sub_category=?,product_tags=?,update_ts=? WHERE vendor_id=? AND order_id=? ",
									pr.getProduct_id(), pr.getProduct_name(),
									pr.getProduct_description(), pr.getProduct_img_link(), pr.getProduct_price(),
									1, pr.getProduct_discount(), pr.getProduct_mrp(), 00.00,
									pr.getProduct_category(), pr.getProduct_sub_category(), pr.getProduct_tags(),
									new Timestamp(System.currentTimeMillis()), storeId, cartId);
						}
						return response = new StringBuilder("product Updated to products order table with storeId:"
								+ storeId + "cartId" + cartId);
					}
				}
			}

			return response = new StringBuilder("No product found in with productId:" + req.getProduct_id() + "StoreId"
					+ storeId + "in Data base!");
		} catch (Exception e) {
			logger.error("Exception Occurred while Connecting to database :{} " + e.getMessage(), e);
		}
		return response;
	}


	public StringBuilder addUpdateVendorConfig(VendorConfig req) {
		final String methodName = "addUpdateVendorConfig()";
		StringBuilder response = new StringBuilder("ERROR ADDING UPDATING VENDOR CONFIG");
		logger.info("{}:", methodName);
		try {
			logger.info("INSERT_UPDATE_DELETE_CART_PRODUCTS:"+req.getVendorId());
			//ShopDAOHelper.createKeySQLParams(
			//"UPDATE SHOPDB.VENDOR_CONFIG set categories=?,delivery_flag=?, delivery_start_time=?, delivery_end_time=?, delivery_loc_id=?, store_hours_start=?, store_hours_end=?, store_days=?, update_ts= ? where vendor_id = ?";
			int status = jdbcTemplate.update(UPDATE_STORE_CONFIG_SQL,
					req.getCategories(),
					req.getDeliveryFlag(),
					req.getDeliveryStartTime(),
					req.getDeliveryEndTime(),
					req.getDeliveryLocationId(),
					req.getStoreStartTime(),
					req.getStoreEndTime(),
					req.getStoreOpenDays(),
					new Timestamp(System.currentTimeMillis()),
					req.getShopName(),
					req.getCategoryDescription(),
					req.getStatus(),
					req.getPhone(),
					req.getAddress(),
					req.getShopLogoLink(),
					req.getEmail(),
					req.getFeaturesOne(),
					req.getFeaturesTwo(),
					req.getFeaturesThree(),
					req.getFeaturesFour(),
					req.getFeaturesFive(),
					req.getFeaturesSix(),
					req.getAboutUs(),
					req.getAboutShopLogoLink(),
					req.getCountry_code(),
					req.getVendorId()
					);
					
			        if(status != 0){
			            logger.info("Vendor data updated for Vendor ID " + req.getVendorId());
			            response = new StringBuilder("Vendor data updated for Vendor ID " + req.getVendorId());
			        }else{
			        	jdbcTemplate.update(INSERT_STORE_CONFIG_SQL,
			        			req.getVendorId(),
			        			req.getCategories(),
								req.getDeliveryFlag(),
								req.getDeliveryStartTime(),
								req.getDeliveryEndTime(),
								req.getDeliveryLocationId(),
								req.getStoreStartTime(),
								req.getStoreEndTime(),
								req.getStoreOpenDays(),
								new Timestamp(System.currentTimeMillis()),
								new Timestamp(System.currentTimeMillis()),
								req.getShopName(),
								req.getCategoryDescription(),
								req.getStatus(),
								req.getPhone(),
								req.getAddress(),
								req.getShopLogoLink(),
								req.getEmail(),
								req.getFeaturesOne(),
								req.getFeaturesTwo(),
								req.getFeaturesThree(),
								req.getFeaturesFour(),
								req.getFeaturesFive(),
								req.getFeaturesSix(),
								req.getAboutUs(),
								req.getAboutShopLogoLink(),
								req.getCountry_code(),
								true
								);
			        	logger.info("Vendor data added for Vendor ID " + req.getVendorId());
			            response = new StringBuilder("Vendor data added for Vendor ID " + req.getVendorId());
			        } 
			} catch(Exception e)
			{
				logger.error("Exception Adding Updating Vendor Config to database :{} "+e.getMessage(), e);
			}
		return response;
	}


	public StringBuilder addUpdateProducts(ProductReqVO req) {
		final String methodName = "addUpdateProducts()";
		StringBuilder response = new StringBuilder("ERROR ADDING UPDATING PRODUCTS");
		logger.info("{}:", methodName);
		try {
			logger.info("ADD UPDATE DELETE PRODUCTS:"+req.getStoreID());
//UPDATE SHOPDB.PRODUCTS_VENDOR_ID set product_name = ?, product_description=?, product_price=?, update_ts = ? WHERE product_id = ? AND vendor_id = ?" ;
			String storeID = req.getStoreID();
			ArrayList<Product> prds = req.getProduct();
			response = new StringBuilder("INSIDE ADD UPDATE");
			boolean productExists  = false;
			productExists =  jdbcTemplate.queryForObject(GET_PRODUCT_EXISTS_SQL, new Object[] {storeID, prds.get(0).getProduct_id()}, boolean.class);	
			for(int j=0;j<prds.size();j++)
			{    	
				if(productExists)
				{
				jdbcTemplate.update(UPDATE_PRODUCTS_SQL,
						prds.get(j).getProduct_description(),
						prds.get(j).getProduct_price(),
						prds.get(j).getProduct_quantity_avlbl(),
						new Timestamp(System.currentTimeMillis()),
						prds.get(j).getProduct_img_link(),
						prds.get(j).getProduct_img_sqr_link(),
						prds.get(j).getProduct_category(),
						prds.get(j).getProduct_name(),
						storeID,
						prds.get(j).getProduct_id()
						);	
		            logger.info("Product data updated for Vendor ID :" + storeID );
		            logger.info("Product data updated for Product ID :" + prds.get(j).getProduct_id() );
		            response = response.append("|UPDATED"+"VENDOR ID:"+storeID+":PRODUCT_ID:"+prds.get(j).getProduct_id()+"|");
		        }else{
		        	
		        	//INSERT INTO shopdb.products_vendor_id (unique_prod_id, vendor_id, product_id, product_name, product_description, product_price, product_quantity_avlbl, create_ts, update_ts) values (?,?,?,?,?,?,?,?,?)
		        	jdbcTemplate.update(INSERT_PRODUCTS_SQL,
		        			uniqueID(storeID),
		        			storeID,
		        			prds.get(j).getProduct_name(),
							prds.get(j).getProduct_description(),
							prds.get(j).getProduct_price(),
							prds.get(j).getProduct_quantity_avlbl(),
							new Timestamp(System.currentTimeMillis()),
							new Timestamp(System.currentTimeMillis()),
							prds.get(j).getProduct_img_link(),
							prds.get(j).getProduct_img_sqr_link(),
							prds.get(j).getProduct_category(),
							true
							);
		        	logger.info("Product Data added for Vendor ID " + storeID);
		        	logger.info("Product Data added for Product ID " + prds.get(j).getProduct_id());
		        	response = response.append("|ADDED"+"VENDOR ID:"+storeID+":PRODUCT_ID:"+prds.get(j).getProduct_id()+"|");
		        }
				
			}
			} catch(Exception e)
			{
				logger.error("Exception Adding Updating Product to database :{} "+e.getMessage(), e);;
				response = response.append("|Exception Adding Updating Product|");
			}
		return response;
	}
	public StringBuilder removeProduct(ProductReqVO req) {
		final String methodName = "removeProduct()";
		StringBuilder response = new StringBuilder("ERROR REMOVING PRODUCTS");
		logger.info("{}:", methodName);
		try {
			logger.info("REMOVING PRODUCT");
//UPDATE SHOPDB.PRODUCTS_VENDOR_ID set product_name = ?, product_description=?, product_price=?, update_ts = ? WHERE product_id = ? AND vendor_id = ?" ;
			String storeID = req.getStoreID();
			ArrayList<Product> prds = req.getProduct();
			response = new StringBuilder("INSIDE REMOVE PRODUCT");
			for(int j=0;j<prds.size();j++)
			{
		        	jdbcTemplate.update(DELETE_PRODUCT_SQL,
							prds.get(j).getProduct_id(),
							storeID
							);
		        	logger.info("Product Data removed for Vendor ID " + storeID);
		        	logger.info("Product Data removed for Product ID " + prds.get(j).getProduct_id());
		        	response = response.append("|REMOVED"+"VENDOR ID:"+storeID+":PRODUCT_ID:"+prds.get(j).getProduct_id()+"|");
		        }
			} catch(Exception e)
			{
				logger.error("Exception Removing Product from database :{} "+e.getMessage(), e);;
				response = response.append("|Exception removing Product|");
			}
		return response;
	}
	public StringBuilder deleteVendorConfig(String storeId) {
		final String methodName = "deleteVendorConfig()";
		StringBuilder response = new StringBuilder("ERROR ADDING UPDATING PRODUCTS");
		logger.info("{}:", methodName);
		try {
			String checkstatus = "select count(vendor_id) from vendor_config where vendor_id =? and is_enabled ='1' ";
			String query = "Update shopdb.vendor_config set is_enabled ='0' where vendor_id =? and is_enabled ='1' ";
			int count = jdbcTemplate.queryForObject(checkstatus, new Object[] {storeId}, int.class);
			if(count != 0) {
				jdbcTemplate.update(query, new Object[]{storeId});
				response = new StringBuilder("VENDOR CONFIG DELETED!");
			} else {
				response = new StringBuilder("NO ACTIVE VENDOR CONFIG");
			}
			
		} catch (Exception e) {
			logger.error("Exception Deleting Vendor Config to database :{} "+e.getMessage(), e);;
		}
		return response;
	}


	public ArrayList<Product> getCategoryProducts(String storeID, String product_category) {
		final String methodName = "getCategoryProducts()";
		logger.info("{}: Logger DB ", methodName);
		
		try {
			
			logger.info("GET_CATEGORY_PRODUCTS_SQL:"+GET_CATEGORY_PRODUCTS_SQL+":"+storeID+product_category);
			jdbcTemplate.query(GET_CATEGORY_PRODUCTS_SQL,  ShopDAOHelper.createKeySQLParams(storeID,product_category), shopProductsMapper);
			logger.info("GETTING PRODUCTS..");
		} catch(Exception e)
		{
			logger.error("Exception Occurred while Connecting to database :{} "+e.getMessage(), e);;
		}
		return productsList;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<CartRequestVO> initCart(CartRequestVO req, Cookie[] ck, HttpServletResponse httpResponse) {
		StringBuilder response = new StringBuilder("ERROR INITIALISING CART");
		final String methodName = "initCart()";
		logger.info("{}:", methodName);
		String userId = req.getUserID();
		String tranId = req.getTranID();
		String cartId = req.getCartID();
		boolean cartWithCurrentUserIdExits = false;
		boolean cartWithCokiesSessionIDExist = false;
		String transCartID = "";
		String userCartID = "";
		boolean isCookieNameExists = false;
		String cart_sessionId =null;
		ArrayList<CartRequestVO> cartRequestVOList = new ArrayList<CartRequestVO>();
		ArrayList<CartProduct> productsList = new ArrayList<CartProduct>();
		if (tranId == null || tranId.isEmpty() || tranId.equalsIgnoreCase("null")) {
			tranId = ShopConstants.uniqueID(tranId);
		}
		if (cartId == null || cartId.isEmpty() || cartId.equalsIgnoreCase("null")) {
			cartId = ShopConstants.uniqueID(cartId);
		}
		if(ck==null || ck.length==0) {
			setCookies("CARTSESSIONID_"+req.getStoreID(), cartId, httpResponse);
			cart_sessionId = cartId;
			
		}else {
			for(Cookie c : ck) {
				if(c.getName().equalsIgnoreCase("CARTSESSIONID_"+req.getStoreID())) {
					cart_sessionId = c.getValue();
					isCookieNameExists =true;
					boolean isCookievalid = c.getMaxAge() == -1 ? true : false;
					if(isCookievalid) {
						cartWithCokiesSessionIDExist = jdbcTemplate.queryForObject(
								"SELECT EXISTS(SELECT 1 from shopdb.order_main WHERE order_status=? AND cart_session=? AND vendor_id =CAST(? AS UNSIGNED))", boolean.class,
								new Object[] {ShopConstants.ORDER_STATUS_INIT,c.getValue(), req.getStoreID()});
					}
					if(!cartWithCokiesSessionIDExist) {
						setCookies("CARTSESSIONID_"+req.getStoreID(), cartId, httpResponse);
						cart_sessionId = cartId;
					}
					break;
				} 
			}
			
			if(!isCookieNameExists) {
				setCookies("CARTSESSIONID_"+req.getStoreID(), cartId, httpResponse);
				cart_sessionId = cartId;
			}
		}
		if(userId != null && !StringUtils.isEmpty(userId) && !userId.equalsIgnoreCase("null")) {
			cartWithCurrentUserIdExits = jdbcTemplate.queryForObject(
					"SELECT EXISTS(SELECT 1 FROM shopdb.order_main WHERE  user_id=CAST(? AS UNSIGNED) AND order_status=? and vendor_id=CAST(? AS UNSIGNED))", boolean.class,
					new Object[] { userId,ShopConstants.ORDER_STATUS_INIT, req.getStoreID()});
		}
		try {
			if (userId != null && !StringUtils.isEmpty(userId) && !userId.equalsIgnoreCase("null")) {
				if (!cartWithCurrentUserIdExits && !cartWithCokiesSessionIDExist) {
					logger.info("initialising cart with userId and sessionId");
					jdbcTemplate.update(
							"INSERT INTO shopdb.order_main(unique_id,user_id, cart_id,order_status,order_type,create_ts, update_ts,cart_session=?,vendor_id) VALUES(?,?,?,?,?,?,?,?,?)",
							tranId, userId, cartId, ShopConstants.ORDER_STATUS_INIT, ShopConstants.ORDER_TYPE_DELIVERY,
							new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()),cart_sessionId,req.getStoreID());
				} else if (cartWithCurrentUserIdExits && cartWithCokiesSessionIDExist) {
					logger.info(
							"Merging newly created cart to old cart and deleting new cart.All refrences and products list updateing to old cart");
					transCartID = jdbcTemplate.queryForObject("SELECT cart_id FROM shopdb.order_main WHERE cart_session=? AND vendor_id=CAST(? AS UNSIGNED)",
							new Object[] {cart_sessionId, req.getStoreID() }, String.class);
					userCartID = jdbcTemplate.queryForObject("SELECT cart_id FROM shopdb.order_main WHERE user_id = ? AND vendor_id=CAST(? AS UNSIGNED)",
							new Object[] { userId, req.getStoreID() }, String.class);
					if (transCartID != null && !transCartID.isEmpty()) {
						logger.info("GET_CART_PRODUCTS_SQL:" + GET_CART_PRODUCTS_SQL + ":" + transCartID);
						productsList = jdbcTemplate.query(GET_CART_PRODUCTS_SQL,
								ShopDAOHelper.createKeySQLParams(transCartID), shopOrderProductsMapper);
						logger.info("updating new cart Id to old cart ID");
						if (!productsList.isEmpty() && productsList != null) {
							for (CartProduct pr : productsList) {
								jdbcTemplate.update(
										"UPDATE shopdb.order_products SET order_id=?,update_ts=? WHERE vendor_id=? AND product_id=? ",
										userCartID, new Timestamp(System.currentTimeMillis()), req.getStoreID(),
										pr.getProduct_id());
							}
						}
					}
				} else {
					logger.info("cart existed for user.Returning same cart Id for further use");
				}

			} else {
				logger.info("initialising cart with sessionId and cart_sessionId and vendor_id..............");
				if (!cartWithCokiesSessionIDExist || ck == null) {
					jdbcTemplate.update(
							"INSERT INTO shopdb.order_main (unique_id,user_id, cart_id,order_status, order_type,create_ts, update_ts,cart_session,vendor_id) VALUES(?,?,?,?,?,?,?,?,?)",
							tranId, "", cartId, ShopConstants.ORDER_STATUS_INIT, ShopConstants.ORDER_TYPE_DELIVERY,
							new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()),cart_sessionId, req.getStoreID());
				}
			}
			logger.info("get cart with details.This cart will be load with all necessary details.User add update product in this cart only.......");
			if (cartWithCurrentUserIdExits) {
				req.setUserID(userId);
				cartRequestVOList = refreshCart(req);
			} else {
				req.setCart_session(cart_sessionId);
				cartRequestVOList = refreshCart(req);
						
			}
		} catch (Exception e) {
			logger.error("Exception Occurred while Connecting to database :{} " + e.getMessage(), e);
		}
		return cartRequestVOList;
	}


	private void setCookies(String cartCookieName, String cartId, HttpServletResponse httpResponse) {
		Cookie c = new Cookie(cartCookieName, cartId);
		c.setMaxAge(15*24*60*60);
		c.setPath("store/initCart");
		c.setHttpOnly(true);
		c.setSecure(false);
		httpResponse.addCookie(c);
	}


	protected CartRequestVO cartResquestVoObject(ResultSet rs, CartRequestVO cartRequestVO) {
		try {
			cartRequestVO.setCartID(rs.getString("cart_id"));
			cartRequestVO.setTotal_cost(calucateTotalCast(cartRequestVO));
			cartRequestVO.setTranID(rs.getString("unique_id"));
			cartRequestVO.setPayment_status(rs.getString("payment_status"));
			cartRequestVO.setPayment_type(rs.getString("payment_type"));
			cartRequestVO.setOrder_status(rs.getString("order_status"));
			cartRequestVO.setOrder_type(rs.getString("order_type"));
			cartRequestVO.setUpdate_ts(rs.getDate("update_ts"));
			cartRequestVO.setCreate_ts(rs.getDate("create_ts"));
			if(rs.getString("user_id") != null) {
				cartRequestVO.setUserID(rs.getString("user_id"));
			}
		} catch (SQLException e) {
			logger.error("error in row mapping"+e.getMessage());
		}
		return cartRequestVO;
	}


	public ArrayList<Product> getProductByProductId(String storeId, String product_id) {
	try {
			
			logger.info("GET_PRODUCTS_SQL:"+storeId+product_id);
			jdbcTemplate.query("SELECT product_id, product_name, product_description, product_price, product_quantity_avlbl, product_category,product_img_link,product_img_sqr_link FROM shopdb.products_vendor_id WHERE vendor_id = CAST(? AS UNSIGNED) AND product_id=? AND is_enabled = True",  ShopDAOHelper.createKeySQLParams(storeId,product_id), shopProductsMapper);
			logger.info("GETTING PRODUCTS..");
		} catch(Exception e)
		{
			logger.error("Exception Occurred while Connecting to database :{} "+e.getMessage(), e);
		}
		return productsList;
	}


	public StringBuilder deleteCartProduct(CartProduct req) {
		final String methodName = "deleteCartProduct()";
		StringBuilder response = new StringBuilder("ERROR DELETING PRODUCT CART");
		logger.info("{}:", methodName);
		try {
			String checkstatus = "SELECT EXISTS(SELECT 1 FROM shopdb.order_products  WHERE product_id =? AND vendor_id=? AND order_id=?)";
			String query = "DELETE FROM  shopdb.order_products  WHERE product_id =? AND vendor_id=? AND order_id=? ";
			boolean cartProductExists = jdbcTemplate.queryForObject(checkstatus, boolean.class, new Object[] {req.getProduct_id(),req.getVendor_id(),req.getOrder_id()});
			if(cartProductExists) {
				jdbcTemplate.update(query, new Object[]{req.getProduct_id(),req.getVendor_id(),req.getOrder_id()});
				response = new StringBuilder("CART PRODUCT DELETED!");
			} else {
				response = new StringBuilder("NO PRODUCT FOUND TO DELETE");
			}
			
		} catch (Exception e) {
			logger.error("Exception Deleting Cart Product from database :{} "+e.getMessage(), e.getMessage());
		}
		return response;
	}
	public StringBuilder addUpdateQuantity(CartProduct req) {
		final String methodName = "addUpdateQuantity()";
		StringBuilder response = new StringBuilder("ERROR UPDATING PRODUCT CART");
		logger.info("{}:", methodName);
		try {
			String checkstatus = "SELECT EXISTS(SELECT 1 from shopdb.order_products  WHERE product_id =? AND vendor_id=? AND order_id=?)";
			String query = "UPDATE  shopdb.order_products SET product_quantity=?  WHERE product_id =? AND vendor_id=? AND order_id=? ";
			boolean cartProductExists = jdbcTemplate.queryForObject(checkstatus, boolean.class, new Object[] {req.getProduct_id(),req.getVendor_id(),req.getOrder_id()});
			if(cartProductExists) {
				jdbcTemplate.update(query, new Object[]{req.getProduct_quantity(),req.getProduct_id(),req.getVendor_id(),req.getOrder_id()});
				response = new StringBuilder("CART PRODUCT UPDATED!");
			} else {
				response = new StringBuilder("NO PRODUCT FOUND TO UPDATED");
			}
			
		} catch (Exception e) {
			logger.error("Exception Deleting Cart Product from database :{} "+e.getMessage(), e.getMessage());
		}
		return response;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<CartRequestVO> refreshCart(CartRequestVO req) {
		final String methodName = "refreshCart()";
		StringBuilder response = new StringBuilder("ERROR REFRESHING CART");
		logger.info("{}:", methodName);
		ArrayList<CartRequestVO> cartRequestVOList = new ArrayList<CartRequestVO>();
		try {
			if(req.getCart_session() != null && !req.getCart_session().isEmpty()) {
				cartRequestVOList = (ArrayList<CartRequestVO>) jdbcTemplate.query(
						"SELECT * FROM shopdb.order_main WHERE cart_session = ?", new Object[] { req.getCart_session() }, new RowMapper() {

							@Override
							public CartRequestVO mapRow(ResultSet rs, int rowNum) throws SQLException {
								if (rs.getString("cart_id") != null) {
									return cartResquestVoObject(rs, new CartRequestVO());
								}
								return null;
							}
						});
			} else if(req.getUserID() != null && !req.getUserID().isEmpty()) {
				cartRequestVOList = (ArrayList<CartRequestVO>) jdbcTemplate.query(
						"SELECT * FROM shopdb.order_main WHERE user_id = ?", new Object[] { req.getUserID() }, new RowMapper() {

							@Override
							public CartRequestVO mapRow(ResultSet rs, int rowNum) throws SQLException {
								if (rs.getString("cart_id") != null) {
									return cartResquestVoObject(rs, new CartRequestVO());
								}
								return null;
							}
						});
			} else if(req.getCartID() != null && !req.getCartID().isEmpty()) {
				cartRequestVOList = (ArrayList<CartRequestVO>) jdbcTemplate.query(
						"SELECT * FROM shopdb.order_main WHERE cart_id = ?", new Object[] { req.getCartID() }, new RowMapper() {

							@Override
							public CartRequestVO mapRow(ResultSet rs, int rowNum) throws SQLException {
								if (rs.getString("cart_id") != null) {
									return cartResquestVoObject(rs, new CartRequestVO());
								}
								return null;
							}
						});
			}
			
		} catch (Exception e) {
			logger.error("Exception Getting Cart from database :{} "+e.getMessage(), e.getMessage());
		}
		return cartRequestVOList;
	}
	
	
	public  Double calucateTotalCast(CartRequestVO cartRequestVO) {
		productsListOrder = getCart(cartRequestVO);
		double total = 00.00; 
		if(productsListOrder != null && !productsListOrder.isEmpty()) {
			for(CartProduct pl :productsListOrder) {
				total = total+pl.getProduct_price()*pl.getProduct_quantity();
			}
		}
		return total;
	}


	public ArrayList<CartAddress> getCartDeliveryAddress(CartAddress req) {
		final String methodName = "getCartDeliveryAddress()";
		logger.info("{}:", methodName);
		
		try {
			if(req.getUserID() == null || req.getUserID() == ""|| req.getUserID().isEmpty()) {
				logger.info("GET_CART_DELIVERY_ADDRESS_SQL:"+GET_CART_DELIVERY_ADDRESS_SQL+":"+req.getCartID());
				jdbcTemplate.query(GET_CART_DELIVERY_ADDRESS_SQL,  ShopDAOHelper.createKeySQLParams(req.getCartID()), shopOrderAddressMapper);
			} else {
				logger.info("GET_CART_DELIVERY_ADDRESS_USER_ID_SQL:"+GET_CART_DELIVERY_ADDRESS_USER_ID_SQL+":"+req.getCartID(),req.getUserID());
				jdbcTemplate.query(GET_CART_DELIVERY_ADDRESS_USER_ID_SQL,  ShopDAOHelper.createKeySQLParams(req.getCartID(), req.getUserID()), shopOrderAddressMapper);
			}
			
			logger.info("GETTING DELIVERY ADDRESS..");
		} catch(Exception e)
		{
			logger.error("Exception Occurred while Connecting to database :{} "+e.getMessage(), e);
		}
		return cartDeliveryAddressList;
	}


	public StringBuilder addUpdateDeliveryAddress(CartAddress req) {
		final String methodName = "addUpdateDeliveryAddress()";
		StringBuilder response = new StringBuilder("ERROR ADDING UPDATING DELIVERY ADDRESS");
		logger.info("{}:", methodName);
		boolean isAddressExists = false;
		try {
			if (req.getCartID() != null && !req.getCartID().equalsIgnoreCase("")) {
				logger.info("adding updating cart delivery address..");
				if (req.getAddressID() != null && !req.getAddressID().equals("")) {
					isAddressExists = jdbcTemplate.queryForObject(
							"SELECT EXISTS(SELECT 1 FROM shopdb.order_address WHERE   order_address.address_id=? AND order_address.cart_id =?)",
							boolean.class, new Object[] { req.getAddressID(), req.getCartID() });
				} else {
					isAddressExists = jdbcTemplate.queryForObject(
							"SELECT EXISTS(SELECT 1 FROM shopdb.order_address WHERE   order_address.cart_id =?)",
							boolean.class, new Object[] { req.getCartID() });
				}

				if (isAddressExists) {
					logger.info("updating cart delivery address..");
					jdbcTemplate.update(
							"UPDATE shopdb.order_address SET user_id=?,full_name=?,house_no=?,address_first=?,address_two=?,city=?,"
									+ "state=?,zip=?,landmark=?,country=?,update_date=?,phone=? WHERE address_id=? AND cart_id=? ",
							req.getUserID(), req.getFullName(), req.getHouseNo(), req.getAddressFirst(),
							req.getAddressTwo(), req.getCity(), req.getState(), req.getZip(), req.getLandmark(),
							req.getCountry(), new Timestamp(System.currentTimeMillis()), req.getPhone(), req.getAddressID(),
							req.getCartID());
					return response = new StringBuilder("Cart Delivery Address Updated:" + "cartId" + req.getCartID());

				} else {
					logger.info("adding new cart delivery address..");
					jdbcTemplate.update(
							"INSERT INTO shopdb.order_address(address_id,cart_id,user_id,full_name,house_no,address_first,address_two,city,state,"
									+ "zip,landmark,country,created_date,update_date,phone) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
							ShopConstants.uniqueID(""), req.getCartID(), req.getUserID(), req.getFullName(),
							req.getHouseNo(), req.getAddressFirst(), req.getAddressTwo(), req.getCity(), req.getState(),
							req.getZip(), req.getLandmark(), req.getCountry(),
							new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()),req.getPhone());

					return response = new StringBuilder("New Delivery Address Addedd:" + "cartId" + req.getCartID());
				}

			}
			return response = new StringBuilder("ERROR IN ADDING UPDATING DELIVERY LOCATION:");
		} catch (Exception e) {
			logger.error("Exception Occurred while Connecting to database :{} " + e.getMessage(), e);
		}
		return response;
	}


	public boolean isValidStore(String vendorId) {
		boolean isExist = false;
		if(vendorId != null && !vendorId.isEmpty() &&! "".equals(vendorId)) {
			try {
				String query = "SELECT EXISTS(SELECT 1 FROM shopdb.vendor_config  WHERE vendor_id =CAST(? AS UNSIGNED) AND is_enabled=1)";
				isExist = jdbcTemplate.queryForObject(query,  new Object[] {vendorId}, boolean.class);
			} catch (Exception e) {
				logger.error("Exception Occurred while Connecting to database :{} " + e.getMessage(), e);
			}
			
		}
		return isExist;
	}

	public StringBuilder changeCartStatusToBilled(CartRequestVO cartRequestVo) {
		int vendorId = Integer.parseInt(cartRequestVo.getStoreID());
		User user = getAdminDetailsByID(vendorId);
		if (user != null) {
			try {
				logger.info("updating  cart payment status.......................");
				jdbcTemplate.update(
						"UPDATE shopdb.order_main SET user_id=?, order_status=?, order_type=?, update_ts=?,payment_type=?,payment_status=? WHERE cart_id =?",
						cartRequestVo.getUserID(), ShopConstants.ORDER_STATUS_COMPLETED,
						ShopConstants.ORDER_TYPE_DELIVERY, new Timestamp(System.currentTimeMillis()),
						cartRequestVo.getPayment_type(), "PENDING", cartRequestVo.getCartID());

				SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
				simpleMailMessage.setTo(user.getEmailId());
				simpleMailMessage.setText(
						"Hi,\n " + user.getFirstName() + "" + user.getLastName() + "");
				simpleMailMessage.setSubject("New Order Received!");
				emailSenderService.sendEmail(simpleMailMessage);
				return new StringBuilder("CART BILLED");
			} catch (Exception e) {
				logger.error("Exception Occurred while Connecting to database :{} " + e.getMessage(), e);
			}
		}

		return new StringBuilder("ERROR IN CART BILLING");

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public User getAdminDetailsByID(int id) {
		logger.info("Getting user info by ID :{} ");
		ArrayList<User> adminUserList = new ArrayList<User>();
		if (id != 0) {
			boolean isExists = jdbcTemplate.queryForObject(
					"SELECT EXISTS(SELECT 1 from shopdb.vendor_user_details where user_id=?)", new Object[] { id }, boolean.class);
			if (isExists) {
				try {
					adminUserList =  (ArrayList<User>) jdbcTemplate.query(GET_USER_BY_ID, new Object[] { id },
							new RowMapper() {

								@Override
								public User mapRow(ResultSet rs, int rowNum) throws SQLException {
									if (rs.getString("user_id") != null) {
										ArrayList<UserRoles> r = new ArrayList<UserRoles>();
										User u = new User();
										u.setUserId(rs.getLong("user_id"));
										u.setEmailId(rs.getString("email_id"));
										u.setEnabled(rs.getBoolean("is_enabled"));
										u.setFirstName(rs.getString("first_name"));
										u.setLastName(rs.getString("last_name"));
										if (rs.getString("role_name").split(",").length > 1) {
											for (int i = 0; i < rs.getString("role_name").split(",").length; i++) {
												UserRoles roles = new UserRoles();
												roles.setRoleId(Long.decode(rs.getString("role_id").split(",")[i]));
												roles.setRoleName(rs.getString("role_name").split(",")[i]);
												r.add(roles);
											}
										} else {
											if (rs.getString("role_name") != null) {
												UserRoles roles = new UserRoles();
												roles.setRoleId(rs.getLong("role_id"));
												roles.setRoleName(rs.getString("role_name"));
												r.add(roles);
											}
										}
										u.setRoles(r);
										return u;
									}
									return null;
								}
							});
				} catch (Exception e) {
					logger.error("Exception Occurred while Connecting to database :{} " + e.getMessage(), e);
				}
				if (adminUserList != null && !adminUserList.isEmpty()) {
					return adminUserList.get(0);
				}
				return new User();
			}
		}
		return new User();

	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<CartRequestVO> getBilledCart(CartRequestVO req) {
		final String methodName = "getBilledCart()";
		logger.info("{}:", methodName);
		
		ArrayList<CartRequestVO> cartRequestVOList = new ArrayList<CartRequestVO>();
		cartRequestVOList = (ArrayList<CartRequestVO>) jdbcTemplate.query(
				"SELECT * FROM shopdb.order_main WHERE cart_id = ? AND order_status=? AND payment_type=? AND vendor_id=CAST(? AS UNSIGNED)", new Object[] { req.getCartID(),ShopConstants.ORDER_STATUS_COMPLETED, req.getPayment_type(), req.getStoreID()}, new RowMapper() {

					@Override
					public CartRequestVO mapRow(ResultSet rs, int rowNum) throws SQLException {
						if (rs.getString("cart_id") != null) {
							return cartResquestVoObject(rs, new CartRequestVO());
						}
						return null;
					}
				});
		return cartRequestVOList;
	}


	@SuppressWarnings("unchecked")
	public ArrayList<CartRequestVO> getAllBilledCart(CartRequestVO req) {
		final String methodName = "getBilledCart()";
		logger.info("{}:", methodName);
		
		ArrayList<CartRequestVO> cartRequestVOList = new ArrayList<CartRequestVO>();
		cartRequestVOList = (ArrayList<CartRequestVO>) jdbcTemplate.query(
				"SELECT * FROM shopdb.order_main WHERE order_status=? AND vendor_id=CAST(? AS UNSIGNED)", new Object[] {ShopConstants.ORDER_STATUS_COMPLETED, req.getStoreID()}, new RowMapper() {

					@Override
					public CartRequestVO mapRow(ResultSet rs, int rowNum) throws SQLException {
						if (rs.getString("cart_id") != null) {
							return cartResquestVoObject(rs, new CartRequestVO());
						}
						return null;
					}
				});
		return cartRequestVOList;
	}


	public ArrayList<CartDetailsVO> getCartDetailsByID(String vendorId, String cartId) {
		try {
			jdbcTemplate.query(GET_CART_DETAILS_BY_ID, ShopDAOHelper.createKeySQLParams(vendorId, cartId),
					shopOrderDetailsMapper);
			return cartDetailsVOList;
		} catch (Exception e) {
			logger.error("Exception Occurred while Connecting to database :{} " + e.getMessage());
		}
		return cartDetailsVOList;

	}

}
