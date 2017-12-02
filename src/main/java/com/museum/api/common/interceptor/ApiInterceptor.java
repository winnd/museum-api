package com.museum.api.common.interceptor;



import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.museum.api.common.orm.mapper.UserMapper;
import com.museum.api.common.orm.model.User;
import com.museum.api.common.orm.model.UserExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.museum.api.common.orm.model.UserExample.Criteria;
import com.museum.api.common.util.CommonUtil;
import com.museum.api.common.util.StringEncrypt;
import com.museum.api.common.util.StringUtil;


public class ApiInterceptor implements HandlerInterceptor {

	private static Logger logger = LoggerFactory.getLogger(ApiInterceptor.class);
	@Autowired
	private UserMapper userMapper;

	public void afterCompletion(HttpServletRequest request, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// logger.info("className--->" + arg2);
		logger.info(request.getRequestURL().toString());
		if (arg3 != null) {
			logger.error(arg3.getMessage());
		}

	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {

	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {

		if (!checkHeaderAuth(request, response)) {
			response.setStatus(401);
			response.setHeader("Cache-Control", "no-store");
			response.setDateHeader("Expires", 0);
			response.setHeader("WWW-authenticate", "Basic Realm=\"请先登录系统\"");
			return false;
		}
		return true;
	}

	private boolean checkHeaderAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String auth = request.getHeader("Authorization");
		String userType = request.getHeader("userType");

//		if (StringUtils.isEmpty(userType)) {
//			return false;
//		}

		logger.info("ControlInterceptor auth encoded in base64 is " + auth);

		if ((auth != null) && (auth.length() > 6)) {
			auth = auth.substring(6, auth.length());

			String decodedAuth = CommonUtil.getFromBASE64(auth);
			logger.info(decodedAuth);
			String account = "";
			String password = "";
			if (StringUtil.isNotNull(decodedAuth)) {

				if (decodedAuth.indexOf(":") >= 0) {
					String[] infos = decodedAuth.split(":");
					account = infos[0];
					password = infos[1];
					logger.info(decodedAuth);
					User user = null;
					UserExample example = new UserExample();
					Criteria crit= example.createCriteria();
					crit.andAccountEqualTo(account);
//					if (StringUtil.isNotNull(userType)) {
//						crit.andUserTypeEqualTo(userType);
//					}
					
					List<User> list = userMapper.selectByExample(example);
					if (list != null && list.size() > 0) {
						user = list.get(0);
					}

					if (user == null)
						return false;
					if (user.getPassword().equals(StringEncrypt.Encrypt(password))) {
						System.out.println("请求用户" + account + ":" + user.getId());
						request.setAttribute("userId", user.getId());
						return true;
					}

				}
			}

			return false; // user == null ? false : true;

		} else {
			return false;
		}

	}
}
