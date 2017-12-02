package com.museum.api.core.vo;

public class TokenModel {

    private Integer userId; // 用户ID

    private String token; // 随机生成的uuid

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TokenModel() {
    }

    public TokenModel(Integer userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}
