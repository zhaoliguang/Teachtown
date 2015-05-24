package com.teachtown.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Md5Utils {


	private static byte[] md5(String s) {
		MessageDigest algorithm;
		try {
			algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(s.getBytes("UTF-8"));
			byte[] messageDigest = algorithm.digest();
			return messageDigest;
		} catch (Exception e) {
		}
		return null;
	}

	private static byte[] hmac(String data, String secret) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
		Mac mac = Mac.getInstance("HmacSHA256");
		// get the bytes of the hmac key and data string
		byte[] secretByte = secret.getBytes("UTF-8");
		byte[] dataBytes = data.getBytes("UTF-8");
		SecretKey secretKey = new SecretKeySpec(secretByte, "HMACSHA256");
		mac.init(secretKey);
		return mac.doFinal(dataBytes);
	}

	private static final String toHex(byte hash[]) {
		if (hash == null) {
			return null;
		}
		StringBuffer buf = new StringBuffer(hash.length * 2);
		int i;

		for (i = 0; i < hash.length; i++) {
			if ((hash[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString(hash[i] & 0xff, 16));
		}
		return buf.toString();
	}

	public static String hash(String data, String key) {
		try {
			return new String(toHex(hmac(data, key)).getBytes("UTF-8"), "UTF-8");
		} catch (Exception e) {
			return data;
		}
	}
	
	/**
	 * 返回32位大写MD5�?
	 * @param content
	 * @return  异常返回null 转换成功返回 32位大写MD5
	 */
	public static String getEncode32MD5(String content){
		 try {
             MessageDigest digest = MessageDigest.getInstance("MD5");
             digest.update(content.getBytes("UTF-8"));
             StringBuilder builder = new StringBuilder();
             for (byte b : digest.digest()) {
                     builder.append(Integer.toHexString((b >> 4) & 0xf));
                     builder.append(Integer.toHexString(b & 0xf));
             }
           return builder.toString().toUpperCase();
     } catch (Exception e) {
    	 return null;
     }
	}

}
