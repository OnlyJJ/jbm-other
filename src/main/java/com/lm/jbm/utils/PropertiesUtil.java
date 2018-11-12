package com.lm.jbm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public final class PropertiesUtil {
	
	public static Properties pro;
	
	public static void load(boolean dev) {
		try {
			pro = new Properties();
			if(dev) {
				pro.load(RandomUtil.class.getClassLoader().getResourceAsStream("config.properties"));
				File file = new File("C:\\gift\\conf.properties");
				if(file.exists()) {
					FileInputStream in = new FileInputStream(file);
					Properties myPro = new Properties();
					myPro.load(in);
					if(!myPro.isEmpty()) {
						for(Object key : myPro.keySet()) {
							String keyStr = key.toString().trim();
							String value = myPro.getProperty(keyStr).trim();
							pro.put(keyStr, value);
						}
					}
				}
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
