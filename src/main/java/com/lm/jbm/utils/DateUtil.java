package com.lm.jbm.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static String format2Str(Date nowDate, String format) {
		SimpleDateFormat sdf =new SimpleDateFormat(format);
		return sdf.format(nowDate);
	}
	
	public static Date parse(String dateStr,String format) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(dateStr);
	}
	
}
