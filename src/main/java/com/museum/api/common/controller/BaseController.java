package com.museum.api.common.controller;



import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;


public class BaseController {

	protected HttpSession session;

	@ModelAttribute
	public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
		this.session = request.getSession();
	}

	private static final Logger logger = Logger.getLogger(BaseController.class);

	protected JSONObject convertRequestBody() {
		String param = null;
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		try {
			request.setCharacterEncoding("UTF-8");
			BufferedReader br = request.getReader();
			String buffer = null;
			StringBuffer buff = new StringBuffer();
			while ((buffer = br.readLine()) != null) {
				buff.append(buffer + "\n");
			}
			br.close();
			param = buff.toString();
			System.out.print("请求参数：" + param);
			logger.info(param);
			return JSONObject.parseObject(param);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	protected void setApplicationInfo(String key, Object value) {
		session.setAttribute(key, value);
	}

	protected Object getApplicationInfo(String key) {
		return session.getAttribute(key);
	}
	


	protected String getParam() {
		String param = null;
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		try {
			request.setCharacterEncoding("UTF-8");
			BufferedReader br = request.getReader();
			String buffer = null;
			StringBuffer buff = new StringBuffer();
			while ((buffer = br.readLine()) != null) {
				buff.append(buffer + "\n");
			}
			br.close();
			param = buff.toString();
			System.out.print("请求参数：" + param);
			logger.info(param);
			return param;

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	
//	protected Integer getUserId() {
//		UserInfoVO userInfo = (UserInfoVO) session.getAttribute("user");
//		if (userInfo != null) {
//			return userInfo.getUserId();
//		} else {
//			//TODO
//			return null;
//		}
//
//	}
	
//	protected Integer getCompanyId() {
//		UserInfoVO userInfo = (UserInfoVO) session.getAttribute("user");
//		if (userInfo != null) {
//			return userInfo.getCompanyId();
//		} else {
//			//TODO
//			return null;
//		}
//
//	}
	
//	public String getUserType() {
//
//		UserInfoVO userInfo = (UserInfoVO) session.getAttribute("user");
//		if (userInfo != null) {
//			return userInfo.getUserType();
//		} else {
//			return null;
//		}
//	}
	public String getDeviceType() {

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String userAgent = request.getHeader("user-agent");

		if (userAgent == null)
			return null;
		System.out.print("设备类型：" + userAgent);
		return userAgent;

	}
	
}
