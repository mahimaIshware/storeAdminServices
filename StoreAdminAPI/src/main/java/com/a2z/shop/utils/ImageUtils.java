package com.a2z.shop.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
@Component
public class ImageUtils {
	public static final Logger logger= LoggerFactory.getLogger(ImageUtils.class);
	
	public String scale(File convertedFile, String targetedLocation, int width, int height,String fileExtention ) {
		BufferedImage img = null;
		
		try {
			/* scale image to given size size */
			img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			img.createGraphics().drawImage(ImageIO.read(convertedFile).getScaledInstance(width, height, Image.SCALE_SMOOTH),0,0,null);
			File fl = new File(targetedLocation);
			fl.createNewFile();
			ImageIO.write(img, fileExtention.replace(".", ""),fl);
			logger.info("Image rescaled... " +width+"'"+height);
		} catch (IOException e) {
			logger.error("Error in scaling image" +e);
		}
		if(Files.exists(Paths.get(targetedLocation))) {
			return targetedLocation;
		}
		return null;
		
	}

}
