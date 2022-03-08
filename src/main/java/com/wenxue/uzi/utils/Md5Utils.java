package com.wenxue.uzi.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author yl
 */
public class Md5Utils {
	static final String MD5 = "MD5";
	private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	private static final ThreadLocal<MessageDigest> MD5_DIGEST = ThreadLocal.withInitial(() -> {
		try {
			return MessageDigest.getInstance(MD5);
		}catch (Exception e) {
			return null;
		}
	});
	public static String MD5_FORMAT = "data=%s&iv=%s&key=%s&secret=%s&timestamp=%d";

	public static String md5(String value) {
		MessageDigest md5 = MD5_DIGEST.get();
		md5.reset();
		byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
		md5.update(bytes);
		return new String(encodeHex(md5.digest()));
	}
	private static char[] encodeHex(final byte[] data) {
		int l = data.length; char[] out = new char[l << 1];
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS_UPPER[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS_UPPER[0x0F & data[i]];
		}
		return out;
	}
}
