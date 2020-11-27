package com.a2z.shop.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.a2z.shop.constant.ShopConstants;
@Component
public class ConvertMultipartToFileUtils {
	public static final Logger logger= LoggerFactory.getLogger(ConvertMultipartToFileUtils.class);
	
	public File convertMultipartfile(MultipartFile file, String fileExtention, String pathTmp) {
		File convertedFile = null;
		FileOutputStream fos = null;
		try {
			convertedFile = new File(Paths.get(pathTmp+"/"+System.currentTimeMillis()+"_"+ShopConstants.uniqueID("")+fileExtention).toAbsolutePath().normalize().toString());
			convertedFile.createNewFile();
			fos = new FileOutputStream(convertedFile);
			fos.write(file.getBytes());
			fos.close();	
		} catch (FileNotFoundException e) {
			logger.error("Error in converting multipart file to file" +e);
		} catch (IOException e) {
			logger.error("Error intialising new file from multipart file" +e);
		}finally {
			try {
				if(fos != null) {
					fos.close();
				}
			} catch (Exception e2) {
				logger.error("Error in closing streams image" +e2.getMessage());
			}
		}
		return convertedFile;
	}

}
