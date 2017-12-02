package com.museum.api.common.resolvers;

import com.museum.api.common.annotation.CurrentUser;
import com.museum.api.common.constant.Constants;
import com.museum.api.common.orm.model.User;
import com.museum.api.core.service.UserService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.annotation.Resource;

@Component
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Resource
    UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {

        if(methodParameter.getParameterType().isAssignableFrom(User.class)
                && methodParameter.hasParameterAnnotation(CurrentUser.class)) {
            return true;
        }

        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        // 取出鉴权是存入的userId
        Integer currentUserId = (Integer) nativeWebRequest.getAttribute(Constants.CURRENT_USER_ID, RequestAttributes.SCOPE_REQUEST);

        if(currentUserId != null) {
            return userService.getUserById(currentUserId);
        }

        throw new MissingServletRequestPartException(Constants.CURRENT_USER_ID);

    }
}
