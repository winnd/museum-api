package com.museum.api.common.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.museum.api.common.constant.Constants;
import com.museum.api.common.orm.model.UserFuncRel;
import com.museum.api.core.service.UserService;
import com.museum.api.core.vo.TokenModel;
import org.apache.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class RedisTokenManager implements TokenManager {

    public static final Logger logger = Logger.getLogger(RedisTokenManager.class);

    @Resource
    RedisTemplate redis;

    @Resource
    UserService userService;

    @Override
    public TokenModel createToken(Integer userId) {

        String token = "";

        try {
            Algorithm algorithm = Algorithm.HMAC256(Constants.SECRET); // 采用HMAC256加密算法

            List<UserFuncRel> userFuncRels = userService.getUserAuth(userId);
            List<Integer> authList = new ArrayList<>();

            for(UserFuncRel userFuncRel : userFuncRels) {
                authList.add(userFuncRel.getFuncId());
            }

            // 将用户的唯一id和时间戳一起加密进去
            token = JWT.create().withClaim("userId", userId)
                    .withClaim("timeStamp", System.currentTimeMillis())
                    .withArrayClaim("authList", authList.toArray(new Integer[authList.size()]))
                    .sign(algorithm);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 生成token信息对象
        TokenModel tokenModel = new TokenModel(userId, token);

        // 将生成的token字符串存如Redis数据库中
        redis.boundValueOps(userId).set(token, Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);

        return tokenModel;
    }

    @Override
    public boolean checkToken(TokenModel tokenModel) {

        // 如果传入的token信息为空，则退出
        if(tokenModel == null || tokenModel.getUserId() == null) {
            return false;
        }

        // 在Redis数据库中根据用户id获取token字符串
        String token = (String) redis.boundValueOps(tokenModel.getUserId()).get();

        // 当从Redis数据库中获取的token字符串为空时，表示用户未登录，验证失败
        // 或者从Redis数据库中获取的token和客户端传过来的token不匹配，验证失败
        if(token == null || !token.equals(tokenModel.getToken())) {
            return false;
        }

        // 延长用户在Redis数据库中的有效时间
        redis.boundValueOps(tokenModel.getUserId())
                .expire(Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);

        return true;
    }

    @Override
    public TokenModel getToken(String authentication) {

        logger.info("用户token为 " + authentication);

        // 如果传入的参数为空，则返回
        if(authentication == null || "".equals(authentication)) {
            return null;
        }

        TokenModel tokenModel = new TokenModel();

        try {

            // 解密并获取用户id
            DecodedJWT jwt = JWT.decode(authentication);
            Integer userId = jwt.getClaim("userId").asInt();
            Integer[] authList = jwt.getClaim("authList").asArray(Integer.class);

            if(userId == null) {
                return null;
            }

            tokenModel.setToken(authentication);
            tokenModel.setUserId(userId);
            tokenModel.setAuthList(authList);

        }
        catch (JWTDecodeException e) {
            e.printStackTrace();
        }


        return tokenModel;
    }

    @Override
    public void deleteToken(Integer userId) {
        redis.delete(userId);
    }
}
