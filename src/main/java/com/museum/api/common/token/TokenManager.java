package com.museum.api.common.token;

import com.museum.api.core.vo.TokenModel;

public interface TokenManager {

    /**
     * 创建一个关联用户的Token
     * @param userId
     * @return
     */
    TokenModel createToken(Integer userId);

    /**
     * 检查Token是否有效
     * @param tokenModel
     * @return
     */
    boolean checkToken(TokenModel tokenModel);

    /**
     * 从字符串中解析Token
     * @param authentication
     * @return
     */
    TokenModel getToken(String authentication);

    /**
     * 清除Token
     * @param userId
     */
    void deleteToken(Integer userId);


}
