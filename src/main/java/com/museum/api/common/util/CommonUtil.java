package com.museum.api.common.util;



import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
/**
 * 共通工具类
 * 
 * @author ganfeng
 * 
 */
public class CommonUtil {

	private static Logger log = Logger.getLogger(CommonUtil.class);

	/**
	 * <p>
	 * 字符串拼接
	 * </p>
	 * <p>
	 * 字符串拼接处理
	 * </p>
	 * 
	 * @param strs
	 *            字符串
	 * @return 合并后的字符串
	 */
	public static String combineStrs(String... strs) {

		if ((strs == null) || (strs.length == 0)) {
			return "";
		}

		StringBuilder keyBuff = new StringBuilder();

		for (int i = 0; i < strs.length; i++) {
			if (strs[i] != null) {
				keyBuff.append(strs[i]);
			}
		}

		return keyBuff.toString();
	}

	/**
	 * 生成15位长度的字符串（0-9数字组成）
	 * 
	 * @return string
	 */
	public static String getRandomString(int length) {

		String base = "0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 获取字符串的MD5值
	 * 
	 * @param plainText
	 *            plainText
	 * @return string
	 */
	public static String EncoderByMd5(String plainText) {
		String md5str = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte[] b = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			// 进行加密处理
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			md5str = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md5str;
	}

	public static String EncoderPwdByMd5(String str) {
		String newstr = null;
		try {
			// 确定计算方法
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			// 加密后的字符串
			newstr = Base64
					.encodeBase64String(md5.digest(str.getBytes("utf-8")));
		} catch (Exception e) {
			return null;
		}
		return newstr;
	}

	/**
	 * <p>
	 * 字符串拼接
	 * </p>
	 * <p>
	 * 字符串拼接处理
	 * </p>
	 * 
	 * @param strs
	 *            字符串
	 * @return 合并后的字符串
	 */
	public static String setClientMessages(String... strs) {

		if ((strs == null) || (strs.length == 0)) {
			return "";
		}

		StringBuilder keyBuff = new StringBuilder();

		for (int i = 0; i < strs.length; i++) {
			if (strs[i] != null) {
				keyBuff.append(strs[i]);
			}
		}

		return keyBuff.toString();
	}

	public static Integer toInteger(Object target) {
		return toInteger(target, null);
	}

	/**
	 * 
	 * @param target
	 * @param defaultValue
	 * @return
	 */
	public static Integer toInteger(Object target, Integer defaultValue) {

		if (isNullOrEmpty(target)) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(toString(target).trim());
		} catch (Exception ex) {
			System.out.println();
			return defaultValue;
		}
	}

	/**
	 * 
	 * @param target
	 * @return
	 */
	public static boolean isNullOrEmpty(Object target) {

		if (isNull(target)) {
			return true;
		}
		return isEmpty(target);
	}

	/**
	 * 
	 * @param target
	 * @return
	 */
	public static boolean isEmpty(Object target) {

		if (isNull(target)) {
			return false;
		}
		Class<?> clazz = target.getClass();
		if (String.class.isAssignableFrom(clazz)) {
			return isEmpty(toString(target));
		}
		if (Iterable.class.isAssignableFrom(clazz)) {
			return isEmpty((Iterable<?>) target);
		}

		if (Map.class.isAssignableFrom(clazz)) {
			return isEmpty((Map<?, ?>) target);
		}
		if (clazz.isArray()) {
			return Array.getLength(target) < 1;
		}
		return isEmpty(toString(target));
	}

	public static boolean isEmpty(String target) {
		return isEmpty(target, true);
	}

	public static boolean isEmpty(String target, boolean ignoreSpace) {
		if (target == null)
			return false;
		if (ignoreSpace)
			target = trim(target);

		return "".equals(target);
	}

	public static boolean isEmpty(Iterable<?> target) {
		if (isNull(target))
			return false;

		return !target.iterator().hasNext();
	}

	public static String trim(String target) {
		if (target == null)
			return "";

		return target.trim();
	}

	/**
	 * 
	 * @param target
	 * @return
	 */
	public static boolean isNull(Object target) {
		return target == null;
	}

	/**
	 * 
	 * @param target
	 * @return
	 */
	public static String toString(Object target) {
		if (isNull(target)) {
			return "";
		}

		return target.toString();
	}

	/**
	 * 
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	public static boolean equal(Object arg0, Object arg1) {
		if (isNull(arg0) && isNull(arg1)) {
			return true;
		}
		if (isNull(arg0) || isNull(arg1)) {
			return false;
		}
		return arg0.equals(arg1);
	}

	/**
	 * list转换为json格式字符串
	 * 
	 * @param list
	 *            list对象
	 * @param keys
	 *            关键字
	 * @return string
	 */
	public static String listToJsonString(List list, String... keys) {
		StringBuilder jsonString = new StringBuilder();
		jsonString.append("[");
		if (isNullOrEmpty(list)) {
			return "";
		}
		try {
			int listSize = list.size();
			for (int i = 0; i < listSize; i++) {
				if (i != listSize - 1) {
					jsonString.append("{");
					for (int j = 0; j < keys.length; j++) {
						if (j != keys.length - 1) {
							jsonString.append(keys[j]);
							jsonString.append(":'");
							jsonString
									.append(invokeMethod(list.get(i), keys[j]));
							jsonString.append("',");
						} else {
							jsonString.append(keys[j]);
							jsonString.append(":'");
							jsonString
									.append(invokeMethod(list.get(i), keys[j]));
							jsonString.append("'");
						}
					}

					jsonString.append("},");
				} else {
					jsonString.append("{");
					for (int j = 0; j < keys.length; j++) {
						if (j != keys.length - 1) {
							jsonString.append(keys[j]);
							jsonString.append(":'");
							jsonString
									.append(invokeMethod(list.get(i), keys[j]));
							jsonString.append("',");
						} else {
							jsonString.append(keys[j]);
							jsonString.append(":'");
							jsonString
									.append(invokeMethod(list.get(i), keys[j]));
							jsonString.append("'");
						}
					}
					jsonString.append("}");
				}
			}
		} catch (Throwable e) {
			log.error(" list转换为json出错", e);
		}
		jsonString.append("]");
		return toString(jsonString);
	}

	/**
	 * @param methodObject
	 *            方法所在的对象
	 * @param methodName
	 *            方法名
	 * @throws Exception
	 *             Exception
	 * @return obj
	 */
	private static Object invokeMethod(Object methodObject, String methodName)
			throws Exception {

		methodName = combineStrs("get", methodName.substring(0, 1)
				.toUpperCase(), methodName.substring(1));

		Class ownerClass = methodObject.getClass();

		Method method = ownerClass.getMethod(methodName);
		return method.invoke(methodObject);
	}

	/**
	 * 用来计算两个日期之间相差天数
	 */
	public static int getDifDays(String date1, String date2) {
		Date date_1 = DateUtil.getDate(date1);
		Date date_2 = DateUtil.getDate(date2);

		long time1 = date_1.getTime();
		long time2 = date_2.getTime();
		long time3 = Math.abs(time1 - time2);

		int day = getDay(time3);
		return day;
	}

	/**
	 * 将两个日期之间的毫秒数换算成天数
	 * 
	 * @param time
	 * @return
	 */
	private static int getDay(long time) {
		int day = (int) (time / 1000 / 60 / 60 / 24);
		return day;
	}

	/**
	 * 创建token 50位
	 * 
	 * @return token
	 */
	public static String createToken() {

		StringBuffer sb = new StringBuffer();

		Random randGen = new Random();
		char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ")
				.toCharArray();

		char[] randBuffer = new char[18];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}

		char[] randBuffer2 = new char[18];
		for (int i = 0; i < randBuffer2.length; i++) {
			randBuffer2[i] = numbersAndLetters[randGen.nextInt(71)];
		}

		sb.append(new String(randBuffer));
		sb.append(System.currentTimeMillis());
		sb.append(new String(randBuffer2));

		return sb.toString();
	}

	public static Map getMapFromJson(String jsonString) {

		ObjectMapper mapper = new ObjectMapper();
		Map<?, ?> map = null;
		try {
			map = mapper.readValue(jsonString, Map.class);
		} catch (Exception e) {

			return null;
		}

		return map;
	}

	public static String getFromBASE64(String s) {
		if (s == null)
			return null;
		try {
			byte[] encodeBase64 = Base64.decodeBase64(s.getBytes("UTF-8"));
			return new String(encodeBase64);
		} catch (UnsupportedEncodingException e) {
			return null;
		}

	}
	
	/**
	 * 获取随机函数 验证码
	 * @param charCount  随机函数的位数
	 * @return
	 */
	public static String getRandValidateCode(int charCount) {
        String charValue = "";
        for (int i = 0; i < charCount; i++) {
            char c = (char) (randomInt(0, 10) + '0');
            charValue += String.valueOf(c);
        }
        return charValue;
    }
	
	
	private static int randomInt(int from, int to) {
        Random r = new Random();
        return from + r.nextInt(to - from);
    }
	
	private static Double EARTH_RADIUS =6378.137 *1000;
	
	
	//lng-经度 ,lat-纬度
	/**
	 * 
	 * @param lng1 1的经度
	 * @param lat1 1的纬度
	 * @param lng2
	 * @param lat2
	 * @return 返回两个经纬度坐标的距离 单位：米
	 *
	 */
	public static Double getDistance(Double lng1,Double lat1,Double lng2,Double lat2){
		
		
		double radLng1 =rad(lng1);
		double radLat1  = rad(lat1);
		
		
		double radLng2 =rad(lng2);
		double radLat2  = rad(lat2);
		
		//计算两个点的余弦夹角
		double t1 = Math.cos(radLat1) * Math.cos(radLng1) * Math.cos(radLat2) * Math.cos(radLng2);
		double t2 = Math.cos(radLat1) * Math.sin(radLng1) * Math.cos(radLat2) * Math.sin(radLng2); 
		double t3 = Math.sin(radLat1) * Math.sin(radLat2);
		
//		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)))
//				+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2);
//		s =s * EARTH_RADIUS;
		return Math.acos(t1+t2+t3)* EARTH_RADIUS;
	}
	
	/**
	 * 计算弧度
	 * @param d
	 * @return
	 */
	private static Double rad(Double d){
		return Math.PI * d /180;
	}
	
	public static void main(String[] args) {

//		System.out.println(Base64.encodeBase64String("UrBiJMz10uGPf78YGq1474959254359ap42fAaU2yooD7rWnh".getBytes()));
//		
//		System.out.println(DateUtil.parse("2016-08-29 12:19:12.123",DateUtil.DB_TIMESTAMP_FORMAT).getTime());
//		System.out.println(DateUtil.getDate("2016-08-01 12:19:12"));
		
		Double a = CommonUtil.getDistance(121.467113,37.480563,121.467926, 37.480591);
		System.out.println(a);
	}
}
