package com.museum.api.core.service;

import com.alibaba.fastjson.JSONArray;
import com.museum.api.common.constant.Constants;
import com.museum.api.common.exception.InheaterSOAException;
import com.museum.api.common.exception.InheaterSOAExceptionCode;
import com.museum.api.common.exception.InheaterSOAExceptionType;
import com.museum.api.common.orm.mapper.FunctionMapper;
import com.museum.api.common.orm.mapper.MenuMapper;
import com.museum.api.common.orm.mapper.UserFuncRelMapper;
import com.museum.api.common.orm.mapper.UserMapper;
import com.museum.api.common.orm.model.*;
import com.museum.api.common.util.CommonUtil;
import com.museum.api.common.util.StringEncrypt;
import com.museum.api.common.vo.BaseModel;
import com.museum.api.core.vo.AuthMenuVO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Resource
    UserMapper userMapper;

    @Resource
    UserFuncRelMapper userFuncRelMapper;

    @Resource
    MenuMapper menuMapper;

    @Resource
    FunctionMapper functionMapper;

    public User login(String account, String password) {
        UserExample example = new UserExample();

        example.createCriteria().andAccountEqualTo(account);

        List<User> users = userMapper.selectByExample(example);

        if(users == null || users.size() == 0) {
            throw new InheaterSOAException(InheaterSOAExceptionCode.BUNIESS_EXCEPTION, InheaterSOAExceptionType.BUSINESS, "用户不存在");
        }

        User user = users.get(0);

        password = CommonUtil.getFromBASE64(password); // BASE64解码

        password = StringEncrypt.Encrypt(password, "MD5");

        if(!password.equals(user.getPassword())) {
            throw new InheaterSOAException(InheaterSOAExceptionCode.BUNIESS_EXCEPTION, InheaterSOAExceptionType.BUSINESS, "密码不正确");
        }

        return user;

    }

    public User register(String account, String password, String name){

        UserExample example = new UserExample();

        example.createCriteria().andAccountEqualTo(account);

        if (!userMapper.selectByExample(example).isEmpty()) {
            throw new InheaterSOAException(InheaterSOAExceptionCode.BUNIESS_EXCEPTION, InheaterSOAExceptionType.BUSINESS, "用户已存在");
        }

        Long currentTime = System.currentTimeMillis();

        password = CommonUtil.getFromBASE64(password); // Base64解码
        password = StringEncrypt.Encrypt(password, "MD5"); // MD5加密

        User user = new User();

        user.setAccount(account);
        user.setPassword(password);
        user.setName(name);
        user.setCreateBy(1);
        user.setCreateTime(currentTime);
        user.setUpdateBy(1);
        user.setUpdateTime(currentTime);
        user.setLastLoginTime(currentTime);

        userMapper.insert(user);

        return user;


    }

    public User getUserById(Integer userId) {

        return userMapper.selectByPrimaryKey(userId);

    }

    public void saveUserAuth(Integer userId, JSONArray authList){

        Long currentTime = System.currentTimeMillis();

        UserFuncRelExample example = new UserFuncRelExample();

        example.createCriteria().andUserIdEqualTo(userId);

        userFuncRelMapper.deleteByExample(example);

        for(int i = 0; i < authList.size(); i++) {
            UserFuncRel userFuncRel = new UserFuncRel();
            userFuncRel.setUserId(userId);
            userFuncRel.setFuncId(authList.getInteger(i));
            userFuncRel.setCreateTime(currentTime);
            userFuncRel.setCreateBy(1);
            userFuncRelMapper.insert(userFuncRel);
        }

    }

    public List<UserFuncRel> getUserAuth(Integer userId) {

        UserFuncRelExample example = new UserFuncRelExample();

        example.createCriteria().andUserIdEqualTo(userId);

        return userFuncRelMapper.selectByExample(example);


    }

    public AuthMenuVO getAuthMenu() {

        AuthMenuVO authMenuVO = new AuthMenuVO();

        MenuExample menuExample = new MenuExample();

        menuExample.createCriteria();

        List<Menu> menus = menuMapper.selectByExample(menuExample);

        FunctionExample functionExample = new FunctionExample();

        functionExample.createCriteria();

        List<Function> functions = functionMapper.selectByExample(functionExample);

        for (Menu menu : menus) {


            List<Function> childFunctions = new ArrayList<>();

            for (Function function : functions) {
                if (menu.getId().equals(function.getMenuId())) {
                    childFunctions.add(function);
                }
            }

            if (menu.getParentId() == 0) {
                authMenuVO.convertMenu(menu);
                authMenuVO.setMenus(new ArrayList<AuthMenuVO>());
            }
            else {
                AuthMenuVO tempAuth = new AuthMenuVO(menu);

                tempAuth.setFunctions(childFunctions);

                authMenuVO.getMenus().add(tempAuth);

            }


        }

        return authMenuVO;

    }

    public List<User> getAllUsers() {

        UserExample example = new UserExample();

        example.createCriteria();

        return userMapper.selectByExample(example);

    }

    public int updateUser(User user){
        if(userMapper.selectByPrimaryKey(user.getId()) == null) {
            throw new InheaterSOAException(InheaterSOAExceptionCode.BUNIESS_EXCEPTION,
                    InheaterSOAExceptionType.BUSINESS, "用户不存在");
        }

        return userMapper.updateByPrimaryKeySelective(user);
    }

    public int deleteUser(Integer userId) {
        if(userMapper.selectByPrimaryKey(userId) == null) {
            throw new InheaterSOAException(InheaterSOAExceptionCode.BUNIESS_EXCEPTION,
                    InheaterSOAExceptionType.BUSINESS, "用户不存在");
        }

        return userMapper.deleteByPrimaryKey(userId);
    }


}
