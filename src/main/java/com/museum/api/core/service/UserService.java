package com.museum.api.core.service;

import com.museum.api.common.constant.Constants;
import com.museum.api.common.exception.InheaterSOAException;
import com.museum.api.common.exception.InheaterSOAExceptionCode;
import com.museum.api.common.exception.InheaterSOAExceptionType;
import com.museum.api.common.orm.mapper.UserMapper;
import com.museum.api.common.orm.model.User;
import com.museum.api.common.orm.model.UserExample;
import com.museum.api.common.util.CommonUtil;
import com.museum.api.common.util.StringEncrypt;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    UserMapper userMapper;

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

}
