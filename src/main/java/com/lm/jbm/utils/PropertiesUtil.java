package com.lm.jbm.utils;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public final class PropertiesUtil {
	
	public static Properties pro;
	
	public static void load(boolean dev) {
		try {
			pro = new Properties();
			if(dev) {
				pro.load(RandomUtil.class.getClassLoader().getResourceAsStream("config.properties"));
			} else {
				pro.load(RandomUtil.class.getClassLoader().getResourceAsStream("config-test.properties"));
			}
			System.err.println("当前环境：" + pro.getProperty("environment"));
		} catch (Exception e) {
		}
	}
	
	public static String getValue(String key) {
		if(StringUtils.isEmpty(key)) {
			return "";
		}
		return pro.getProperty(key);
	}
}
