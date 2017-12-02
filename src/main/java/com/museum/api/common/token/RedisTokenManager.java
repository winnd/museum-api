package com.museum.api.common.token;

import com.museum.api.common.constant.Constants;
import com.museum.api.core.vo.TokenModel;
import org.apache.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class RedisTokenManager implements TokenManager {

    public static final Logger logger = Logger.getLogger(RedisTokenManager.class);

    @Resource
    private RedisTemplate redis;

    @Override
    public TokenModel createToken(Integer userId) {

        String token = UUID.randomUUID().toString().replace("-", "");

        TokenModel tokenModel = new TokenModel(userId, token);

        redis.boundValueOps(userId).set(token, Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);

        return tokenModel;
    }

    @Override
    public boolean checkToken(TokenModel tokenModel) {
        if(tokenModel == null) {
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

        logger.info(authentication + "-----");

        if(authentication == null || "".equals(authentication)) {
            return null;
        }

        String[] params = authentication.split("_");

        if(params.length != 2) {
            return null;
        }

        Integer userId = Integer.valueOf(params[0]);

        String token = params[1];

        return new TokenModel(userId, token);
    }

    @Override
    public void deleteToken(Integer userId) {
        redis.delete(userId);
    }
}
