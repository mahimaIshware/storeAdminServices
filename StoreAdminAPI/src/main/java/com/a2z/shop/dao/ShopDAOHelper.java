package com.a2z.shop.dao;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShopDAOHelper {
	private static final Logger logger = LoggerFactory.getLogger(ShopDAOHelper.class);
	
	public static Object[] createKeySQLParams(String ...strings)
	{
		logger.info("createKeySQLParams...String");
		return (Object[]) strings;
	}
//	public static Object[] createKeySQLParams(ArrayList<String> strings)
//	{
//		return (Object[]) strings.toArray();
//	}
	public static Object[] createKeySQLParams(Object ...objects)
	{
		logger.info("createKeySQLParams...Object");
		return objects;
	}


}
