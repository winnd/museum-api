package com.museum.api.common.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.museum.api.common.constant.Constants;
import com.museum.api.core.vo.TokenModel;
import org.apache.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class RedisTokenManager implements TokenManager {

    public static final Logger logger = Logger.getLogger(RedisTokenManager.class);

    @Resource
    private RedisTemplate redis;

    @Override
    public TokenModel createToken(Integer userId) {

        String token = "";

        try {
            Algorithm algorithm = Algorithm.HMAC256(Constants.SECRET);

            token = JWT.create().withClaim("userId", userId).withClaim("timeStamp", System.currentTimeMillis()   ).sign(algorithm);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        TokenModel tokenModel = new TokenModel(userId, token);

        redis.boundValueOps(userId).set(token, Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);

        return tokenModel;
    }

    @Override
    public boolean checkToken(TokenModel tokenModel) {
        if(tokenModel == null || tokenModel.getUserId() == null) {
            return false;
        }

        String token = (String) redis.boundValueOps(tokenModel.getUserId()).get();

        if(token == null || !token.equals(tokenModel.getToken())) {
            return false;
        }

        redis.boundValueOps(tokenModel.getUserId()).expire(Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);

        return true;
    }

    @Override
    public TokenModel getToken(String authentication) {

        logger.info("用户token为 " + authentication);

        if(authentication == null || "".equals(authentication)) {
            return null;
        }
        TokenModel tokenModel = new TokenModel();

        try {
            DecodedJWT jwt = JWT.decode(authentication);

            Integer userId = jwt.getClaim("userId").asInt();

            if(userId == null) {
                return null;
            }

            tokenModel.setToken(authentication);
            tokenModel.setUserId(userId);

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
