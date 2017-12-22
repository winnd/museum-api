package com.museum.api.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.museum.api.common.annotation.Authorization;
import com.museum.api.common.constant.Constants;
import com.museum.api.common.vo.BaseModel;
import com.museum.api.core.vo.TokenModel;
import com.museum.api.common.token.TokenManager;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter{

    @Resource(name = "redisTokenManager")
    TokenManager tokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 如果拦截的不是方法，则返回
        if(!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        Method method = handlerMethod.getMethod();

        // 从header中获取token
        String authorization = request.getHeader(Constants.AUTHORIZATION);

        // 验证token
        TokenModel tokenModel = tokenManager.getToken(authorization);

        if(tokenModel != null && tokenModel.getUserId() != null) {
            // 如果认证成功，将token对应的用户id存在request中，便于之后注入
            request.setAttribute(Constants.CURRENT_USER_ID, tokenModel.getUserId());
        }

        Authorization annotation = method.getAnnotation(Authorization.class);

        if(annotation != null) {

            if(tokenModel != null) {

                Integer[] authList = tokenModel.getAuthList();

                if(tokenManager.checkToken(tokenModel) && authList != null) {

                    for(int i = 0; i < authList.length; i++) {
                        if(authList[i].equals(annotation.authCode())) {
                            return true;
                        }
                    }

                }

            }



            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");

            PrintWriter out = null;

            BaseModel<String> result = new BaseModel<>();

            result.setStatus(Constants.FAIL_INVALID_AUTH);
            result.setMessage("没有权限访问");

            try {
                out = response.getWriter();
                out.append(JSONObject.toJSONString(result));

            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                out.close();
            }

            return false;
        }

        return true;

    }
}
