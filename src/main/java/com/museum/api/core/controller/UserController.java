package com.museum.api.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.museum.api.common.annotation.Authorization;
import com.museum.api.common.annotation.CurrentUser;
import com.museum.api.common.constant.Constants;
import com.museum.api.common.controller.BaseController;
import com.museum.api.common.exception.InheaterSOAException;
import com.museum.api.common.orm.mapper.FunctionMapper;
import com.museum.api.common.orm.mapper.MenuMapper;
import com.museum.api.common.orm.model.Menu;
import com.museum.api.common.orm.model.MenuExample;
import com.museum.api.common.orm.model.User;
import com.museum.api.common.orm.model.UserFuncRel;
import com.museum.api.common.vo.BaseModel;
import com.museum.api.core.vo.AuthMenuVO;
import com.museum.api.core.vo.TokenModel;
import com.museum.api.core.service.UserService;
import com.museum.api.common.token.TokenManager;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 用户权限管理
     * @return
     */
    @RequestMapping(value = "/user-auth", method = RequestMethod.POST)
    @ResponseBody
    public BaseModel<String> saveUserAuth() {

        BaseModel<String> result = new BaseModel<>();

        JSONObject json = this.convertRequestBody();

        Integer userId = json.getInteger("userId");

        JSONArray authList = json.getJSONArray("authList");

        try{
            Assert.notNull(userId, "用户id不能为空");

            userService.saveUserAuth(userId, authList);

        }
        catch (IllegalArgumentException e) {
            result.setMessage(e.getMessage());
            result.setStatus(Constants.FAIL_BUSINESS_ERROR);
        }

        return result;

    }

    /**
     * 获取用户权限
     * @return
     */
    @RequestMapping(value = "/user-auth/id/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public BaseModel<List<UserFuncRel>> getUserAuth(@PathVariable Integer userId) {

        BaseModel<List<UserFuncRel>> result = new BaseModel<>();

        try{

            List<UserFuncRel> userFuncRels = userService.getUserAuth(userId);

            result.setData(userFuncRels);

        }
        catch (Exception e) {
            result.setMessage(e.getMessage());
            result.setStatus(Constants.FAIL_BUSINESS_ERROR);
        }

        return result;

    }

    @RequestMapping(value = "auth-menu", method = RequestMethod.GET)
    @ResponseBody
    public BaseModel<AuthMenuVO> getAuthMenu(){

        BaseModel<AuthMenuVO> result = new BaseModel<>();

        try {
            AuthMenuVO authMenuVO = userService.getAuthMenu();

            result.setData(authMenuVO);

        }
        catch (Exception e) {
            result.setMessage(Constants.FAIL_BUSINESS_ERROR);
            result.setMessage("系统错误");
        }

        return result;
    }


    /**
     * 获取所有用户
     * @return
     */
    @RequestMapping(value = "all-users", method = RequestMethod.GET)
    @ResponseBody
    public BaseModel<List<User>> getAllUsers() {

        BaseModel<List<User>> result = new BaseModel<>();

        try {
            List<User> users = userService.getAllUsers();

            for (User user : users) {
                user.setPassword("");
            }

            result.setData(users);

        }
        catch (Exception e) {
            result.setStatus(Constants.FAIL_BUSINESS_ERROR);
            result.setMessage("数据库错误");
        }

        return result;

    }


}
