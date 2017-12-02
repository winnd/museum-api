package com.museum.api.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.museum.api.common.annotation.Authorization;
import com.museum.api.common.annotation.CurrentUser;
import com.museum.api.common.constant.Constants;
import com.museum.api.common.controller.BaseController;
import com.museum.api.common.exception.InheaterSOAException;
import com.museum.api.common.orm.model.User;
import com.museum.api.common.vo.BaseModel;
import com.museum.api.core.vo.TokenModel;
import com.museum.api.core.service.UserService;
import com.museum.api.common.token.TokenManager;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    UserService userService;

    @Resource(name="redisTokenManager")
    TokenManager tokenManager;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody BaseModel<TokenModel> login() {

        BaseModel<TokenModel> result = new BaseModel<>();

        JSONObject json = this.convertRequestBody();

        String account = json.getString("account");
        String password = json.getString("password");

        try {
            Assert.notNull(account, "用户名不能为空");
            Assert.notNull(password, "密码不能为空");

            User user = userService.login(account, password);

            TokenModel tokenModel = tokenManager.createToken(user.getId());

            result.setData(tokenModel);
            return result;

        }
        catch (InheaterSOAException e) {
            result.setMessage(e.getMessage());
            result.setStatus(Constants.FAIL_BUSINESS_ERROR);
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setMessage(e.getMessage());
            result.setStatus(Constants.FAIL_INVALID_DATA);
            return result;
        }

    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public @ResponseBody BaseModel<User> register() {

        BaseModel<User> result = new BaseModel<>();

        JSONObject json = this.convertRequestBody();

        String account = json.getString("account");

        String password = json.getString("password");

        String name = json.getString("name");

        try {
            Assert.notNull(password, "密码不能为空");
            Assert.notNull(account, "账号不能为空");
            Assert.notNull(name, "用户名不能为空");

            User user = userService.register(account, password, name);

            result.setData(user);

            user.setPassword("");

            return result;

        }
        catch (InheaterSOAException e) {
            result.setMessage(e.getMessage());
            result.setStatus(Constants.FAIL_BUSINESS_ERROR);
            return result;
        }
        catch (Exception e) {
            result.setMessage(e.getMessage());
            result.setStatus(Constants.FAIL_INVALID_DATA);
            return result;
        }


    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @Authorization
    public @ResponseBody BaseModel<String> logout(@CurrentUser User user) {

        BaseModel<String> result = new BaseModel<>();

        try {
            tokenManager.deleteToken(user.getId());
            return  result;
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setStatus(Constants.FAIL_BUSINESS_ERROR);
            result.setMessage("业务错误");

            return result;
        }



    }

}
