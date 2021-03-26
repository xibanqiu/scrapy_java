package com.github.crawler.tools;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * @author WaJn 2017年7月4日
 */
public class MD5 {
	private static String crypt(String str, boolean digit) {
		StringBuffer cryptString = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte[] hash = md.digest();

			for (int i = 0; i < hash.length; i++) {
				if (digit) {
					cryptString.append(byteDigit(hash[i]));
				} else {
					if ((0xff & hash[i]) < 0x10) {
						cryptString.append("0" + Integer.toHexString((0xFF & hash[i])));
					} else {
						cryptString.append(Integer.toHexString(0xFF & hash[i]));
					}
				}
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return cryptString.toString();
	}

	private static String byteDigit(byte ib) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		char[] ob = new char[2];
		ob[0] = Digit[(ib >>> 4) & 0X09];
		ob[1] = Digit[ib & 0X09];
		String s = new String(ob);
		return s;
	}

	/**
	 * MD5加盐
	 * 
	 * @param str
	 *            加密字符
	 * @param salt
	 *            盐
	 * @return
	 */
	public static String crypt(String str, String salt) {
		if (StringUtils.isEmpty(str) && StringUtils.isEmpty(salt)) {
			throw new NullPointerException("加密字符或盐不能为空");
		}
		String result = crypt(crypt(str, false) + crypt(salt, true), false);
		result = DigestUtils.sha256Hex(crypt(result, true) + DigestUtils.sha1Hex(salt));
		return result;
	}

	/**
	 * MD5
	 * 
	 * @param str
	 * @return
	 */
	public static String crypt(String str) {
		if (StringUtils.isEmpty(str)) {
			throw new NullPointerException("加密字符不能为空");
		}
		return crypt(str, false);
	}

}
