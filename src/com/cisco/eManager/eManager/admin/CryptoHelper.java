package com.cisco.eManager.eManager.admin;

import org.apache.log4j.*;
import java.security.MessageDigest;

public class CryptoHelper
{
	
	private static Logger logger = Logger.getLogger(CryptoHelper.class);
	CryptoHelper() {}

	public static String genCryptoMessage(String key)
	{ 
		logger.debug("genCryptoMessage for key: " + key);

		String prehashId = key + System.currentTimeMillis();
		String id =  prehashId;
		
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] bytes = prehashId.getBytes();

			try {
				// convert a byte array to base64 string
				id = new sun.misc.BASE64Encoder().encode(bytes);
			} catch (Exception e) {
				logger.info("Caugh exception during convert a byte array to base64 string: " + e);
			} 
		} catch (java.security.NoSuchAlgorithmException e) {
			logger.info("Caugh NoSuchAlgorithmException: " + e);
		} 

		logger.debug("Encrypted ID = " + id);
		return id;
	}

}
