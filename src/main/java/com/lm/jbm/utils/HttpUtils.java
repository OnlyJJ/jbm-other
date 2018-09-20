package com.lm.jbm.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;





@SuppressWarnings("unchecked")
public class HttpUtils {
	
	/**  设置http链接超时时间,单位:毫秒 */
	private static int connectTimeOutMillionSeconds = 30000;
	
	/**  设置http读超时时�?单位:毫秒 */
	private static final int readTimeOutMillionSeconds = 30000;
	
	
	public static  final String webClient = "web"; 
	public static  final String androidClient = "android";
	public static  final String iosClient = "ios";
	
	public static  int webClienttypeInt = 1; 
	public static  int androidClienttypeInt = 2;
	public static  int iosClienttypeInt = 3;
	
	public static String HOST = PropertiesUtil.getValue("HOST");
	public static int PORT = Integer.parseInt(PropertiesUtil.getValue("HOST_PORT"));
	
	 public static String post3(String url, String json, String ip) {
	        // 创建Httpclient对象
	        CloseableHttpClient httpClient = HttpClients.createDefault();
	        CloseableHttpResponse response = null;
	        String resultString = "";
	        try {
	            // 创建Http Post请求
	            HttpPost post = new HttpPost(url);
	            HttpHost proxy = new HttpHost(HOST,PORT);
	            RequestConfig requestConfig = RequestConfig.custom()
	                    .setProxy(proxy)
	                    .setConnectTimeout(45000)
	                    .setSocketTimeout(45000)
	                    .setConnectionRequestTimeout(3000)
	                    .build();
	            post.setConfig(requestConfig);
	            // 创建请求内容
	            ByteArrayEntity entity = new ByteArrayEntity(GZipUtil.compressToByte(json));
//	            StringEntity entity = new StringEntity(GZipUtil.compressToString(json), ContentType.APPLICATION_JSON);
	            post.setEntity(entity);
	            post.addHeader(HTTP.CONTENT_TYPE, "application/json");
	            post.addHeader("X-Real-IP", ip);
	            post.addHeader("X-Forwarded-For", ip);
	            post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
	            // 执行http请求
	            response = httpClient.execute(post);
	            HttpEntity entitys = response.getEntity();
	            resultString = EntityUtils.toString(entitys, "utf-8");
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	            	if(response != null) {
	            		response.close();
	            	}
	            	if(httpClient != null) {
	            		httpClient.close();
	            	}
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }

	        return resultString;
	    }	
	
	/**
	 * 
	 * @param url
	 * @param parms
	 * @param chartSet 为空则采用默认编�?
	 * @return
	 * @throws Exception 
	 */
	public static String post2(String url, String param, String ip) throws Exception {
		long beginTimeMillis = System.currentTimeMillis();
		HttpURLConnection conn = null;
		StringBuffer result = new StringBuffer();
		BufferedReader bufr = null;
		try {
			URL u = new URL(url);
            conn = (HttpURLConnection) u.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setRequestProperty("Charset", "utf-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);	
			conn.setRequestProperty("ip", ip);
			conn.setRequestProperty("X-Real-IP", ip);
			conn.setRequestProperty("X-Forwarded-For", ip);
			conn.setRequestProperty("HTTP_X_FORWARDED_FOR", ip);
			conn.setRequestProperty("HTTP_CLIENT_IP", ip);
			conn.setRequestProperty("REMOTE_ADDR", ip);
//			conn.setRequestProperty("Host", "");
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Content-Length", "17");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Origin", "ORIGIN");
//			conn.setRequestProperty(
//					"User-Agent",
//					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
//			conn.setRequestProperty("Content-Type",
//					"application/x-www-form-urlencoded");
//			conn.setRequestProperty(
//					"Referer",
//					"REFERER");
//			conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
//			conn.setRequestProperty("Accept-Language",
//					"zh-CN,zh;q=0.8,en;q=0.6,ja;q=0.4,pt;q=0.2");
//			
			
			conn.setConnectTimeout(connectTimeOutMillionSeconds);
			conn.setReadTimeout(readTimeOutMillionSeconds);
			conn.setRequestMethod("POST");
//			conn.getOutputStream().write(GZipUtil.compressToByte(param,"utf-8"));
			conn.getOutputStream().write(param.getBytes("utf-8"));
			conn.connect();
			InputStream in = conn.getInputStream();
			bufr = new BufferedReader(new InputStreamReader(in, "utf-8"));
			String line = null;
			while ((line = bufr.readLine()) != null) {
				result.append(line);
			}
		}catch (Exception e) {
			throw e;
		} finally {
			try {
				if (bufr != null)
					bufr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (conn != null) {
				conn.disconnect();
			}
			long endTimeMillis = System.currentTimeMillis();
			logResponseMsg(url, result.toString(),beginTimeMillis,endTimeMillis);
		}
		
		return result.toString();
	}
	
	public static String post(String url, String par) throws Exception {
		long beginTimeMillis = System.currentTimeMillis();
		HttpURLConnection conn = null;
		StringBuffer result = new StringBuffer();
		BufferedReader bufr = null;
		String ret = "";
		try {
			URL u = new URL(url);
            conn = (HttpURLConnection) u.openConnection();
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setRequestProperty("Charset", "utf-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(connectTimeOutMillionSeconds);
			conn.setReadTimeout(readTimeOutMillionSeconds);
			conn.setRequestMethod("POST");
			conn.getOutputStream().write(GZipUtil.compressToByte(par));
			conn.getOutputStream().write(par.getBytes("utf-8"));
			conn.connect();
			InputStream in = conn.getInputStream();
//			bufr = new BufferedReader(new InputStreamReader(in, "utf-8"));
//			String line = null;
//			while ((line = bufr.readLine()) != null) {
//				result.append(line);
//			}
			ByteArrayOutputStream bat = new ByteArrayOutputStream();
			byte[] b = new byte[512];
			int n = 0;
			while((n = in.read(b, 0 , 512)) >0) {
				bat.write(b, 0 , n);
			}
			byte[] out = bat.toByteArray();
			ret = GZipUtil.uncompressToString(out);
		}catch (Exception e) {
			throw e;
		} finally {
			try {
				if (bufr != null)
					bufr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (conn != null) {
				conn.disconnect();
			}
			long endTimeMillis = System.currentTimeMillis();
			logResponseMsg(url, ret,beginTimeMillis,endTimeMillis);
		}
		
		return ret;
	}
	
	
	
	/**
	 * 打印http请求日志
	 * @param url
	 * @param result
	 * @param beginTimeMillis
	 * @param endTimeMillis
	 */
	private static void logResponseMsg(String url, String result,
			long beginTimeMillis, long endTimeMillis) {
		double second = ((double)(endTimeMillis-beginTimeMillis))/1000;
	}

	private static String maptostr(Map<String, Object> parms) {
		if (parms == null || parms.size() == 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (Entry<String, Object> e : parms.entrySet()) {
			sb.append(e.getKey()).append("=").append(e.getValue()).append("&");
		}
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}

}