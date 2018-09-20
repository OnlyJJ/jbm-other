package com.lm.jbm.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipException;

import org.apache.commons.lang.StringUtils;

/**
 * GZIP压缩解压�?
 * 
 * @author huangzp
 * @date 2015-4-14
 */
public class GZipUtil {

	private static String encode = "utf-8";
	private static final int length = 1024;

	public String getEncode() {
		return encode;
	}

	/**
	 * 设置 编码，默认编码：UTF-8
	 */
	public static void setEncode(String encode) {
		GZipUtil.encode = encode;
	}

	/**
	 * 字符串压缩为字节数组
	 */
	public static byte[] compressToByte(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes(encode));
			gzip.close();
		} catch (Exception e) {
		}
		return out.toByteArray();
	}

	/**
	 * 字符串压缩为字节数组
	 */
	public static byte[] compressToByte(String str, String encoding) {
		if (str == null || str.length() == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes(encoding));
			gzip.close();
		} catch (Exception e) {
		}
		return out.toByteArray();
	}
	
	public static String compressToString(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes(encode));
			gzip.close();
		} catch (Exception e) {
		}
		return out.toString(encode);
	}
	

	/**
	 * 字节数组解压缩后返回字符�?
	 */
	public static String uncompressToString(byte[] b) {
		if (b == null || b.length == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(b);

		try {
			GZIPInputStream gunzip = new GZIPInputStream(in);
			byte[] buffer = new byte[length];
			int n;
			while ((n = gunzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
		} catch (Exception e) {
		}
		return out.toString();
	}

	/**
	 * 字节数组解压缩后返回字符�?
	 * @throws IOException 
	 */
	public static String uncompressToString(byte[] b, String encoding) throws IOException {
		if (b == null || b.length == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(b);

		try {
			GZIPInputStream gunzip = new GZIPInputStream(in);
			byte[] buffer = new byte[length];
			int n;
			while ((n = gunzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
		}catch(ZipException zipException){ 
		}catch (IOException e) {
			throw e;
		}
		return out.toString(encoding);
	}
	
	public static String uncompress(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(
				str.getBytes("UTF-8"));
		byte[] b = new byte[512];
		int n = 0;
		while((n = in.read(b, 0 , 512)) >0) {
			out.write(b, 0 , n);
		}
		byte[] bt = out.toByteArray();
		return uncompressToString(bt);
	}
}
