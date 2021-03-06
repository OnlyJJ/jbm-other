package com.lm.jbm.utils;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;


public class RandomUtil {
	
	public static ConcurrentHashMap<String, String> ipMap = new ConcurrentHashMap<String, String>(512);
	
	public static final String[] ips = {
		"120.15.129.116",
		"120.15.4.153",
		"120.193.158.191",
		"120.193.252.165",
		"120.202.118.182",
		"120.229.126.113",
		"120.229.131.203",
		"120.236.173.194",
		"120.236.61.218",
		"120.239.162.72",
		"120.239.18.81",
		"120.3.163.113",
		"120.3.234.98",
		"120.37.40.248",
		"120.4.9.208",
		"120.85.87.211",
		"121.10.59.164",
		"121.17.117.104",
		"121.20.217.180",
		"121.207.17.218",
		"121.207.75.150",
		"121.22.243.245",
		"121.224.183.7",
		"121.226.252.223",
		"121.227.1.141",
		"121.236.119.229",
		"121.238.36.79",
		"121.32.193.217",
		"121.33.175.154",
		"121.33.33.32",
		"121.33.48.182",
		"121.35.202.244",
		"121.62.184.209",
		"121.62.220.178",
		"121.69.46.134",
		"122.143.20.117",
		"122.192.14.237",
		"122.195.136.228",
		"122.224.34.147",
		"122.224.52.36",
		"122.228.60.34",
		"122.233.110.59",
		"122.244.50.63",
		"122.70.145.82",
		"122.96.42.246",
		"122.97.178.254",
		"123.103.15.11",
		"123.115.99.157",
		"123.13.204.151",
		"123.135.243.129",
		"123.138.153.149",
		"123.138.228.69",
		"123.138.232.232",
		"123.147.244.34",
		"123.147.244.9",
		"123.147.244.9",
		"123.147.246.240",
		"123.147.250.44",
		"123.149.34.105",
		"123.149.86.129",
		"123.15.160.155",
		"123.158.205.63",
		"123.171.13.100",
		"123.179.253.46",
		"123.4.118.106",
		"123.52.219.74",
		"123.5.232.76",
		"123.7.30.22",
		"124.116.241.2",
		"124.126.224.207",
		"124.129.196.244",
		"124.130.85.237",
		"124.135.104.179",
		"124.160.153.157",
		"124.160.212.242",
		"124.160.215.131",
		"124.160.219.246",
		"124.163.231.180",
		"124.164.243.162",
		"124.165.143.70",
		"124.166.38.248",
		"124.200.106.90",
		"124.207.50.227",
		"124.227.55.95",
		"124.228.155.100",
		"124.228.19.2",
		"124.238.204.156",
		"124.239.251.104",
		"124.31.62.173",
		"124.72.51.198",
		"124.90.187.192",
		"125.39.30.150",
		"125.45.231.86",
		"125.65.222.210",
		"125.71.55.207",
		"125.72.45.119",
		"125.73.78.10",
		"125.73.93.29",
		"125.75.76.235",
		"125.76.128.22",
		"125.76.135.79",
		"125.79.67.34",
		"125.80.188.52",
		"125.82.186.104",
		"125.85.186.160",
		"125.85.186.242",
		"125.85.186.242",
		"125.91.49.46",
		"125.94.232.239",
		"139.189.155.142",
		"139.205.228.250",
		"139.206.193.130",
		"139.212.219.226",
		"139.214.99.241",
		"140.206.89.57",
		"140.240.27.172",
		"144.0.22.188",
		"153.34.21.235",
		"163.179.0.136",
		"171.105.232.113",
		"171.105.34.84",
		"171.106.2.173",
		"171.109.182.49",
		"171.111.92.32",
		"171.115.151.83",
		"171.11.72.195",
		"171.12.1.96",
		"171.12.3.222",
		"171.13.187.183",
		"171.221.143.69",
		"171.38.246.157",
		"171.40.144.106",
		"171.44.174.24",
		"171.8.18.185",
		"171.82.219.228",
		"175.12.144.223",
		"175.12.96.248",
		"175.146.37.22",
		"175.149.68.54",
		"175.15.144.13",
		"175.167.174.168",
		"175.167.58.70",
		"175.17.22.148",
		"175.19.130.101",
		"175.19.130.171",
		"175.19.51.75",
		"175.19.53.35",
		"175.19.54.85",
		"175.43.183.139",
		"180.103.220.111",
		"180.104.230.154",
		"180.126.145.67",
		"180.143.82.69",
		"180.156.41.51",
		"180.162.177.172",
		"180.230.36.94",
		"180.95.195.223",
		"181.92.139.166",
		"182.105.47.111",
		"182.114.5.88",
		"182.127.142.234",
		"182.139.208.155",
		"182.147.56.144",
		"182.151.166.74",
		"182.203.155.146",
		"182.207.216.61",
		"182.247.251.136",
		"182.38.140.20",
		"182.41.114.1",
		"182.45.29.7",
		"182.85.15.66",
		"182.90.221.44",
		"182.90.223.166",
		"182.97.184.200",
		"183.0.91.25",
		"183.150.36.177",
		"183.154.177.3",
		"183.157.160.24",
		"183.158.222.38",
		"183.162.3.2",
		"183.167.178.172",
		"183.167.211.11",
		"183.1.77.113",
		"183.197.43.127",
		"183.198.201.17",
		"183.198.34.195",
		"183.202.161.249",
		"183.202.208.190",
		"183.202.209.46",
		"183.202.209.6",
		"183.202.64.200",
		"183.204.45.180",
		"183.205.135.97",
		"183.205.140.166",
		"183.206.175.12",
		"183.208.28.180",
		"183.211.184.75",
		"183.212.144.28",
		"183.213.207.73",
		"183.214.111.243",
		"183.214.147.37",
		"183.214.190.220",
		"183.214.23.146",
		"183.2.200.135",
		"183.22.25.98",
		"183.223.149.17",
		"183.225.68.137",
		"183.226.14.109",
		"183.226.166.115",
		"183.234.194.30",
		"183.236.19.69",
		"183.236.19.71",
		"183.240.19.216",
		"183.240.20.215",
		"183.240.27.27",
		"183.245.221.2",
		"183.248.81.83",
		"183.35.203.141",
		"183.36.31.230",
		"183.39.53.112",
		"183.40.6.167",
		"183.50.98.82",
		"183.53.132.133",
		"183.95.51.69",
		"202.109.166.216",
		"202.109.166.219",
		"202.109.166.220",
		"209.35.30.20",
		"210.21.228.212",
		"210.21.228.212",
		"210.21.68.3",
		"211.142.199.85",
		"211.142.221.152",
		"211.94.234.248",
		"220.168.132.206",
		"220.171.138.111",
		"220.172.55.43",
		"220.175.19.104",
		"220.184.23.239",
		"220.195.64.152",
		"220.195.64.45",
		"220.195.65.74",
		"220.198.240.236",
		"220.202.152.78",
		"221.11.63.186",
		"221.13.63.104",
		"221.180.251.171",
		"221.192.179.30",
		"221.193.0.135",
		"221.193.193.131",
		"221.197.245.8",
		"221.203.194.187",
		"221.207.37.106",
		"221.207.37.41",
		"221.209.36.45",
		"221.211.86.146",
		"221.2.225.190",
		"221.7.7.44",
		"222.189.192.112",
		"222.216.23.140",
		"222.246.184.92",
		"222.38.200.108",
		"222.69.184.130",
		"222.79.238.126",
		"222.88.203.108",
		"222.92.1.198",
		"222.92.159.206",
		"223.10.141.224",
		"223.10.147.45",
		"223.104.12.208",
		"223.104.12.70",
		"223.104.15.207",
		"223.104.170.145",
		"223.104.170.145",
		"223.104.170.84",
		"223.104.17.242",
		"223.104.177.150",
		"223.104.189.229",
		"223.104.189.78",
		"223.104.22.107",
		"223.104.238.99",
		"223.104.24.153",
		"223.104.24.162",
		"223.104.255.163",
		"223.104.63.166",
		"223.104.63.237",
		"223.104.63.252",
		"223.104.94.43",
		"223.104.94.62",
		"223.104.95.48",
		"223.104.95.84",
		"223.112.202.134",
		"223.112.202.207",
		"223.113.11.166",
		"223.113.52.12",
		"223.11.45.131",
		"223.153.22.140",
		"223.155.170.13",
		"223.199.219.245",
		"223.208.105.35",
		"223.214.51.211",
		"223.215.82.233",
		"223.220.140.154",
		"223.220.140.53",
		"223.64.74.185",
		"223.73.135.81",
		"223.73.135.98",
		"223.73.238.211",
		"223.73.60.165",
		"223.73.60.22",
		"223.74.109.11",
		"223.74.34.19",
		"223.74.82.240",
		"223.74.82.59",
		"223.75.11.148",
		"223.79.82.212",
		"223.84.205.137",
		"223.86.232.9",
		"223.86.85.71",
		"223.89.189.176",
		"223.96.157.115",
		"223.97.132.216",
		"223.99.54.188",
		"223.99.57.166"
	};
	
	public static String getUserIp(String userId) {
		if(ipMap.containsKey(userId)) {
			return ipMap.get(userId);
		}
		return getIp();
	}
	
	public static String getIp() {
		int index = new Random().nextInt(ips.length);
		return ips[index];
	}
	
	public static String[] getUserIds() {
		String userId = PropertiesUtil.getValue("userIdForGift");
		if(StringUtils.isNotEmpty(userId)) {
			String[] userIds = userId.split(",");
			return userIds;
		}
		return null;
	}
	
	public static String[] getGoldUserIds() {
		String userId = PropertiesUtil.getValue("userIdForGold");
		if(StringUtils.isNotEmpty(userId)) {
			String[] userIds = userId.split(",");
			return userIds;
		}
		return null;
	}

	public static String getUserId() {
		String userId = PropertiesUtil.getValue("userId");
		if(StringUtils.isNotEmpty(userId)) {
			String[] userIds = userId.split(",");
			int index = new Random().nextInt(userIds.length);
			return userIds[index];
		}
		return "";
	}
	
	public static String getListener() {
		String listener = PropertiesUtil.getValue("listener");
		if(StringUtils.isNotEmpty(listener)) {
			return listener;
		}
		return "";
	}
	
	public static String getRoomId() {
		String roomId = PropertiesUtil.getValue("roomId");
		if(StringUtils.isNotEmpty(roomId)) {
			String[] roomIds = roomId.split(",");
			int index = new Random().nextInt(roomIds.length);
			return roomIds[index];
		}
		return "";
	}
	
	public static String getPwd() {
		String pwd = PropertiesUtil.getValue("pwd");
		if(StringUtils.isNotEmpty(pwd)) {
			return pwd;
		}
		return "";
	}
	
	public static int getTotal() {
		int total = Integer.parseInt(PropertiesUtil.getValue("total"));
		return total;
	}
	
	public static int getRandom(int minValue,int maxValue)
	{
		int returnValue=minValue;
		new java.util.Random();
		returnValue=(int)((maxValue-minValue)*Math.random()+minValue);
		return returnValue;
	}
}
